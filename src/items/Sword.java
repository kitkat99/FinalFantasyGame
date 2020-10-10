package items;

import player.SlotType;

import java.util.List;

public class Sword extends AbstractWeapon {
    private final String itemName = "Sword";
    private List<ItemEffect> itemEffectsList = List.of(new ItemEffect(EffectType.HP_BOOST, getHpBoost()), new ItemEffect(EffectType.STRENGTH_BOOST, getStrengthBoost()));

    public Sword(String weaponDescription, int amountFirstEffect, int amountSecondEffect) {
        super(weaponDescription, amountFirstEffect, amountSecondEffect);
    }

    @Override
    public int hitDamageWeapon() {
        return getStrengthBoost();
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
