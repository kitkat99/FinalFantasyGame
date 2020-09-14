package enemies;
import items.*;

public class Troll extends AbstractEnemy {

    public static final Weapon trollWeapon = new Weapon("trollWeapon", "Wooden mace",6,1,0, DamageType.BLUNT);

    public Troll(){
        super("enemies.Troll", 35, trollWeapon);
    };
    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
        if(DamageType.equals(DamageType.SLASHING))
            return WeaponDamage/3;
        else
            return WeaponDamage;
    };
}
