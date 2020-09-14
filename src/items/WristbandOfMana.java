package items;

import player.SlotType;

import java.util.List;

public class WristbandOfMana implements Equippable {
    private final String itemName = "Wristband Of Mana";
    private List<ItemEffect> itemEffectsList = List.of(new ItemEffect(EffectType.MANA_BOOST, 5));

    public String getItemName() {
        return itemName;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.HAND;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return this.itemEffectsList;
    }
}
