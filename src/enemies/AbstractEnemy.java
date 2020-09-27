package enemies;

import Entity.Entity;
import items.*;

import java.util.List;

public abstract class AbstractEnemy implements Entity {
    private String enemyName;
    private int hitPoints;
    private AbstractWeapon Weapon;
    private int enemyXP;
    private int visibilityRadius;
    private List<Integer> levelAppearance;

    public AbstractEnemy(List<Integer> levelAppearance, String enemyName, int hitPoints, AbstractWeapon Weapon, int enemyXP , int visibilityRadius){
        this.enemyName = enemyName;
        this.hitPoints = hitPoints;
        this.Weapon = Weapon;
        this.enemyXP = enemyXP;
        this.visibilityRadius = visibilityRadius;
        this.levelAppearance=levelAppearance;
    }

    public List<Integer> getLevelAppearance() {
        return levelAppearance;
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

    public AbstractWeapon getWeapon() {
        return Weapon;
    }

    public abstract int calculateDamageTaken( int WeaponDamage);

    @Override
    public String toString() {
        return ("enemies.Enemy: "+this.getEnemyName() +System.lineSeparator());
    }
    public int hit(){
        return getWeapon().hitDamageWeapon();
    }

}
