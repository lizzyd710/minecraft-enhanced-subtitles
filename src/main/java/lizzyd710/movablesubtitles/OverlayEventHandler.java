package lizzyd710.movablesubtitles;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.gui.overlay.SubtitleOverlayGui;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraft.client.gui.AbstractGui;

@EventBusSubscriber
public class OverlayEventHandler implements ISoundEventListener {
    private boolean enabled;
    private final List<SubtitleOverlayGui.Subtitle> subtitles = Lists.newArrayList();
    // just for testing
    private final float GLTRANSLATE_X = 2.0F; //2.0F is default/original value
    private final int GLTRANSLATE_Y1 = 30; //30 is default/original value

    @SubscribeEvent(receiveCanceled = true)
    public void onEvent(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {
            // this is where the magic happens
            event.setCanceled(true);
            render(Minecraft.getInstance());
        }
    }

    private void render(Minecraft mc) {
        if (!this.enabled && mc.gameSettings.showSubtitles) {
            mc.getSoundHandler().addListener(this);
            this.enabled = true;
        } else if (this.enabled && !mc.gameSettings.showSubtitles) {
            mc.getSoundHandler().removeListener(this);
            this.enabled = false;
        }

        if (this.enabled && !this.subtitles.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            Vec3d playerPosVec = new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
            Vec3d vec3d1 = (new Vec3d(0.0D, 0.0D, -1.0D)).rotatePitch(-mc.player.rotationPitch *
                    ((float) Math.PI / 180F)).rotateYaw(-mc.player.rotationYaw * ((float) Math.PI / 180F));
            Vec3d vec3d2 = (new Vec3d(0.0D, 1.0D, 0.0D)).rotatePitch(-mc.player.rotationPitch *
                    ((float) Math.PI / 180F)).rotateYaw(-mc.player.rotationYaw * ((float) Math.PI / 180F));
            Vec3d vec3d3 = vec3d1.crossProduct(vec3d2);
            int i = 0;
            int maxLength = 0;
            Iterator<SubtitleOverlayGui.Subtitle> iterator = this.subtitles.iterator();

            while (iterator.hasNext()) {
                SubtitleOverlayGui.Subtitle caption = iterator.next();
                if (caption.getStartTime() + 3000L <= Util.milliTime()) {
                    iterator.remove();
                } else {
                    maxLength = Math.max(maxLength, mc.fontRenderer.getStringWidth(caption.getString()));
                }
            }

            maxLength = maxLength + mc.fontRenderer.getStringWidth("<") + mc.fontRenderer.getStringWidth(" ")
                    + mc.fontRenderer.getStringWidth(">") + mc.fontRenderer.getStringWidth(" ");

            for (SubtitleOverlayGui.Subtitle caption1 : this.subtitles) {
                String captionText = caption1.getString();
                Vec3d distanceToPlayer = caption1.getLocation().subtract(playerPosVec).normalize();
                double d0 = -vec3d3.dotProduct(distanceToPlayer);
                double d1 = -vec3d1.dotProduct(distanceToPlayer);
                boolean flag = d1 > 0.5D;
                int l = maxLength / 2;
                int subtitleWidth = mc.fontRenderer.getStringWidth(captionText);
                int l1 = MathHelper.floor(MathHelper.clampedLerp(255.0D, 75.0D,
                        (double) ((float) (Util.milliTime() - caption1.getStartTime()) / 3000.0F)));
                int textColor = l1 << 16 | l1 << 8 | l1;
                GlStateManager.pushMatrix();
                GlStateManager.translatef((float) mc.mainWindow.getScaledWidth() - (float) l * 1.0F - GLTRANSLATE_X,
                        (float) (mc.mainWindow.getScaledHeight() - GLTRANSLATE_Y1) - (float) (i * 10) * 1.0F,
                        0.0F);
                GlStateManager.scalef(1.0F, 1.0F, 1.0F); // might be unnecessary
                AbstractGui.fill(-l - 1, -5, l + 1,
                        5, mc.gameSettings.func_216841_b(0.8F));
                GlStateManager.enableBlend();
                if (!flag) {
                    if (d0 > 0.0D)
                        mc.fontRenderer.drawString(">", (float) (l - mc.fontRenderer.getStringWidth(">")),
                                -4.0F, textColor - 16777216);
                    else if (d0 < 0.0D) 
                        mc.fontRenderer.drawString("<", (float) (-l), -4.0F, textColor - 16777216);
                }
                mc.fontRenderer.drawString(captionText, (float) (-subtitleWidth / 2), -4.0F, textColor - 16777216);
                GlStateManager.popMatrix();
                i++;
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onPlaySound(ISound soundIn, SoundEventAccessor accessor) {
        if (accessor.getSubtitle() != null) {
            String s = accessor.getSubtitle().getFormattedText();
            if (!this.subtitles.isEmpty()) {
                for (SubtitleOverlayGui.Subtitle caption : this.subtitles) {
                    if (caption.getString().equals(s)) {
                        caption.refresh(new Vec3d((double) soundIn.getX(), (double) soundIn.getY(), (double) soundIn.getZ()));
                        return;
                    }
                }
            }
            this.subtitles.add(new SubtitleOverlayGui(Minecraft.getInstance()).
                    new Subtitle(s, new Vec3d((double) soundIn.getX(), (double) soundIn.getY(), (double) soundIn.getZ())));
        }
    }
}
