package aster.hivequeen.integrations;

import aster.hivequeen.HiveQueen;
import aster.hivequeen.scrollmaking.ClockworkHiveRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Ingredient;

import java.util.List;

public class EmiClockworkHiveRecipe extends BasicEmiRecipe {

    private static final int CARD_WIDTH = 150;
    private static final int CARD_HEIGHT = 120;

    public EmiClockworkHiveRecipe(ClockworkHiveRecipe recipe) {
        super(HiveQueenEmiPlugin.CLOCKWORK_HIVE, recipe.getId(), CARD_WIDTH, CARD_HEIGHT);

        for (Ingredient ingredient : recipe.getInputs()) {
            this.inputs.add(EmiIngredient.of(ingredient));
        }
        this.outputs.add(EmiStack.of(recipe.getOurScroll()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // 1. Calculate the center for a 32x32 texture
        int textureSize = 48;
        int textureX = (CARD_WIDTH / 2) - (textureSize / 2);
        int textureY = (CARD_HEIGHT / 2) - (textureSize / 2);

        // 2. Draw the 16x16 texture upscaled to 32x32
        // Parameters: id, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight
        widgetHolder.addTexture(
                HiveQueen.makeId("textures/icon.png"),
                textureX, textureY,   // Position on screen (centered)
                textureSize, textureSize,               // Width & Height on screen (upscaled)
                0, 0,                 // U & V starting coordinates in the file
                16, 16,               // Region width & height to read from the file
                16, 16                // Total width & height of the image file
        );

        // 3. Center for the output slot (usually 18x18 pixels in EMI)
        int slotSize = 18;
        int centerX = (CARD_WIDTH / 2) - (slotSize / 2);
        int centerY = (CARD_HEIGHT / 2) - (slotSize / 2);

        widgetHolder.addSlot(outputs.get(0), centerX, centerY).recipeContext(this);

        // 4. Input slots circular distribution
        List<EmiIngredient> recipeInputs = this.getInputs();
        int inputCount = recipeInputs.size();
        double radius = 36.0;

        for (int i = 0; i < inputCount; i++) {
            double angle = (2.0 * Math.PI * (double) i / (double) inputCount) - (Math.PI / 2.0);

            int inputX = centerX + (int) Math.round(radius * Math.cos(angle));
            int inputY = centerY + (int) Math.round(radius * Math.sin(angle));

            widgetHolder.addSlot(recipeInputs.get(i), inputX, inputY);
        }
    }




}