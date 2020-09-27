package player;

import items.EffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warrior extends AbstractPlayer {
    public Warrior(String name) {
        super(0, 0, 0, 0);
        setPlayerName(name);
    }

    @Override
    public LevelBonus levelBonusMap(int level) {
        Map<Integer, LevelBonus> bonusMap = Map.of(
                1, new LevelBonus(10, 0, 30, 0),
                2, new LevelBonus(20, 0, 60, 0),
                3, new LevelBonus(25, 0, 80, 0),
                4, new LevelBonus(30, 0, 90, 0),
                5, new LevelBonus(35, 0, 100, 0),
                6, new LevelBonus(45, 0, 140, 0));

        return bonusMap.get(level);
    }

    @Override
    public int getAttackDamage() {
        return getBoostfromEquippedItems(EffectType.STRENGTH_BOOST);
    }
}
