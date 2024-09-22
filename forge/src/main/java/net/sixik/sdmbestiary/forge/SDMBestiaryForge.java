package net.sixik.sdmbestiary.forge;

import dev.architectury.platform.forge.EventBuses;
import net.sixik.sdmbestiary.SDMBestiary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SDMBestiary.MODID)
public class SDMBestiaryForge {
    public SDMBestiaryForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SDMBestiary.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        SDMBestiary.init();
    }
}