package net.sixik.sdmbestiary.client.screen.modern;

import dev.ftb.mods.ftblibrary.ui.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryTab;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryTabButton;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryTabButton;
import net.sixik.sdmbestiary.client.screen.modern.panels.ModernBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.modern.panels.ModernBestiaryEntryPanel;
import net.sixik.sdmbestiary.client.screen.modern.panels.ModernBestiaryTabPanel;
import net.sixik.sdmbestiary.client.screen.modern.widget.ModernBestiaryEntryContentWidgetPanel;
import net.sixik.sdmbestiary.client.screen.modern.widget.ModernBestiaryEntryTabButton;
import net.sixik.sdmbestiary.client.screen.modern.widget.ModernBestiaryTabButton;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmuilib.client.utils.math.Vector2;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

import java.util.ArrayList;
import java.util.List;

public class ModernBestiaryScreen extends AbstractBestiaryScreen {

    public Vector2 tabEntryPanelSize = new Vector2(0,0);

    @Override
    public void addWidgets() {
        add(this.bestiaryTabPanel = new ModernBestiaryTabPanel(this));
        add(this.bestiaryTabScrollPanel = new PanelScrollBar(this, ScrollBar.Plane.HORIZONTAL, bestiaryTabPanel) {
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(217, 137, 42).draw(graphics, x, y, w, h, 1);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h,0);
            }
        });
        add(this.bestiaryEntryPanel = new ModernBestiaryEntryPanel(this));
        add(this.bestiaryEntryScrollPanel = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, bestiaryEntryPanel) {
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(217, 137, 42).draw(graphics, x, y, w, h, 1);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h,0);
            }
        });
        add(this.bestiaryEntryContentPanel = new ModernBestiaryEntryContentPanel(this));
        add(this.bestiaryEntryContentScrollPanel = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, bestiaryEntryContentPanel) {
            @Override
            public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(217, 137, 42).draw(graphics, x, y, w, h, 1);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                RGBA.create(0,0,0, 255/2).draw(graphics,x,y,w,h,0);
            }
        });
        setProperty();
    }

    @Override
    public void setProperty() {
        this.bestiaryTabPanel.setSize(this.width, this.height / 8);
        this.bestiaryEntryPanel.setSize(this.width / 3, this.height - bestiaryTabPanel.height - 2);
        this.bestiaryEntryPanel.setPos(0, bestiaryTabPanel.height + 2);
        this.bestiaryEntryContentPanel.setSize(this.width - bestiaryEntryPanel.width - 2, this.height - bestiaryTabPanel.height - 2);
        this.bestiaryEntryContentPanel.setPos(bestiaryEntryPanel.width + 2, bestiaryTabPanel.height + 2);

        this.bestiaryTabScrollPanel.setPosAndSize(
                this.bestiaryTabPanel.getPosX() + 10,
                this.bestiaryTabPanel.getPosY() + this.bestiaryTabPanel.getHeight() - this.getScrollbarWidth(),
                this.bestiaryTabPanel.getWidth() - 20,
                this.getScrollbarWidth()
        );

        this.bestiaryEntryScrollPanel.setPosAndSize(
                this.bestiaryEntryPanel.getPosX() + this.bestiaryEntryPanel.getWidth() - this.getScrollbarWidth(),
                this.bestiaryEntryPanel.getPosY() + 10,
                this.getScrollbarWidth(),
                this.bestiaryEntryPanel.getHeight() - 20
        );

        this.bestiaryEntryContentScrollPanel.setPosAndSize(
                this.bestiaryEntryContentPanel.getPosX() + this.bestiaryEntryContentPanel.getWidth() - this.getScrollbarWidth(),
                this.bestiaryEntryContentPanel.getPosY() + 10,
                this.getScrollbarWidth(),
                this.bestiaryEntryContentPanel.getHeight() - 20
        );

        this.tabButtonsSize = this.bestiaryTabPanel.height - (this.bestiaryTabPanel.height / 5);


        int d = (this.bestiaryTabPanel.height - tabButtonsSize) / 2;
        this.tabEntryPanelSize = new Vector2(this.bestiaryEntryPanel.width - d * 2, this.bestiaryEntryPanel.height - d * 2);

        addTabButtons();
        addEntryButtons();
        addEntryContents();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

    }

    @Override
    public void addEntryContents() {
        if(selectedEntry != null) {
            bestiaryEntryContentPanel.clearWidgets();
            List<AbstractBestiaryEntryContentWidgetPanel> contents = new ArrayList<>();

            for (int i = 0; i < selectedEntry.getEntryContents().size(); i++) {
                AbstractBestiaryEntryContent content = selectedEntry.getEntryContents().get(i);
                AbstractBestiaryEntryContentWidgetPanel d = content.createWidget(bestiaryEntryContentPanel);
                d.addWidgets();
                d.alignWidgets();
                contents.add(d);
            }


            calculatePositionEntryContents(contents);


            this.bestiaryEntryContentPanel.getWidgets().clear();
            this.bestiaryEntryContentPanel.addAll(contents);
            this.bestiaryEntryContentScrollPanel.setValue(0);
        }
    }

    public void calculatePositionEntryContents(List<AbstractBestiaryEntryContentWidgetPanel> contents) {

        int x = 0;
        int y = 6;

        for (AbstractBestiaryEntryContentWidgetPanel content : contents) {
            if(content.width > bestiaryEntryContentPanel.width) {

                int d = content.width - bestiaryEntryContentPanel.width;

                content.setWidth(content.width - d);
            }

            switch (content.content.renderSettings.centerType) {
                case CENTER_X -> {
                    if(bestiaryEntryContentPanel.width != content.width) {
                        content.setPos((bestiaryEntryContentPanel.width - content.width) / 2, y);
                    }
                }
                default -> {}
            }

            content.setY(y);
            y += content.height + 2;
        }



        if(SDMBestiary.isEditMode()) {
            ModernBestiaryEntryContentWidgetPanel panel = new ModernBestiaryEntryContentWidgetPanel(bestiaryEntryContentPanel, null, true);
            panel.setSize(bestiaryEntryContentPanel.width - bestiaryEntryContentPanel.width / 6, 10);
            panel.addWidgets();
            panel.alignWidgets();
            panel.setPos((bestiaryEntryContentPanel.width - panel.width) / 2, y);
            y += panel.height + 2;
            contents.add(panel);
        }

        ModernBestiaryEntryContentWidgetPanel panel = new ModernBestiaryEntryContentWidgetPanel(bestiaryEntryContentPanel, null, false) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

            }

            @Override
            public boolean checkMouseOver(int mouseX, int mouseY) {
                return false;
            }
        };
        panel.setPos(0, y + 50);
        contents.add(panel);
    }

    @Override
    public void addEntryButtons() {
        if(selectedTab != null) {
            bestiaryEntryPanel.clearWidgets();
            List<Widget> buttons = new ArrayList<>();

            for (AbstractBestiaryEntry tabEntry : BestiaryBase.CLIENT.getBestiaryTab(selectedTab.tabID).getTabEntries()) {
                ModernBestiaryEntryTabButton button = new ModernBestiaryEntryTabButton(bestiaryEntryPanel, tabEntry);
                button.setSize(tabButtonsSize, tabButtonsSize);
                buttons.add(button);
            }

            if(SDMBestiary.isEditMode()) {
                ModernBestiaryEntryTabButton button = new ModernBestiaryEntryTabButton(bestiaryEntryPanel, null);
                button.setEdit();
                button.setSize(tabButtonsSize, tabButtonsSize);
                buttons.add(button);
            }

            calculatePositionEntryButtons(buttons);

            this.bestiaryEntryPanel.getWidgets().clear();
            this.bestiaryEntryPanel.addAll(buttons);
            this.bestiaryEntryScrollPanel.setValue(0);
        }
    }

    public void calculatePositionEntryButtons(List<Widget> buttons) {
        Vector2 d = tabEntryPanelSize;

        int maxElementsOnScreen = countElementsOnPanel(bestiaryEntryPanel, tabButtonsSize, 2) - 1;
        int f = centerPositionXOnPanel(bestiaryEntryPanel, tabButtonsSize, 2);
        int x = f;
        int y = (this.bestiaryTabPanel.height - tabButtonsSize) / 2;

        if(selectedTab.renderTitle) {

            MutableComponent text = Component.empty();

            String[] parts = selectedTab.title.split("(?=&.)");

            for (String part : parts) {
                if (part.startsWith("&")) {
                    String code = part.substring(1, 2);
                    String content = part.substring(2);

                    ChatFormatting formatting = SDMBestiary.getChatFormatting(code.charAt(0));

                    if (formatting == null) {
                        text = text.append(Component.translatable(content));
                    } else text = text.append(Component.translatable(content).withStyle(formatting));
                }
                else {
                    text = text.append(Component.translatable(part));
                }
            }

            TextField field = new TextField(bestiaryEntryPanel);
            field.setPos(x, y);
            field.setText(text);
            field.setScale((float) selectedTab.titleScale);
            field.setMaxWidth(bestiaryEntryPanel.width);
            field.resize(Theme.DEFAULT);
            buttons.add(field);
            y += field.height + 2;
        }


        for (int i = 0; i < buttons.size(); i++) {
            Widget shopEntryButton = buttons.get(i);
            if(!(shopEntryButton instanceof AbstractBestiaryEntryTabButton)) continue;

            if(i > 0) {
                if (i % maxElementsOnScreen == 0) {
                    y += tabButtonsSize + 2;
                    x = f;
                } else {
                    x += tabButtonsSize + 2;
                }
                shopEntryButton.setPos(x,y);
            }
            else shopEntryButton.setPos(x,y);
        }

        AbstractBestiaryEntryTabButton h = new AbstractBestiaryEntryTabButton(bestiaryEntryPanel, null) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

            }

            @Override
            public boolean checkMouseOver(int mouseX, int mouseY) {
                return false;
            }
        };
        h.setEdit();
        h.setPos(0, y + 40);
        buttons.add(h);
    }

    public int countElementsOnPanel(Widget widget, int size, int offset) {
        int count = 0;
        int s = widget.width;
        for(int i = 0; i < 1000; i++) {
            if((size + offset) * i >= s) {
                count = i;
                break;
            }
        }

        return count > 1 ? count - 1 : count;
    }

    public int centerPositionXOnPanel(Widget widget, int size, int offset) {
        return centerPositionXOnPanel(widget, 0, size, offset);
    }

    public int centerPositionXOnPanel(Widget widget, int dX, int size, int offset) {
        int count = countElementsOnPanel(widget, size, offset);
        int x1 = (dX * count ) + (offset * count);
        return x1 / 2;
    }

    @Override
    public void addTabButtons() {
        bestiaryTabPanel.clearWidgets();
        List<AbstractBestiaryTabButton> buttons = new ArrayList<>();

        for (AbstractBestiaryTab bestiaryTab : BestiaryBase.CLIENT.getBestiaryTabs()) {
            ModernBestiaryTabButton button = new ModernBestiaryTabButton(bestiaryTabPanel, bestiaryTab);
            button.setSize(tabButtonsSize, tabButtonsSize);
            buttons.add(button);
        }

        if(SDMBestiary.isEditMode()) {
            ModernBestiaryTabButton button = new ModernBestiaryTabButton(bestiaryTabPanel, null);
            button.setEdit();
            button.setSize(tabButtonsSize, tabButtonsSize);
            buttons.add(button);
        }

        ModernBestiaryTabButton button = new ModernBestiaryTabButton(bestiaryTabPanel, null) {
            @Override
            public boolean checkMouseOver(int mouseX, int mouseY) {
                return false;
            }

            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

            }
        };
        button.setSize(tabButtonsSize, tabButtonsSize);
        buttons.add(button);

        calculatePositonsTabButtons(buttons);

        this.bestiaryTabPanel.getWidgets().clear();
        this.bestiaryTabPanel.addAll(buttons);
        this.bestiaryTabScrollPanel.setValue(0);
    }

    public void calculatePositonsTabButtons(List<AbstractBestiaryTabButton> buttons) {

        int x = (this.bestiaryTabPanel.height - tabButtonsSize) / 2 * 2;
        int y = (this.bestiaryTabPanel.height - tabButtonsSize) / 2;

        for (AbstractBestiaryTabButton button : buttons) {
            button.setPos(x, y);
            x += tabButtonsSize + 2;
        }
    }

    protected int getScrollbarWidth() {
        return 2;
    }
}
