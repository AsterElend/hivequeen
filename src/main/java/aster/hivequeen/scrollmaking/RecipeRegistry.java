package aster.hivequeen.scrollmaking;

import aster.hivequeen.HiveQueen;
import aster.hivequeen.HiveQueenItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class RecipeRegistry {
    public static  RecipeType<ClockworkHiveRecipe> CLOCKWORK_HIVE_RECIPE_TYPE;
    public static  RecipeSerializer<ClockworkHiveRecipe> CLOCKWORK_HIVE_RECIPE_SERIALIZER;
    public static  Block CLOCKWORK_HIVE;

    public static void register(){
        CLOCKWORK_HIVE_RECIPE_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                HiveQueen.makeId("clockwork_hive"),
                new RecipeType<ClockworkHiveRecipe>() {
                    @Override
                    public String toString() {
                        return "hivequeen:clockwork_hive";
                    }
                }
        );

        CLOCKWORK_HIVE_RECIPE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                HiveQueen.makeId("clockwork_hive"),
                new ClockworkHiveRecipe.Serializer()
        );

        CLOCKWORK_HIVE = Registry.register(Registries.BLOCK,
                HiveQueen.makeId("clockwork_hive"), new ClockworkHiveBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).requiresTool().sounds(BlockSoundGroup.DEEPSLATE_BRICKS)));

    HiveQueenItems.registerItem("clockwork_hive", new BlockItem(CLOCKWORK_HIVE, new FabricItemSettings()));

    }
}
