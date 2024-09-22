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

public class ImageBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public ItemStack image = Items.ITEM_FRAME.getDefaultInstance();
    public int pos_x_offset = 0;
    public int pos_y_offset = 0;


    public ImageBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
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
                SDMBestiary.getIcon(image).draw(graphics,x + pos_x_offset,y + pos_y_offset,w,h);
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
    public String getID() {
        return "imageEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.add("image", new ConfigIconItemStack(), image, v -> image = v, Items.ITEM_FRAME.getDefaultInstance());
        config.addInt("pos_x_offset", pos_x_offset, v -> pos_x_offset = v, 0, -5000, 5000);
        config.addInt("pos_y_offset", pos_y_offset, v -> pos_y_offset = v, 0, -5000, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        ItemUtils.putItemStack(nbt, "image", image);
        nbt.putInt("pos_y_offset", pos_y_offset);
        nbt.putInt("pos_x_offset", pos_x_offset);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.image = ItemUtils.getItemStack(nbt, "image");
        if(nbt.contains("pos_y_offset")) this.pos_y_offset = nbt.getInt("pos_y_offset");
        if(nbt.contains("pos_x_offset")) this.pos_x_offset = nbt.getInt("pos_x_offset");
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new ImageBestiaryEntryContent(null);
        }
    }
}
