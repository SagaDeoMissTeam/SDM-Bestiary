package net.sixik.sdmbestiary.common.bestiary.entry.content.text;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGB;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class FTBTextBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public String text = "";
    public MutableComponent formatedText = Component.empty();
    public boolean isCentered_x = true;
    public boolean isCentered_y = false;
    public boolean drawBackground = false;
    public int text_x_offset = 0;
    public int text_y_offset = 0;


    public FTBTextBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        super(bestiaryEntry);
    }

    @Override
    public AbstractBestiaryEntryContentWidgetPanel createWidget(AbstractBestiaryEntryContentPanel contentPanel) {
        return new AbstractBestiaryEntryContentWidgetPanel(contentPanel, this, false) {
            public TextField textField;
            @Override
            public void addWidgets() {
                add(this.textField = new TextField(this));
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                if(SDMBestiary.isEditMode() && isMouseOver()) {
                    RenderHelper.drawHollowRect(graphics,x,y,w,h, RGB.create(255,255,255), false);
                }
                if(drawBackground) {
                    RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,4);
                }

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

                String d = text;

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

                this.setY(5);
                this.textField.setSize(this.width, this.height);
                this.textField.setMaxWidth(this.width);
                this.textField.setScale((float) content.renderSettings.scale);
                this.textField.setText(formatedText);
                this.textField.setColor(Color4I.rgba(renderSettings.color.r, renderSettings.color.g, renderSettings.color.b, renderSettings.color.a));
                if(isCentered_x) {
                    this.textField.addFlags(4);
                }
                if(isCentered_y) {
                    this.textField.addFlags(32);
                }

                this.textField.resize(Theme.DEFAULT);
                this.setPos(text_x_offset, text_y_offset);
                if(content.renderSettings.autoSize) {
                    setSize(content.renderSettings.autoSize_advanced ? this.textField.width : content.renderSettings.size.x, this.textField.height);
                }

            }
        };
    }

    @Override
    public String getID() {
        return "ftbTextEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.addString("text", text, v -> text = v, "");
        config.addBool("drawBackground", drawBackground, v -> drawBackground = v, false);
        config.addBool("isCentered_x", isCentered_x, v -> isCentered_x = v, true);
        config.addBool("isCentered_y", isCentered_y, v -> isCentered_y = v, true);
        config.addInt("text_x_offset", text_x_offset, v -> text_x_offset = v, 0, -5000, 5000);
        config.addInt("text_y_offset", text_y_offset, v -> text_y_offset = v, 0, -5000, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        nbt.putString("text", text);
        nbt.putBoolean("drawBackground", drawBackground);
        nbt.putBoolean("isCentered_y", isCentered_y);
        nbt.putBoolean("isCentered_x", isCentered_x);
        nbt.putInt("text_x_offset", text_x_offset);
        nbt.putInt("text_y_offset", text_y_offset);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.text = nbt.getString("text");
        this.drawBackground = nbt.getBoolean("drawBackground");
        this.isCentered_x = nbt.getBoolean("isCentered_x");
        this.isCentered_y = nbt.getBoolean("isCentered_y");
        if(nbt.contains("text_x_offset")) this.text_x_offset = nbt.getInt("text_x_offset");
        if(nbt.contains("text_y_offset")) this.text_y_offset = nbt.getInt("text_y_offset");
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new FTBTextBestiaryEntryContent(null);
        }
    }
}
