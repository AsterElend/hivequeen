package aster.hivequeen;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "hivequeen")
public class HiveQueenConfig implements ConfigData {

    @Comment("Whether to show the entry for the clockwork hive in the hex notebook. Requires game restart, unfortunately.")
    boolean showClockworkHiveEntry = false;
}
