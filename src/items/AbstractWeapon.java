package items;

import dice.*;
import player.SlotType;

import java.util.List;

public abstract class AbstractWeapon implements Equippable{
    public String weaponName;
    public String weaponDescription;
    Dice RollDiceWeapon ;
    protected int hpBoost;
    protected int strengthBoost;
    protected int mpBoost;
    protected int intellectBoost;

    public AbstractWeapon ( String weaponDescription, int hpBoost, int strengthBoost){
        this.weaponDescription = weaponDescription;
        this.hpBoost = hpBoost;
        this.strengthBoost = strengthBoost;
    };
    public AbstractWeapon( String weaponDescription, int hpBoost, int mpBoost, int intellectBoost){
        this.weaponDescription = weaponDescription;
        this.hpBoost = hpBoost;
        this.mpBoost = mpBoost;
        this.intellectBoost = intellectBoost;
    };
    public AbstractWeapon(String weaponName, String weaponDescription, Dice rollDiceWeapon ){
        RollDiceWeapon = rollDiceWeapon;
        this.weaponName = weaponName;
        this.weaponDescription = weaponDescription;
    };

    public int getHpBoost() {
        return hpBoost;
    }

    public int getStrengthBoost() {
        return strengthBoost;
    }

    public int getMpBoost() {
        return mpBoost;
    }

    public int getIntellectBoost() {
        return intellectBoost;
    }

    public String getWeaponName() {
        return weaponName;
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
    public abstract int hitDamageWeapon();


    @Override
    public abstract SlotType getSlotType();

    @Override
    public abstract List<ItemEffect> getItemEffects();

    @Override
    public abstract String getItemName() ;

    public String toString(){
        return getWeaponDescription() +" "+ getItemName() +" "+ getItemEffects().toString();
    }
}
