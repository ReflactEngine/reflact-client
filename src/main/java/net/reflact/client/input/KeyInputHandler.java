package net.reflact.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.reflact.client.network.ClientNetworkManager;
import net.reflact.common.network.packet.CastSpellPacket;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    private static KeyBinding spell1Key;
    private static KeyBinding spell2Key;
    private static KeyBinding menuKey;
    private static KeyBinding toggleInfoKey;
    private static KeyBinding hudEditorKey;

    public static void register() {
        spell1Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.spell_1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                new KeyBinding.Category(Identifier.of("reflact", "bindings"))
        ));

        spell2Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.spell_2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                new KeyBinding.Category(Identifier.of("reflact", "bindings"))
        ));

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reflact.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
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

        registerEvents();
    }

    private static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (hudEditorKey.wasPressed()) {
                client.setScreen(new net.reflact.client.gui.HudEditorScreen());
            }

            while (spell1Key.wasPressed()) {
                String spell = "fireball";
                long lastCast = net.reflact.client.ClientData.spellLastCast.getOrDefault(spell, 0L);
                long cooldown = net.reflact.client.ClientData.spellCooldowns.getOrDefault(spell, 0L);
                
                if (System.currentTimeMillis() - lastCast >= cooldown) {
                    ClientNetworkManager.sendPacket(new CastSpellPacket(spell));
                    net.reflact.client.ClientData.spellLastCast.put(spell, System.currentTimeMillis());
                    client.player.sendMessage(net.minecraft.text.Text.of("§7[Reflact] Casting Fireball..."), true);
                    
                    // Simulate Damage Indicator
                    if (client.targetedEntity != null) {
                        net.reflact.client.particle.DamageIndicatorManager.add(
                            client.targetedEntity.getX(), 
                            client.targetedEntity.getY() + client.targetedEntity.getHeight(), 
                            client.targetedEntity.getZ(), 
                            15, true
                        );
                    }
                } else {
                     client.player.sendMessage(net.minecraft.text.Text.of("§c[Reflact] Fireball is on cooldown!"), true);
                }
            }

            while (spell2Key.wasPressed()) {
                String spell = "heal";
                long lastCast = net.reflact.client.ClientData.spellLastCast.getOrDefault(spell, 0L);
                long cooldown = net.reflact.client.ClientData.spellCooldowns.getOrDefault(spell, 0L);
                
                if (System.currentTimeMillis() - lastCast >= cooldown) {
                    ClientNetworkManager.sendPacket(new CastSpellPacket(spell));
                    net.reflact.client.ClientData.spellLastCast.put(spell, System.currentTimeMillis());
                    client.player.sendMessage(net.minecraft.text.Text.of("§7[Reflact] Casting Heal..."), true);
                } else {
                    client.player.sendMessage(net.minecraft.text.Text.of("§c[Reflact] Heal is on cooldown!"), true);
                }
            }
            
            while (menuKey.wasPressed()) {
                client.setScreen(io.wispforest.owo.config.ui.ConfigScreen.create(net.reflact.client.ReflactClient.CONFIG, client.currentScreen));
            }

            while (toggleInfoKey.wasPressed()) {
                boolean current = net.reflact.client.ReflactClient.CONFIG.showInfoHud();
                net.reflact.client.ReflactClient.CONFIG.showInfoHud(!current);
                // client.player.sendMessage(net.minecraft.text.Text.of("§7[Reflact] Info HUD: " + (!current ? "ON" : "OFF")), true);
                net.reflact.client.ui.NotificationManager.add(net.minecraft.text.Text.of("Info HUD: " + (!current ? "ON" : "OFF")), 0xFF00FF00);
            }
        });
    }
}