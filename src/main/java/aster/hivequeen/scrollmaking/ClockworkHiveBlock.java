package aster.hivequeen.scrollmaking;

import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class ClockworkHiveBlock extends Block {
    public ClockworkHiveBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;
        ServerWorld serverWorld = (ServerWorld) world;
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isOf(HexItems.SCROLL_LARGE)) {
            Box box = new Box(pos).expand(4); // Fixed: box.expand returns a new Box instance
            List<ItemEntity> items = serverWorld.getEntitiesByClass(ItemEntity.class, box, e -> !e.getStack().isEmpty());
            if (items.isEmpty()) return ActionResult.PASS;

            // 1. Build the SimpleInventory for the recipe manager to check
            List<ItemStack> floatingStacks = new ArrayList<>();
            for (ItemEntity ent : items) {
                floatingStacks.add(ent.getStack());
            }

            SimpleInventory inv = new SimpleInventory(floatingStacks.toArray(new ItemStack[0]));

            // 2. Check for recipe match
            Optional<ClockworkHiveRecipe> match = serverWorld.getRecipeManager().getFirstMatch(RecipeRegistry.CLOCKWORK_HIVE_RECIPE_TYPE, inv, serverWorld);
            if (match.isEmpty()) return ActionResult.PASS;

            ClockworkHiveRecipe recipe = match.get();

            // 3. Match ingredients back to the original physical ItemEntities to consume them
            List<ItemEntity> remainingEntities = new ArrayList<>(items);
            for (Ingredient ingredient : recipe.getInputs()) { // Assumes a getter for ingredients exists
                Iterator<ItemEntity> iter = remainingEntities.iterator();
                while (iter.hasNext()) {
                    ItemEntity itemEntity = iter.next();
                    ItemStack entityStack = itemEntity.getStack();

                    if (ingredient.test(entityStack)) {
                        // Decrement the physical item stack
                        entityStack.decrement(1);

                        // If stack is empty, remove the entity entirely, otherwise update it
                        if (entityStack.isEmpty()) {
                            itemEntity.discard();
                        } else {
                            itemEntity.setStack(entityStack);
                        }

                        iter.remove(); // Remove from matching pool so it isn't consumed twice
                        break;
                    }
                }
            }

            stack.decrement(1);
            // 4. Generate and give the output
            ItemStack output = recipe.getOurScroll();
            ItemEntity outputEntity = new ItemEntity(serverWorld, pos.toCenterPos().getX(), pos.up().toCenterPos().getY(), pos.toCenterPos().getZ(), output);
            outputEntity.setVelocity(0, 1, 0);
            world.spawnEntity(outputEntity);
            ParticleSpray spray = ParticleSpray.burst(Vec3d.ofCenter(pos.up()), 64.0, recipe.getIngredients().size());
            spray.sprayParticles(serverWorld, IXplatAbstractions.INSTANCE.getPigment(player));
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            player.playSound(SoundEvents.ENTITY_BEE_LOOP, 1, 1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }


}
