package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.reflact.client.ClientData
import net.reflact.client.ReflactClient

class HealthBarOverlay : ReflactOverlay {

    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return
        ClientData.currentHealth = client.player!!.health.toDouble()
        ClientData.maxHealth = client.player!!.maxHealth.toDouble()

        val x = x
        val y = y
        val width = width
        val height = height

        val hpPercent = (ClientData.currentHealth / ClientData.maxHealth).toFloat()

        // Draw Polished Bar
        // 1. Background Texture
        context.drawTexturedQuad(BG_TEXTURE, x, x + width, y, y + height, 0f, 1f, 0f, 1f)

        // 2. Progress Texture
        val progressWidth = (width * hpPercent).toInt()
        if (progressWidth > 0) {
            context.drawTexturedQuad(PROGRESS_TEXTURE, x, x + progressWidth, y, y + height, 0f, hpPercent, 0f, 1f)
        }

        // 3. Text (Shadowed, Centered)
        val text = "HP " + ClientData.currentHealth.toInt() + "/" + ClientData.maxHealth.toInt()
        val textWidth = client.textRenderer.getWidth(text)
        context.drawText(
            client.textRenderer,
            text,
            x + (width - textWidth) / 2,
            y + (height - 8) / 2,
            0xFFFFFF,
            true
        )
    }

    override var x: Int
        get() = ReflactClient.CONFIG.healthBarX()
        set(value) { ReflactClient.CONFIG.healthBarX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.healthBarY()
        set(value) { ReflactClient.CONFIG.healthBarY(value) }
    override var width: Int = 100
    override var height: Int = 10

    override val isEnabled: Boolean get() = true
    override val name: String get() = "Health Bar"

    companion object {
        private val BG_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/health_bg.png")
        private val PROGRESS_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/health.png")
    }
}
