package enemies;

import dice.Dice;
import items.AbstractWeapon;
import items.EnemyWeapon;

import java.awt.*;
import java.util.List;

public class Ettin extends AbstractEnemy {
    public static final AbstractWeapon ettinWeapon = new EnemyWeapon("ettinWeapon", "Feeble sword",new Dice(0,0, 20)) ;

    public Ettin(){

        super(List.of(4,5,6),"Ettin", 60, ettinWeapon, 150, 9 ,new Color(21, 129, 250));
    }

    public int calculateDamageTaken( int WeaponDamage){
        return WeaponDamage;
    }
}
