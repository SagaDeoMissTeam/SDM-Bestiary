package net.sixik.sdmbestiary.api.bestiary;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmbestiary.api.INBTSerializable;
import net.sixik.sdmbestiary.common.register.ftb.ConfigIconItemStack;
import net.sixik.sdmbestiary.common.utils.ItemUtils;
import net.sixik.sdmbestiary.common.utils.ListHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBestiaryEntry implements INBTSerializable<CompoundTag> {

    public AbstractBestiaryTab bestiaryTab;

    public UUID entryID;

    public ItemStack icon = ItemStack.EMPTY;
    public ItemStack background = ItemStack.EMPTY;
    public boolean defaultBackground = true;

    public String title = "";

    private List<String> descriptions = new LinkedList<String>();

    private LinkedList<AbstractBestiaryEntryContent> entryContents = new LinkedList<>();

    public boolean swap(AbstractBestiaryEntryContent from, AbstractBestiaryEntryContent to) {
        int index1 = entryContents.indexOf(from);
        int index2 = entryContents.indexOf(to);

        if (index1 != -1 && index2 != -1) {
            ListHelper.swap(bestiaryTab.getTabEntries(), index1, index2);
            return true;
        }
        return false;
    }

    public boolean swap(UUID from, UUID to) {
        int index1 = -1;
        int index2 = -1;

        for (int i = 0; i < getEntryContents().size(); i++) {
            AbstractBestiaryEntryContent entry = getEntryContents().get(i);
            if (Objects.equals(entry.entryContentID, from)) {
                index1 = i;
            }
            if (Objects.equals(entry.entryContentID, to)) {
                index2 = i;
            }
        }

        if (index1 != -1 && index2 != -1) {
            ListHelper.swap(getEntryContents(), index1, index2);
            return true;
        }
        return false;
    }

    public void getConfig(ConfigGroup config) {
        config.addString("title", title, v -> title = v, "");
        config.addList("descriptions", descriptions, new StringConfig(null), "");

        config.add("image", new ConfigIconItemStack(), icon, v -> icon = v, Items.ITEM_FRAME.getDefaultInstance());
        config.add("background", new ConfigIconItemStack(), background, v -> background = v, ItemStack.EMPTY);
        config.addBool("defaultBackground", defaultBackground, v -> defaultBackground = v, true);
    }

    public AbstractBestiaryEntry(AbstractBestiaryTab bestiaryTab){
        this.bestiaryTab = bestiaryTab;
        this.entryID = UUID.randomUUID();
    }


    public AbstractBestiaryTab getBestiaryTab() {
        return bestiaryTab;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public LinkedList<AbstractBestiaryEntryContent> getEntryContents() {
        return entryContents;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt =new CompoundTag();
        nbt.putUUID("entryID", entryID);
        ItemUtils.putItemStack(nbt, "icon", icon);
        ItemUtils.putItemStack(nbt, "background", background);
        nbt.putBoolean("defaultBackground", defaultBackground);
        nbt.putString("title", title);

        ListTag d1 = new ListTag();
        for (String description : descriptions) {
            d1.add(StringTag.valueOf(description));
        }
        nbt.put("descriptions", d1);

        ListTag tagEntryList = new ListTag();
        for (AbstractBestiaryEntryContent entryContent : entryContents) {
            tagEntryList.add(entryContent.serializeNBT());
        }
        nbt.put("entries", tagEntryList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.entryID = nbt.getUUID("entryID");
        this.icon = ItemUtils.getItemStack(nbt, "icon");
        this.background = ItemUtils.getItemStack(nbt, "background");
        this.defaultBackground = nbt.getBoolean("defaultBackground");
        this.title = nbt.getString("title");
        this.descriptions.clear();
        ListTag tagDescriptions = (ListTag) nbt.get("descriptions");
        for (Tag tagDescription : tagDescriptions) {
            descriptions.add(tagDescription.getAsString());
        }
        ListTag tagEntryList = (ListTag) nbt.get("entries");
        entryContents.clear();
        for (Tag tag : tagEntryList) {
            CompoundTag d1 = (CompoundTag) tag;
            AbstractBestiaryEntryContent entryContent = AbstractBestiaryEntryContent.from(d1);
            if(entryContent == null) continue;
            entryContents.add(entryContent);
        }

    }
}
