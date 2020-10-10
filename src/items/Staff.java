package items;

import player.SlotType;

import java.util.List;

public class Staff extends AbstractWeapon {
    private final String itemName = "Staff";
    private List<ItemEffect> itemEffectsList = List.of(new ItemEffect(EffectType.HP_BOOST, getHpBoost()), new ItemEffect(EffectType.MANA_BOOST, getMpBoost()), new ItemEffect(EffectType.INTELLECT_BOOST, getIntellectBoost()));

    public Staff(String weaponDescription, int hpBoost, int mpBoost, int intellectBoost) {
        super (weaponDescription, hpBoost, mpBoost, intellectBoost);
    }

    @Override
    public int hitDamageWeapon() {
        return getIntellectBoost();
    }

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
