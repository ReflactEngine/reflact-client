package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.EquipmentSlot
import net.reflact.client.ReflactClient

class ArmorHudOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        if (client.player == null) return
        val x = x
        val y = y
        var offset = 0
        for (slot in EquipmentSlot.entries) {
            if (slot.type != EquipmentSlot.Type.HUMANOID_ARMOR) continue
            val stack = client.player!!.getEquippedStack(slot)
            if (stack.isEmpty) continue
            context.drawItem(stack, x, y - offset)
            context.drawStackOverlay(client.textRenderer, stack, x, y - offset)
            offset += 20
        }
    }

    override var x: Int
        get() = ReflactClient.CONFIG.armorHudX()
        set(value) { ReflactClient.CONFIG.armorHudX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.armorHudY()
        set(value) { ReflactClient.CONFIG.armorHudY(value) }
    override var width: Int get() = 16; set(value) {}
    override var height: Int get() = 80; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showArmorHud()
    override val name: String get() = "Armor HUD"
}
