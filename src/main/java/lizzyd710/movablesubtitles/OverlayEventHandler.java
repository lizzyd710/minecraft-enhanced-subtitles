package lizzyd710.movablesubtitles;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber
public class OverlayEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent(receiveCanceled = true)
    public void onEvent(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {
            // this is where the magic happens
            LOGGER.info("SUBTITLE PRERENDER EVENT");
        }
    }

}
