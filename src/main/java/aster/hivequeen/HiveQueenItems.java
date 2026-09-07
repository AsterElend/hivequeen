package aster.hivequeen;

import aster.hivequeen.scrollmaking.RecipeRegistry;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class HiveQueenItems {
    static boolean doWeHaveTrinkets = false;

   public static Item JEWEL_SOCKS;



    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(HiveQueen.MOD_ID, name), item); }

    private static void addToHexGroup(FabricItemGroupEntries entries) {
        if (doWeHaveTrinkets){
            entries.add(JEWEL_SOCKS);
        }

        entries.add(RecipeRegistry.CLOCKWORK_HIVE);
    }


    public static void register() {
        doWeHaveTrinkets = FabricLoader.getInstance().isModLoaded("trinkets");
        JEWEL_SOCKS = registerItem("jewel_socks", new JewelSocksItem(new FabricItemSettings().maxCount(1)));





        HiveQueen.LOGGER.info("Registering modded items for " + HiveQueen.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier("hexcasting:hexcasting"))).register(HiveQueenItems::addToHexGroup);
    }


    static class JewelSocksItem extends TrinketItem {
        public JewelSocksItem(Item.Settings settings) {
            super(settings);
        }
        public static final UUID SOCK_HAND = UUID.fromString("10c64e9f-3462-424b-9fc1-3263c42cc98f");
        public static final UUID SOCK_OFFHAND = UUID.fromString("6b322bd2-494a-4d8d-9aac-b64a703f8f36");

        @Override
        public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
            Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);


            SlotAttributes.addSlotModifier(modifiers, "hand/ring", SOCK_HAND, 1, EntityAttributeModifier.Operation.ADDITION);
            SlotAttributes.addSlotModifier(modifiers, "offhand/ring", SOCK_OFFHAND, 1, EntityAttributeModifier.Operation.ADDITION);

            return modifiers;
        }
    }
}
