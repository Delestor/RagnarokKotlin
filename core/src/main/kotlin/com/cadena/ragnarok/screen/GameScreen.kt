package com.cadena.ragnarok.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.cadena.ragnarok.component.ImageComponent
import com.cadena.ragnarok.component.ImageComponent.Companion.ImageComponentListener
import com.cadena.ragnarok.system.RenderSystem
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {

    private val stage : Stage = Stage(ExtendViewport(16f, 9f))
    private val playerTexture:Texture = Texture("assets/graphics/Novice_Male.png")
    private val poringTexture:Texture = Texture("assets/graphics/Poring_SantaPoring_Angeling.png")
    private val world:World = World{
        inject(stage)

        componentListener<ImageComponentListener>()

        system<RenderSystem>()
    }

    override fun show() {
        log.debug { "GameScreen HelloWorld!" }

        world.entity{
            add<ImageComponent>{
                image = Image(TextureRegion(playerTexture, 48, 88)).apply {
                    setSize(3f, 4.5f,)
                }
            }
        }

        world.entity{
            add<ImageComponent>{
                image = Image(TextureRegion(poringTexture, 48, 48)).apply {
                    setSize(2f, 2f,)
                    setPosition(12f, 0f)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun render(delta: Float) {
       world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        playerTexture.disposeSafely()
        poringTexture.disposeSafely()
        world.dispose()
    }

    companion object{
        private val log = logger<GameScreen>()
    }
}
