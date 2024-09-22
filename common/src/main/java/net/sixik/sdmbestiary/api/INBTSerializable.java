package net.sixik.sdmbestiary.api;

import net.minecraft.nbt.CompoundTag;

public interface INBTSerializable<T> {

    T serializeNBT();
    void deserializeNBT(T nbt);


    default void serializeNBT(CompoundTag nbt) {}
}
