package com.example.btchat

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.btchat.viewModels.SharedViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    companion object {
        val btDataService: BtDataService = BtDataService()
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
            sendMessage(text)
        }

        sharedViewModel.newMessageLiveData.observe(viewLifecycleOwner) {
            view.typing_message.setText(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (btDataService.connectionState == ConnectionState.STATE_NONE) {
            btDataService.startBtAccept()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                        view?.typing_message?.setText(it.toString())
                    }
        }
    }

    fun sendMessage(text: String) {
        val bytes = text.toByteArray()
        btDataService.write(bytes)
    }
}