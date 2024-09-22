package net.sixik.sdmbestiary.client.screen.basic.widget;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.bestiary.BestiaryTab;
import net.sixik.sdmbestiary.common.bestiary.entry.BestiaryEntry;
import net.sixik.sdmuilib.client.utils.GLHelper;
import net.sixik.sdmuilib.client.utils.RenderHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBestiaryEntryTabButton extends SimpleTextButton {

    public boolean isEdit = false;
    public AbstractBestiaryEntry bestiaryEntry;

    public AbstractBestiaryEntryTabButton(Panel panel, AbstractBestiaryEntry bestiaryEntry) {
        super(panel, Component.empty(), bestiaryEntry != null ? SDMBestiary.getIcon(bestiaryEntry.icon) : Icon.empty());
        this.bestiaryEntry = bestiaryEntry;
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if(button.isLeft() && isCtrlKeyDown() && isMouseOver() && SDMBestiary.isEditMode() && !isEdit) {
            if(getBestiaryScreen().selectedEntryUUID == null) {
                getBestiaryScreen().selectedEntry = bestiaryEntry;
                return true;
            } else if(getBestiaryScreen().selectedEntryUUID != bestiaryEntry.entryID) {
                boolean flag = bestiaryEntry.swap(getBestiaryScreen().selectedEntryUUID, bestiaryEntry.entryID);
                if(flag) {
                    getBestiaryScreen().selectedEntryUUID = null;
                    getBestiaryScreen().addEntryButtons();
                    BestiaryBase.save();
                    return true;
                }
            } else {
                getBestiaryScreen().selectedEntryUUID = null;
                return true;
            }
        }

        return super.mousePressed(button);
    }

    @Override
    public void onClicked(MouseButton button) {
        if (button.isLeft()) {

            if(isEdit) {

                AbstractBestiaryTab tab =  BestiaryBase.CLIENT.getBestiaryTab(getBestiaryScreen().selectedTab.tabID);
                if(tab != null) {
                    BestiaryEntry entry = new BestiaryEntry(tab);
                    tab.getTabEntries().add(entry);
                }
                getBestiaryScreen().addEntryButtons();
                getBestiaryScreen().bestiaryEntryScrollPanel.setValue(getBestiaryScreen().bestiaryEntryScrollPanel.getMaxValue());
                BestiaryBase.save();
                return;
            }

            if (bestiaryEntry != null) {
                getBestiaryScreen().selectedEntry = bestiaryEntry;
                getBestiaryScreen().addEntryContents();
            }
        } else if (button.isRight() && SDMBestiary.isEditMode() && !isEdit) {
            List<ContextMenuItem> contextMenu = new ArrayList<>();

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.entry.edit"), Icons.SETTINGS, (d) -> {
                ConfigGroup group = new ConfigGroup("sdmr", b -> {
                    openGui();

                    if (b) {
                        BestiaryBase.save();
                        getBestiaryScreen().refreshWidgets();
                    } else {
                        getBestiaryScreen().refreshWidgets();
                    }
                }).setNameKey("sidebar_button.sdmr.bestiary");


                ConfigGroup g = group.getOrCreateSubgroup("bestiary").getOrCreateSubgroup("entry");
                bestiaryEntry.getConfig(g);
                new EditConfigScreen(group).openGui();

            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.delete"), Icons.REMOVE, (b) -> {
                if(getBestiaryScreen().selectedEntry == bestiaryEntry) {
                    getBestiaryScreen().selectedEntry = null;
                }

                bestiaryEntry.getBestiaryTab().getTabEntries().remove(bestiaryEntry);
                getBestiaryScreen().refreshWidgets();
            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.copy"), Icons.INFO, (b) -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(String.valueOf(bestiaryEntry.entryID));
            }));


            getGui().openContextMenu(contextMenu);
        }
    }

    public void setEdit() {
        isEdit = true;
        icon = Icons.ADD;
    }

    public boolean isSelected() {
        return getBestiaryScreen().selectedEntryUUID != null && getBestiaryScreen().selectedEntryUUID == bestiaryEntry.entryID;
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if(!isEdit) {
            list.add(Component.translatable(bestiaryEntry.title));
            if(!bestiaryEntry.getDescriptions().isEmpty()) {
                list.add(Component.empty());
                bestiaryEntry.getDescriptions().stream().map(Component::translatable).forEach(list::add);
            }
        } else {
            list.add(Component.literal("Create"));
        }
    }

    public AbstractBestiaryScreen getBestiaryScreen() {
        return (AbstractBestiaryScreen) getGui();
    }
}
