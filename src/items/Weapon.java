package items;

import dice.*;

public abstract class Weapon implements Equippable{
    public String weaponName;
    public String weaponDescription;
    Dice RollDiceWeapon ;
    public DamageType DamageTypeWeapon;

    public Weapon(String weaponName, String weaponDescription, int numberDicesEdges, int numberOfDices, int diceBonus, DamageType DamageTypeWeapon){
        RollDiceWeapon = new Dice();
        RollDiceWeapon.numberDicesEdges = numberDicesEdges;
        RollDiceWeapon.numberOfDices = numberOfDices;
        RollDiceWeapon.diceBonus = diceBonus;
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
        return RollDiceWeapon.numberDicesEdges;
    }
    public int getRollDiceWeaponNum() {
        return RollDiceWeapon.numberOfDices;
    }

    public int getRollDiceWeaponBonus() {
        return RollDiceWeapon.diceBonus;
    }
    public int hitDamageWeapon(){
        RollDiceWeapon.roll();
        return RollDiceWeapon.result;
    }
}
