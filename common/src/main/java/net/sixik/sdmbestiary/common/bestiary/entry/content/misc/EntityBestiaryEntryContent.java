package net.sixik.sdmbestiary.common.bestiary.entry.content.misc;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.util.KnownServerRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.sixik.sdmbestiary.IConst;
import net.sixik.sdmbestiary.SDMBestiary;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntry;
import net.sixik.sdmbestiary.api.bestiary.AbstractBestiaryEntryContent;
import net.sixik.sdmbestiary.client.screen.basic.panel.AbstractBestiaryEntryContentPanel;
import net.sixik.sdmbestiary.client.screen.basic.widget.AbstractBestiaryEntryContentWidgetPanel;
import net.sixik.sdmuilib.client.utils.RenderHelper;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGB;
import org.jetbrains.annotations.Nullable;

public class EntityBestiaryEntryContent extends AbstractBestiaryEntryContent {

    public ResourceLocation entityType = new ResourceLocation("minecraft:zombie");
    public double yaw = 0.0f;
    public double pitch = 180f;
    public int pos_x_offset = 0;
    public int pos_y_offset = 0;
    public boolean auto_rotate = false;
    public int speed_rotate = 1;

    public EntityBestiaryEntryContent(AbstractBestiaryEntry bestiaryEntry) {
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
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                if(SDMBestiary.isEditMode() && isMouseOver()) {
                    RenderHelper.drawHollowRect(graphics,x,y,w,h, RGB.create(255,255,255), false);
                }
                LivingEntity entity = getEntity(BuiltInRegistries.ENTITY_TYPE.get(entityType));
                if(entity == null) return;
                y += pos_y_offset;
                x += pos_x_offset;
                RenderHelper.drawLivingEntity(graphics, x + w / 2,y + h, content.renderSettings.scale, auto_rotate ? contentPanel.getBestiaryScreen().tick + speed_rotate : yaw, pitch, entity);
            }

            public @Nullable LivingEntity getEntity(EntityType<?> type) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    Entity entity = type.create(mc.level);
                    if(entity instanceof LivingEntity livingEntity)
                        return livingEntity;
                }
                return null;
            }

            @Override
            public void setProperty() {
                if(!content.renderSettings.isCreated) {
                    content.renderSettings.centerType = CenterOperators.Type.CENTER_X;
                    content.renderSettings.positions.setPosition(contentPanel.posX, contentPanel.posY);
                    content.renderSettings.size.setPosition(32, 32);
                    content.renderSettings.scale = 20f;
                    content.renderSettings.isCreated = true;
                }

                setSize(content.renderSettings.size.x, content.renderSettings.size.y);
            }
        };
    }

    @Override
    public String getID() {
        return "entityBestiaryEntryContent";
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {
        nbt.putDouble("yaw", yaw);
        nbt.putDouble("pitch", pitch);
        nbt.putInt("pos_x_offset", pos_x_offset);
        nbt.putInt("pos_y_offset", pos_y_offset);
        nbt.putInt("speed_rotate", speed_rotate);
        nbt.putBoolean("auto_rotate", auto_rotate);
        nbt.putString("entity", entityType.toString());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.yaw = nbt.getDouble("yaw");
        this.pitch = nbt.getDouble("pitch");
        this.pos_x_offset = nbt.getInt("pos_x_offset");
        this.pos_y_offset = nbt.getInt("pos_y_offset");
        this.speed_rotate = nbt.getInt("speed_rotate");
        this.auto_rotate = nbt.getBoolean("auto_rotate");
        this.entityType = new ResourceLocation(nbt.getString("entity"));
    }

    @Override
    public void getConfig(ConfigGroup config) {
        config.addEnum("entity", entityType, v -> entityType = v, NameMap.of(BuiltInRegistries.ENTITY_TYPE.keySet().iterator().next(), BuiltInRegistries.ENTITY_TYPE.keySet().toArray(new ResourceLocation[0]))
                .name(resourceLocation -> Component.literal(resourceLocation.toString()))
                .create()
        );
        config.addDouble("yaw", yaw, v -> yaw = v, 0, -360f, 360f);
        config.addDouble("pitch", pitch, v -> pitch = v, 180f, -360f, 360f);
        config.addInt("pos_x_offset", pos_x_offset, v -> pos_x_offset = v, 0, -5000, 5000);
        config.addInt("pos_y_offset", pos_y_offset, v -> pos_y_offset = v, 0, -5000, 5000);
        config.addBool("auto_rotate", auto_rotate , v -> auto_rotate = v, false);
        config.addInt("speed_rotate", speed_rotate, v -> speed_rotate = v, 1, 1, 5000);
    }

    public static class Construct implements IConst<AbstractBestiaryEntryContent> {

        @Override
        public AbstractBestiaryEntryContent createDefaultInstance() {
            return new EntityBestiaryEntryContent(null);
        }
    }
}
