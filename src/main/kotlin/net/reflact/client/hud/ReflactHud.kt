package net.reflact.client.hud

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.reflact.client.hud.overlay.*
import net.reflact.client.ui.NotificationManager

object ReflactHud {

    fun initialize() {
        OverlayManager.INSTANCE.register(HealthBarOverlay())
        OverlayManager.INSTANCE.register(ManaBarOverlay())
        OverlayManager.INSTANCE.register(HotbarOverlay())
        OverlayManager.INSTANCE.register(InfoHudOverlay())
        OverlayManager.INSTANCE.register(ArmorHudOverlay())
        OverlayManager.INSTANCE.register(StatusEffectOverlay())
        OverlayManager.INSTANCE.register(TargetHudOverlay())
        OverlayManager.INSTANCE.register(SpellHudOverlay())
        OverlayManager.INSTANCE.register(MinimapOverlay())

        // Notifications
        OverlayManager.INSTANCE.register(object : ReflactOverlay {
            override fun render(context: DrawContext, tickDelta: Float) {
                val client = MinecraftClient.getInstance()
                val width = client.window.scaledWidth
                val height = client.window.scaledHeight
                NotificationManager.render(context, client.textRenderer, width, height)
            }

            override var x: Int
                get() = 0
                set(value) {}
            override var y: Int
                get() = 0
                set(value) {}
            override var width: Int get() = 0; set(value) {}
            override var height: Int get() = 0; set(value) {}
            override val isEnabled: Boolean get() = true
            override val name: String get() = "Notifications"
        })

        HudRenderCallback.EVENT.register(OverlayManager.INSTANCE::onHudRender)
    }
}
