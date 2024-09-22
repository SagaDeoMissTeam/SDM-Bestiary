package net.sixik.sdmbestiary.api.bestiary;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmbestiary.api.INBTSerializable;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.bestiary.entry.BestiaryEntry;
import net.sixik.sdmbestiary.common.register.ftb.ConfigIconItemStack;
import net.sixik.sdmbestiary.common.utils.ItemUtils;
import net.sixik.sdmbestiary.common.utils.ListHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBestiaryTab implements INBTSerializable<CompoundTag> {

    public BestiaryBase bestiaryBase;

    public UUID tabID;

    public ItemStack icon = ItemStack.EMPTY;
    public ItemStack background = ItemStack.EMPTY;
    public boolean defaultBackground = true;
    public String title = "";
    private List<String> descriptions = new LinkedList<String>();

    public boolean renderTitle = false;
    public double titleScale = 1f;

    private LinkedList<AbstractBestiaryEntry> tabEntries = new LinkedList<>();

    public AbstractBestiaryTab(BestiaryBase bestiaryBase) {
        this.bestiaryBase = bestiaryBase;
        this.tabID = UUID.randomUUID();
    }

    public boolean swap(UUID from, UUID to) {
        int index1 = -1;
        int index2 = -1;

        for (int i = 0; i < getTabEntries().size(); i++) {
            AbstractBestiaryEntry entry = getTabEntries().get(i);
            if (Objects.equals(entry.entryID, from)) {
                index1 = i;
            }
            if (Objects.equals(entry.entryID, to)) {
                index2 = i;
            }
        }

        if (index1 != -1 && index2 != -1) {
            ListHelper.swap(getTabEntries(), index1, index2);
            return true;
        }
        return false;
    }

    public AbstractBestiaryEntry getTabEntry(UUID id) {
        for (AbstractBestiaryEntry tabEntry : tabEntries) {
            if(Objects.equals(tabEntry.entryID, id)) return tabEntry;
        }
        return null;
    }

    public void getConfig(ConfigGroup config) {
        config.addString("title", title, v -> title = v, "");
        config.addBool("renderTitle", renderTitle, v -> renderTitle = v, false);

        config.addList("descriptions", descriptions, new StringConfig(null), "");

        config.add("image", new ConfigIconItemStack(), icon, v -> icon = v, Items.ITEM_FRAME.getDefaultInstance());
        config.add("background", new ConfigIconItemStack(), background, v -> background = v, ItemStack.EMPTY);
        config.addBool("defaultBackground", defaultBackground, v -> defaultBackground = v, true);
        config.addDouble("titleScale", titleScale, v -> titleScale = v, titleScale, 0.1, 20f);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("title", title);
        nbt.putUUID("tabID", tabID);
        nbt.putDouble("titleScale", titleScale);
        nbt.putBoolean("defaultBackground", defaultBackground);
        nbt.putBoolean("renderTitle", renderTitle);
        ItemUtils.putItemStack(nbt, "icon", icon);
        ItemUtils.putItemStack(nbt, "background", background);
        ListTag d1 = new ListTag();

        for (AbstractBestiaryEntry tabEntry : tabEntries) {
            d1.add(tabEntry.serializeNBT());
        }
        nbt.put("tabEntries", d1);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains("title")) this.title = nbt.getString("title");
        if(nbt.contains("titleScale")) this.titleScale = nbt.getDouble("titleScale");
        if(nbt.contains("icon")) this.icon = ItemUtils.getItemStack(nbt, "icon");
        if(nbt.contains("background")) this.background = ItemUtils.getItemStack(nbt, "background");
        if(nbt.contains("defaultBackground")) this.defaultBackground = nbt.getBoolean("defaultBackground");
        this.tabID = nbt.getUUID("tabID");
        this.renderTitle = nbt.getBoolean("renderTitle");


        ListTag tags = (ListTag) nbt.get("tabEntries");
        tabEntries.clear();
        for (Tag tag : tags) {
            BestiaryEntry entry = new BestiaryEntry(this);
            entry.deserializeNBT((CompoundTag) tag);
            tabEntries.add(entry);
        }
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public LinkedList<AbstractBestiaryEntry> getTabEntries() {
        return tabEntries;
    }
}
