package aster.hivequeen.scrollmaking;

import at.petrak.hexcasting.common.items.storage.ItemScroll;
import at.petrak.hexcasting.common.lib.HexItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ClockworkHiveRecipe implements Recipe<SimpleInventory> {
    private final Identifier recipeId;
    private final Identifier greatSpellId;
    private final List<Ingredient> ingredients;

    public ClockworkHiveRecipe(Identifier id, List<Ingredient> ingredients, Identifier outputScroll){
        recipeId = id;
        this.ingredients = ingredients;
        greatSpellId = outputScroll;
    }



    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        List<ItemStack> inventoryStacks = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                inventoryStacks.add(stack);
            }
        }

        // Quick size check — inventory must have at least as many stacks as ingredients
        if (inventoryStacks.size() < ingredients.size()) {
            return false;
        }

        // For each ingredient, find a matching stack in the inventory (shapeless)
        List<ItemStack> remaining = new ArrayList<>(inventoryStacks);
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            Iterator<ItemStack> iter = remaining.iterator();
            while (iter.hasNext()) {
                if (ingredient.test(iter.next())) {
                    iter.remove();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= ingredients.size();
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return HexItems.SCROLL_LARGE.getDefaultStack();
    }

    @Override
    public Identifier getId() {
        return recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CLOCKWORK_HIVE_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.CLOCKWORK_HIVE_RECIPE_TYPE;
    }


    public List<Ingredient> getInputs() {
        return this.ingredients;
    }


    public static class Serializer implements RecipeSerializer<ClockworkHiveRecipe> {

        @Override
        public ClockworkHiveRecipe read(Identifier id, JsonObject json) {
            List<Ingredient> ingredients = new ArrayList<>();
           JsonArray ingredientArray = JsonHelper.getArray(json, "inputs");
           for (JsonElement el: ingredientArray){
             ingredients.add(Ingredient.fromJson(el));
           }
          Identifier greatSpell = new Identifier(JsonHelper.getString(json, "greatSpell"));
           return new ClockworkHiveRecipe(id, ingredients, greatSpell);

        }

        @Override
        public ClockworkHiveRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient[] inputs = new Ingredient[buf.readVarInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.fromPacket(buf);
            }
            Identifier greatSpell = buf.readIdentifier();
            return new ClockworkHiveRecipe(id, Arrays.stream(inputs).toList(), greatSpell);
        }

        @Override
        public void write(PacketByteBuf buf, ClockworkHiveRecipe recipe) {
        buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient input : recipe.getIngredients()) {
                input.write(buf);
            }
            buf.writeIdentifier(recipe.greatSpellId);
        }
    }

    public ItemStack getOurScroll(){
        return ItemScroll.withPerWorldPattern(new ItemStack(HexItems.SCROLL_LARGE), greatSpellId.toString());
    }
}
