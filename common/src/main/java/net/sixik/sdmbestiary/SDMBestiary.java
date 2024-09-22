package net.sixik.sdmbestiary;

import com.mojang.logging.LogUtils;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmbestiary.api.register.BestiaryContentRegister;
import net.sixik.sdmbestiary.common.bestiary.entry.content.button.ButtonBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.image.ImageBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.image.ImageWithBackgroundBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.misc.EntityBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.misc.SpaceBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.text.FTBTextBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.text.SDMMultiTextBestiaryEntryContent;
import net.sixik.sdmbestiary.common.bestiary.entry.content.text.SDMTextBestiaryEntryContent;
import net.sixik.sdmbestiary.common.register.item.BestiaryCustomIconItem;
import net.sixik.sdmbestiary.common.register.item.ItemsRegister;
import org.slf4j.Logger;

import java.util.Objects;

public class SDMBestiary
{
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String MODID = "sdm_bestiary";


	public static Icon getIcon(ItemStack itemStack){
		if(itemStack.is(ItemsRegister.CUSTOM_ICON.get())){
			return BestiaryCustomIconItem.getIcon(itemStack);
		}
		return ItemIcon.getItemIcon(itemStack);
	}

	public static boolean isEditMode() {
		return Config.EDIT_MODE.get();
	}

	public static ChatFormatting getChatFormatting(char id) {
		return ChatFormatting.getByCode(id);
	}

	public static void init() {
		SDMBestiaryPath.initFilesAndFolders();
		Config.init(SDMBestiaryPath.getModFolder());
		ItemsRegister.ITEMS.register();
		BestiaryContentRegister.init();
		register();
		loadEvents();
		EnvExecutor.runInEnv(Env.CLIENT, () -> SDMBestiaryClient::init);
	}

	public static void register() {
		BestiaryContentRegister.registerContent(new ButtonBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new ImageBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new ImageWithBackgroundBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new FTBTextBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new SDMTextBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new SDMMultiTextBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new SpaceBestiaryEntryContent.Construct());
		BestiaryContentRegister.registerContent(new EntityBestiaryEntryContent.Construct());
//		BestiaryContentRegister.registerContent(new TextMultilineEntryContent.Construct());
	}

	public static void loadEvents() {

	}

	public static void printStackTrace(String str, Throwable s){
		StringBuilder strBuilder = new StringBuilder(str);
		for (StackTraceElement stackTraceElement : s.getStackTrace()) {
			strBuilder.append("\t").append(" ").append("at").append(" ").append(stackTraceElement).append("\n");
		}
		str = strBuilder.toString();

		for (Throwable throwable : s.getSuppressed()) {
			printStackTrace(str, throwable);
		}

		Throwable ourCause = s.getCause();
		if(ourCause != null){
			printStackTrace(str, ourCause);
		}


		SDMBestiary.LOGGER.error(str);

	}
}
