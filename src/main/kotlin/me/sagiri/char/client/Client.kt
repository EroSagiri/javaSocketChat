package me.sagiri.char.client

import com.sun.jndi.toolkit.url.Uri
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.net.ConnectException
import java.net.Socket
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.Exception

class Client : Thread{
    var sockt : Socket = Socket()
    val logger = Logger.getLogger("Client").apply {
        level = Level.INFO
    }
    var dis = DataInputStream(null)
    var dos = DataOutputStream(null)
    var userName = ""
    constructor(hostname : String = "localhost", port : Int = 8888, userName : String = "null") {
        connect(hostname, port, userName)
    }

    fun connect(hostname : String = "localhost", port : Int = 8888, userName : String = "null") {
        try {
            sockt = Socket(hostname, port)
            this.userName = userName
            logger.info("connect ${hostname}:${port}")
            dis = DataInputStream(sockt.getInputStream())
            dos = DataOutputStream(sockt.getOutputStream())

        } catch (e: java.lang.Exception) {
            logger.info("connect fail")
            println("connect failed")
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
                println(msg)
            } catch (e: Exception) {
                logger.info(e.toString())
            }
        } else {
            logger.info("socket closed")
        }
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
                            println(msg)
                        }
                    } catch (e: Exception) {
                        sockt.close()
                        break
                    }
                }
            }
        }).start()
    }
}