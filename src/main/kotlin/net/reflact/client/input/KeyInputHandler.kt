package net.reflact.client.input

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import net.reflact.client.ClientData
import net.reflact.client.ReflactClient
import net.reflact.client.network.ClientNetworkManager
import net.reflact.common.network.packet.CastSpellPacket
import org.lwjgl.glfw.GLFW

object KeyInputHandler {

    private val spellKeys = ArrayList<KeyBinding>()
    private lateinit var mapKey: KeyBinding
    private lateinit var menuKey: KeyBinding
    private lateinit var toggleInfoKey: KeyBinding
    private lateinit var hudEditorKey: KeyBinding
    private lateinit var questLogKey: KeyBinding
    private lateinit var guildKey: KeyBinding
    private lateinit var partyKey: KeyBinding
    private lateinit var reportKey: KeyBinding

    fun register() {
        // Register 10 spell keys
        for (i in 0 until 10) {
            var code = GLFW.GLFW_KEY_UNKNOWN
            if (i == 0) code = GLFW.GLFW_KEY_R
            if (i == 1) code = GLFW.GLFW_KEY_G

            val key = KeyBindingHelper.registerKeyBinding(
                KeyBinding(
                    "key.reflact.spell_${i + 1}",
                    InputUtil.Type.KEYSYM,
                    code,
                    KeyBinding.Category.MISC
                )
            )
            spellKeys.add(key)
        }

        mapKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.map",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                KeyBinding.Category.MISC
            )
        )

        menuKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                KeyBinding.Category.MISC
            )
        )

        guildKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.guild",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                KeyBinding.Category.MISC
            )
        )

        partyKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.party",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                KeyBinding.Category.MISC
            )
        )

        reportKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.report",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                KeyBinding.Category.MISC
            )
        )

        toggleInfoKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.toggle_info",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KeyBinding.Category.MISC
            )
        )

        hudEditorKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.hud_editor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeyBinding.Category.MISC
            )
        )

        questLogKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.reflact.quest_log",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                KeyBinding.Category.MISC
            )
        )

        registerEvents()
    }

    fun getSpellKey(index: Int): KeyBinding? {
        return if (index >= 0 && index < spellKeys.size) {
            spellKeys[index]
        } else null
    }

    private fun registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            if (client.player == null) return@EndTick

            while (hudEditorKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.HudEditorScreen())
            }

            while (questLogKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.QuestScreen())
            }

            while (mapKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.WorldMapScreen())
            }

            while (menuKey.wasPressed()) {
                client.setScreen(
                    io.wispforest.owo.config.ui.ConfigScreen.create(
                        ReflactClient.CONFIG,
                        client.currentScreen
                    )
                )
            }

            while (guildKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.GuildScreen())
            }

            while (partyKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.PartyScreen())
            }

            while (reportKey.wasPressed()) {
                client.setScreen(net.reflact.client.gui.ReportScreen())
            }

            while (toggleInfoKey.wasPressed()) {
                val current = ReflactClient.CONFIG.showInfoHud()
                ReflactClient.CONFIG.showInfoHud(!current)
                net.reflact.client.ui.NotificationManager.add(
                    net.minecraft.text.Text.of("Info HUD: " + if (!current) "ON" else "OFF"),
                    0xFF00FF00.toInt()
                )
            }

            for (i in spellKeys.indices) {
                while (spellKeys[i].wasPressed()) {
                    castSpell(client, i + 1)
                }
            }
        })
    }

    private fun castSpell(client: MinecraftClient, slot: Int) {
        // Check for required item (Stick or Blaze Rod) to match server side listener requirements
        val mainHand = client.player!!.mainHandStack
        val hasWand = mainHand.isOf(net.minecraft.item.Items.STICK) || mainHand.isOf(net.minecraft.item.Items.BLAZE_ROD)

        if (!hasWand) {
            client.player!!.sendMessage(net.minecraft.text.Text.of("§c[Reflact] You need a Stick or Blaze Rod to cast spells!"), true)
            // For now we proceed so server can decide, but user asked about sticks.
            return
        }

        val spell = when (slot) {
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

        if (spell.isEmpty()) return

        ReflactClient.LOGGER.info("Key pressed for slot {}: {}", slot, spell)

        val lastCast = ClientData.spellLastCast.getOrDefault(spell, 0L)
        val cooldown = ClientData.spellCooldowns.getOrDefault(spell, 0L)

        if (System.currentTimeMillis() - lastCast >= cooldown) {
            ClientNetworkManager.sendPacket(CastSpellPacket(spell))
            ClientData.spellLastCast[spell] = System.currentTimeMillis()
            client.player!!.sendMessage(net.minecraft.text.Text.of("§7[Reflact] Casting $spell..."), true)

            if (client.targetedEntity != null) {
                net.reflact.client.particle.DamageIndicatorManager.add(
                    client.targetedEntity!!.x,
                    client.targetedEntity!!.y + client.targetedEntity!!.height,
                    client.targetedEntity!!.z,
                    15, true
                )
            }
        } else {
            client.player!!.sendMessage(net.minecraft.text.Text.of("§c[Reflact] $spell is on cooldown!"), true)
        }
    }
}
