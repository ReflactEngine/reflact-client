package net.reflact.client;

import java.util.HashMap;
import java.util.Map;

public class ClientData {
    public static double maxMana = 100.0;
    public static double currentMana = 100.0;
    public static double maxHealth = 20.0;
    public static double currentHealth = 20.0;

    public static final Map<String, Long> spellCooldowns = new HashMap<>();
    public static final Map<String, Long> spellLastCast = new HashMap<>();

    static {
        spellCooldowns.put("fireball", 5000L); // 5 seconds
        spellCooldowns.put("heal", 12000L); // 12 seconds
    }
}
