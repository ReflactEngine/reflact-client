package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.LivingEntity
import net.reflact.client.ReflactClient

class TargetHudOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        val target = client.targetedEntity
        if (target is LivingEntity) {
            val x = x
            val y = y
            context.drawText(client.textRenderer, target.displayName, x, y, 0xFFFFFFFF.toInt(), true)
            val health = target.health
            val maxHealth = target.maxHealth
            val percent = health / maxHealth
            val targetBarWidth = 100
            val targetBarHeight = 8
            val targetBarY = y + 12

            context.fill(x, targetBarY, x + targetBarWidth, targetBarY + targetBarHeight, 0xFF000000.toInt())
            val fill = (targetBarWidth * percent).toInt()
            if (fill > 0) context.fill(x, targetBarY, x + fill, targetBarY + targetBarHeight, 0xFFFF0000.toInt())

            val targetHpText = String.format("%.0f/%.0f", health, maxHealth)
            context.drawText(client.textRenderer, targetHpText, x + 2, targetBarY, 0xFFFFFFFF.toInt(), true)
        }
    }

    override var x: Int
        get() = ReflactClient.CONFIG.targetHudX()
        set(value) { ReflactClient.CONFIG.targetHudX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.targetHudY()
        set(value) { ReflactClient.CONFIG.targetHudY(value) }
    override var width: Int get() = 100; set(value) {}
    override var height: Int get() = 30; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showTargetHud()
    override val name: String get() = "Target HUD"
}
