package player;

import items.EffectType;
import java.util.Map;

public class Wizard extends AbstractPlayer {
    public Wizard(String name) {
        super(0, 0, 0, 0);
        setPlayerName(name);
    }


    @Override
    public LevelBonus levelBonusMap(int level) {
        Map<Integer, LevelBonus> bonusMap = Map.of(
                1, new LevelBonus(0, 10, 20, 30),
                2, new LevelBonus(0, 20, 40, 50),
                3, new LevelBonus(0, 30, 50, 70),
                4, new LevelBonus(0, 40, 55, 90),
                5, new LevelBonus(0, 50, 60, 110),
                6, new LevelBonus(0, 70, 80, 140));

        return bonusMap.get(level);
    }

    @Override
    public int getAttackDamage() {
        return getBoostfromEquippedItems(EffectType.INTELLECT_BOOST);
    }


}

