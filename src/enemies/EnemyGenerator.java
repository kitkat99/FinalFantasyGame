package enemies;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnemyGenerator {
    public static AbstractEnemy generateEnemy (int playerLevel) {
        List<AbstractEnemy> listOfAllEnemies = List.of(new GiantRat(), new Goblin(), new GraySlime(), new OrcGrunt(), new OrcWarlord(), new Ettin(), new Skeleton(), new Wyrm(), new Vampire());
        List<AbstractEnemy> listOfLevelEnemies = listOfAllEnemies.stream().filter(e -> e.getLevelAppearance().contains(playerLevel)).collect(Collectors.toList());
        Collections.shuffle(listOfLevelEnemies);
        return listOfLevelEnemies.stream().findFirst().orElse(null);
    }

    public static void main(String[] args) {

        AbstractEnemy e1 = generateEnemy(3);
        AbstractEnemy e2 = generateEnemy(3);
        AbstractEnemy e3 = generateEnemy(3);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(e1.equals(e2));
        System.out.println(e1.equals(e3));
        System.out.println(e3.equals(e2));
    }
}

