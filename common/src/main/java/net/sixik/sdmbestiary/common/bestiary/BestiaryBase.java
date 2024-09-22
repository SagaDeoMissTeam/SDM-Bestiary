package net.sixik.sdmbestiary.common.bestiary;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.SDMBestiaryPath;
import net.sixik.sdmbestiary.api.INBTSerializable;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class BestiaryBase implements INBTSerializable<CompoundTag> {

//    public static BestiaryBase SERVER;
    public static BestiaryBase CLIENT = new BestiaryBase();

    private LinkedList<AbstractBestiaryTab> bestiaryTabs = new LinkedList<>();

    public LinkedList<AbstractBestiaryTab> getBestiaryTabs() {
        return bestiaryTabs;
    }

    public AbstractBestiaryTab getBestiaryTab(UUID id){
        for (AbstractBestiaryTab bestiaryTab : getBestiaryTabs()) {
            if(Objects.equals(bestiaryTab.tabID, id)) return bestiaryTab;
        }
        return null;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag tagBestiaryTabs = new ListTag();
        for (AbstractBestiaryTab bestiaryTab : bestiaryTabs) {
            tagBestiaryTabs.add(bestiaryTab.serializeNBT());
        }
        nbt.put("bestiaryTabs", tagBestiaryTabs);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag tagBestiaryTabs = (ListTag) nbt.get("bestiaryTabs");
        bestiaryTabs.clear();
        for (Tag tagBestiaryTab : tagBestiaryTabs) {
            BestiaryTab tab = new BestiaryTab(this);
            tab.deserializeNBT((CompoundTag) tagBestiaryTab);
            bestiaryTabs.add(tab);
        }
    }

    public static void save() {
        SNBT.write(SDMBestiaryPath.getFile(), CLIENT.serializeNBT());
    }

    public static void load() {
        CompoundTag nbt = SNBT.read(SDMBestiaryPath.getFile());
        if (nbt != null) {
            CLIENT.deserializeNBT(nbt);
        }
    }
}
