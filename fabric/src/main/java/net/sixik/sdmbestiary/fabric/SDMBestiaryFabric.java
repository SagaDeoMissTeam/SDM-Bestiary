package net.sixik.sdmbestiary.fabric;

import net.sixik.sdmbestiary.SDMBestiary;
import net.fabricmc.api.ModInitializer;

public class SDMBestiaryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SDMBestiary.init();
    }
}