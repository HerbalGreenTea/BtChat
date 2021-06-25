package com.example.btchat.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.btchat.BtMessage

class SharedViewModel: ViewModel() {

    private val mutableNewMessageLiveData: MutableLiveData<BtMessage> = MutableLiveData()
    val newMessageLiveData: LiveData<BtMessage> = mutableNewMessageLiveData

    fun setNewMessage(message: BtMessage) {
        mutableNewMessageLiveData.value = message
    }
}