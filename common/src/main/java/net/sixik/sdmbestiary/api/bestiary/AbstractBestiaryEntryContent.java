package net.sixik.sdmbestiary.api.bestiary;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.sixik.sdmbestiary.api.IModIdentifier;
import net.sixik.sdmbestiary.api.INBTSerializable;
import net.sixik.sdmbestiary.api.entry.EAttachment;
import net.sixik.sdmbestiary.api.register.BestiaryContentRegister;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractBestiaryEntryContent implements INBTSerializable<CompoundTag>, IModIdentifier {

    public AbstractBestiaryEntry bestiaryEntry;
    public UUID entryContentID = UUID.randomUUID();

    public EntryContentRenderSettings renderSettings;

    public AbstractBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        this.bestiaryEntry = bestiaryEntry;
        this.renderSettings = new EntryContentRenderSettings();
    }

    public void setBestiaryEntry(AbstractBestiaryEntry bestiaryEntry) {
        this.bestiaryEntry = bestiaryEntry;
    }

    public List<ContextMenuItem> getContextMenuItems() {
        return new ArrayList<>();
    }

    public List<EAttachment> getAttachmentList() {
        return new ArrayList<>(List.of(EAttachment.NONE));
    }

    public static AbstractBestiaryEntryContent from(CompoundTag nbt) {
        String id = nbt.getString("entryID");
        if(BestiaryContentRegister.CONTENTS.containsKey(id)) {
            AbstractBestiaryEntryContent d1 = BestiaryContentRegister.CONTENTS.get(id).createDefaultInstance();
            d1.deserializeNBT(nbt);
            return d1;
        }

        return null;
    }

    public abstract AbstractBestiaryEntryContentWidgetPanel createWidget(net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel contentPanel);

    public Component getTranslatableForCreativeMenu() {
        return Component.translatable("sdm.bestiary.content." + getID().toLowerCase());
    }

    public Icon getCreativeIcon() {
        return Icons.DIAMOND;
    }

    public abstract String getID();

    public abstract void getConfig(ConfigGroup config);

    public void getConfigMain(ConfigGroup config) {
        getConfig(config);
        renderSettings.getConfig(config);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("entryID", getID());
        nbt.put("render_setting", renderSettings.serializeNBT());
        nbt.putUUID("entryContentID", entryContentID);
        serializeNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.renderSettings.deserializeNBT(nbt.getCompound("render_setting"));
        this.entryContentID = nbt.getUUID("entryContentID");
    }
}
