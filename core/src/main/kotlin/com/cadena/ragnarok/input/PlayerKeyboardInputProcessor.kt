package com.cadena.ragnarok.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.*
import com.cadena.ragnarok.component.MoveComponent
import com.cadena.ragnarok.component.PlayerComponent
import com.cadena.ragnarok.connection.ConnectionSocket
import com.cadena.ragnarok.screen.GameScreen
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import kotlinx.coroutines.*
import ktx.app.KtxInputAdapter
import ktx.log.logger

class PlayerKeyboardInputProcessor(
    world: World,
    private val moveCmps: ComponentMapper<MoveComponent>
) : KtxInputAdapter {

    private var playerSin = 0f
    private var playerCos = 0f

    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = this
    }

    private fun updatePlayerMovement() {
        playerEntities.forEach { player ->
            with(moveCmps[player]){
                cos = playerCos
                sin = playerSin
            }
        }
    }

    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = 1f
                DOWN -> playerSin = -1f
                RIGHT -> playerCos = 1f
                LEFT -> playerCos = -1f
            }
            scope.taskSendMessageToServer(keycode.toString())
            updatePlayerMovement()
            return true
        }
        return false
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    private var job2 : Job? = null

    private fun CoroutineScope.taskSendMessageToServer(tecla:String){
        job2 = launch {
            log.debug {"===Conexión==="}
            try {
                val connection = ConnectionSocket()
                connection.sendMessageToServer("Soy el cliente, y he presionado la tecla $tecla")
            }catch (e: Exception){
                log.debug { "The Server is not reachable." }
            }
            log.debug {"===Finaliza Conexión==="}
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return super.keyUp(keycode)
    }

    companion object{
        private val log = logger<PlayerKeyboardInputProcessor>()
    }

}
