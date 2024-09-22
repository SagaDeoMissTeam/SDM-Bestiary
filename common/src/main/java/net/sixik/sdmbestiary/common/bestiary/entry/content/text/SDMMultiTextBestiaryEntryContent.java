package net.sixik.sdmbestiary.common.bestiary.entry.content.text;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.common.utils.ModeUtils;
import net.sixik.sdmuilib.client.utils.GLHelper;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.TextHelper;
import net.sixik.sdmuilib.client.utils.math.Vector2;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGB;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SDMMultiTextBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public List<String> text = new ArrayList<>();
    public MutableComponent formatedText = Component.empty();
    public boolean isCentered_x = true;
    public boolean isCentered_y = false;
    public boolean drawBackground = false;
    public int text_y_offset = 0;


    public SDMMultiTextBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        super(bestiaryEntry);
    }

    @Override
    public AbstractBestiaryEntryContentWidgetPanel createWidget(AbstractBestiaryEntryContentPanel contentPanel) {
        return new AbstractBestiaryEntryContentWidgetPanel(contentPanel, this, false) {

            @Override
            public void addWidgets() {

            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                if(SDMBestiary.isEditMode() && isMouseOver()) {
                    RenderHelper.drawHollowRect(graphics,x,y,w,h, RGB.create(255,255,255), false);
                }
                if(drawBackground) {
                    RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,4);
                }



                GLHelper.pushTransform(graphics, new Vector2(x,y), new Vector2(w,h), (float) content.renderSettings.scale, 0);

                int d1 = x;
                int d2 = y;
                StringBuilder builder = new StringBuilder();
                text.stream().map(builder::append);
                String d = builder.toString();

                d2 += text_y_offset;
                if(isCentered_x) {
                    int h1 = TextHelper.getTextWidth(d);
                    d1 = w / 3 + h1 / 2;
                    d1 = x + d1;
                }


                d2 += Minecraft.getInstance().font.lineHeight + 2;


                ModeUtils.drawText(graphics, formatedText, d1, d2, w,h);

                GLHelper.popTransform(graphics);

            }

            @Override
            public void setProperty() {
                formatedText = Component.empty();
                if(!content.renderSettings.isCreated) {
                    content.renderSettings.centerType = CenterOperators.Type.CENTER_X;
                    content.renderSettings.positions.setPosition(contentPanel.posX, contentPanel.posY);
                    content.renderSettings.size.setPosition(contentPanel.width - contentPanel.width / 6, 14);
                    content.renderSettings.isCreated = true;
                }

                setSize(content.renderSettings.size.x, content.renderSettings.size.y);

                StringBuilder builder = new StringBuilder();
                text.stream().map(builder::append);
                String d = builder.toString();

                String[] parts = d.split("(?=&.)");

                for (String part : parts) {
                    if (part.startsWith("&")) {
                        String code = part.substring(1, 2);
                        String content = part.substring(2);

                        ChatFormatting formatting = SDMBestiary.getChatFormatting(code.charAt(0));

                        if (formatting == null) {
                            formatedText = formatedText.append(Component.translatable(content));
                        } else formatedText = formatedText.append(Component.translatable(content).withStyle(formatting));
                    }
                    else {
                        formatedText = formatedText.append(Component.translatable(part));
                    }
                }


                if(content.renderSettings.autoSize) {
                    int size = (int) ((Minecraft.getInstance().font.lineHeight + 2 * text.size()) * renderSettings.scale);
                    setSize(content.renderSettings.size.x, size);
                }
            }
        };
    }

    @Override
    public String getID() {
        return "sdmMultiTextEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.addList("text", text, new StringConfig(null), "");
        config.addBool("drawBackground", drawBackground, v -> drawBackground = v, false);
        config.addBool("isCentered_x", isCentered_x, v -> isCentered_x = v, true);
        config.addBool("isCentered_y", isCentered_y, v -> isCentered_y = v, true);
        config.addInt("text_y_offset", text_y_offset, v -> text_y_offset = v, 0, 0, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        ListTag d = new ListTag();
        for (String s : text) {
            d.add(StringTag.valueOf(s));
        }
        nbt.put("text",d);
        nbt.putBoolean("drawBackground", drawBackground);
        nbt.putBoolean("isCentered_y", isCentered_y);
        nbt.putBoolean("isCentered_x", isCentered_x);
        nbt.putInt("text_y_offset", text_y_offset);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        ListTag d = (ListTag) nbt.get("text");
        text.clear();
        for (Tag tag : d) {
            text.add(tag.getAsString());
        }

        this.drawBackground = nbt.getBoolean("drawBackground");
        this.isCentered_x = nbt.getBoolean("isCentered_x");
        this.isCentered_y = nbt.getBoolean("isCentered_y");
        this.text_y_offset = nbt.getInt("text_y_offset");
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new SDMMultiTextBestiaryEntryContent(null);
        }
    }
}
