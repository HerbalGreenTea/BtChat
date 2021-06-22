package com.example.btchat.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    private val mutableNewMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val newMessageLiveData: LiveData<String> = mutableNewMessageLiveData

    fun setNewMessage(message: String) {
        mutableNewMessageLiveData.value = message
    }
}