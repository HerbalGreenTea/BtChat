package com.example.btchat.adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.btchat.OnItemBtDevClickListener
import com.example.btchat.R
import kotlinx.android.synthetic.main.pair_dev_item.view.*

class PairDevListAdapter(private var devs: List<BluetoothDevice>
): RecyclerView.Adapter<PairDevListAdapter.PairDevListViewHolder>() {

    private var onItemBtDevClickListener: OnItemBtDevClickListener? = null

    inner class PairDevListViewHolder(inflater: LayoutInflater, parent: ViewGroup
    ): RecyclerView.ViewHolder(inflater.inflate(R.layout.pair_dev_item, parent, false)) {

        fun bind(dev: BluetoothDevice) {
            itemView.dev_name.text = dev.name
            itemView.dev_mac.text = dev.address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairDevListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PairDevListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PairDevListViewHolder, position: Int) {
        val dev = devs[position]
        holder.bind(dev)

        if (onItemBtDevClickListener != null)
            holder.itemView.setOnClickListener {
                onItemBtDevClickListener?.onDevClick(holder.itemView.dev_mac.text.toString())
            }
    }

    override fun getItemCount(): Int = devs.size

    fun setOnItemBtDevClickListener(listener: OnItemBtDevClickListener) {
        onItemBtDevClickListener = listener
    }

    fun addDev(dev: BluetoothDevice) {
        devs = mutableListOf<BluetoothDevice>().apply {
            addAll(devs)
            add(dev)
        }
        notifyDataSetChanged()
    }
}