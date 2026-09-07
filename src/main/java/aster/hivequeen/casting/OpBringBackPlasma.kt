package aster.hivequeen.casting

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.blocks.behavior.HexComposting
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageSources
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import ram.talia.hexal.common.casting.actions.spells.OpParticles
import ram.talia.hexal.common.network.MsgParticleLinesAck
import kotlin.math.pow
object OpBringBackPlasma : SpellAction {
    override val argc = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val startPos = args.getVec3(0, argc)
        val endPos = args.getVec3(1, argc)
        env.assertVecInRange(startPos)
        env.assertVecInRange(endPos)
        val distance = startPos.distanceTo(endPos)
        val cost = distance.pow(3)

        return SpellAction.Result(Spell(startPos, endPos), cost.toLong(), listOf())
    }

    private data class Spell(val startPos: Vec3d, val endPos: Vec3d) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val blockCast = env.world.raycast(
                RaycastContext(
                    startPos, endPos, RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.ANY, null
                )
            )
            val whereWeActuallyStop = blockCast?.pos ?: endPos

            val hitEntities = mutableListOf<LivingEntity>()
            var latestHit: EntityHitResult?

            // Loop bounding box setup
            val scanBox = Box(startPos, whereWeActuallyStop).expand(1.0)

            do {
                // Fixed filter: we check that the list does NOT contain 'it' yet
                latestHit = ProjectileUtil.raycast(
                    env.castingEntity,
                    startPos,
                    whereWeActuallyStop,
                    scanBox,
                    { it is LivingEntity && it.isAlive && !hitEntities.contains(it) },
                    startPos.distanceTo(whereWeActuallyStop) // Use the real maximum distance
                )

                if (latestHit != null && latestHit.entity is LivingEntity) {
                    hitEntities.add(latestHit.entity as LivingEntity)
                }
            } while (latestHit != null && hitEntities.size < 100) // Added a safety cap to guarantee no infinite freezes

            // Damage the targets
            for (target in hitEntities) {
                target.damage(target.damageSources.magic(), 4f)
            }

            // Handle block breaking
            if (blockCast != null) {
                val hitBlockState = env.world.getBlockState(blockCast.blockPos)
                val hitBlockType = hitBlockState.block
                // Double/Float structural safety mapping
                if (hitBlockType.hardness in 0.0..2.5) {
                    if (env.world.random.nextBetween(0, 5) >= hitBlockType.hardness
                        && IXplatAbstractions.INSTANCE.isBreakingAllowed(env.world, blockCast.blockPos, hitBlockState, env.caster)
                    ) {
                        env.world.breakBlock(blockCast.blockPos, true, env.caster)
                    }
                }
            }

            // Sync rendering packet safely
            IXplatAbstractions.INSTANCE.sendPacketNear(
                startPos, 128.0, env.world, MsgParticleLinesAck(
                    listOf(startPos, whereWeActuallyStop),
                    IXplatAbstractions.INSTANCE.getPigment(env.caster)
                )
            )
        }
    }


}
