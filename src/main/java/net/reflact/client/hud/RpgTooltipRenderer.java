package net.reflact.client.hud;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.reflact.client.managers.ClientItemManager;
import net.reflact.common.item.CustomItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RpgTooltipRenderer implements ItemTooltipCallback {
    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext context, net.minecraft.item.tooltip.TooltipType type, List<Text> lines) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return;
        
        // customData.copyNbt() returns NbtCompound
        NbtCompound nbt = customData.copyNbt();
        
        if (!nbt.contains("reflact_uuid")) return;
        
        // Handling Optional<String> return type
        Object rawUuid = nbt.getString("reflact_uuid");
        String uuidStr;
        if (rawUuid instanceof java.util.Optional) {
             uuidStr = ((java.util.Optional<String>) rawUuid).orElse("");
        } else {
             uuidStr = (String) rawUuid;
        }

        if (uuidStr == null || uuidStr.isEmpty()) return;
        
        UUID uuid;
        try {
            uuid = UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            return;
        }
        
        CustomItem rpgItem = ClientItemManager.getItem(uuid);
        if (rpgItem == null) return;
        
        // RENDER RPG TOOLTIP
        lines.clear(); // Clear default tooltip? Or keep name?
        // Let's keep the name but re-style it
        
        // Name with Tier Color
        lines.add(Text.literal(rpgItem.getName()).styled(style -> style.withColor(rpgItem.getTier().getColor())));
        
        // Tier & Type
        lines.add(Text.literal(rpgItem.getTier().getDisplayName() + " " + rpgItem.getType().name())
                .styled(style -> style.withColor(rpgItem.getTier().getColor()).withItalic(false)));
        
        lines.add(Text.empty()); // Spacer
        
        // Stats
        for (Map.Entry<String, Double> entry : rpgItem.getAttributes().entrySet()) {
            String attrName = formatAttrName(entry.getKey());
            double value = entry.getValue();
            if (value != 0) {
                Formatting color = value > 0 ? Formatting.GREEN : Formatting.RED;
                String sign = value > 0 ? "+" : "";
                lines.add(Text.literal(sign + value + " " + attrName).formatted(color));
            }
        }
        
        if (!rpgItem.getAttributes().isEmpty()) {
            lines.add(Text.empty());
        }
        
        // Requirements (using metadata)
        if (rpgItem.getMeta("level_req") != null) {
            lines.add(Text.literal("Level Min: " + rpgItem.getMeta("level_req")).formatted(Formatting.GRAY));
        }
        if (rpgItem.getMeta("class_req") != null) {
            lines.add(Text.literal("Class Req: " + rpgItem.getMeta("class_req")).formatted(Formatting.GRAY));
        }
        
        lines.add(Text.empty());

        // Lore
        if (rpgItem.getLore() != null) {
            for (String loreLine : rpgItem.getLore()) {
                lines.add(Text.literal(loreLine).formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
            }
        }
    }
    
    private String formatAttrName(String key) {
        String[] parts = key.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }
}
