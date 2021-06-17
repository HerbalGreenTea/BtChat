package com.example.btchat

import android.bluetooth.BluetoothDevice

interface OnItemBtDevClickListener {
    fun onDevClick(devAddress: String)
}