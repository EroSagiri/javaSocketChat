package me.sagiri.char.server

import java.net.Socket

class User {
    var socket : Socket? = null
    var name = ""

    init {

    }

    constructor(socket : Socket, name : String) {
        this.socket = socket
        this.name = name
    }

    fun close() {
        socket?.close()
    }
}