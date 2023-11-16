package com.visualinnovate.almursheed.common.realTime

import android.util.Log
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange

class PusherProvider(private val apiKey: String, private val cluster: String) : RealTimeProviderInteractor {

    private val subscribedChannels = mutableSetOf<String>()
    private var isConnected = false

    private val options = PusherOptions().apply {
        setCluster(cluster)
    }
    private val pusher: Pusher = Pusher(apiKey, options)

    override fun connect() {
        if (pusher.connection.state != ConnectionState.CONNECTED) {
            pusher.connect(
                object : ConnectionEventListener {
                    override fun onConnectionStateChange(change: ConnectionStateChange) {
                        // Update the connection state flag
                        isConnected = change.currentState == ConnectionState.CONNECTED
                        Log.d("here", "$isConnected")
                    }

                    override fun onError(message: String?, code: String?, e: Exception?) { print(e?.localizedMessage) } },
                ConnectionState.ALL,
            )
        } else {
            Log.d("Connection State ", "Connected")
        }
    }

    override fun disconnect() {
        pusher.disconnect()
    }

    override fun isConnectionEstablished(): Boolean {
        return isConnected
    }

    override fun addEventListener(channelName: String, eventName: String, listener: RealTimeEventListener) {
        if (!isConnected) {
            if (!isSubscribed(channelName)) {
                val channel = pusher.subscribe(channelName)
                subscribedChannels.add(channelName) // Track the subscription
                channel.bind(eventName) { event ->
                    listener.onEvent(event.data)
                }
            } else { // listen for more than one event at same channelName
                pusher.getChannel(channelName).bind(eventName) { event ->
                    listener.onEvent(event.data)
                }
            }
        }
    }

    override fun removeEventListener(channelName: String, eventName: String, listener: RealTimeEventListener) {
        // Implement removing the listener
        pusher.unsubscribe(channelName)
        subscribedChannels.remove(channelName)
    }

    private fun isSubscribed(channelName: String): Boolean {
        return channelName in subscribedChannels
    }
}