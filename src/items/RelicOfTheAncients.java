package items;

import dice.Dice;
import player.SlotType;

import java.util.List;

public class RelicOfTheAncients  extends AbstractWeapon {
    private final String itemName = "Relic Of The Ancients";
    private List<ItemEffect> itemEffectsList = List.of(new ItemEffect(EffectType.HP_BOOST, 80), new ItemEffect(EffectType.DAMAGE_BOOST, 42));;

    public RelicOfTheAncients(String weaponName, String weaponDescription,  Dice dice, DamageType DamageTypeWeapon) {
        super(weaponName, weaponDescription, dice, DamageTypeWeapon);
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.NECK;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return this.itemEffectsList;
    }

}
