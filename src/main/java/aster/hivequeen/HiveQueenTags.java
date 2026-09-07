package aster.hivequeen;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class HiveQueenTags {
    public static final TagKey<Enchantment> SKIP_BREAK_COST_INCREMENT = TagKey.of(RegistryKeys.ENCHANTMENT,
            HiveQueen.makeId("skip_break_cost_increment"));
}
