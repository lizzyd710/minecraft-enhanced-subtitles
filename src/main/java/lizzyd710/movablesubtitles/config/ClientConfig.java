package lizzyd710.movablesubtitles.config;

import net.minecraftforge.common.ForgeConfigSpec;

// change variable names, comments, and min/max values later. Just trying to get basics from Cadiboo's tutorial
final class ClientConfig {
    final ForgeConfigSpec.DoubleValue xPos; // no FloatValue - convert to float for X value at some point
    final ForgeConfigSpec.IntValue yPos; // this will be for the Y value

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        xPos = builder.comment("The new x position for subtitles.")
                .translation("movablesubtitles.config.xPos")
                .defineInRange("xPos", 2.0, 0.0, 100.0);
        yPos = builder.comment("The new y position for subtitles.")
                .translation("movablesubtitles.config.yPos")
                .defineInRange("yPos", 30, 0, 250);
        builder.pop();
    }
}
