package aster.hivequeen.casting

import aster.hivequeen.api.ToggleableOvercastEnvironment
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import ram.talia.hexal.api.casting.mishaps.MishapNonPlayer

public object OpToggleOvercast: ConstMediaAction {
    override val argc = 0
    override val mediaCost = MediaConstants.DUST_UNIT


    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        if (env !is PlayerBasedCastEnv){
            throw MishapNonPlayer()
        }

        val environment = env as ToggleableOvercastEnvironment

        val isSuppressed = environment.`hivequeen$querySuppressed`()

        when (isSuppressed) {
            true -> environment.`hivequeen$setSuppressed`(false)
            false -> environment.`hivequeen$setSuppressed`(true)
        }

        return args
    }


}