package enemies;


import items.*;

public class Goblin extends AbstractEnemy {

    public static final items.Weapon goblinWeapon = new Weapon("goblinWeapon", "Feeble sword",4,1,0, DamageType.SLASHING);

    public Goblin(){
        super("enemies.Goblin", 10,  goblinWeapon );
    };
    public int calculateDamageTaken(DamageType DamageType, int WeaponDamage){
            return WeaponDamage;
    };
}
