package net.sixik.sdmbestiary;

import dev.architectury.platform.Platform;

import java.nio.file.Path;

public class SDMBestiaryPath {


    public static void initFilesAndFolders() {
        if(!getModFolder().toFile().exists()){
            getModFolder().toFile().mkdir();
        }
    }

    public static Path getModFolder(){
        return Platform.getConfigFolder().resolve("SDMBestiary");
    }

    public static Path getClientConfig(){
        return getModFolder().resolve("sdmbestiary-client.snbt");
    }

    public static Path getFile() {
        return getModFolder().resolve("sdmbestiary.snbt");
    }
}
