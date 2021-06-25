package com.example.btchat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BtChatService {
    companion object {
        private val MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
        private const val APP_NAME = "BtChat"
    }

    var connectionState: ConnectionState = ConnectionState.STATE_NONE

    private val btAdapter = BluetoothAdapter.getDefaultAdapter()

    private var bluetoothServerSocket: BluetoothServerSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    fun startBtAccept(): Flowable<BtMessage> = Flowable.create({ acceptSubscribe ->
        bluetoothServerSocket = btAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID_INSECURE)
        connectionState = ConnectionState.STATE_LISTEN

        while (connectionState != ConnectionState.STATE_CONNECTED) {
            val btSocket = bluetoothServerSocket?.accept()

            if (btSocket != null) {
                when(connectionState) {
                    ConnectionState.STATE_LISTEN, ConnectionState.STATE_CONNECTING ->
                        connected(btSocket)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.io())
                                .subscribe {
                                    acceptSubscribe.onNext(it)
                                }
                    ConnectionState.STATE_NONE, ConnectionState.STATE_CONNECTED -> btSocket.close()
                }
            }
        }

    }, BackpressureStrategy.DROP)

    private fun connected(btSocket: BluetoothSocket): Flowable<BtMessage> = Flowable.create({
        inputStream = btSocket.inputStream
        outputStream = btSocket.outputStream
        connectionState = ConnectionState.STATE_CONNECTED
        it.onNext(BtMessage(btSocket.remoteDevice.name, TypeMessage.CONNECTED_MESSAGE))

        val buffer = ByteArray(1024)
        var bytes: Int? = null

        while (connectionState == ConnectionState.STATE_CONNECTED) {
            try {
                bytes = inputStream?.read(buffer)
                if (bytes != null) {
                    val message = String(buffer, 0, bytes)
                    it.onNext(BtMessage(message, TypeMessage.USER_MESSAGE))
                }
            } catch (e: Exception) {
                connectionState = ConnectionState.STATE_NONE
                break
            }
        }

    }, BackpressureStrategy.DROP)

    fun write(bytes: ByteArray) = outputStream?.write(bytes)

    fun startBtConnect(dev: BluetoothDevice): Flowable<BtMessage> = Flowable.create({ connectSubscribe ->
        val btSocket: BluetoothSocket? = dev.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE)
        connectionState = ConnectionState.STATE_CONNECTING
        connectSubscribe.onNext(BtMessage("подключение", TypeMessage.CONNECTING_MESSAGE))

        btAdapter.cancelDiscovery()
        btSocket?.connect()

        if (btSocket != null) {
            connected(btSocket)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .subscribe {
                        connectSubscribe.onNext(it)
                    }
        }
    }, BackpressureStrategy.DROP)

    // дописать канселы

    fun stop() {
        bluetoothServerSocket?.close()
        inputStream?.close()
        outputStream?.close()
    }
}