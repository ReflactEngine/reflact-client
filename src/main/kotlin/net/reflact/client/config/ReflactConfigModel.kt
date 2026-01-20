package net.reflact.client.config

import io.wispforest.owo.config.annotation.*
import io.wispforest.owo.ui.core.Color

@Modmenu(modId = "reflact", uiModelId = "reflact:config")
@Config(name = "reflact-client", wrapperName = "ReflactConfig")
class ReflactConfigModel {

    @SectionHeader("general")
    @JvmField
    var showHud = true
    @JvmField
    var showInfoHud = true

    @SectionHeader("bars")
    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var healthBarX = 300

    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var healthBarY = 500

    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var manaBarX = 500

    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var manaBarY = 500

    @JvmField
    var healthColor = Color.ofRgb(0xFFFF0000.toInt())
    @JvmField
    var manaColor = Color.ofRgb(0xFF0000FF.toInt())

    @SectionHeader("info_hud")
    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var infoHudX = 5

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var infoHudY = 5

    @JvmField
    var infoHudColor = Color.WHITE

    @SectionHeader("armor_hud")
    @JvmField
    var showArmorHud = true

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var armorHudX = 5

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var armorHudY = 50

    @SectionHeader("effects_hud")
    @JvmField
    var showStatusEffectHud = true

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var statusEffectHudX = 5

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var statusEffectHudY = 100

    @SectionHeader("target_hud")
    @JvmField
    var showTargetHud = true

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var targetHudX = 300 // Default somewhat to the right of center

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var targetHudY = 5

    @SectionHeader("spell_hud")
    @JvmField
    var showSpellHud = true

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var spellHudX = 5

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var spellHudY = 150

    @SectionHeader("radar")
    @JvmField
    var showEntityRadar = true

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var radarX = 5

    @RangeConstraint(min = 0.0, max = 2000.0)
    @JvmField
    var radarY = 180

    @RangeConstraint(min = 50.0, max = 200.0)
    @JvmField
    var radarSize = 80

    @SectionHeader("hotbar")
    @JvmField
    var showHotbar = true
    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var hotbarX = 0 // Default 0 means auto-center if handled by overlay
    @RangeConstraint(min = 0.0, max = 5000.0)
    @JvmField
    var hotbarY = 0 // Default 0 means auto-bottom

    @SectionHeader("spells")
    @JvmField
    var spellSlot1 = "fireball"
    @JvmField
    var spellSlot2 = "heal"
    @JvmField
    var spellSlot3 = ""
    @JvmField
    var spellSlot4 = ""
    @JvmField
    var spellSlot5 = ""
    @JvmField
    var spellSlot6 = ""
    @JvmField
    var spellSlot7 = ""
    @JvmField
    var spellSlot8 = ""
    @JvmField
    var spellSlot9 = ""
    @JvmField
    var spellSlot10 = ""
}
