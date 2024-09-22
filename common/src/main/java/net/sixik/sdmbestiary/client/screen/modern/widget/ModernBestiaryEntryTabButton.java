package net.sixik.sdmbestiary.client.screen.modern.widget;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryTabButton;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.Colors;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ModernBestiaryEntryTabButton extends AbstractBestiaryEntryTabButton {

    public ModernBestiaryEntryTabButton(Panel panel, AbstractBestiaryEntry bestiaryEntry) {
        super(panel, bestiaryEntry);
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
            if (bestiaryEntry.defaultBackground)
                RGBA.create(0, 0, 0, 255 / 2).drawRoundFill(graphics, x, y, w, h, 4);
            else {
                SDMBestiary.getIcon(bestiaryEntry.background).draw(graphics, x, y, w, h);
            }
        }
    }
}
