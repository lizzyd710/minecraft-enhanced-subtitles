package lizzyd710.movablesubtitles.config;

import net.minecraftforge.fml.config.ModConfig;

// bakes config values to normal fields
// might merge into MovableSubtitlesConfig later, but just copying config structure from Cadiboo's tutorial
public class ConfigHelper {

    public static void bakeClient(final ModConfig config) {
        MovableSubtitlesConfig.overlayPosition = ConfigHolder.CLIENT.overlayPosition.get();
    }
    private static void setValueAndSave(final ModConfig modConfig, final String path, final Object newValue) {
        modConfig.getConfigData().set(path, newValue);
        modConfig.save();
    }
}
