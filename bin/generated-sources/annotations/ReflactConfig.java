package net.reflact.client.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.ConfigWrapper.BuilderConsumer;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ReflactConfig extends ConfigWrapper<net.reflact.client.config.ReflactConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Boolean> showHud = this.optionForKey(this.keys.showHud);
    private final Option<java.lang.Boolean> showInfoHud = this.optionForKey(this.keys.showInfoHud);
    private final Option<java.lang.Integer> healthBarX = this.optionForKey(this.keys.healthBarX);
    private final Option<java.lang.Integer> healthBarY = this.optionForKey(this.keys.healthBarY);
    private final Option<java.lang.Integer> manaBarX = this.optionForKey(this.keys.manaBarX);
    private final Option<java.lang.Integer> manaBarY = this.optionForKey(this.keys.manaBarY);
    private final Option<io.wispforest.owo.ui.core.Color> healthColor = this.optionForKey(this.keys.healthColor);
    private final Option<io.wispforest.owo.ui.core.Color> manaColor = this.optionForKey(this.keys.manaColor);
    private final Option<java.lang.Integer> infoHudX = this.optionForKey(this.keys.infoHudX);
    private final Option<java.lang.Integer> infoHudY = this.optionForKey(this.keys.infoHudY);
    private final Option<io.wispforest.owo.ui.core.Color> infoHudColor = this.optionForKey(this.keys.infoHudColor);
    private final Option<java.lang.Boolean> showArmorHud = this.optionForKey(this.keys.showArmorHud);
    private final Option<java.lang.Integer> armorHudX = this.optionForKey(this.keys.armorHudX);
    private final Option<java.lang.Integer> armorHudY = this.optionForKey(this.keys.armorHudY);
    private final Option<java.lang.Boolean> showStatusEffectHud = this.optionForKey(this.keys.showStatusEffectHud);
    private final Option<java.lang.Integer> statusEffectHudX = this.optionForKey(this.keys.statusEffectHudX);
    private final Option<java.lang.Integer> statusEffectHudY = this.optionForKey(this.keys.statusEffectHudY);
    private final Option<java.lang.Boolean> showTargetHud = this.optionForKey(this.keys.showTargetHud);
    private final Option<java.lang.Integer> targetHudX = this.optionForKey(this.keys.targetHudX);
    private final Option<java.lang.Integer> targetHudY = this.optionForKey(this.keys.targetHudY);
    private final Option<java.lang.Boolean> showSpellHud = this.optionForKey(this.keys.showSpellHud);
    private final Option<java.lang.Integer> spellHudX = this.optionForKey(this.keys.spellHudX);
    private final Option<java.lang.Integer> spellHudY = this.optionForKey(this.keys.spellHudY);
    private final Option<java.lang.Boolean> showEntityRadar = this.optionForKey(this.keys.showEntityRadar);
    private final Option<java.lang.Integer> radarX = this.optionForKey(this.keys.radarX);
    private final Option<java.lang.Integer> radarY = this.optionForKey(this.keys.radarY);
    private final Option<java.lang.Integer> radarSize = this.optionForKey(this.keys.radarSize);

    private ReflactConfig() {
        super(net.reflact.client.config.ReflactConfigModel.class);
    }

    private ReflactConfig(BuilderConsumer consumer) {
        super(net.reflact.client.config.ReflactConfigModel.class, consumer);
    }

    public static ReflactConfig createAndLoad() {
        var wrapper = new ReflactConfig();
        wrapper.load();
        return wrapper;
    }

    public static ReflactConfig createAndLoad(BuilderConsumer consumer) {
        var wrapper = new ReflactConfig(consumer);
        wrapper.load();
        return wrapper;
    }

    public boolean showHud() {
        return showHud.value();
    }

    public void showHud(boolean value) {
        showHud.set(value);
    }

    public boolean showInfoHud() {
        return showInfoHud.value();
    }

    public void showInfoHud(boolean value) {
        showInfoHud.set(value);
    }

    public int healthBarX() {
        return healthBarX.value();
    }

    public void healthBarX(int value) {
        healthBarX.set(value);
    }

    public int healthBarY() {
        return healthBarY.value();
    }

    public void healthBarY(int value) {
        healthBarY.set(value);
    }

    public int manaBarX() {
        return manaBarX.value();
    }

    public void manaBarX(int value) {
        manaBarX.set(value);
    }

    public int manaBarY() {
        return manaBarY.value();
    }

    public void manaBarY(int value) {
        manaBarY.set(value);
    }

    public io.wispforest.owo.ui.core.Color healthColor() {
        return healthColor.value();
    }

    public void healthColor(io.wispforest.owo.ui.core.Color value) {
        healthColor.set(value);
    }

    public io.wispforest.owo.ui.core.Color manaColor() {
        return manaColor.value();
    }

    public void manaColor(io.wispforest.owo.ui.core.Color value) {
        manaColor.set(value);
    }

    public int infoHudX() {
        return infoHudX.value();
    }

    public void infoHudX(int value) {
        infoHudX.set(value);
    }

    public int infoHudY() {
        return infoHudY.value();
    }

    public void infoHudY(int value) {
        infoHudY.set(value);
    }

    public io.wispforest.owo.ui.core.Color infoHudColor() {
        return infoHudColor.value();
    }

    public void infoHudColor(io.wispforest.owo.ui.core.Color value) {
        infoHudColor.set(value);
    }

    public boolean showArmorHud() {
        return showArmorHud.value();
    }

    public void showArmorHud(boolean value) {
        showArmorHud.set(value);
    }

    public int armorHudX() {
        return armorHudX.value();
    }

    public void armorHudX(int value) {
        armorHudX.set(value);
    }

    public int armorHudY() {
        return armorHudY.value();
    }

    public void armorHudY(int value) {
        armorHudY.set(value);
    }

    public boolean showStatusEffectHud() {
        return showStatusEffectHud.value();
    }

    public void showStatusEffectHud(boolean value) {
        showStatusEffectHud.set(value);
    }

    public int statusEffectHudX() {
        return statusEffectHudX.value();
    }

    public void statusEffectHudX(int value) {
        statusEffectHudX.set(value);
    }

    public int statusEffectHudY() {
        return statusEffectHudY.value();
    }

    public void statusEffectHudY(int value) {
        statusEffectHudY.set(value);
    }

    public boolean showTargetHud() {
        return showTargetHud.value();
    }

    public void showTargetHud(boolean value) {
        showTargetHud.set(value);
    }

    public int targetHudX() {
        return targetHudX.value();
    }

    public void targetHudX(int value) {
        targetHudX.set(value);
    }

    public int targetHudY() {
        return targetHudY.value();
    }

    public void targetHudY(int value) {
        targetHudY.set(value);
    }

    public boolean showSpellHud() {
        return showSpellHud.value();
    }

    public void showSpellHud(boolean value) {
        showSpellHud.set(value);
    }

    public int spellHudX() {
        return spellHudX.value();
    }

    public void spellHudX(int value) {
        spellHudX.set(value);
    }

    public int spellHudY() {
        return spellHudY.value();
    }

    public void spellHudY(int value) {
        spellHudY.set(value);
    }

    public boolean showEntityRadar() {
        return showEntityRadar.value();
    }

    public void showEntityRadar(boolean value) {
        showEntityRadar.set(value);
    }

    public int radarX() {
        return radarX.value();
    }

    public void radarX(int value) {
        radarX.set(value);
    }

    public int radarY() {
        return radarY.value();
    }

    public void radarY(int value) {
        radarY.set(value);
    }

    public int radarSize() {
        return radarSize.value();
    }

    public void radarSize(int value) {
        radarSize.set(value);
    }


    public static class Keys {
        public final Option.Key showHud = new Option.Key("showHud");
        public final Option.Key showInfoHud = new Option.Key("showInfoHud");
        public final Option.Key healthBarX = new Option.Key("healthBarX");
        public final Option.Key healthBarY = new Option.Key("healthBarY");
        public final Option.Key manaBarX = new Option.Key("manaBarX");
        public final Option.Key manaBarY = new Option.Key("manaBarY");
        public final Option.Key healthColor = new Option.Key("healthColor");
        public final Option.Key manaColor = new Option.Key("manaColor");
        public final Option.Key infoHudX = new Option.Key("infoHudX");
        public final Option.Key infoHudY = new Option.Key("infoHudY");
        public final Option.Key infoHudColor = new Option.Key("infoHudColor");
        public final Option.Key showArmorHud = new Option.Key("showArmorHud");
        public final Option.Key armorHudX = new Option.Key("armorHudX");
        public final Option.Key armorHudY = new Option.Key("armorHudY");
        public final Option.Key showStatusEffectHud = new Option.Key("showStatusEffectHud");
        public final Option.Key statusEffectHudX = new Option.Key("statusEffectHudX");
        public final Option.Key statusEffectHudY = new Option.Key("statusEffectHudY");
        public final Option.Key showTargetHud = new Option.Key("showTargetHud");
        public final Option.Key targetHudX = new Option.Key("targetHudX");
        public final Option.Key targetHudY = new Option.Key("targetHudY");
        public final Option.Key showSpellHud = new Option.Key("showSpellHud");
        public final Option.Key spellHudX = new Option.Key("spellHudX");
        public final Option.Key spellHudY = new Option.Key("spellHudY");
        public final Option.Key showEntityRadar = new Option.Key("showEntityRadar");
        public final Option.Key radarX = new Option.Key("radarX");
        public final Option.Key radarY = new Option.Key("radarY");
        public final Option.Key radarSize = new Option.Key("radarSize");
    }
}

