package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.reflact.client.ClientData
import net.reflact.client.ReflactClient

class ManaBarOverlay : ReflactOverlay {

    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        val x = x
        val y = y
        val width = width
        val height = height

        val manaPercent = (ClientData.currentMana / ClientData.maxMana).toFloat()

        // Draw Polished Bar
        // 1. Background Texture
        context.drawTexturedQuad(BG_TEXTURE, x, x + width, y, y + height, 0f, 1f, 0f, 1f)

        // 2. Progress Texture
        val progressWidth = (width * manaPercent).toInt()
        if (progressWidth > 0) {
            context.drawTexturedQuad(PROGRESS_TEXTURE, x, x + progressWidth, y, y + height, 0f, manaPercent, 0f, 1f)
        }

        // 3. Text (Shadowed, Centered)
        val text = "MP " + ClientData.currentMana.toInt() + "/" + ClientData.maxMana.toInt()
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
        get() = ReflactClient.CONFIG.manaBarX()
        set(value) { ReflactClient.CONFIG.manaBarX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.manaBarY()
        set(value) { ReflactClient.CONFIG.manaBarY(value) }
    override var width: Int = 100
    override var height: Int = 10

    override val isEnabled: Boolean get() = true
    override val name: String get() = "Mana Bar"

    companion object {
        private val BG_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/mana_bg.png")
        private val PROGRESS_TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/mana.png")
    }
}
