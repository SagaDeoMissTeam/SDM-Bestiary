package net.sixik.sdmbestiary.api.register;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.AbstractBestiaryScreen;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import net.sixik.sdmbestiary.common.bestiary.entry.BestiaryEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BestiaryContentRegister {

    public static final LinkedHashMap<String, IConst<AbstractBestiaryEntryContent>> CONTENTS = new LinkedHashMap<>();


    public static IConst<AbstractBestiaryEntryContent> registerContent(IConst<AbstractBestiaryEntryContent> content) {
        AbstractBestiaryEntryContent d1 = content.createDefaultInstance();
        if(!CONTENTS.containsKey(d1.getID())) {
            CONTENTS.put(d1.getID(), content);
        } else {
            SDMBestiary.LOGGER.error("Duplicate content ID {} in {}", d1.getID(), d1);
        }

        return content;
    }

    public static List<ContextMenuItem> createContextMenuItems(AbstractBestiaryScreen screen) {
        List<ContextMenuItem> contextMenu = new ArrayList<>();

        for (Map.Entry<String, IConst<AbstractBestiaryEntryContent>> constEntry : CONTENTS.entrySet()) {
            AbstractBestiaryEntryContent d1 = constEntry.getValue().createDefaultInstance();
            if(Platform.isModLoaded(d1.getModId())) {
                contextMenu.add(new ContextMenuItem(d1.getTranslatableForCreativeMenu(), d1.getCreativeIcon(), (b) -> {
                    try {
                        if (screen.selectedEntry != null) {
                            d1.setBestiaryEntry(screen.selectedEntry);
                            screen.selectedEntry.getEntryContents().add(d1);
                            screen.addEntryContents();

                            screen.bestiaryEntryContentScrollPanel.setValue(screen.bestiaryEntryContentScrollPanel.getMaxValue());
                            BestiaryBase.save();
                        }
                    } catch (Exception e) {
                        SDMBestiary.printStackTrace("", e);
                    }
                }));
            }
        }

        return contextMenu;
    }


    public static void init() {

    }
}
