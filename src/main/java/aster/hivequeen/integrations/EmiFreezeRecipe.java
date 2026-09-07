package aster.hivequeen.integrations;

import aster.hivequeen.HiveQueen;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Ingredient;
import ram.talia.hexal.common.recipe.FreezeRecipe;

public class EmiFreezeRecipe extends BasicEmiRecipe {
    public EmiFreezeRecipe(FreezeRecipe recipe){
        super(HiveQueenEmiPlugin.FREEZE, recipe.getId(), 48, 32);
        this.inputs.add(EmiIngredient.of(Ingredient.ofStacks(recipe.getBlockIn().getDisplayedStacks().stream())));
        this.outputs.add(EmiStack.of(recipe.getResult().getBlock()));
    }


    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(HiveQueen.makeId("textures/gui/freeze_recipe_texture.png"), 0, 0, 48, 32, 0, 0);
        widgetHolder.addSlot(inputs.get(0),10, 12);
        widgetHolder.addSlot(outputs.get(0), 58, 12).recipeContext(this);
    }
}
