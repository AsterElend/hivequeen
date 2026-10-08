package aster.hivequeen.casting

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getBool
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.ktxt.UseOnContext
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import vazkii.botania.common.item.BotaniaItems

object OpBurgeon: SpellAction{
    override val argc = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val target = args.getBlockPos(0, argc)
        val flowers = args.getBool(1, argc)
        env.assertPosInRangeForEditing(target)
        var multipler = 1;
        if (flowers) multipler = 2;
        return SpellAction.Result(
            Spell(target, flowers),
            (MediaConstants.DUST_UNIT * 1.125 * 5 * multipler).toLong(),
            listOf(ParticleSpray.burst(Vec3d.ofCenter(BlockPos(target).up()), 1.0))
        )
    }

    private data class Spell(val pos: BlockPos, val flowers: Boolean ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val hit = BlockHitResult(Vec3d.ZERO, Direction.UP, pos, false)
            val fakeContext = UseOnContext(env.world, env.castingEntity as? ServerPlayerEntity, Hand.MAIN_HAND,
                ItemStack(BotaniaItems.fertilizer), hit)
                val otherFakeContext = UseOnContext(env.world, env.castingEntity as? ServerPlayerEntity, Hand.MAIN_HAND,
                    ItemStack(BotaniaItems.grassSeeds), hit)
            if (flowers){
               BotaniaItems.fertilizer.useOnBlock(fakeContext)
            } else {
                BotaniaItems.grassSeeds.useOnBlock(otherFakeContext);
            }

        }
    }
}