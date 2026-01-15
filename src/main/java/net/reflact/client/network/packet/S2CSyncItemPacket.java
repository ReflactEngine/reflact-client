package net.reflact.client.network.packet;

import net.reflact.client.item.RpgItem;

public class S2CSyncItemPacket {
    private RpgItem item;
    private int slotId;

    public RpgItem getItem() { return item; }
    public int getSlotId() { return slotId; }
}
