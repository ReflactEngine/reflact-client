package net.reflact.client.managers

import net.reflact.common.item.CustomItem
import java.util.HashMap
import java.util.UUID

object ClientItemManager {
    private val itemCache: MutableMap<UUID, CustomItem> = HashMap()

    fun cacheItem(item: CustomItem) {
        if (item.uuid != null) {
            itemCache[item.uuid!!] = item
        }
    }

    fun getItem(uuid: UUID): CustomItem? {
        return itemCache[uuid]
    }
}
