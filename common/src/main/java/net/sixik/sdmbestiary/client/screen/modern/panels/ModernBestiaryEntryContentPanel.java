package net.sixik.sdmbestiary.client.screen.modern.panels;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ModernBestiaryEntryContentPanel extends AbstractBestiaryEntryContentPanel {
    public ModernBestiaryEntryContentPanel(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {

    }

    @Override
    public void alignWidgets() {

    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255 / 3).drawRoundFill(graphics,x,y,w,h,10);
    }
}
