package lizzyd710.movablesubtitles.config;

import net.minecraftforge.fml.config.ModConfig;

// bakes config values to normal fields
// might merge into MovableSubtitlesConfig later, but just copying config structure from Cadiboo's tutorial
public class ConfigHelper {
    // store reference to the configs so I can change values from config GUI later
    private static ModConfig clientConfig;

    public static void bakeClient(final ModConfig config) {
        clientConfig = config;

        MovableSubtitlesConfig.translateX =  ConfigHolder.CLIENT.clientDouble.get().floatValue();
        MovableSubtitlesConfig.translateY = ConfigHolder.CLIENT.clientInt.get();
    }
    private static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
        modConfig.getConfigData().set(path, newValue);
        modConfig.save();
    }
}
