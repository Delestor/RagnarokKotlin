package com.cadena.ragnarok

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import com.cadena.ragnarok.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class RagnarokMain : KtxGame<KtxScreen>(){

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()

        senMessageToServer()
    }

    private fun senMessageToServer(){
        log.debug { "Preparamos para enviar el mensaje." }
        val hints = SocketHints()
        //hints.connectTimeout = 4000
        var client = Gdx.net.newClientSocket(Net.Protocol.TCP , "localhost", 9999, hints);

        val output = PrintWriter(client.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(client.inputStream))

        var mensaje = "Hello World from RagnarokKotlin"

        println("El cliente envia: [$mensaje]")
        output.println(mensaje)
        //println("Client receiving [${input.readLine()}]")
        client.disposeSafely()
    }

    companion object{
        const val UNIT_SCALE = 1/64f
        private val log = logger<GameScreen>()
    }
}
