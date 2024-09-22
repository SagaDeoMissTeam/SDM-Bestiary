package net.sixik.sdmbestiary;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.sixik.sdmbestiary.client.screen.modern.ModernBestiaryScreen;
import net.sixik.sdmbestiary.common.bestiary.BestiaryBase;
import org.lwjgl.glfw.GLFW;

public class SDMBestiaryClient {

    public static final ResourceLocation OPEN_GUI = new ResourceLocation(SDMBestiary.MODID, "open_gui");
    public static final String BESTIARY_CATEGORY = "key.category.sdmbestiary";
    public static final String KEY_NAME = "key.sdmbestiary.screen";

    public static KeyMapping KEY_BESTIARY = new KeyMapping(KEY_NAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, BESTIARY_CATEGORY);

    public static void init() {
        ClientTickEvent.CLIENT_PRE.register(SDMBestiaryClient::keyInput);
        CustomClickEvent.EVENT.register(SDMBestiaryClient::customClick);
        KeyMappingRegistry.register(KEY_BESTIARY);
        loadEvents();
    }


    public static void loadEvents() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((player -> {
            BestiaryBase.load();
            Config.reload();
        }));
    }

    public static void keyInput(Minecraft mc) {
        if (KEY_BESTIARY.consumeClick()) {
            new ModernBestiaryScreen().openGui();
        }
    }

    public static EventResult customClick(CustomClickEvent event) {
        if (event.id().equals(OPEN_GUI)) {
            new ModernBestiaryScreen().openGui();
            return EventResult.interruptTrue();
        }

        return EventResult.pass();
    }

}
