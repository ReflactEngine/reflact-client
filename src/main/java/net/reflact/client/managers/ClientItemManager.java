package net.reflact.client.managers;

import net.reflact.common.item.CustomItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientItemManager {
    private static final Map<UUID, CustomItem> itemCache = new HashMap<>();

    public static void cacheItem(CustomItem item) {
        if (item.getUuid() != null) {
            itemCache.put(item.getUuid(), item);
        }
    }

    public static CustomItem getItem(UUID uuid) {
        return itemCache.get(uuid);
    }
}
