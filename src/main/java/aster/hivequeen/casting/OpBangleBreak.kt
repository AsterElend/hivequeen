package aster.hivequeen.casting

import aster.hivequeen.HiveQueenTags
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.mod.HexConfig
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.xplat.IXplatAbstractions
import de.dafuqs.spectrum.enchantments.VoidingEnchantment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import ram.talia.hexal.api.getMoteOrItemStackOrItemEntity

object OpBangleBreak : SpellAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val asVec = args.getVec3(1, argc)
        val pos = BlockPos.ofFloored(asVec)
        env.assertPosInRangeForEditing(pos)
        val world = env.world

        val maybeItemStuff = args.getMoteOrItemStackOrItemEntity(0, argc) ?:
        throw MishapInvalidIota(args[0], 0, Text.of("ItemEntity, ItemStack or Mote!"))


       val tool: ItemStack = maybeItemStuff.flatMap<ItemStack>(
           aMap = { a -> val stack = ItemStack(a.item, a.count.toInt())
           stack.nbt = a.tag
               stack},
           bMap = { b ->  b },
           cMap = { c -> when (c){
               is ItemEntity -> c.stack
               else -> ItemStack.EMPTY
           } }
       )

      
    return SpellAction.Result(Spell(pos, world, tool), calcCost(tool, world, pos), listOf(ParticleSpray.burst(Vec3d.ofCenter(pos), 1.0)))
       
    }


    fun calcCost(bangle: ItemStack, world: World, pos: BlockPos): Long {
        val enchants: Map<Enchantment?, Int?> = EnchantmentHelper.get(bangle) ?: return 0
        var cost = MediaConstants.DUST_UNIT / 8;

        val isCheap = world.getBlockState(pos).isIn(HexTags.Blocks.CHEAP_TO_BREAK_BLOCK);
        if (isCheap)  cost = MediaConstants.DUST_UNIT / 100
        for (enchantment in enchants) {
            if (FabricLoader.getInstance().isModLoaded("spectrum") && enchantment is VoidingEnchantment){
                cost = (MediaConstants.DUST_UNIT / 100L)
                break
            }

            if (doWeCareAboutThisEnchantment(world, enchantment, )){
                cost += (enchantment.value!! * MediaConstants.DUST_UNIT)
            }

            }

        return cost
    }

    private fun doWeCareAboutThisEnchantment(world: World, entry: Map.Entry<Enchantment?, Int?>): Boolean{
        if (entry.key == null) return false
        val key = entry.key!!
        val registryFetch = world.registryManager.get(RegistryKeys.ENCHANTMENT).getEntry(key) ?: return true
        if (registryFetch.isIn(HiveQueenTags.SKIP_BREAK_COST_INCREMENT)) return false;
        return true
    }

    private data class Spell(val pos: BlockPos, val world: World, val stack: ItemStack) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val state = env.world.getBlockState(pos)
            val enchants = EnchantmentHelper.get(stack);
            val tier = HexConfig.server().opBreakHarvestLevel()
            if (
                !state.isAir
                && state.getHardness(env.world, pos) >= 0f
                && IXplatAbstractions.INSTANCE.isCorrectTierForDrops(tier, state)
                && IXplatAbstractions.INSTANCE.isBreakingAllowed(env.world, pos, state, env.castingEntity as? ServerPlayerEntity)
            ) {
                val blockEntity = env.world.getBlockEntity(pos)
                Block.dropStacks(state, env.world, pos, blockEntity, null, ItemStack(Items.DIAMOND_PICKAXE).apply
                  {
                      for ((key, value) in enchants){
                          addEnchantment(key, value)
                      }
                  }
                )
                env.world.breakBlock(pos, true, env.castingEntity as? ServerPlayerEntity)
            }
        }
    }

}
