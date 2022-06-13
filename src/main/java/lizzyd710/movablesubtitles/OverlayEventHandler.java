package lizzyd710.movablesubtitles;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import lizzyd710.movablesubtitles.config.MovableSubtitlesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Iterator;
import java.util.List;

@EventBusSubscriber
public class OverlayEventHandler extends GuiComponent implements SoundEventListener {
    private boolean isListening;
    private final List<SubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();

    @SubscribeEvent(receiveCanceled = true)
    public void onEvent(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.SUBTITLES_ELEMENT) {
            // this is where the magic happens
            event.setCanceled(true);
            render(Minecraft.getInstance(), event.getMatrixStack());
        }
    }
    // basically copied from net.minecraft.client.components.SubtitleOverlay
    private void render(Minecraft mc, PoseStack poseStack) {
        if (!this.isListening && mc.options.showSubtitles) {
            mc.getSoundManager().addListener(this);
            this.isListening = true;
        } else if (this.isListening && !mc.options.showSubtitles) {
            mc.getSoundManager().removeListener(this);
            this.isListening = false;
        }

        if (this.isListening && !this.subtitles.isEmpty()) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Vec3 playerPosVec = new Vec3(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());
            Vec3 vec3d1 = (new Vec3(0.0D, 0.0D, -1.0D)).xRot(-mc.player.getXRot() *
                    ((float) Math.PI / 180F)).yRot(-mc.player.getYRot() * ((float) Math.PI / 180F));
            Vec3 vec3d2 = (new Vec3(0.0D, 1.0D, 0.0D)).xRot(-mc.player.getXRot() *
                    ((float) Math.PI / 180F)).yRot(-mc.player.getYRot() * ((float) Math.PI / 180F));
            Vec3 vec3d3 = vec3d1.cross(vec3d2);
            int maxLength = 0;
            Iterator<SubtitleOverlay.Subtitle> iterator = this.subtitles.iterator();

            while (iterator.hasNext()) {
                SubtitleOverlay.Subtitle caption = iterator.next();
                if (caption.getTime() + 3000L <= Util.getMillis()) {
                    iterator.remove();
                } else {
                    maxLength = Math.max(maxLength, mc.font.width(caption.getText()));
                }
            }

            maxLength = maxLength + mc.font.width("<") + mc.font.width(" ")
                    + mc.font.width(">") + mc.font.width(" ");

            int captionIndex = 0;
            for (SubtitleOverlay.Subtitle caption1 : this.subtitles) {
                Component captionText = caption1.getText();
                Vec3 soundDistanceToPlayer = caption1.getLocation().subtract(playerPosVec).normalize();
                double d0 = -vec3d3.dot(soundDistanceToPlayer);
                double d1 = -vec3d1.dot(soundDistanceToPlayer);
                boolean flag = d1 > 0.5D;
                int halfMaxLength = maxLength / 2;
                int subtitleWidth = mc.font.width(captionText);
                int l1 = Mth.floor(Mth.clampedLerp(255.0D, 75.0D,
                        (double) ((float) (Util.getMillis() - caption1.getTime()) / 3000.0F)));
                int textColor = l1 << 16 | l1 << 8 | l1;
                poseStack.pushPose();

                /*
                This is where the change happens
                 */
                OverlayPosition position = MovableSubtitlesConfig.overlayPosition;
                float xTranslate, yTranslate;
                // Factoring some numbers out so it's easier to tell what they do
                // Also might be changed later when implementing more features.
                int verticalSpacing = 10;
                //TODO: Make this code smarter
                switch (position) {
                    case BOTTOM_RIGHT:
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() - (float) halfMaxLength * 1.0F - 2.0F;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() - 30) - (float) (captionIndex * verticalSpacing) * 1.0F;
                        break;
                    case BOTTOM_CENTER:
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() / 2;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() - 50) - (float) (captionIndex * verticalSpacing) * 1.0F;
                        break;
                    case BOTTOM_LEFT:
                        xTranslate = (float) halfMaxLength * 1.0F;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() - 30) - (float) (captionIndex * verticalSpacing) * 1.0F;
                        break;
                    case CENTER_LEFT:
                        xTranslate = (float) halfMaxLength * 1.0F;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() / 2) - (float) (captionIndex * verticalSpacing + 5) * 1.0F;
                        break;
                    case TOP_LEFT:
                        xTranslate = (float) halfMaxLength * 1.0F;
                        yTranslate = (float) (captionIndex * verticalSpacing + 5) * 1.0F;
                        break;
                    case TOP_CENTER:
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() / 2;
                        yTranslate = (float) (captionIndex * verticalSpacing + 5) * 1.0F;
                        break;
                    case TOP_RIGHT:
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() - (float) halfMaxLength * 1.0F;
                        yTranslate = (float) (captionIndex * verticalSpacing + 5) * 1.0F;
                        break;
                    case CENTER_RIGHT:
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() - (float) halfMaxLength * 1.0F;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() / 2) - (float) (captionIndex * verticalSpacing + 5) * 1.0F;
                        break;
                    default: //if there's any invalid input just show it in the bottom right
                        xTranslate = (float) mc.getWindow().getGuiScaledWidth() - (float) halfMaxLength * 1.0F - 2.0F;
                        yTranslate = (float) (mc.getWindow().getGuiScaledHeight() - 30) - (float) (captionIndex * verticalSpacing) * 1.0F;
                        break;
                }
                poseStack.translate(xTranslate, yTranslate, 0.0F);

                /*
                This stuff stays the same for now, but with new features, more change will happen
                 */
                poseStack.scale(1.0F, 1.0F, 1.0F);
                fill(poseStack,-halfMaxLength - 1, -5, halfMaxLength + 1,
                        5, mc.options.getBackgroundColor(0.8F));
                RenderSystem.enableBlend();
                if (!flag) {
                    if (d0 > 0.0D)
                        mc.font.draw(poseStack, ">", (float) (halfMaxLength - mc.font.width(">")),
                                -4.0F, textColor - 16777216);
                    else if (d0 < 0.0D)
                        mc.font.draw(poseStack, "<", (float) (-halfMaxLength), -4.0F, textColor - 16777216);
                }
                mc.font.draw(poseStack, captionText, (float) (-subtitleWidth / 2), -4.0F, textColor - 16777216);
                poseStack.popPose();
                captionIndex++;
            }
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void onPlaySound(SoundInstance soundIn, WeighedSoundEvents accessor) {
        if (accessor.getSubtitle() != null) {
            Component subtitleText = accessor.getSubtitle();
            if (!this.subtitles.isEmpty()) {
                for (SubtitleOverlay.Subtitle caption : this.subtitles) {
                    if (caption.getText().equals(subtitleText)) {
                        caption.refresh(new Vec3((double) soundIn.getX(), (double) soundIn.getY(), (double) soundIn.getZ()));
                        return;
                    }
                }
            }
            this.subtitles.add(new SubtitleOverlay.Subtitle(subtitleText,
                    new Vec3((double) soundIn.getX(), (double) soundIn.getY(), (double) soundIn.getZ())));
        }
    }
}
