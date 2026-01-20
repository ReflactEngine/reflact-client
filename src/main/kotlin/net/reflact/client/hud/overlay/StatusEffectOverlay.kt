package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.StringHelper
import net.reflact.client.ReflactClient

class StatusEffectOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return
        val x = x
        val y = y
        var effectOffset = 0
        val effects = client.player!!.statusEffects
        if (effects.isNotEmpty()) {
            for (effect in effects) {
                val name = effect.effectType.value().name
                val duration = StringHelper.formatTicks(effect.duration, client.world!!.tickManager.tickRate)
                val amplifier = if (effect.amplifier > 0) " " + (effect.amplifier + 1) else ""
                val text = name.string + amplifier + " (" + duration + ")"
                val color = if (effect.effectType.value().isBeneficial) 0xFF55FF55.toInt() else 0xFFFF5555.toInt()
                context.drawText(client.textRenderer, text, x, y + effectOffset, color, true)
                effectOffset += 10
            }
        }
    }

    override var x: Int
        get() = ReflactClient.CONFIG.statusEffectHudX()
        set(value) { ReflactClient.CONFIG.statusEffectHudX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.statusEffectHudY()
        set(value) { ReflactClient.CONFIG.statusEffectHudY(value) }
    override var width: Int get() = 100; set(value) {}
    override var height: Int get() = 100; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showStatusEffectHud()
    override val name: String get() = "Status Effects"
}
