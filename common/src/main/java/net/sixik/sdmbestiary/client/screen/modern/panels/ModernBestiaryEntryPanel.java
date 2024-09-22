package net.sixik.sdmbestiary.client.screen.modern.panels;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryPanel;
import net.sixik.sdmbestiary.client.screen.modern.ModernBestiaryScreen;
import net.sixik.sdmuilib.client.utils.math.Vector2;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ModernBestiaryEntryPanel extends AbstractBestiaryEntryPanel {

    public ModernBestiaryEntryPanel(Panel panel) {
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
