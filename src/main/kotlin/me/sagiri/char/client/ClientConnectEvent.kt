package me.sagiri.char.client

interface ClientConnectEvent {
    open fun onConnect() {

    }
    open fun onFailed() {

    }
    open fun onClose() {

    }
}
