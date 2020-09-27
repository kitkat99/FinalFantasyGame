package enemies;
import dice.Dice;
import items.*;

import java.util.List;

public class OrcGrunt extends AbstractEnemy {

    public static final AbstractWeapon orcWeapon = new EnemyWeapon("orcWeapon", "Feeble mace",new Dice(0,0, 5));

    public OrcGrunt(){
        super(List.of(3,4,5),"OrcGrunt", 40, orcWeapon, 100,6);
    };
    public int calculateDamageTaken( int WeaponDamage){
            return WeaponDamage;
    };

}
