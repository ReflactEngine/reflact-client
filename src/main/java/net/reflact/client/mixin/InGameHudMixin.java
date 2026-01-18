package net.reflact.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    // Note: Method signatures depend on specific Yarn mappings for 1.21.
    // This is a best-guess attempt. If it fails at runtime/compile, it needs adjustment.
    
    // Cancelling Health
    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void onRenderHealth(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        ci.cancel();
    }

    // Cancelling Food (Hunger)
    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void onRenderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        ci.cancel();
    }

    // Cancelling Vanilla Status Effects
    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusEffectOverlay(net.minecraft.client.gui.DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter, CallbackInfo ci) {
        if (net.reflact.client.ReflactClient.CONFIG.showStatusEffectHud()) {
            ci.cancel();
        }
    }

    // Cancelling Vanilla Experience Bar
    // @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    // private void onRenderExperienceBar(net.minecraft.client.gui.DrawContext context, int x, CallbackInfo ci) {
    //    if (net.reflact.client.ReflactClient.CONFIG.showHud()) {
    //        ci.cancel();
    //    }
    // }

    // Cancelling Vanilla Hotbar
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void onRenderHotbar(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter, CallbackInfo ci) {
        if (net.reflact.client.ReflactClient.CONFIG.showHotbar()) {
            ci.cancel();
        }
    }
}
