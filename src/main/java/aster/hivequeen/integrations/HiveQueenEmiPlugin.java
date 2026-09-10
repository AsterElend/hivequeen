package aster.hivequeen.integrations;

import aster.hivequeen.HiveQueen;
import aster.hivequeen.scrollmaking.ClockworkHiveRecipe;
import aster.hivequeen.scrollmaking.RecipeRegistry;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.fabric.interop.emi.PatternRendererEMI;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import ram.talia.hexal.common.recipe.FreezeRecipe;
import ram.talia.hexal.common.recipe.HexalRecipeTypes;

public class HiveQueenEmiPlugin implements EmiPlugin {
    private static final Identifier FREEZE_AS_TEXTURE = HiveQueen.makeId("textures/gui/freezepattern.png");
    private static final Identifier FREEZE_ID = Identifier.of("hexal", "freeze");
    public static EmiRecipeCategory FREEZE;

    public static EmiRecipeCategory CLOCKWORK_HIVE;
    private static final Identifier CLOCKWORK_HIVE_ID = HiveQueen.makeId("clockwork_hive");

    @Override
    public void register(EmiRegistry registry) {
        registry.setDefaultComparison(HexItems.SCROLL_LARGE, Comparison.compareNbt());
        RecipeManager manager = registry.getRecipeManager();


        FREEZE = new EmiRecipeCategory(
                FREEZE_ID, new PatternRendererEMI(FREEZE_ID, 16, 16),
                new EmiTexture(FREEZE_AS_TEXTURE, 0, 0, 305, 187)
        );

        registry.addCategory(FREEZE);
        registry.addWorkstation(FREEZE, EmiIngredient.of(HexTags.Items.STAVES));
        for (FreezeRecipe recipe: manager.listAllOfType(HexalRecipeTypes.Companion.getFREEZE_TYPE())){
            registry.addRecipe(new EmiFreezeRecipe(recipe));
        }


        if (!manager.listAllOfType(RecipeRegistry.CLOCKWORK_HIVE_RECIPE_TYPE).isEmpty()){
            CLOCKWORK_HIVE = new EmiRecipeCategory(CLOCKWORK_HIVE_ID, EmiStack.of(Items.HONEYCOMB), EmiStack.of(Items.HONEYCOMB));
            registry.addCategory(CLOCKWORK_HIVE);
            registry.addWorkstation(CLOCKWORK_HIVE, EmiStack.of(RecipeRegistry.CLOCKWORK_HIVE));
            for (ClockworkHiveRecipe recipe: manager.listAllOfType(RecipeRegistry.CLOCKWORK_HIVE_RECIPE_TYPE)){
                registry.addRecipe(new EmiClockworkHiveRecipe(recipe));
            }
        }



    }
}
