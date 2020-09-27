package items;

import player.LevelBonus;

import java.util.Map;

public class WeaponBonusFloor {
    public static Integer floorBonusMap(int floor) {
        Map<Integer, Integer> bonusMap = Map.of(
                0, 10,
                1, 15,
                2, 20,
                3, 25,
                4, 30,
                5, 35,
                6, 40,
                7, 45,
                8, 50,
                9,60);

        return bonusMap.get(floor);
    }
}
