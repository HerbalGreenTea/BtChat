package com.example.btchat

enum class ConnectionState {
    STATE_NONE,      // we're doing nothing
    STATE_LISTEN,     // now listening for incoming connections
    STATE_CONNECTING, // now initiating an outgoing connection
    STATE_CONNECTED  // now connected to a remote device
}