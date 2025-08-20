package com.cadena.ragnarok

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.SocketHints
import ktx.assets.disposeSafely
import ktx.log.logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main(){
    Thread{senMessageToServer()}.start()
    Thread{senMessageToServer()}.start()
}

fun senMessageToServer(){
    val hints = SocketHints()
    //hints.connectTimeout = 4000
    //val client = Gdx.net.newClientSocket(Net.Protocol.TCP , "localhost", 9999, hints);
    val client = Socket("localhost", 9999)

    val waitTime:Long = 2000

    client.sendMessage("Hola Mundo")
    Thread.sleep(waitTime)
    client.sendMessage("Mensaje 2")
    Thread.sleep(waitTime)
    client.sendMessage("Mensaje 3")
    Thread.sleep(waitTime)
    client.sendMessage("Mensaje 4")
    Thread.sleep(waitTime)
    client.sendMessage("Mensaje 5")

    client.close()
}

private fun Socket.sendMessage(mensaje:String) {
    val output = PrintWriter(this.outputStream, true)
    val input = BufferedReader(InputStreamReader(this.inputStream))

    println("El cliente envia: [$mensaje]")
    output.println(mensaje)

    println("El cliente recibe: [${input.readLine()}]")
}


