package com.example.btchat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.btchat.adapters.PairDevListAdapter
import kotlinx.android.synthetic.main.fragment_paired_dev.view.*

class PairedDevFragment : Fragment() {

    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) findNavController().navigate(R.id.discoveryDevsFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_paired_dev, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val devs = btAdapter.bondedDevices.toList()

        view.rv_list_pair_dev.adapter = PairDevListAdapter(devs).apply {

            setOnItemBtDevClickListener(object : OnItemBtDevClickListener {
                override fun onDevClick(devAddress: String) {

                }
            })
        }

        view.btn_open_fragment_discovery_dev.setOnClickListener {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun connectDev() {

    }
}