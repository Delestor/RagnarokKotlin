package com.cadena.ragnarok

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.cadena.ragnarok.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class RagnarokMain : KtxGame<KtxScreen>(){

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    companion object{
        const val UNIT_SCALE = 1/64f
    }
}
