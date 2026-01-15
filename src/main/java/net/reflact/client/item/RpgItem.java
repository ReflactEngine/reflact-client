package net.reflact.client.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RpgItem {
    private String id;
    private String displayName;
    private List<String> lore;
    private ItemType type;
    private ItemTier tier;
    private int levelRequirement;
    private String classRequirement;
    private Map<String, Double> attributes = new HashMap<>();
    private UUID uuid;

    // Getters
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public List<String> getLore() { return lore; }
    public ItemType getType() { return type; }
    public ItemTier getTier() { return tier; }
    public int getLevelRequirement() { return levelRequirement; }
    public String getClassRequirement() { return classRequirement; }
    public Map<String, Double> getAttributes() { return attributes; }
    public double getAttribute(String id) { return attributes.getOrDefault(id, 0.0); }
    public UUID getUuid() { return uuid; }
}
