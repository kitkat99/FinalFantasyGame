package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.util.List;
//5-6 Wyrm 80 2d6+10 200 5


public class Wyrm extends AbstractEnemy {
    public static final AbstractWeapon wyrmWeapon = new EnemyWeapon("wyrmWeapon", "Feeble sword",new Dice(2,6, 10)) ;

    public Wyrm(){

        super(List.of(5,6),"Wyrm", 80, wyrmWeapon, 200, 5 );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };
}
