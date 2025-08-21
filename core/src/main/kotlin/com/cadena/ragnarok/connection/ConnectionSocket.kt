package com.cadena.ragnarok.connection

import com.badlogic.gdx.net.SocketHints
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.random.Random
import kotlin.random.nextLong

class ConnectionSocket : Socket() {

    fun sendMessageToServer(titulo: String) {
        val hints = SocketHints()
        //hints.connectTimeout = 4000
        //val client = Gdx.net.newClientSocket(Net.Protocol.TCP , "localhost", 9999, hints);
        val client = Socket("localhost", 9999)

        val waitTime: Long = Random.nextLong(0, 100)

        client.sendMessage("$titulo Hola Mundo")
        Thread.sleep(waitTime)
        client.sendMessage("$titulo Mensaje 2")
        Thread.sleep(waitTime)
        client.sendMessage("$titulo Mensaje 3")
        Thread.sleep(waitTime)
        client.sendMessage("$titulo Mensaje 4")
        Thread.sleep(waitTime)
        client.sendMessage("$titulo Mensaje 5")

        client.close()
    }

    private fun Socket.sendMessage(mensaje: String) {
        val output = PrintWriter(this.outputStream, true)
        val input = BufferedReader(InputStreamReader(this.inputStream))

        println("El cliente envia: [$mensaje]")
        output.println(mensaje)

        println("El cliente recibe: [${input.readLine()}]")
    }
}
