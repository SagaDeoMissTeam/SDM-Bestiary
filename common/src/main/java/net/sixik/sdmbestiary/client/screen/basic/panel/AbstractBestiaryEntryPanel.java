package net.sixik.sdmbestiary.client.screen.basic.panel;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmuilib.client.utils.GLHelper;

public abstract class AbstractBestiaryEntryPanel extends Panel {

    public AbstractBestiaryEntryPanel(Panel panel) {
        super(panel);
    }

    public AbstractBestiaryScreen getBestiaryScreen() {
        return (AbstractBestiaryScreen) getGui();
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
//        GLHelper.pushScissor(graphics,x + 2,y + 2,w - 4,h - 4);
        super.draw(graphics, theme, x, y, w, h);
//        GLHelper.popScissor(graphics);
    }


}
