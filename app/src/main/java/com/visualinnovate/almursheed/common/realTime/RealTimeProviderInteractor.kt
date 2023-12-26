package com.visualinnovate.almursheed.common.realTime

interface RealTimeProviderInteractor {
    fun connect()
    fun disconnect()
    fun isConnectionEstablished(): Boolean
    fun addEventListener(channelName: String, eventName: String, listener: RealTimeEventListener)
    fun removeEventListener(channelName: String, eventName: String)
}