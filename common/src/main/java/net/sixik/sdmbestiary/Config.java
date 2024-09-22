package net.sixik.sdmbestiary;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;

import java.nio.file.Path;

public class Config {

    public static void init(Path file)
    {
        ConfigUtil.loadDefaulted(CONFIG, file, "sdmbestiary");
    }

    public static void reload(){
        CONFIG.load(SDMBestiaryPath.getClientConfig());
    }

    public static final SNBTConfig CONFIG;
    public static final BooleanValue EDIT_MODE;

    static {
        CONFIG = SNBTConfig.create("sdmbestiary-client");
        EDIT_MODE = CONFIG.addBoolean("isEditMode", false);
    }
}
