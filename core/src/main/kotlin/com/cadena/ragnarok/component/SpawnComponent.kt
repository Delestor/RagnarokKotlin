package com.cadena.ragnarok.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

data class SpawnCfg(
    val model: AnimationModel
    //Aquí irian los atributos de speed, attackDamage
)

data class SpawnComponent(
    var type:String = "",
    var location: Vector2 = vec2()
) {

}
