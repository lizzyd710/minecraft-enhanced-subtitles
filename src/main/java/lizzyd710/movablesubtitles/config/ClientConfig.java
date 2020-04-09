package lizzyd710.movablesubtitles.config;

import net.minecraftforge.common.ForgeConfigSpec;

// change variable names, comments, and min/max values later. Just trying to get basics from Cadiboo's tutorial
final class ClientConfig {
    final ForgeConfigSpec.DoubleValue xPos; // no FloatValue - convert to float for X value at some point
    final ForgeConfigSpec.IntValue yPos; // this will be for the Y value

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        xPos = builder.comment("An example double in client config.")
                .translation("movablesubtitles.config.xPos")
                .defineInRange("xPos", 2.0, 2.0, 2.0);
        yPos = builder.comment("An example int in client config.")
                .translation("movablesubtitles.config.yPos")
                .defineInRange("yPos", 30, 30, 30);
        builder.pop();
    }
}
