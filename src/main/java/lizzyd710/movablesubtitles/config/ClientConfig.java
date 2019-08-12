package lizzyd710.movablesubtitles.config;

import net.minecraftforge.common.ForgeConfigSpec;

// change variable names, comments, and min/max values later. Just trying to get basics from Cadiboo's tutorial
final class ClientConfig {
    final ForgeConfigSpec.DoubleValue clientDouble; // no FloatValue - convert to float for X value at some point
    final ForgeConfigSpec.IntValue clientInt; // this will be for the Y value

    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        clientDouble = builder.comment("An example double in client config.")
                .translation("movablesubtitles.config.clientDouble")
                .defineInRange("clientDouble", 2.0, 2.0, 2.0);
        clientInt = builder.comment("An example int in client config.")
                .translation("movablesubtitles.config.clientInt")
                .defineInRange("clientInt", 30, 30, 30);
        builder.pop();
    }
}
