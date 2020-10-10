package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.awt.*;
import java.util.List;

public class Dragon extends AbstractEnemy {
    public static final AbstractWeapon dragonWeapon = new EnemyWeapon("dragonWeapon", "Feeble sword",new Dice(8,6, 10)) ;

    public Dragon(){

        super(List.of(1,2,3,4,5,6),"Dragon", 1500, dragonWeapon, 0, 10 ,new Color(22, 18, 250));
    }

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    }

}
