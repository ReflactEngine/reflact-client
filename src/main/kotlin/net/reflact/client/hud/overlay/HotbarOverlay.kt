package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import net.reflact.client.ReflactClient

class HotbarOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        val player = client.player ?: return

        val x = x
        val y = y
        val width = width
        val height = height

        // Draw Background (Polished: Translucent Black with Border)
        // Main background
        context.fill(x, y, x + width, y + height, 0xAA000000.toInt())
        // Top/Bottom borders
        context.fill(x, y, x + width, y + 1, 0xFF555555.toInt())
        context.fill(x, y + height - 1, x + width, y + height, 0xFF555555.toInt())
        // Left/Right borders
        context.fill(x, y, x + 1, y + height, 0xFF555555.toInt())
        context.fill(x + width - 1, y, x + width, y + height, 0xFF555555.toInt())

        // Draw Slots
        for (i in 0 until 9) {
            val stack = player.inventory.getStack(i)
            val itemX = x + 3 + i * 20
            val itemY = y + 3

            // Slot background (slightly lighter)
            context.fill(itemX - 1, itemY - 1, itemX + 17, itemY + 17, 0x44FFFFFF)

            context.drawItem(stack, itemX, itemY)
            context.drawStackOverlay(client.textRenderer, stack, itemX, itemY)

            // Draw selection
            if (player.inventory.selectedSlot == i) {
                // Selection Border (White, thicker)
                context.fill(itemX - 2, itemY - 2, itemX + 18, itemY - 1, 0xFFFFFFFF.toInt()) // Top
                context.fill(itemX - 2, itemY + 17, itemX + 18, itemY + 18, 0xFFFFFFFF.toInt()) // Bottom
                context.fill(itemX - 2, itemY - 1, itemX - 1, itemY + 17, 0xFFFFFFFF.toInt()) // Left
                context.fill(itemX + 17, itemY - 1, itemX + 18, itemY + 17, 0xFFFFFFFF.toInt()) // Right
            }
        }
    }

    override var x: Int
        get() {
            val cfg = ReflactClient.CONFIG.hotbarX()
            if (cfg == 0) {
                val client = MinecraftClient.getInstance()
                return client.window.scaledWidth / 2 - 91
            }
            return cfg
        }
        set(value) { ReflactClient.CONFIG.hotbarX(value) }

    override var y: Int
        get() {
            val cfg = ReflactClient.CONFIG.hotbarY()
            if (cfg == 0) {
                val client = MinecraftClient.getInstance()
                return client.window.scaledHeight - 22
            }
            return cfg
        }
        set(value) { ReflactClient.CONFIG.hotbarY(value) }

    override var width: Int get() = 182; set(value) {}
    override var height: Int get() = 22; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showHotbar()
    override val name: String get() = "Hotbar"

    companion object {
        private val TEXTURE = Identifier.of("reflact", "textures/gui/sprites/hud/hotbar.png")
    }
}
