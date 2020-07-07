package me.sagiri.char.client

fun main(argv : Array<String>) {
    print("host: ")
    val host = readLine()
    print("port: ")
    val tmp = readLine()
    val port = if (tmp != null) tmp.toInt() else null

    if (host != null && port != null) {
        print("name: ")
        val name = readLine()
        if (name != null) {
            val c = Client(host, port, name)
            c.setConnectEvent(object : ClientConnectEvent {
                override fun onConnect() {
                    println("连接成功")
                }

                override fun onFailed() {
                    println("连接失败")
                }

                override fun onClose() {

                }
            })
            c.setMsgEvent(object : ClientMsgEvent{
                override fun onMsg(msg: String) {
                    println(msg)
                }
            })
            c.connect()
        }
    }
}