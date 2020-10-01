package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.awt.*;
import java.util.List;

public class GraySlime extends AbstractEnemy {
    public static final AbstractWeapon graySlimeWeapon = new EnemyWeapon("graySlimeWeapon", "Feeble sword",new Dice(0,0, 8)) ;

    public GraySlime(){

        super(List.of(2,3),"GraySlime", 30, graySlimeWeapon, 80, 2, new Color(250, 111, 103) );
    };

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    };
}
