package lizzyd710.enhancedsubtitles.config;

import lizzyd710.enhancedsubtitles.OverlayPosition;
import net.minecraftforge.common.ForgeConfigSpec;

// change variable names, comments, and min/max values later. Just trying to get basics from Cadiboo's tutorial
final class ClientConfig {
    final ForgeConfigSpec.EnumValue<OverlayPosition> overlayPosition;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        overlayPosition = builder.comment("The position for the subtitle overlay.\nAcceptable values: BOTTOM_RIGHT, " +
                "BOTTOM_CENTER, BOTTOM_LEFT, CENTER_LEFT, TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_RIGHT")
                .translation("enhancedsubtitles.config.overlayPosition")
                .defineEnum("overlayPosition", OverlayPosition.BOTTOM_RIGHT);
        builder.pop();
    }
}
