package enemies;


import dice.Dice;
import items.*;

import java.util.List;

public class Goblin extends AbstractEnemy {

   public static final AbstractWeapon goblinWeapon = new EnemyWeapon("goblinWeapon", "Feeble sword",new Dice(0,0, 5), DamageType.SLASHING) ;

    public Goblin(){

        super(List.of(1,2,3),"Goblin", 15, goblinWeapon, 50, 7 );
    };

    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
            return WeaponDamage;
    };
}
