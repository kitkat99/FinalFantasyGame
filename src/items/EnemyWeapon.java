package items;

import dice.Dice;
import player.SlotType;

import java.util.List;

public class EnemyWeapon extends AbstractWeapon {
    private final String itemName = "Enemy Weapon";
    private List<ItemEffect> itemEffectsList = List.of(new ItemEffect(EffectType.NONE, 0));;

    public EnemyWeapon(String weaponName, String weaponDescription, Dice dice ) {
        super(weaponName, weaponDescription, dice );
    }

    @Override
    public int hitDamageWeapon() {

        return  RollDiceWeapon.roll();
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
