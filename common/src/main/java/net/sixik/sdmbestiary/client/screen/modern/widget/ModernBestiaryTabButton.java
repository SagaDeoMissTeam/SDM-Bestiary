package net.sixik.sdmbestiary.client.screen.modern.widget;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryTabButton;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.Colors;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ModernBestiaryTabButton extends AbstractBestiaryTabButton {

    public ModernBestiaryTabButton(Panel panel, AbstractBestiaryTab bestiaryTab) {
        super(panel, bestiaryTab);
    }


    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(isSelected()) {

            if(h <= 2) {
                Colors.UI_GOLD_2.draw(graphics,x,y,w,h, 0);
            } else
                RenderHelper.drawHollowRect(graphics,x,y,w,h, Colors.UI_GOLD_2, false);
        }

        if(isEdit) {
            RGBA.create(0,0,0,255 / 2).drawRoundFill(graphics,x,y,w,h,4);
        } else {
            if (bestiaryTab.defaultBackground)
                RGBA.create(0, 0, 0, 255 / 2).drawRoundFill(graphics, x, y, w, h, 4);
            else {
                SDMBestiary.getIcon(bestiaryTab.background).draw(graphics, x, y, w, h);
            }
        }
    }
}
