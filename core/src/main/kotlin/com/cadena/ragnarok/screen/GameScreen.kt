package com.cadena.ragnarok.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.cadena.ragnarok.component.AnimationComponent
import com.cadena.ragnarok.component.AnimationType
import com.cadena.ragnarok.component.ImageComponent
import com.cadena.ragnarok.component.ImageComponent.Companion.ImageComponentListener
import com.cadena.ragnarok.system.RenderSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.mysticwoods.system.AnimationSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {

    private val stage : Stage = Stage(ExtendViewport(16f, 9f))

    private val textureAtlas = TextureAtlas("assets/graphics/ragnarokObjects.atlas")

    private val world:World = World{
        inject(stage)
        inject(textureAtlas)

        componentListener<ImageComponentListener>()

        system<AnimationSystem>()
        system<RenderSystem>()
    }

    override fun show() {
        log.debug { "GameScreen HelloWorld!" }

        world.entity{
            add<ImageComponent>{
                image = Image().apply {
                    setSize(3f, 4.5f,)
                }
            }
            add<AnimationComponent>{
                nextAnimation("Novie_Male", AnimationType.WALK_LEFT)
            }
        }

        world.entity{
            add<ImageComponent>{
                image = Image().apply {
                    setSize(2f, 2f,)
                    setPosition(12f, 0f)
                }
            }
            add<AnimationComponent>{
                nextAnimation("Novie_Male", AnimationType.WALK_LEFT)
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
        textureAtlas.disposeSafely()
        world.dispose()
    }

    companion object{
        private val log = logger<GameScreen>()
    }
}
