package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.util.List;

public class Vampire extends AbstractEnemy {
    public static final AbstractWeapon vampireWeapon = new EnemyWeapon("vampireWeapon", "Feeble sword",new Dice(3,6, 10)) ;

    public Vampire(){

        super(List.of(5,6),"Vampire", 50, vampireWeapon, 400, 10 );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };

}
