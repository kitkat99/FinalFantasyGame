package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.util.List;

public class Ettin extends AbstractEnemy {
    public static final AbstractWeapon ettinWeapon = new EnemyWeapon("ettinWeapon", "Feeble sword",new Dice(0,0, 20)) ;

    public Ettin(){

        super(List.of(4,5,6),"Ettin", 60, ettinWeapon, 150, 9 );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };
}
