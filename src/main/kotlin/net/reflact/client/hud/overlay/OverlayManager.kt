package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.reflact.client.ReflactClient
import net.reflact.client.gui.HudEditorScreen

class OverlayManager private constructor() {

    val overlays: MutableList<ReflactOverlay> = ArrayList()

    fun register(overlay: ReflactOverlay) {
        overlays.add(overlay)
    }

    fun onHudRender(context: DrawContext, tickCounter: RenderTickCounter) {
        val client = MinecraftClient.getInstance()
        if (client.player == null || client.options.hudHidden || !ReflactClient.CONFIG.showHud())
            return

        // Don't render if we are in the editor screen to avoid duplication
        if (client.currentScreen is HudEditorScreen)
            return

        val tickDelta = tickCounter.getTickProgress(true)

        for (overlay in overlays) {
            if (overlay.isEnabled) {
                overlay.render(context, tickDelta)
            }
        }
    }

    fun saveConfig() {
        ReflactClient.CONFIG.save()
    }

    companion object {
        val INSTANCE = OverlayManager()
    }
}
