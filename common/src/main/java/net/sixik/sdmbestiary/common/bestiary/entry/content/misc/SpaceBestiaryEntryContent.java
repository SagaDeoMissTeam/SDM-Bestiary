package net.sixik.sdmbestiary.common.bestiary.entry.content.misc;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.common.bestiary.entry.content.text.SDMMultiTextBestiaryEntryContent;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.Colors;
import net.sixik.sdmuilib.client.utils.misc.RGB;

public class SpaceBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public int space = 0;

    public SpaceBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        super(bestiaryEntry);
    }

    @Override
    public AbstractBestiaryEntryContentWidgetPanel createWidget(AbstractBestiaryEntryContentPanel contentPanel) {
        return new AbstractBestiaryEntryContentWidgetPanel(contentPanel, this, false) {
            @Override
            public void addWidgets() {
                setProperty();
            }

            @Override
            public void setProperty() {
                if(!content.renderSettings.isCreated) {
                    content.renderSettings.centerType = CenterOperators.Type.CENTER_X;
                    content.renderSettings.positions.setPosition(contentPanel.posX, contentPanel.posY);
                    content.renderSettings.size.setPosition(contentPanel.width - contentPanel.width / 6, 2);
                    content.renderSettings.isCreated = true;
                }

                setSize(content.renderSettings.size.x, content.renderSettings.size.y);

                if(content.renderSettings.autoSize) {
                    setSize(contentPanel.width - contentPanel.width / 6, content.renderSettings.size.y);
                }
                if(space != 0) {
                    setHeight(height + space);
                }
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                if(SDMBestiary.isEditMode() && isMouseOver()) {
                    RenderHelper.drawHollowRect(graphics,x,y,w,h, RGB.create(255,255,255), false);
                }
                content.renderSettings.color.draw(graphics, x, y, w, content.renderSettings.size.y, 0);
            }
        };
    }

    @Override
    public String getID() {
        return "spaceBestiaryEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.addInt("space", space, v -> space = v, 0, 0, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        nbt.putInt("space", space);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.space = nbt.getInt("space");
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new SpaceBestiaryEntryContent(null);
        }
    }
}
