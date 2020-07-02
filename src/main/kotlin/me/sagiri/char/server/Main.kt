package me.sagiri.char.server

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
    Server(port).start()
}