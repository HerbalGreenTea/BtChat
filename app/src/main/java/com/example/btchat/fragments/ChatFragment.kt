package com.example.btchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.btchat.*
import com.example.btchat.adapters.MessageListAdapter
import com.example.btchat.viewModels.SharedViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    companion object {
        val btDataService: BtChatService = BtChatService()
    }

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btn_choose_dev.setOnClickListener {
            findNavController().navigate(R.id.pairedDevFragment)
        }

        view.btn_send_message.setOnClickListener {
            val text = view.typing_message.text.toString()
            if (text.isNotEmpty()) {
                sendMessage(text)
                view.typing_message.setText("")
            }
        }

        view.rv_list_message.adapter = MessageListAdapter().apply {
            sharedViewModel.newMessageLiveData.observe(viewLifecycleOwner) { btMessage ->

                when(btMessage.typeMessage) {
                    TypeMessage.CONNECTING_MESSAGE, TypeMessage.CONNECTED_MESSAGE ->
                        setStatusConnect(btMessage.textMessage)
                    TypeMessage.USER_MESSAGE -> addMessage(btMessage.textMessage)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (btDataService.connectionState == ConnectionState.STATE_NONE) {
            btDataService.startBtAccept()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        sharedViewModel.setNewMessage(it)
                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO ??????????????
    }

    private fun sendMessage(text: String) {
        val bytes = text.toByteArray()
        btDataService.write(bytes)
        sharedViewModel.setNewMessage(BtMessage(text, TypeMessage.USER_MESSAGE))
    }

    private fun setStatusConnect(status: String) {
        (requireActivity() as MainActivity).fieldStatus.text = status
    }
}