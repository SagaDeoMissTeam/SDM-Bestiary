package net.sixik.sdmbestiary.common.bestiary.entry.content.panels;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;

import java.util.ArrayList;
import java.util.List;

public class ArrayBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public List<AbstractBestiaryEntryContent> contentList = new ArrayList<>();

    public ArrayBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
        super(bestiaryEntry);
    }

    @Override
    public AbstractBestiaryEntryContentWidgetPanel createWidget(AbstractBestiaryEntryContentPanel contentPanel) {
        return new AbstractBestiaryEntryContentWidgetPanel(contentPanel, this, false) {
            @Override
            public void addWidgets() {
                for (AbstractBestiaryEntryContent abstractBestiaryEntryContent : contentList) {
//                    abstractBestiaryEntryContent.createWidget()
                }

                super.addWidgets();
            }
        };
    }

    @Override
    public String getID() {
        return "arrayBestiaryEntryContent";
    }

    @Override
    public void getConfig(ConfigGroup config) {

    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        ListTag tagContent = new ListTag();
        for (AbstractBestiaryEntryContent abstractBestiaryEntryContent : contentList) {
            tagContent.add(abstractBestiaryEntryContent.serializeNBT());
        }
        nbt.put("contents", tagContent);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        ListTag tagContent = (ListTag) nbt.get("contents");
        contentList.clear();
        for (Tag tag : tagContent) {
            contentList.add(AbstractBestiaryEntryContent.from((CompoundTag) tag));
        }
    }
}
