package com.cadena.ragnarok.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.cadena.ragnarok.RagnarokMain.Companion.UNIT_SCALE
import com.cadena.ragnarok.component.*
import com.cadena.ragnarok.component.PhysicComponent.Companion.physicCmpFromImage
import com.cadena.ragnarok.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val phWorld:World,
    private val atlas:TextureAtlas,
    private val spawnCmps:ComponentMapper<SpawnComponent>
) : EventListener, IteratingSystem(){
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]){
            val cfg = spawnCfg(type)
            val relativeSize = size(cfg.model)
            world.entity{

                //Carga de la Imagen
                val imageCmp = add<ImageComponent>{
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }

                //Carga de la Animación
                add<AnimationComponent>{
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                //Carga de las físicas
                physicCmpFromImage(phWorld, imageCmp.image, cfg.bodyType){ phCmp, width, height ->
                    val w = width*cfg.physicScaling.x
                    val h = height*cfg.physicScaling.y

                    box(w, h, cfg.physicOffset){
                        isSensor = false
                        //userData = "INTERACTION_SENSOR"
                    }
                }

                if(cfg.speedScaling > 0f){
                    add<MoveComponent>{
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }

                if(type == "novice_male"){
                    add<PlayerComponent>()
                }

            }
        }
        world.remove(entity)
    }

    private fun spawnCfg(type:String):SpawnCfg = cachedCfgs.getOrPut(type){
        when (type) {
            "novice_male" -> SpawnCfg(
                AnimationModel.NOVICE_MALE,
                physicScaling = vec2(0.9f, 1.2f),
                physicOffset = vec2(0f, +10f*UNIT_SCALE)
            )
            "poring" -> SpawnCfg(
                AnimationModel.PORING,
                physicScaling = vec2(0.9f, 0.9f)
            )
            "npc_kafra" -> SpawnCfg(
                AnimationModel.NPC_KAFRA,
                speedScaling = 0f,
                physicScaling = vec2(0.9f, 0.9f),
                bodyType = StaticBody
            )
            else -> gdxError("Type $type has no SpawnCfg setup.")
        }
    }

    private fun size(model : AnimationModel) = cachedSizes.getOrPut(model){
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if (regions.isEmpty) {
            gdxError("There are no texture regions for $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth* UNIT_SCALE, firstFrame.originalHeight* UNIT_SCALE)

    }

    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent ->{
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach { mapObj ->
                    val type = mapObj.type ?: gdxError("MapObject $mapObj does not have a type")
                    world.entity{
                        add<SpawnComponent>{
                            this.type = type
                            this.location.set(mapObj.x*UNIT_SCALE, mapObj.y*UNIT_SCALE)
                        }
                    }
                }
                return true
            }

        }
        return false
    }
}
