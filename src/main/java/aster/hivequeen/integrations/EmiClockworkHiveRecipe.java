package aster.hivequeen.integrations;

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

        int centerX = CARD_WIDTH / 2 - 9;
        int centerY = CARD_HEIGHT / 2 - 9;


        widgetHolder.addSlot(outputs.get(0), centerX, centerY).recipeContext(this);


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