package com.cadena.ragnarok.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.cadena.ragnarok.component.ImageComponent.Companion.ImageComponentListener
import com.cadena.ragnarok.component.PhysicComponent.Companion.PhysicComponentListener
import com.cadena.ragnarok.event.MapChangeEvent
import com.cadena.ragnarok.event.fire
import com.cadena.ragnarok.input.PlayerKeyboardInputProcessor
import com.cadena.ragnarok.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.mysticwoods.system.AnimationSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {

    private val stage : Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas("assets/graphics/ragnarokObjects.atlas")
    private var currentMap : TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld:World = World{
        inject(stage)
        inject(textureAtlas)
        inject(phWorld)

        componentListener<ImageComponentListener>()
        componentListener<PhysicComponentListener>()

        system<EntitySpawnSystem>()
        system<MoveSystem>()
        system<PhysicSystem>()
        system<AnimationSystem>()
        system<CameraSystem>()
        system<RenderSystem>()
        system<DebugSystem>()
    }

    override fun show() {
        log.debug { "GameScreen HelloWorld!" }

        eWorld.systems.forEach { system ->
            if(system is EventListener){
                stage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load("map/map1.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(eWorld, eWorld.mapper())
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
       eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        stage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }

    companion object{
        private val log = logger<GameScreen>()
    }
}
