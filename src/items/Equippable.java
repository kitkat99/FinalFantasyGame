package items;

import player.SlotType;

public interface Equippable extends Item {
    public abstract SlotType getSlotType();

}
