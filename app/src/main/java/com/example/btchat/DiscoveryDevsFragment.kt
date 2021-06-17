package com.example.btchat

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.btchat.adapters.PairDevListAdapter
import kotlinx.android.synthetic.main.fragment_discovery_devs.view.*

class DiscoveryDevsFragment : Fragment() {

    private val btAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discovery_devs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.rv_discovery_dev_list.adapter = PairDevListAdapter(listOf()).apply {

            setOnItemBtDevClickListener(object : OnItemBtDevClickListener{
                override fun onDevClick(devAddress: String) {

                }
            })
        }
    }

    private fun addNewDev() {

    }
}