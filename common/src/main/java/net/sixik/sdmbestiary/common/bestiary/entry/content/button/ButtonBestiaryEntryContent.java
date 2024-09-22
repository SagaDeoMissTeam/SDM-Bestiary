package net.sixik.sdmbestiary.common.bestiary.entry.content.button;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.register.ftb.ConfigIconItemStack;
import net.sixik.sdmbestiary.common.utils.ItemUtils;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

import java.awt.*;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

public class ButtonBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public int pos_x_offset = 0;
    public int pos_y_offset = 0;

    public enum Type {
        URL,
        LINK
    }

    public static final NameMap<Type> NAME_MAP = NameMap.of(Type.LINK, Type.values()).create();

    public String text = "";
    public ItemStack image = Items.ITEM_FRAME.getDefaultInstance();
    public ItemStack imageBackground = ItemStack.EMPTY;
    public Type type = Type.LINK;
    public String path = "";

    public ButtonBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        super(bestiaryEntry);
    }

    @Override
    public AbstractBestiaryEntryContentWidgetPanel createWidget(AbstractBestiaryEntryContentPanel contentPanel) {
        return new AbstractBestiaryEntryContentWidgetPanel(contentPanel, this, false) {

            public SimpleTextButton button;

            @Override
            public void addWidgets() {
                add(this.button = new SimpleTextButton(this, Component.translatable(text), SDMBestiary.getIcon(image)) {
                    @Override
                    public void onClicked(MouseButton button) {
                        if(button.isLeft() && !path.isEmpty()) {
                            switch (type) {
                                case URL -> {
                                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                                        try {
                                            desktop.browse(new URI(path));
                                        } catch (Exception e) {
                                            SDMBestiary.printStackTrace("", e);
                                        }
                                    }
                                }
                                case LINK -> {
                                    try {
                                        UUID uuid = UUID.fromString(path);
                                        AbstractBestiaryTab tab = BestiaryBase.CLIENT.getBestiaryTab(uuid);
                                        if(tab != null) {
                                            contentPanel.getBestiaryScreen().selectedTab = tab;
                                            contentPanel.getBestiaryScreen().setProperty();
                                            return;
                                        } else {
                                            for (AbstractBestiaryTab bestiaryTab : BestiaryBase.CLIENT.getBestiaryTabs()) {
                                                for (AbstractBestiaryEntry tabEntry : bestiaryTab.getTabEntries()) {
                                                    if(Objects.equals(tabEntry.entryID, uuid)) {
                                                        contentPanel.getBestiaryScreen().selectedTab = bestiaryTab;
                                                        contentPanel.getBestiaryScreen().selectedEntry = tabEntry;
                                                        contentPanel.getBestiaryScreen().setProperty();
                                                        return;
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e){
                                        SDMBestiary.printStackTrace("", e);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                        if (!imageBackground.isEmpty()) {
                            Icon d = SDMBestiary.getIcon(imageBackground);
                            d.draw(graphics, x, y, w, h);
                        }
                        super.drawIcon(graphics, theme, x, y, w, h);
                    }

                    @Override
                    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                        if(isMouseOver) {
                            RGBA.create(255,255,255,255/3).drawRoundFill(graphics, x,y,w,h,2);
                        } else {
                            RGBA.create(0,0,0,255/3).drawRoundFill(graphics, x,y,w,h,2);
                        }
                    }
                });
            }

            @Override
            public void setProperty() {
                if(!content.renderSettings.isCreated) {
                    content.renderSettings.centerType = CenterOperators.Type.CENTER_X;
                    content.renderSettings.positions.setPosition(contentPanel.posX, contentPanel.posY);
                    content.renderSettings.size.setPosition(contentPanel.width - contentPanel.width / 6, 14);
                    content.renderSettings.isCreated = true;
                }

                setSize(content.renderSettings.size.x, content.renderSettings.size.y);
                this.button.setSize(this.width, this.height);
            }
        };
    }

    @Override
    public String getID() {
        return "buttonEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.addString("text", text, v -> text = v, "");
        config.add("image", new ConfigIconItemStack(), image, v -> image = v, Items.ITEM_FRAME.getDefaultInstance());
        config.add("imageBackground", new ConfigIconItemStack(), imageBackground, v -> imageBackground = v, ItemStack.EMPTY);
        config.addEnum("button_type", Type.LINK, v -> type = v, NAME_MAP);
        config.addString("path", path, v -> path = v, "");
        config.addInt("pos_x_offset", pos_x_offset, v -> pos_x_offset = v, 0, -5000, 5000);
        config.addInt("pos_y_offset", pos_y_offset, v -> pos_y_offset = v, 0, -5000, 5000);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        ItemUtils.putItemStack(nbt, "image", image);
        ItemUtils.putItemStack(nbt, "imageBackground", imageBackground);
        nbt.putString("text", text);
        nbt.putString("path", path);
        nbt.putString("button_type", type.name());
        nbt.putInt("pos_y_offset", pos_y_offset);
        nbt.putInt("pos_x_offset", pos_x_offset);

    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.image = ItemUtils.getItemStack(nbt, "image");
        this.imageBackground = ItemUtils.getItemStack(nbt, "imageBackground");
        this.text = nbt.getString("text");
        this.path = nbt.getString("path");
        this.type = Type.valueOf(nbt.getString("button_type"));
        if(nbt.contains("pos_y_offset")) this.pos_y_offset = nbt.getInt("pos_y_offset");
        if(nbt.contains("pos_x_offset")) this.pos_x_offset = nbt.getInt("pos_x_offset");
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new ButtonBestiaryEntryContent(null);
        }
    }
}
