package net.reflact.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.reflact.client.ClientData;
import net.reflact.client.ReflactClient;
import net.reflact.client.network.ClientNetworkManager;
import net.reflact.common.network.packet.CastSpellPacket;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyInputHandler {

    private static final List<KeyBinding> spellKeys = new ArrayList<>();
    private static KeyBinding mapKey; // New Map Key
    private static KeyBinding menuKey;
    private static KeyBinding toggleInfoKey;
    private static KeyBinding hudEditorKey;
    private static KeyBinding questLogKey;

    public static void register() {
        // Register 10 spell keys
        for (int i = 0; i < 10; i++) {
            int code = GLFW.GLFW_KEY_UNKNOWN;
            if (i == 0) code = GLFW.GLFW_KEY_R;
            if (i == 1) code = GLFW.GLFW_KEY_G;
            
            KeyBinding key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.spell_" + (i + 1),
                InputUtil.Type.KEYSYM,
                code,
                new KeyBinding.Category(Identifier.of("reflact", "bindings"))
            ));
            spellKeys.add(key);
        }

        mapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.map",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));

        toggleInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.toggle_info",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));

        hudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.hud_editor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));
        
        questLogKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.quest_log",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                new KeyBinding.Category(Identifier.of("reflact", "general"))
        ));

        registerEvents();
    }
    
    public static KeyBinding getSpellKey(int index) {
        if (index >= 0 && index < spellKeys.size()) {
            return spellKeys.get(index);
        }
        return null;
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (hudEditorKey.wasPressed()) {
                client.setScreen(new net.reflact.client.gui.HudEditorScreen());
            }
            
            while (questLogKey.wasPressed()) {
                client.setScreen(new net.reflact.client.gui.QuestScreen());
            }
            
            while (mapKey.wasPressed()) {
                client.setScreen(new net.reflact.client.gui.WorldMapScreen());
            }
            
            while (menuKey.wasPressed()) {
                client.setScreen(io.wispforest.owo.config.ui.ConfigScreen.create(ReflactClient.CONFIG, client.currentScreen));
            }

            while (toggleInfoKey.wasPressed()) {
                boolean current = ReflactClient.CONFIG.showInfoHud();
                ReflactClient.CONFIG.showInfoHud(!current);
                net.reflact.client.ui.NotificationManager.add(net.minecraft.text.Text.of("Info HUD: " + (!current ? "ON" : "OFF")), 0xFF00FF00);
            }

            for (int i = 0; i < spellKeys.size(); i++) {
                while (spellKeys.get(i).wasPressed()) {
                    castSpell(client, i + 1);
                }
            }
        });
    }

    private static void castSpell(MinecraftClient client, int slot) {
        // Check for required item (Stick or Blaze Rod) to match server side listener requirements
        // This is a temporary client-side check to give feedback, though server enforces it too.
        net.minecraft.item.ItemStack mainHand = client.player.getMainHandStack();
        boolean hasWand = mainHand.isOf(net.minecraft.item.Items.STICK) || mainHand.isOf(net.minecraft.item.Items.BLAZE_ROD);
        
        if (!hasWand) {
             // Optional: Uncomment to warn user
             // client.player.sendMessage(net.minecraft.text.Text.of("§c[Reflact] You need a Stick or Blaze Rod to cast spells!"), true);
             // For now we proceed so server can decide, but user asked about sticks.
        }

        String spell = switch (slot) {
            case 1 -> ReflactClient.CONFIG.spellSlot1();
            case 2 -> ReflactClient.CONFIG.spellSlot2();
            case 3 -> ReflactClient.CONFIG.spellSlot3();
            case 4 -> ReflactClient.CONFIG.spellSlot4();
            case 5 -> ReflactClient.CONFIG.spellSlot5();
            case 6 -> ReflactClient.CONFIG.spellSlot6();
            case 7 -> ReflactClient.CONFIG.spellSlot7();
            case 8 -> ReflactClient.CONFIG.spellSlot8();
            case 9 -> ReflactClient.CONFIG.spellSlot9();
            case 10 -> ReflactClient.CONFIG.spellSlot10();
            default -> "";
        };
        
        if (spell.isEmpty()) return;
        
        ReflactClient.LOGGER.info("Key pressed for slot {}: {}", slot, spell);

        long lastCast = ClientData.spellLastCast.getOrDefault(spell, 0L);
        long cooldown = ClientData.spellCooldowns.getOrDefault(spell, 0L);
        
        if (System.currentTimeMillis() - lastCast >= cooldown) {
            ClientNetworkManager.sendPacket(new CastSpellPacket(spell));
            ClientData.spellLastCast.put(spell, System.currentTimeMillis());
            client.player.sendMessage(net.minecraft.text.Text.of("§7[Reflact] Casting " + spell + "..."), true);
            
            if (client.targetedEntity != null) {
                net.reflact.client.particle.DamageIndicatorManager.add(
                    client.targetedEntity.getX(), 
                    client.targetedEntity.getY() + client.targetedEntity.getHeight(), 
                    client.targetedEntity.getZ(), 
                    15, true
                );
            }
        } else {
             client.player.sendMessage(net.minecraft.text.Text.of("§c[Reflact] " + spell + " is on cooldown!"), true);
        }
    }
}
