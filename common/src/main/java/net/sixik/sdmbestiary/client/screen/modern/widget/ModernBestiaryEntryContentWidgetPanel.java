package net.sixik.sdmbestiary.client.screen.modern.widget;

import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ModernBestiaryEntryContentWidgetPanel extends AbstractBestiaryEntryContentWidgetPanel {

    public ModernBestiaryEntryContentWidgetPanel(AbstractBestiaryEntryContentPanel p, AbstractBestiaryEntryContent content, boolean isEdit) {
        super(p, content, isEdit);
    }


    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(isEdit) {
            if(isMouseOver) {
                RGBA.create(255,255,255,255/3).drawRoundFill(graphics, x,y,w,h,2);
            } else {
                RGBA.create(0,0,0,255/3).drawRoundFill(graphics, x,y,w,h,2);
            }
        }
    }
}
