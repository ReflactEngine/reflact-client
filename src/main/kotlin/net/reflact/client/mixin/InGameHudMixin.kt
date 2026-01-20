package net.reflact.client.mixin

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(InGameHud::class)
class InGameHudMixin {

    // Cancelling Health
    @Inject(method = ["renderHealthBar"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun onRenderHealth(
        context: DrawContext,
        player: PlayerEntity,
        x: Int,
        y: Int,
        lines: Int,
        regeneratingHeartIndex: Int,
        maxHealth: Float,
        lastHealth: Int,
        health: Int,
        absorption: Int,
        blinking: Boolean,
        ci: CallbackInfo
    ) {
        ci.cancel()
    }

    // Cancelling Food (Hunger)
    @Inject(method = ["renderFood"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun onRenderFood(
        context: DrawContext,
        player: PlayerEntity,
        top: Int,
        right: Int,
        ci: CallbackInfo
    ) {
        ci.cancel()
    }

    // Cancelling Vanilla Status Effects
    @Inject(method = ["renderStatusEffectOverlay"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun onRenderStatusEffectOverlay(
        context: DrawContext,
        tickCounter: RenderTickCounter,
        ci: CallbackInfo
    ) {
        if (net.reflact.client.ReflactClient.CONFIG.showStatusEffectHud()) {
            ci.cancel()
        }
    }

    // Cancelling Vanilla Hotbar
    @Inject(method = ["renderHotbar"], at = [At("HEAD")], cancellable = true, remap = false)
    private fun onRenderHotbar(
        context: DrawContext,
        tickCounter: RenderTickCounter,
        ci: CallbackInfo
    ) {
        if (net.reflact.client.ReflactClient.CONFIG.showHotbar()) {
            ci.cancel()
        }
    }
}
