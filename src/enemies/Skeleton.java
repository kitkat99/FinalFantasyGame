package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.DamageType;
import items.EnemyWeapon;

import java.util.List;

public class Skeleton extends AbstractEnemy {
    public static final AbstractWeapon skeletonWeapon = new EnemyWeapon("skeletonWeapon", "Feeble sword",new Dice(4,6, 10), DamageType.SLASHING) ;

    public Skeleton(){

        super(List.of(3,4,5),"Skeleton", 20, skeletonWeapon, 100, 4 );
    };

    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
        return WeaponDamage;
    };
}
