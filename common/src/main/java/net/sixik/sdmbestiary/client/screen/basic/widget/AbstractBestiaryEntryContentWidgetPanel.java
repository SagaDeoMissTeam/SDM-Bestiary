package net.sixik.sdmbestiary.client.screen.basic.widget;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.api.register.BestiaryContentRegister;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.utils.ListHelper;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.Colors;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBestiaryEntryContentWidgetPanel extends Panel {


    public SimpleTextButton createButton;
    public boolean isEdit;
    public AbstractBestiaryEntryContent content;

    public AbstractBestiaryEntryContentPanel contentPanel;

    public AbstractBestiaryEntryContentWidgetPanel(AbstractBestiaryEntryContentPanel p) {
        this(p, null, false);
    }

    public AbstractBestiaryEntryContentWidgetPanel(AbstractBestiaryEntryContentPanel p, AbstractBestiaryEntryContent content) {
        this(p, content, false);
    }

    public AbstractBestiaryEntryContentWidgetPanel(AbstractBestiaryEntryContentPanel p, AbstractBestiaryEntryContent content, boolean isEdit) {
        super(p);
        this.contentPanel = p;
        this.content = content;
        this.isEdit = isEdit;
    }

    @Override
    public void addWidgets() {
        if(isEdit) {
            add(this.createButton = new SimpleTextButton(this, Component.literal("Create"), Icons.ADD) {
                @Override
                public void onClicked(MouseButton button) {
                    if(button.isLeft() && SDMBestiary.isEditMode()) {
                        getGui().openContextMenu(BestiaryContentRegister.createContextMenuItems((AbstractBestiaryScreen) getGui()));
                    }
                }

                @Override
                public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

                }
            });
        }
        setProperty();
    }

    @Override
    public void alignWidgets() {
        setProperty();
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if(button.isRight() && isMouseOver() && SDMBestiary.isEditMode() && !isEdit) {
            List<ContextMenuItem> contextMenu = new ArrayList<>();

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.entry.content.edit"), Icons.SETTINGS, (d) -> {
                ConfigGroup group = new ConfigGroup("sdmr", b -> {
                    openGui();

                    if (b) {
                        BestiaryBase.save();
                        getGui().refreshWidgets();
                    } else {
                        getGui().refreshWidgets();
                    }
                }).setNameKey("sidebar_button.sdmr.bestiary");


                ConfigGroup g = group.getOrCreateSubgroup("bestiary").getOrCreateSubgroup("content");
                content.getConfigMain(g);
                new EditConfigScreen(group).openGui();

            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.bestiary.delete"), Icons.REMOVE, (b) -> {
                content.bestiaryEntry.getEntryContents().remove(content);
                getBestiaryScreen().addEntryContents();
            }));

            contextMenu.addAll(content.getContextMenuItems());

            getGui().openContextMenu(contextMenu);
            return true;
        }
        if(button.isLeft() && isCtrlKeyDown() && isMouseOver() && SDMBestiary.isEditMode() && !isEdit) {
            if(getBestiaryScreen().selectedEntryContentUUID == null) {
                getBestiaryScreen().selectedEntryContentUUID = content.entryContentID;
                return true;
            } else if(getBestiaryScreen().selectedEntryContentUUID != content.entryContentID) {
                boolean flag = content.bestiaryEntry.swap(getBestiaryScreen().selectedEntryContentUUID, content.entryContentID);
                if(flag) {
                    getBestiaryScreen().selectedEntryContentUUID = null;
                    getBestiaryScreen().addEntryContents();
                    BestiaryBase.save();
                    return true;
                }
            } else {
                getBestiaryScreen().selectedEntryContentUUID = null;
                return true;
            }
        }

        return super.mousePressed(button);
    }

    @Override
    public void drawOffsetBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(isSelected()) {

            if(h <= 2) {
                Colors.UI_GOLD_2.draw(graphics,x,y,w,h, 0);
            } else
                RenderHelper.drawHollowRect(graphics,x,y,w,h, Colors.UI_GOLD_2, false);
        }
    }

    public boolean isSelected() {
        return content != null && getBestiaryScreen().selectedEntryContentUUID != null && Objects.equals(getBestiaryScreen().selectedEntryContentUUID, content.entryContentID);
    }

    public void setProperty() {
        if(isEdit) {
            this.createButton.setSize(this.width, this.height);
        }
    }

    public AbstractBestiaryScreen getBestiaryScreen() {
        return (AbstractBestiaryScreen) getGui();
    }
}
