package net.reflact.client.render

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter

object DamageIndicatorRenderer {

    private var lastHitTime: Long = 0
    private var lastDamage: Double = 0.0
    private var lastCrit: Boolean = false

    fun init() {
        HudRenderCallback.EVENT.register { context, tickCounter ->
            render(context, tickCounter)
        }
    }

    fun add(entityId: Int, damage: Double, crit: Boolean) {
        lastHitTime = System.currentTimeMillis()
        lastDamage = damage
        lastCrit = crit

        val client = MinecraftClient.getInstance()
        if (client.player != null) {
            client.player!!.playSound(
                net.minecraft.sound.SoundEvents.ENTITY_ARROW_HIT_PLAYER,
                0.5f,
                1.0f + if (crit) 0.2f else 0.0f
            )
        }
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        val time = System.currentTimeMillis()
        val elapsed = time - lastHitTime

        if (elapsed > 1000) return // Show for 1 second

        val client = MinecraftClient.getInstance()
        val width = client.window.scaledWidth
        val height = client.window.scaledHeight

        val cx = width / 2
        val cy = height / 2

        var alpha = 1.0f - (elapsed / 1000.0f)
        if (alpha < 0) alpha = 0f

        val color = if (lastCrit) 0xFF0000 else 0xFFFFFF // Red for crit, White for normal
        val alphaInt = (alpha * 255).toInt()
        val argb = (alphaInt shl 24) or color

        val text = (if (lastCrit) "CRIT " else "") + lastDamage.toInt()
        val tw = client.textRenderer.getWidth(text)

        // Float up animation (2D)
        val yOffset = (elapsed / 10.0).toInt()

        context.drawTextWithShadow(client.textRenderer, text, cx - tw / 2, cy - 10 - yOffset, argb)
    }
}
