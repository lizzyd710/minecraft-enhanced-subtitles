package lizzyd710.enhancedsubtitles.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

// holds client configs and configspecs
// might merge into MovableSubtitlesConfig later, but just copying config structure from Cadiboo's tutorial
public final class ConfigHolder {
    public static final ForgeConfigSpec CLIENT_SPEC;
    static final ClientConfig CLIENT;
    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        CLIENT_SPEC = specPair.getRight();
    }
}