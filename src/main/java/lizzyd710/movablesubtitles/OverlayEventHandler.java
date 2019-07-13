package lizzyd710.movablesubtitles;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.SubtitleOverlayGui;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.AbstractGui;

@EventBusSubscriber
public class OverlayEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean enabled;
    private final List<SubtitleOverlayGui.Subtitle> subtitles = Lists.newArrayList();


    @SubscribeEvent(receiveCanceled = true)
    public void onEvent(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {
            // this is where the magic happens
            LOGGER.info("SUBTITLE PRERENDER EVENT");
            Minecraft mc = Minecraft.getInstance();
            event.setCanceled(true);
            render(mc);
        }
    }

    private void render(Minecraft mc) {
        if (!this.enabled && mc.gameSettings.showSubtitles) {
            //mc.getSoundHandler().addListener(this);
            this.enabled = true;
        } else if (this.enabled && !mc.gameSettings.showSubtitles) {
            //mc.getSoundHandler().removeListener(this);
            this.enabled = false;
        }

        if (this.enabled && !this.subtitles.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Vec3d vec3d = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
            Vec3d vec3d1 = (new Vec3d(0.0D, 0.0D, -1.0D)).rotatePitch(-mc.player.rotationPitch * 
                    ((float)Math.PI / 180F)).rotateYaw(-mc.player.rotationYaw * ((float)Math.PI / 180F));
            Vec3d vec3d2 = (new Vec3d(0.0D, 1.0D, 0.0D)).rotatePitch(-mc.player.rotationPitch * 
                    ((float)Math.PI / 180F)).rotateYaw(-mc.player.rotationYaw * ((float)Math.PI / 180F));
            Vec3d vec3d3 = vec3d1.crossProduct(vec3d2);
            int i = 0;
            int j = 0;
            Iterator<SubtitleOverlayGui.Subtitle> iterator = this.subtitles.iterator();

            while(iterator.hasNext()) {
                SubtitleOverlayGui.Subtitle subtitleoverlaygui$subtitle = iterator.next();
                if (subtitleoverlaygui$subtitle.getStartTime() + 3000L <= Util.milliTime()) {
                    iterator.remove();
                } else {
                    j = Math.max(j, mc.fontRenderer.getStringWidth(subtitleoverlaygui$subtitle.getString()));
                }
            }

            j = j + mc.fontRenderer.getStringWidth("<") + mc.fontRenderer.getStringWidth(" ") + 
                    mc.fontRenderer.getStringWidth(">") + mc.fontRenderer.getStringWidth(" ");

            for(SubtitleOverlayGui.Subtitle subtitleoverlaygui$subtitle1 : this.subtitles) {
                String s = subtitleoverlaygui$subtitle1.getString();
                Vec3d vec3d4 = subtitleoverlaygui$subtitle1.getLocation().subtract(vec3d).normalize();
                double d0 = -vec3d3.dotProduct(vec3d4);
                double d1 = -vec3d1.dotProduct(vec3d4);
                boolean flag = d1 > 0.5D;
                int l = j / 2;
                int j1 = 9 / 2;
                int k1 = mc.fontRenderer.getStringWidth(s);
                int l1 = MathHelper.floor(MathHelper.clampedLerp(255.0D, 75.0D, 
                        (double)((float)(Util.milliTime() - subtitleoverlaygui$subtitle1.getStartTime()) / 3000.0F)));
                int i2 = l1 << 16 | l1 << 8 | l1;
                GlStateManager.pushMatrix();
                GlStateManager.translatef((float)mc.mainWindow.getScaledWidth() - (float)l * 1.0F - 2.0F,
                        (float)(mc.mainWindow.getScaledHeight() - 30) - (float)(i * (9 + 1)) * 1.0F,
                        0.0F);
                GlStateManager.scalef(1.0F, 1.0F, 1.0F);
                AbstractGui.fill(-l - 1, -j1 - 1, l + 1, 
                        j1 + 1, mc.gameSettings.func_216841_b(0.8F));
                GlStateManager.enableBlend();
                if (!flag) {
                    if (d0 > 0.0D) {
                        mc.fontRenderer.drawString(">", (float)(l - mc.fontRenderer.getStringWidth(">")), 
                                (float)(-j1), i2 + -16777216);
                    } 
                    else if (d0 < 0.0D) {
                        mc.fontRenderer.drawString("<", (float)(-l), (float)(-j1), i2 + -16777216);
                    }
                }

                mc.fontRenderer.drawString(s, (float)(-k1 / 2), (float)(-j1), i2 + -16777216);
                GlStateManager.popMatrix();
                ++i;
            }

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
}
