package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.util.List;

public class GiantRat extends AbstractEnemy {
    public static final AbstractWeapon giantRatWeapon = new EnemyWeapon("giantRatWeapon", "Feeble sword",new Dice(0,0, 2)) ;

    public GiantRat(){

        super(List.of(1,2),"GiantRat", 5, giantRatWeapon, 30, 4 );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };
}
