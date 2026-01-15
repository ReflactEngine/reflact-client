package net.reflact.client.managers;

import net.reflact.client.item.RpgItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientItemManager {
    private static final Map<UUID, RpgItem> itemCache = new HashMap<>();

    public static void cacheItem(RpgItem item) {
        if (item.getUuid() != null) {
            itemCache.put(item.getUuid(), item);
        }
    }

    public static RpgItem getItem(UUID uuid) {
        return itemCache.get(uuid);
    }
}
