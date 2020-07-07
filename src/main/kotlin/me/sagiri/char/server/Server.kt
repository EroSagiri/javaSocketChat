package me.sagiri.char.server

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Logger
import java.util.regex.Pattern

class Server: Thread {
    var serverSocket = ServerSocket()
    val users = arrayListOf<User>()
    val logger = Logger.getLogger("server")
    constructor(port : Int = 8888) {
        serverSocket = ServerSocket(port)
        logger.info("listen port ${port}")
    }
    override fun run() {
        super.run()
        while (!serverSocket.isClosed) {
            logger.info("wait...")
            val clientSocket = serverSocket.accept()
            val dis = DataInputStream(clientSocket.getInputStream())
            val dos = DataOutputStream(clientSocket.getOutputStream())

            logger.info("connected from ip ${clientSocket.inetAddress.hostAddress} users.size: ${users.size}")
            try {
                dos.writeUTF("/getUser")
                val userName = dis.readUTF()
                val user = User(clientSocket, userName)
                dos.writeUTF("you name set ${userName}")
                users.add(user)
                logger.info("user ${userName} join")
                users.forEach{
                    if(it.socket != clientSocket) {
                        DataOutputStream(it.socket?.getOutputStream()).writeUTF("${userName} join")
                    }
                }

                Thread(object : Runnable {
                    override fun run() {
                        while (!clientSocket.isClosed) {
                            try {
                                val msg = dis.readUTF()
                                logger.info("${user.name}@${user.socket?.inetAddress?.hostAddress}: ${msg}")

                                // 处理指令
                                if(msg.matches(Regex("^/(.+)"))) {
                                    if(msg.matches(Regex("^/list"))) {
                                        var s = ""
                                        users.forEach {
                                            s += "${it.name} "
                                        }
                                        dos.writeUTF(s)
                                    }
                                    // change name
                                    var m = Pattern.compile("/name\b+(.+)").matcher(msg)
                                    if(m.find()) {
                                        logger.info("changge ${user.name} ${m.group()}")
                                    }
                                } else {
                                    // 广播消息
                                    sendAll(msg, clientSocket)
                                }
                            } catch (e : Exception) {
                                logger.info(e.toString())
                                user.close()
                                val quitName = user.name
                                users.remove(user)
                                users.forEach {
                                    DataOutputStream(it.socket?.getOutputStream()).writeUTF("$quitName quit")
                                }
                                break
                            }
                        }
                        user.close()
                        users.remove(user)
                    }

                    fun sendAll(msg : String, clientSocket : Socket? =null) {
                        users.forEach {
                            if (it.socket != clientSocket && clientSocket != null) {
                                DataOutputStream(it.socket?.getOutputStream()).writeUTF("${user.name}: ${msg}")
                            }
                        }
                    }
                }).start()
            } catch (e: Exception) {
                clientSocket.close()
            }

        }
    }
}