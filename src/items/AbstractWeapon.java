package items;

import dice.*;
import player.SlotType;

import java.util.List;

public abstract class AbstractWeapon implements Equippable{
    public String weaponName;
    public String weaponDescription;
    Dice RollDiceWeapon ;
    public DamageType DamageTypeWeapon;

    public AbstractWeapon(String weaponName, String weaponDescription, Dice rollDiceWeapon, DamageType DamageTypeWeapon){
        RollDiceWeapon = rollDiceWeapon;
        this.weaponName = weaponName;
        this.weaponDescription = weaponDescription;
        this.DamageTypeWeapon = DamageTypeWeapon;
    };

    public String getWeaponName() {
        return weaponName;
    }
    public DamageType getDamageTypeWeapon() {
        return DamageTypeWeapon;
    }

    public String getWeaponDescription() {
        return weaponDescription;
    }
    public int getRollDiceWeaponEdges() {
        return RollDiceWeapon.getNumberDicesEdges();
    }
    public int getRollDiceWeaponNum() {
        return RollDiceWeapon.getNumberOfDices();
    }

    public int getRollDiceWeaponBonus() {
        return RollDiceWeapon.getDiceBonus();
    }
    public int hitDamageWeapon(){
        RollDiceWeapon.roll();
        return RollDiceWeapon.getResult();
    }


    @Override
    public abstract SlotType getSlotType();

    @Override
    public abstract List<ItemEffect> getItemEffects();

    @Override
    public abstract String getItemName() ;
}
