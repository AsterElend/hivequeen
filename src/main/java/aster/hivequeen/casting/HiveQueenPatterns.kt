package aster.hivequeen.casting


import aster.hivequeen.HiveQueen.makeId
import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.registry.Registry

object HiveQueenPatterns {
    @JvmStatic
    fun init(){
        val loader = FabricLoader.getInstance()
        register("bangle_break", "adaeqaqqqqqedad", HexDir.SOUTH_EAST, OpBangleBreak)
        register("toggle_healthcast", "awwwaqwwaadada", HexDir.SOUTH_WEST, OpToggleOvercast)
        register("plasma_beam", "aqqqadweaqa", HexDir.NORTH_EAST, OpBringBackPlasma)
        if (loader.isModLoaded("botania")){
            register("burgeon", "wqaqwwedewwqaqw", HexDir.NORTH_EAST, OpBurgeon)
        }
    }

    private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
        Registry.register(HexActions.REGISTRY, makeId(name),
            ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action)
        )
}
