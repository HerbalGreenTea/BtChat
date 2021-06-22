package com.example.btchat

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.btchat.adapters.PairDevListAdapter
import com.example.btchat.viewModels.SharedViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_paired_dev.view.*

class PairedDevFragment : Fragment() {

    private val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) findNavController().navigate(R.id.discoveryDevsFragment)
    }

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_paired_dev, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val devs = btAdapter.bondedDevices.toList()

        view.rv_list_pair_dev.adapter = PairDevListAdapter(devs).apply {

            setOnItemBtDevClickListener(object : OnItemBtDevClickListener {
                override fun onDevClick(devAddress: String) {
                    val dev = btAdapter.getRemoteDevice(devAddress)
                    ChatFragment.btDataService.startBtConnect(dev)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                sharedViewModel.setNewMessage(it)
                            }
                }
            })
        }

        view.btn_open_fragment_discovery_dev.setOnClickListener {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}