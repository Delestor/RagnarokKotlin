package com.cadena.ragnarok

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.cadena.ragnarok.connection.ConnectionSocket
import com.cadena.ragnarok.screen.GameScreen
import kotlinx.coroutines.*
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.logger

class RagnarokMain : KtxGame<KtxScreen>(){

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()

        scope.taskSendMessageToServer()
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    private var job2 : Job? = null

    private fun CoroutineScope.taskSendMessageToServer(){
        job2 = launch {
            var i = 0
            while (true){
                println("===Conexión $i===")
                val connection = ConnectionSocket()
                connection.sendMessageToServer("Soy el cliente $i")
                println("===Finaliza Conexión $i===")
                delay(1000)
                i++
            }
        }
    }

    fun cancelScopes() {
        job2?.cancel()
        scope.cancel()
    }

    companion object{
        const val UNIT_SCALE = 1/64f
        private val log = logger<GameScreen>()
    }
}


