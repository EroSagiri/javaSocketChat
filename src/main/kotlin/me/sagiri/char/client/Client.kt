package me.sagiri.char.client

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.Exception

class Client : Thread{
    var sockt : Socket = Socket()
    val logger = Logger.getLogger("Client").apply {
        level = Level.OFF
    }
    var dis = DataInputStream(null)
    var dos = DataOutputStream(null)
    var hostname = ""
    val port : Int;
    var userName = ""
    var connectE : ClientConnectEvent? = null;
    var msgE : ClientMsgEvent? = null;

    constructor(hostname : String = "localhost", port : Int = 8888, userName : String = "null") {
        this.hostname = hostname
        this.port = port
        this.userName = userName
    }

    fun connect() {
        try {
            sockt = Socket(hostname, port)
            connectE?.onConnect()
            logger.info("connect ${hostname}:${port}")
            dis = DataInputStream(sockt.getInputStream())
            dos = DataOutputStream(sockt.getOutputStream())
            start()

        } catch (e: java.lang.Exception) {
            connectE?.onFailed()
            logger.info("connect fail")
        }
    }

    fun close() {
        dis.close()
        dos.close()
        sockt.close()
    }

    fun sendMsg(msg : String) {
        if(!sockt.isClosed) {
            try {
                dos.writeUTF(msg)
            } catch (e: Exception) {
                logger.info(e.toString())
            }
        } else {
            logger.info("socket closed")
        }
    }

    fun setConnectEvent(e : ClientConnectEvent) {
        this.connectE = e
    }

    fun  setMsgEvent(e : ClientMsgEvent) {
        this.msgE = e;
    }

    override fun run() {
        super.run()
        Thread(object : Runnable {
            override fun run() {
                var msg = ""
                while (msg != "/quit" && !sockt.isClosed) {
                    try {
                        msg = readLine().toString()
                        dos.writeUTF(msg)
                    } catch (e : Exception) {
                        sockt.close()
                        connectE?.onClose()
                        break
                    }
                }
            }
        }).start()

        Thread(object : Runnable {
            override fun run() {
                while (!sockt.isClosed) {
                    try {
                        val msg = dis.readUTF()
                        logger.info(msg)
                        if (msg.matches(Regex("^/(.+)"))) {
                            logger.info(msg)
                            if (msg.matches(Regex("/getUser"))) {
                                logger.info(userName)
                                dos.writeUTF(userName)
                            }
                        } else {
                            msgE?.onMsg(msg)
                        }
                    } catch (e: Exception) {
                        sockt.close()
                        connectE?.onClose()
                        break
                    }
                }
            }
        }).start()
    }
}
