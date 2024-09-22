package net.sixik.sdmbestiary.client.screen.basic;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryPanel;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryTabPanel;

import java.util.UUID;

public abstract class AbstractBestiaryScreen extends BaseScreen {

    @Override public boolean drawDefaultBackground(GuiGraphics graphics) {return false;}

    public int tabButtonsSize = 32;

    public int tick = 0;

    public AbstractBestiaryTab selectedTab = null;
    public AbstractBestiaryEntry selectedEntry = null;

    public UUID selectedTabUUID = null;
    public UUID selectedEntryUUID = null;
    public UUID selectedEntryContentUUID = null;

    public AbstractBestiaryTabPanel bestiaryTabPanel;
    public AbstractBestiaryEntryContentPanel bestiaryEntryContentPanel;
    public AbstractBestiaryEntryPanel bestiaryEntryPanel;

    public PanelScrollBar bestiaryTabScrollPanel;
    public PanelScrollBar bestiaryEntryScrollPanel;
    public PanelScrollBar bestiaryEntryContentScrollPanel;


    @Override
    public boolean onInit() {
        setWidth(getScreen().getGuiScaledWidth() * 4/5);
        setHeight(getScreen().getGuiScaledHeight() * 4/5);

        closeContextMenu();
        return true;
    }

    @Override
    public void alignWidgets() {
        setProperty();
    }

    public void addAll(){
        addTabButtons();
        addEntryButtons();
        addEntryContents();
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
    }

    public abstract void setProperty();
    public void addTabButtons() {}
    public void addEntryButtons() {}
    public void addEntryContents() {};
}
