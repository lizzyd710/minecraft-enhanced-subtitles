package lizzyd710.movablesubtitles;

import lizzyd710.movablesubtitles.config.ConfigHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = "movablesubtitles", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        ConfigHelper.bakeClient(config);
    }
}
