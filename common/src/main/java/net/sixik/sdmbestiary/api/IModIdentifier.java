package net.sixik.sdmbestiary.api;

public interface IModIdentifier extends IIdentifier{

    default String getModId() {
        return "minecraft";
    }
}
