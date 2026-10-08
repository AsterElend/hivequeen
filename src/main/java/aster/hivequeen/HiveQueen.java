package aster.hivequeen;

import aster.hivequeen.casting.HiveQueenPatterns;

import aster.hivequeen.scrollmaking.RecipeRegistry;
import at.petrak.hexcasting.interop.HexInterop;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class HiveQueen implements ModInitializer {
    public static final String MOD_ID = "hivequeen";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier makeId(String name){
        return new Identifier(MOD_ID, name);
    }
    private static final List<String> interopTests = List.of("spectrum", "botania", "trinkets");

    private static final String CLOCKWORK_FLAG = "hivequeen:show_clockwork_hive_entry";
    public HiveQueenConfig config;
    @Override
    public void onInitialize() {
        PatchouliAPI.IPatchouliAPI patchi = PatchouliAPI.get();
        AutoConfig.register(HiveQueenConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(HiveQueenConfig.class).getConfig();
        HiveQueenPatterns.init();
        HiveQueenItems.register();
        RecipeRegistry.register();
        FabricLoader loader = FabricLoader.getInstance();
        boolean forceloadInterop = false;
        for (String string: interopTests){
            if (loader.isModLoaded(string)){
                forceloadInterop = true;
                break;
            }
        }
        if (forceloadInterop){
            PatchouliAPI.get().setConfigFlag(HexInterop.PATCHOULI_ANY_INTEROP_FLAG, true);
        }

        if (config.showClockworkHiveEntry) patchi.setConfigFlag(CLOCKWORK_FLAG, true);

    }


}
