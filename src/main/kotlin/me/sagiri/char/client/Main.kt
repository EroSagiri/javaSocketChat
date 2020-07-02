package me.sagiri.char.client

fun main(argv : Array<String>) {
    var port = 8888
    if("-port" in argv) {
        for(i in 0..argv.size-1) {
            if(argv[i] == "-port") {
                port = argv[i+1].toInt()
                break
            }
        }
    }
    val c = Client("localhost", 8888, "sagiri")
    c.start()
//    Thread(object : Runnable{
//        override fun run() {
//            Thread.sleep(2000)
//            c.sendMsg("Hi,Sagiri")
//        }
//    }).start()
}