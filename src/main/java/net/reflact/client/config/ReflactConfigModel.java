package net.reflact.client.config;

import io.wispforest.owo.config.annotation.*;
import io.wispforest.owo.ui.core.Color;

@Modmenu(modId = "reflact", uiModelId = "reflact:config")
@Config(name = "reflact-client", wrapperName = "ReflactConfig")
public class ReflactConfigModel {

    @SectionHeader("general")
    public boolean showHud = true;
    public boolean showInfoHud = true;

    @SectionHeader("bars")
    @RangeConstraint(min = 0, max = 5000)
    public int healthBarX = 300;
    
    @RangeConstraint(min = 0, max = 5000)
    public int healthBarY = 500;

    @RangeConstraint(min = 0, max = 5000)
    public int manaBarX = 500;

    @RangeConstraint(min = 0, max = 5000)
    public int manaBarY = 500;

    public Color healthColor = Color.ofRgb(0xFFFF0000);
    public Color manaColor = Color.ofRgb(0xFF0000FF);

    @SectionHeader("info_hud")
    @RangeConstraint(min = 0, max = 2000)
    public int infoHudX = 5;
    
    @RangeConstraint(min = 0, max = 2000)
    public int infoHudY = 5;
    
    public Color infoHudColor = Color.WHITE;

    @SectionHeader("armor_hud")
    public boolean showArmorHud = true;

    @RangeConstraint(min = 0, max = 2000)
    public int armorHudX = 5;

    @RangeConstraint(min = 0, max = 2000)
    public int armorHudY = 50;

    @SectionHeader("effects_hud")
    public boolean showStatusEffectHud = true;

    @RangeConstraint(min = 0, max = 2000)
    public int statusEffectHudX = 5;

    @RangeConstraint(min = 0, max = 2000)
    public int statusEffectHudY = 100;

    @SectionHeader("target_hud")
    public boolean showTargetHud = true;

    @RangeConstraint(min = 0, max = 2000)
    public int targetHudX = 300; // Default somewhat to the right of center

    @RangeConstraint(min = 0, max = 2000)
    public int targetHudY = 5;

    @SectionHeader("spell_hud")
    public boolean showSpellHud = true;

    @RangeConstraint(min = 0, max = 2000)
    public int spellHudX = 5;

    @RangeConstraint(min = 0, max = 2000)
    public int spellHudY = 150;

    @SectionHeader("radar")
    public boolean showEntityRadar = true;

    @RangeConstraint(min = 0, max = 2000)
    public int radarX = 5;

    @RangeConstraint(min = 0, max = 2000)
    public int radarY = 180;

    @RangeConstraint(min = 50, max = 200)
    public int radarSize = 80;

    @SectionHeader("hotbar")
    public boolean showHotbar = true;
    @RangeConstraint(min = 0, max = 5000)
    public int hotbarX = 0; // Default 0 means auto-center if handled by overlay
    @RangeConstraint(min = 0, max = 5000)
    public int hotbarY = 0; // Default 0 means auto-bottom

    @SectionHeader("spells")
    public String spellSlot1 = "fireball";
    public String spellSlot2 = "heal";
    public String spellSlot3 = "";
    public String spellSlot4 = "";
    public String spellSlot5 = "";
    public String spellSlot6 = "";
    public String spellSlot7 = "";
    public String spellSlot8 = "";
    public String spellSlot9 = "";
    public String spellSlot10 = "";
}
