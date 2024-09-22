package net.sixik.sdmbestiary.common.register.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.sixik.sdmbestiary.SDMBestiary;

public class ItemsRegister {

    public static final DeferredRegister<Item> ITEMS;
    public static final RegistrySupplier<BestiaryCustomIconItem> CUSTOM_ICON;


    static {
        ITEMS = DeferredRegister.create(SDMBestiary.MODID, Registries.ITEM);
        CUSTOM_ICON = ITEMS.register("custom_icon", BestiaryCustomIconItem::new);
    }
}
