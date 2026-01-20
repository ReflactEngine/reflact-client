package net.reflact.client.hud.overlay

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.reflact.client.ClientData
import net.reflact.client.ReflactClient
import net.reflact.client.input.KeyInputHandler

class SpellHudOverlay : ReflactOverlay {
    override fun render(context: DrawContext, tickDelta: Float) {
        val client = MinecraftClient.getInstance()
        val x = x
        val y = y
        val spellSize = 20
        val spellGap = 5

        var renderedCount = 0
        for (i in 0 until 10) {
            val spell = getSpell(i + 1)
            if (spell.isEmpty()) continue

            val key = KeyInputHandler.getSpellKey(i)
            var keyText = "?"
            if (key != null) {
                keyText = key.boundKeyLocalizedText.string.uppercase()
                // Simplistic cleanup for display
                if (keyText.startsWith("KEY.KEYBOARD.")) keyText = keyText.replace("KEY.KEYBOARD.", "")
                if (keyText.length > 2) keyText = keyText.substring(0, 1)
            }

            val renderX = x + (renderedCount * (spellSize + spellGap))
            renderSpellIcon(context, client.textRenderer, spell, keyText, renderX, y, spellSize)
            renderedCount++
        }
    }

    private fun getSpell(slot: Int): String {
        return when (slot) {
            1 -> ReflactClient.CONFIG.spellSlot1()
            2 -> ReflactClient.CONFIG.spellSlot2()
            3 -> ReflactClient.CONFIG.spellSlot3()
            4 -> ReflactClient.CONFIG.spellSlot4()
            5 -> ReflactClient.CONFIG.spellSlot5()
            6 -> ReflactClient.CONFIG.spellSlot6()
            7 -> ReflactClient.CONFIG.spellSlot7()
            8 -> ReflactClient.CONFIG.spellSlot8()
            9 -> ReflactClient.CONFIG.spellSlot9()
            10 -> ReflactClient.CONFIG.spellSlot10()
            else -> ""
        }
    }

    private fun renderSpellIcon(
        context: DrawContext,
        textRenderer: TextRenderer,
        spellName: String,
        keyText: String,
        x: Int,
        y: Int,
        size: Int
    ) {
        val lastCast = ClientData.spellLastCast.getOrDefault(spellName, 0L)
        val cooldown = ClientData.spellCooldowns.getOrDefault(spellName, 0L)
        val timePassed = System.currentTimeMillis() - lastCast
        val progress = timePassed.toFloat() / cooldown
        val onCooldown = timePassed < cooldown

        context.fill(x, y, x + size, y + size, 0x80000000.toInt())
        val borderColor = if (onCooldown) 0xFFFF0000.toInt() else 0xFFFFFFFF.toInt()

        context.fill(x - 1, y - 1, x + size + 1, y, borderColor)
        context.fill(x - 1, y + size, x + size + 1, y + size + 1, borderColor)
        context.fill(x - 1, y, x, y + size, borderColor)
        context.fill(x + size, y, x + size + 1, y + size, borderColor)

        val iconLetter = spellName.substring(0, 1).uppercase()
        context.drawText(textRenderer, iconLetter, x + 7, y + 6, 0xFFCCCCCC.toInt(), true)

        if (onCooldown) {
            val cooldownHeight = (size * (1.0f - progress)).toInt()
            context.fill(x, y + size - cooldownHeight, x + size, y + size, 0x80FFFFFF.toInt())
            val secondsLeft = String.format("%.1f", (cooldown - timePassed) / 1000.0f)
            context.drawText(textRenderer, secondsLeft, x + 2, y + 6, 0xFFFF5555.toInt(), true)
        } else {
            context.drawText(textRenderer, keyText, x + size - 6, y + size - 6, 0xFFFFFF00.toInt(), true)
        }
    }

    override var x: Int
        get() = ReflactClient.CONFIG.spellHudX()
        set(value) { ReflactClient.CONFIG.spellHudX(value) }
    override var y: Int
        get() = ReflactClient.CONFIG.spellHudY()
        set(value) { ReflactClient.CONFIG.spellHudY(value) }
    override var width: Int get() = 45; set(value) {}
    override var height: Int get() = 20; set(value) {}
    override val isEnabled: Boolean get() = ReflactClient.CONFIG.showSpellHud()
    override val name: String get() = "Spells"
}
