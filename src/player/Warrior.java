package player;

import items.EffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warrior extends AbstractPlayer {
    public Warrior(String name) {
        super(0, 0, 0, 0);
        playerSlotList = setPlayerSlotList();
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
    public List<Slot> setPlayerSlotList() {
        return List.of(new Slot(SlotType.CHEST, 1), new Slot(SlotType.NECK, 1), new Slot(SlotType.HAND, 2), new Slot(SlotType.FINGER, 10), new Slot(SlotType.LEGS, 2));
    }

    @Override
    public int getLevelHP() {
        return levelBonusMap(getLevel(getExperiencePoints())).getHP();
    }

    @Override
    public int getLevelMP() {
        return levelBonusMap(getLevel(getExperiencePoints())).getMP();
    }

    @Override
    public int getLevelInt() {
        return levelBonusMap(getLevel(getExperiencePoints())).getInt();
    }

    @Override
    public int getLevelStr() {
        return levelBonusMap(getLevel(getExperiencePoints())).getStr();
    }

    @Override
    public int getIntelligence() {
        return getBaseIntelligence() + getLevelInt();
    }

    @Override
    public int getStrength() {
        return getBaseStrength() + getLevelStr();
    }

    @Override
    public int getMaxHP() {
        return getHitPoints() + getLevelHP() + getBoostfromEquippedItems(EffectType.HP_BOOST);
    }

    @Override
    public int getMaxMana() {
        return getManaPoints() + getLevelMP() + getBoostfromEquippedItems(EffectType.MANA_BOOST);
    }

    @Override
    public int getAttackDamage() {
        return getBoostfromEquippedItems(EffectType.DAMAGE_BOOST);
    }


}
