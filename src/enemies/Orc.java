package enemies;
import items.*;
public class Orc extends AbstractEnemy {

    public static final Weapon orcWeapon = new Weapon("orcWeapon", "Feeble mace",3,2,0, DamageType.BLUNT);

    public Orc(){
        super("enemies.Orc", 20, orcWeapon);
    };
    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
        if(DamageType.equals(DamageType.BLUNT))
            return WeaponDamage/2;
        else
            return WeaponDamage;
    };

}
