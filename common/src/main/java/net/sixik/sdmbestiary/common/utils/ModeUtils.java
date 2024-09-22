package net.sixik.sdmbestiary.common.utils;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.sixik.sdmuilib.client.utils.misc.RGB;

public class ModeUtils {

    public static void drawText(GuiGraphics graphics, FormattedText text, int x, int y, RGB rgb) {
        Theme.DEFAULT.drawString(graphics, text, x, y, Color4I.rgba(rgb.r, rgb.g, rgb.b, 255), 0);
    }

    public static void drawText(GuiGraphics graphics, Component text, int x, int y, int w, int h) {
        TextField textField = new TextField(new BaseScreen() {
            @Override
            public void addWidgets() {

            }
        });
        textField.setText(text);
        textField.resize(Theme.DEFAULT);
        textField.draw(graphics, Theme.DEFAULT, x,y,w,h);
    }
}
