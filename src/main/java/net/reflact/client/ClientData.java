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

    public static class Waypoint {
        public String name;
        public int x, y, z;
        public int color;
        
        public Waypoint(String name, int x, int y, int z, int color) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
        }
    }
    
    public static final java.util.List<Waypoint> waypoints = new java.util.ArrayList<>();

    static {
        spellCooldowns.put("fireball", 5000L); // 5 seconds
        spellCooldowns.put("heal", 12000L); // 12 seconds
        
        waypoints.add(new Waypoint("Spawn", 0, 100, 0, 0xFF00FF00));
    }
}
