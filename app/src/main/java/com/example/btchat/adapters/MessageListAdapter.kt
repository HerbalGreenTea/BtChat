package com.example.btchat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.btchat.R
import kotlinx.android.synthetic.main.message_item_in.view.*

class MessageListAdapter(
        private val messages: MutableList<String> = mutableListOf()
): RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder>() {

    inner class MessageListViewHolder(inflater: LayoutInflater, parent: ViewGroup
    ): RecyclerView.ViewHolder(inflater.inflate(R.layout.message_item_in, parent, false)) {

        fun bind(message: String) {
            itemView.text_message_in.text = message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessageListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: String) {
        messages.add(message)
        notifyDataSetChanged()
    }
}