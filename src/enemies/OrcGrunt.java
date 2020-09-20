package enemies;
import dice.Dice;
import items.*;

import java.util.List;

public class OrcGrunt extends AbstractEnemy {

    public static final AbstractWeapon orcWeapon = new EnemyWeapon("orcWeapon", "Feeble mace",new Dice(0,0, 5), DamageType.BLUNT);

    public OrcGrunt(){
        super(List.of(3,4,5),"OrcGrunt", 40, orcWeapon, 100,6);
    };
    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
        if(DamageType.equals(DamageType.BLUNT))
            return WeaponDamage/2;
        else
            return WeaponDamage;
    };

}
