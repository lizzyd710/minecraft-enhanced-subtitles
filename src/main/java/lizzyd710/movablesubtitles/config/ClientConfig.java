package lizzyd710.movablesubtitles.config;

import lizzyd710.movablesubtitles.OverlayPosition;
import net.minecraftforge.common.ForgeConfigSpec;

// change variable names, comments, and min/max values later. Just trying to get basics from Cadiboo's tutorial
final class ClientConfig {
    final ForgeConfigSpec.EnumValue<OverlayPosition> overlayPosition;

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        overlayPosition = builder.comment("The position for the subtitle overlay.")
                .translation("movablesubtitles.config.overlayPosition")
                .defineEnum("overlayPosition", OverlayPosition.BOTTOM_RIGHT);
        builder.pop();
    }
}
