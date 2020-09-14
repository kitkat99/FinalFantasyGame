package enemies;

import items.*;

public abstract class AbstractEnemy {
    private String enemyName;
    private int hitPoints;
    private Weapon Weapon;

    public AbstractEnemy(String enemyName, int hitPoints, Weapon Weapon ){
        this.enemyName = enemyName;
        this.hitPoints = hitPoints;
        this.Weapon = Weapon;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Weapon getWeapon() {
        return Weapon;
    }

    public abstract int calculateDamageTaken(DamageType DamageType, int WeaponDamage);

    @Override
    public String toString() {
        return ("enemies.Enemy: "+this.getEnemyName() +System.lineSeparator());
    }


}
