package aster.hivequeen.integrations;

import aster.hivequeen.HiveQueen;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Ingredient;
import ram.talia.hexal.common.recipe.FreezeRecipe;

public class EmiFreezeRecipe extends BasicEmiRecipe {
    public EmiFreezeRecipe(FreezeRecipe recipe){
        super(HiveQueenEmiPlugin.FREEZE, recipe.getId(), 128, 64);
        this.inputs.add(EmiIngredient.of(Ingredient.ofStacks(recipe.getBlockIn().getDisplayedStacks().stream())));
        this.outputs.add(EmiStack.of(recipe.getResult().getBlock()));
    }


    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        int textureX = 40;
        int textureY = 10;
        widgetHolder.addTexture(EmiTexture.FULL_ARROW, textureX+10, 10);
        // Arguments: Identifier, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight
        widgetHolder.addTexture(
                HiveQueen.makeId("textures/gui/freeze_recipe_texture.png"),
                textureX-28, 2,  // Screen X, Y
                48*2, 32*2,              // Width and height to draw on screen
                0, 0,                // U, V texture offsets
                48, 32,              // Region width and height to sample from the texture
                48, 32               // The ACTUAL width and height of your entire PNG file
        );



        widgetHolder.addSlot(inputs.get(0), textureX -28 , textureY);
        widgetHolder.addSlot(outputs.get(0), textureX + 56, textureY).recipeContext(this);
    }


}
