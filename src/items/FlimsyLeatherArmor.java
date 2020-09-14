package items;

import player.SlotType;

import java.util.Collections;
import java.util.List;

public class FlimsyLeatherArmor implements Equippable {
    private final String itemName = "Flimsy Leather Armor";
    private List<ItemEffect> itemEffectsList = Collections.EMPTY_LIST;

    public String getItemName() {
        return itemName;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.CHEST;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return this.itemEffectsList;
    }

}
