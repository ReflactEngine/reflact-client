package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.reflact.client.ReflactClient

class InfoHudOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return
        val x = x
        val y = y
        val infoColor = ReflactClient.CONFIG.infoHudColor().rgb()
        val coordsText = String.format("XYZ: %.1f / %.1f / %.1f", client.player!!.x, client.player!!.y, client.player!!.z)
        val biomeText = "Biome: " + client.world!!.getBiome(client.player!!.blockPos).key.map { key -> key.value.toString() }.orElse("Unknown")
        context.drawText(client.textRenderer, coordsText, x, y, infoColor, true)
        context.drawText(client.textRenderer, biomeText, x, y + 10, infoColor, true)
    }

    override var x: Int
        get() = ReflactClient.CONFIG.infoHudX()
        set(value) { ReflactClient.CONFIG.infoHudX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.infoHudY()
        set(value) { ReflactClient.CONFIG.infoHudY(value) }
    override var width: Int get() = 150; set(value) {}
    override var height: Int get() = 20; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showInfoHud()
    override val name: String get() = "Info HUD"
}
