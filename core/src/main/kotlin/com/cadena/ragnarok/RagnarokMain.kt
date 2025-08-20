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
    }



    companion object{
        const val UNIT_SCALE = 1/64f
        private val log = logger<GameScreen>()
    }
}


