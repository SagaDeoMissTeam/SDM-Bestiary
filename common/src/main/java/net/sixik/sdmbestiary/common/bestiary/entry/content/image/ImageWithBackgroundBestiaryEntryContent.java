package net.sixik.sdmbestiary.common.bestiary.entry.content.image;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.common.register.ftb.ConfigIconItemStack;
import net.sixik.sdmbestiary.common.utils.ItemUtils;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGB;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class ImageWithBackgroundBestiaryEntryContent extends ImageBestiaryEntryContent{

    public ItemStack imageBackground = ItemStack.EMPTY;
    public boolean isDefaultBackground = true;

    public int pos_x_offset = 0;
    public int pos_y_offset = 0;

    public ImageWithBackgroundBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
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
                if(isDefaultBackground){
                    RGBA.create(0,0,0,255/ 3).drawRoundFill(graphics, x + pos_x_offset,y + pos_y_offset,w,h, 4);
                } else {
                    SDMBestiary.getIcon(imageBackground).draw(graphics,x + pos_x_offset,y + pos_y_offset,w,h);
                }

                SDMBestiary.getIcon(image).draw(graphics,x + 2 + pos_x_offset,y + 2 + pos_y_offset,w - 4,h - 4);

                if(SDMBestiary.isEditMode() && isMouseOver()) {
                    RenderHelper.drawHollowRect(graphics,x,y,w + pos_x_offset,h + pos_y_offset, RGB.create(255,255,255), false);
                }
            }

            @Override
            public void setProperty() {
                if(image.isEmpty()) {
                    image = Items.ITEM_FRAME.getDefaultInstance();
                }

                if(!content.renderSettings.isCreated) {
                    content.renderSettings.centerType = CenterOperators.Type.CENTER_X;
                    content.renderSettings.positions.setPosition(contentPanel.posX, contentPanel.posY);
                    content.renderSettings.size.setPosition(16,16);
                    content.renderSettings.isCreated = true;
                }

                setSize(content.renderSettings.size.x, content.renderSettings.size.y);
            }
        };
    }

    @Override
    public void getConfig(ConfigGroup config) {
        super.getConfig(config);
        config.addBool("isdefaultbackground", isDefaultBackground, v -> isDefaultBackground = v, true);
        config.add("imageBackground", new ConfigIconItemStack(), imageBackground, v -> imageBackground = v, Items.ITEM_FRAME.getDefaultInstance());
        config.addInt("pos_x_offset", pos_x_offset, v -> pos_x_offset = v, 0, -5000, 5000);
        config.addInt("pos_y_offset", pos_y_offset, v -> pos_y_offset = v, 0, -5000, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        super.serializeNBT(nbt);
        ItemUtils.putItemStack(nbt, "imageBackground", imageBackground);
        nbt.putInt("pos_y_offset", pos_y_offset);
        nbt.putInt("pos_x_offset", pos_x_offset);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.imageBackground = ItemUtils.getItemStack(nbt, "imageBackground");
        if(nbt.contains("pos_y_offset")) this.pos_y_offset = nbt.getInt("pos_y_offset");
        if(nbt.contains("pos_x_offset")) this.pos_x_offset = nbt.getInt("pos_x_offset");
    }

    @Override
    public String getID() {
        return "imageWithBackgroundEntryContent";
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new ImageWithBackgroundBestiaryEntryContent(null);
        }
    }
}
