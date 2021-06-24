package com.example.btchat.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.btchat.OnItemBtDevClickListener
import com.example.btchat.R
import com.example.btchat.adapters.PairDevListAdapter
import kotlinx.android.synthetic.main.fragment_discovery_devs.view.*

class DiscoveryDevsFragment : Fragment() {

    private val btAdapter = BluetoothAdapter.getDefaultAdapter()

    private val pairDevListAdapter: PairDevListAdapter = PairDevListAdapter(listOf()).apply {

        setOnItemBtDevClickListener(object : OnItemBtDevClickListener {
            override fun onDevClick(devAddress: String) {
                val dev = btAdapter.getRemoteDevice(devAddress)
                dev.createBond()
            }
        })
    }

    private val btReciver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && device.name != null) {
                        pairDevListAdapter.addDev(device)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discovery_devs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.rv_discovery_dev_list.adapter = pairDevListAdapter
    }

    override fun onResume() {
        super.onResume()
        val f1 = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val f2 = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        activity?.registerReceiver(btReciver, f1)
        activity?.registerReceiver(btReciver, f2)
        btAdapter.startDiscovery()
    }

    override fun onPause() {
        btAdapter.cancelDiscovery()
        activity?.unregisterReceiver(btReciver)
        super.onPause()
    }
}