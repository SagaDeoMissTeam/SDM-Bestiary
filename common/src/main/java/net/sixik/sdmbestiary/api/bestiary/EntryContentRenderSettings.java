package net.sixik.sdmbestiary.api.bestiary;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmbestiary.api.INBTSerializable;
import net.sixik.sdmuilib.client.utils.math.Vector2;
import net.sixik.sdmuilib.client.utils.misc.CenterOperators;
import net.sixik.sdmuilib.client.utils.misc.RGBA;

public class EntryContentRenderSettings implements INBTSerializable<CompoundTag> {

    public boolean isCreated = false;
    public boolean autoSize = false;
    public boolean autoSize_advanced = false;
    public double scale;
    public CenterOperators.Type centerType;
    public Vector2 positions;
    public Vector2 size;

    public RGBA color;

    public EntryContentRenderSettings() {
        scale = 1f;
        centerType = CenterOperators.Type.NONE;
        positions = new Vector2(0,0);
        size = new Vector2(1,1);
        color = RGBA.create(255,255,255,255);
    }

    public void getConfig(ConfigGroup config) {
        config = config.getOrCreateSubgroup("render_settings");
        config.addDouble("scale", scale, v -> scale = v, 1f, 0.01D, 500.0D);
        config.addEnum("center_type", centerType, v -> centerType = v, NAME_MAP);
//        config.addInt("positions_x", positions.x, v -> positions.setX(v), 0, -40000, 40000);
//        config.addInt("positions_y", positions.y, v -> positions.setY(v), 0, -40000, 40000);
        config.addInt("size_x", size.x, v -> size.setX(v), 1, 1, 40000);
        config.addInt("size_y", size.y, v -> size.setY(v), 1, 1, 40000);
        config.addBool("autoSize", autoSize, v -> autoSize = v, false);
        config.addBool("autoSize_advanced", autoSize_advanced, v -> autoSize_advanced = v, false);
        config = config.getOrCreateSubgroup("render_settings_color");
        config.addInt("red", color.r, v -> color.r = v, 255, 0, 255);
        config.addInt("green", color.g, v -> color.g = v, 255, 0, 255);
        config.addInt("blue", color.b, v -> color.b = v, 255, 0, 255);
        config.addInt("alpha", color.a, v -> color.a = v, 255, 0, 255);
    }

    public static final NameMap<CenterOperators.Type> NAME_MAP = NameMap.of(CenterOperators.Type.NONE, CenterOperators.Type.values()).create();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("isCreated", isCreated);
        nbt.putBoolean("autoSize", autoSize);
        nbt.putBoolean("autoSize_advanced", autoSize_advanced);
        nbt.putDouble("scale", scale);
        nbt.putString("centerType", centerType.name());
        nbt.putInt("positions_x", (int) positions.x);
        nbt.putInt("positions_y", (int) positions.y);
        nbt.putInt("size_x", (int) size.x);
        nbt.putInt("size_y", (int) size.y);
        nbt.putInt("red", color.r);
        nbt.putInt("green", color.g);
        nbt.putInt("blue", color.b);
        nbt.putInt("alpha", color.a);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.isCreated = nbt.getBoolean("isCreated");
        this.autoSize = nbt.getBoolean("autoSize");
        this.autoSize_advanced = nbt.getBoolean("autoSize_advanced");
        this.scale = nbt.getDouble("scale");
        this.centerType = CenterOperators.Type.valueOf(nbt.getString("centerType"));
        this.positions.setX(nbt.getInt("positions_x"));
        this.positions.setY(nbt.getInt("positions_y"));
        this.size.setX(nbt.getInt("size_x"));
        this.size.setY(nbt.getInt("size_y"));

        if(nbt.contains("green")) {
            this.color = RGBA.create(nbt.getInt("red"), nbt.getInt("green"), nbt.getInt("blue"), nbt.getInt("alpha"));
        }
    }
}
