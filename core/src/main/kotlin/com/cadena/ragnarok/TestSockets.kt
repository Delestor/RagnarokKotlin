package com.cadena.ragnarok

import com.cadena.ragnarok.connection.ConnectionSocket
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
fun main(){
    var connection = ConnectionSocket()
    connection.sendMessageToServer()
    /*Thread{senMessageToServer()}.start()
    Thread{senMessageToServer()}.start()*/
}*/

fun main() = runBlocking {
    val jobs = List(100) { i ->
        launch {
            println("===Conexión $i===")
            var connection = ConnectionSocket()
            connection.sendMessageToServer("Soy el cliente $i")
            println("===Finaliza Conexión $i===")
        }
    }
    jobs.forEach { it.join() }
}
