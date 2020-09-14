package player;

import items.*;

import java.util.HashSet;
import java.util.Set;

public class Slot {
    private SlotType slotType;
    private int capacity;
    private Set<Item> listOfItems = new HashSet<>();

    public Set<Item> getListOfItems() {
        return listOfItems;
    }

    public int getCapacity() {
        return capacity;
    }

    public Slot(SlotType slotType, int capacity) {
        this.slotType = slotType;
        this.capacity = capacity;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public boolean equip(Item item) throws IllegalArgumentException {
        try {
            if (!(item instanceof Equippable))
                throw new IllegalArgumentException("The items.Item is not items.Equippable");
            else if ((((Equippable) item).getSlotType() != slotType))
                throw new IllegalArgumentException("player.Slot doesn't fit");
            else {
                if (capacity == 0) {
                    System.out.println("Max capacity reached for this slot");
                    return false;
                } else {
                    if (doesDuplicateExist(item))
                        throw new IllegalArgumentException("items.Item already exists");
                    else {
                        listOfItems.add(item);
                        capacity--;
                        return true;
                    }

                }
            }
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
            return false;
        }

    }

    public boolean remove(Item item) throws IllegalArgumentException {
        try {
            capacity++;
            return listOfItems.remove(item);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean doesDuplicateExist(Item item) {
        boolean isDuplicate = false;
        Item[] listofItems = listOfItems.toArray(new Item[listOfItems.size()]);
        for (int i = 1; i < listofItems.length; i++) {
            if (listofItems[i].getItemName() == item.getItemName()) {
                isDuplicate = true;
            }
        }
        return isDuplicate;

    }
}
