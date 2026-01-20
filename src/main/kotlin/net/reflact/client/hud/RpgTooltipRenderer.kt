package net.reflact.client.hud

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.reflact.client.managers.ClientItemManager
import java.util.UUID
import java.util.Optional
import java.util.Locale

class RpgTooltipRenderer : ItemTooltipCallback {
    override fun getTooltip(stack: ItemStack, context: Item.TooltipContext, type: TooltipType, lines: MutableList<Text>) {
        val customData = stack.get(DataComponentTypes.CUSTOM_DATA) ?: return

        // customData.copyNbt() returns NbtCompound
        val nbt = customData.copyNbt()

        if (!nbt.contains("reflact_uuid")) return

        // Handling Optional<String> return type logic from Java port
        // NbtCompound.getString returns String directly in recent versions, or empty string if missing.
        // But error suggests it returns Optional<String>.
        val uuidStr = nbt.getString("reflact_uuid").orElse("")

        if (uuidStr.isEmpty()) return

        val uuid: UUID
        try {
            uuid = UUID.fromString(uuidStr)
        } catch (e: IllegalArgumentException) {
            return
        }

        val rpgItem = ClientItemManager.getItem(uuid) ?: return

        // RENDER RPG TOOLTIP
        lines.clear() // Clear default tooltip? Or keep name?
        // Let's keep the name but re-style it

        // Name with Tier Color
        lines.add(Text.literal(rpgItem.name).styled { style -> style.withColor(rpgItem.tier.color) })

        // Tier & Type
        lines.add(
            Text.literal(rpgItem.tier.displayName + " " + rpgItem.type.name)
                .styled { style -> style.withColor(rpgItem.tier.color).withItalic(false) }
        )

        lines.add(Text.empty()) // Spacer

        // Stats
        for ((key, value) in rpgItem.attributes) {
            val attrName = formatAttrName(key)
            if (value != 0.0) {
                val color = if (value > 0) Formatting.GREEN else Formatting.RED
                val sign = if (value > 0) "+" else ""
                lines.add(Text.literal("$sign$value $attrName").formatted(color))
            }
        }

        if (rpgItem.attributes.isNotEmpty()) {
            lines.add(Text.empty())
        }

        // Requirements (using metadata)
        if (rpgItem.getMeta("level_req") != null) {
            lines.add(Text.literal("Level Min: " + rpgItem.getMeta("level_req")).formatted(Formatting.GRAY))
        }
        if (rpgItem.getMeta("class_req") != null) {
            lines.add(Text.literal("Class Req: " + rpgItem.getMeta("class_req")).formatted(Formatting.GRAY))
        }

        lines.add(Text.empty())

        // Lore
        if (rpgItem.lore != null) {
            for (loreLine in rpgItem.lore!!) {
                lines.add(Text.literal(loreLine).formatted(Formatting.DARK_PURPLE, Formatting.ITALIC))
            }
        }
    }

    private fun formatAttrName(key: String): String {
        val parts = key.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        for (part in parts) {
            if (sb.isNotEmpty()) sb.append(" ")
            sb.append(part.substring(0, 1).uppercase(Locale.getDefault())).append(part.substring(1))
        }
        return sb.toString()
    }
}
