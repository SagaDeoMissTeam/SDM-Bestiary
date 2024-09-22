package net.sixik.sdmbestiary.client.screen.basic.widget;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.bestiary.BestiaryTab;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBestiaryTabButton extends SimpleTextButton {

    public boolean isEdit = false;
    public AbstractBestiaryTab bestiaryTab;
    public AbstractBestiaryTabButton(Panel panel, AbstractBestiaryTab bestiaryTab) {
        super(panel, Component.empty(), bestiaryTab != null ? SDMBestiary.getIcon(bestiaryTab.icon) : Icon.empty());
        this.bestiaryTab = bestiaryTab;
    }

    public void setEdit() {
        isEdit = true;
        icon = Icons.ADD;
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if(button.isLeft() && isCtrlKeyDown() && isMouseOver() && SDMBestiary.isEditMode() && !isEdit) {
            if(getBestiaryScreen().selectedTabUUID == null) {
                getBestiaryScreen().selectedTab = bestiaryTab;
                return true;
            } else if(getBestiaryScreen().selectedTabUUID != bestiaryTab.tabID) {
                boolean flag = bestiaryTab.swap(getBestiaryScreen().selectedTab.tabID, bestiaryTab.tabID);
                if(flag) {
                    getBestiaryScreen().selectedTabUUID = null;
                    getBestiaryScreen().addTabButtons();
                    BestiaryBase.save();
                    return true;
                }
            } else {
                getBestiaryScreen().selectedTabUUID = null;
                return true;
            }
        }

        return super.mousePressed(button);
    }

    @Override
    public void onClicked(MouseButton button) {
        if (button.isLeft()) {


            if(isEdit) {
                BestiaryBase.CLIENT.getBestiaryTabs().add(new BestiaryTab(BestiaryBase.CLIENT));
                getBestiaryScreen().addTabButtons();
                getBestiaryScreen().bestiaryTabScrollPanel.setValue(getBestiaryScreen().bestiaryTabScrollPanel.getMaxValue());
                BestiaryBase.save();
                return;
            }

            if (bestiaryTab != null) {
                getBestiaryScreen().selectedTab = bestiaryTab;
                getBestiaryScreen().addEntryButtons();
            }
        } else if (button.isRight() && SDMBestiary.isEditMode() && !isEdit) {
            List<ContextMenuItem> contextMenu = new ArrayList<>();

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.tab.edit"), Icons.SETTINGS, (d) -> {
                ConfigGroup group = new ConfigGroup("sdmr", b -> {
                    openGui();

                    if (b) {
                        BestiaryBase.save();
                        getBestiaryScreen().refreshWidgets();
                    } else {
                        getBestiaryScreen().refreshWidgets();
                    }
                }).setNameKey("sidebar_button.sdmr.bestiary");


                ConfigGroup g = group.getOrCreateSubgroup("bestiary").getOrCreateSubgroup("tab");
                bestiaryTab.getConfig(g);
                new EditConfigScreen(group).openGui();

            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.delete"), Icons.REMOVE, (b) -> {
                if(getBestiaryScreen().selectedTab == bestiaryTab) {
                    getBestiaryScreen().selectedTab = null;
                }

                BestiaryBase.CLIENT.getBestiaryTabs().remove(bestiaryTab);
                getBestiaryScreen().refreshWidgets();
            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.copy"), Icons.INFO, (b) -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(String.valueOf(bestiaryTab.tabID));
            }));

            getGui().openContextMenu(contextMenu);
        }
    }

    public boolean isSelected() {
        return getBestiaryScreen().selectedTabUUID != null && getBestiaryScreen().selectedTabUUID == bestiaryTab.tabID;
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if(!isEdit) {


            list.add(Component.translatable(bestiaryTab.title));
            if(!bestiaryTab.getDescriptions().isEmpty()) {
                list.add(Component.empty());
                bestiaryTab.getDescriptions().stream().map(Component::translatable).forEach(list::add);
            }
        } else {
            list.add(Component.literal("Create"));
        }
    }

    public AbstractBestiaryScreen getBestiaryScreen() {
        return (AbstractBestiaryScreen) getGui();
    }
}
