package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.util.List;

public class OrcWarlord extends AbstractEnemy {
    public static final AbstractWeapon orcWarlordWeapon = new EnemyWeapon("orcWarlordWeapon", "Feeble sword",new Dice(0,0, 12)) ;

    public OrcWarlord(){

        super(List.of(3,4,5),"OrcWarlord", 50, orcWarlordWeapon, 120, 7 );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };
}
