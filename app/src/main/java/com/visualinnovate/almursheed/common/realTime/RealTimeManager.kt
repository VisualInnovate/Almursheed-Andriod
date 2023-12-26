package com.visualinnovate.almursheed.common.realTime

import java.util.concurrent.CopyOnWriteArrayList

class RealTimeManager(private val provider: RealTimeProviderInteractor) {

    private val eventListeners = HashMap<String, MutableMap<String, MutableList<RealTimeEventListener>>>()

    fun connect() {
        provider.connect()
    }

    fun disconnect() {
        provider.disconnect()
        eventListeners.clear()
    }

    fun addEventListener(channelName: String, eventName: String, listener: RealTimeEventListener) {
        val channelListeners = eventListeners.getOrPut(channelName) { mutableMapOf() }
        val listeners = channelListeners.getOrPut(eventName) { CopyOnWriteArrayList() }
        listeners.add(listener)
        provider.addEventListener(channelName, eventName, listener)
    }

    fun removeEventListener(channelName: String, eventName: String) {
        provider.removeEventListener(channelName, eventName)
    }

}
