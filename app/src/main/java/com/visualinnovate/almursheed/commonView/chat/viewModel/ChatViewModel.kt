package com.visualinnovate.almursheed.commonView.chat.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.MyApplication
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.realTime.RealTimeEventListener
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.commonView.chat.model.Message
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils.conversationId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appContext: Application,
) : BaseViewModel(apiService, appContext) {

    private val _messages: MutableLiveData<ArrayList<Message>?> = MutableLiveData()
    val messages: LiveData<ArrayList<Message>?> = _messages.toSingleEvent()

    private val realTimeManager by lazy {
        (appContext as MyApplication).realTimeManager
    }
    private val PUSHER_CHANNEL_NAME = "Replay" // replay_1
    private val PUSHER_EVENT_NAME = "SendReplay"

    private val messagesArray = ArrayList<Message>()

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    init {
        if (conversationId != null) {
            getMessages()
            initializePusherForReplyMessages()
        }
    }

    fun sendMessage(message: String) {
        if (conversationId != null) {
            sendMessages(message)
        } else {
            createConversation(message)
        }
    }
    private fun createConversation(message: String) {
        _loading.value = true
        viewModelScope.launch {
            safeApiCall {
                apiService.createConversation(message)
            }.collect {
                when (it) {
                    is ResponseHandler.Success -> {
                        conversationId = it.data?.conversationId
                        messagesArray.clear()
                        it.data?.conversation?.let { it1 -> messagesArray.addAll(it1) }
                        _messages.value = messagesArray
                        _loading.value = false
                    }
                    is ResponseHandler.Error -> {
                        _loading.value = false
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getMessages() {
        _loading.value = true
        viewModelScope.launch {
            safeApiCall {
                apiService.getMessages() // conversationId
            }.collect {
                when (it) {
                    is ResponseHandler.Success -> {
                        conversationId = it.data?.conversationId
                        messagesArray.clear()
                        it.data?.conversation?.let { it1 -> messagesArray.addAll(it1) }
                        _messages.value = messagesArray
                        _loading.value = false
                    }
                    is ResponseHandler.Error -> {
                        _loading.value = false
                    }
                    else -> { }
                }
            }
        }
    }

    private fun sendMessages(message: String) {
        _loading.value = true
        viewModelScope.launch {
            safeApiCall {
                apiService.sendMessage(message, conversationId)
            }.collect {
                when (it) {
                    is ResponseHandler.Success -> {
                        messagesArray.add(it.data?.message!!)
                        _messages.value = messagesArray
                        _loading.value = false
                    }
                    is ResponseHandler.Error -> {
                        _loading.value = false
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initializePusherForReplyMessages() {
        realTimeManager.connect()
        // update request realtime
        realTimeManager.addEventListener(
            PUSHER_CHANNEL_NAME, // + conversationId
            PUSHER_EVENT_NAME,
            object : RealTimeEventListener {
                override fun onEvent(eventData: String) {
                    try {
//                      val temp: ArrayList<RequestModel> = ArrayList()
                        val gson = Gson()
                        Log.d("MyDebugData", "initializePusherForReplyMessages : onEvent :  $eventData")

//                            val newRequest = gson.fromJson(eventData, RequestModel::class.java)
//                            temp.add(newRequest)
//                            if (temp.isNotEmpty()) {
//                                if (temp[0].deleted_at == null) {
//                                    getStudents(temp)
//                                }
//                            }
                    } catch (ex: Exception) {
                        println(ex.localizedMessage)
                    }
                }
            },
        )
    }

    override fun onCleared() {
        super.onCleared()
        realTimeManager.removeEventListener(
            PUSHER_CHANNEL_NAME + conversationId,
            PUSHER_EVENT_NAME,
        )
        realTimeManager.disconnect()
    }
}
