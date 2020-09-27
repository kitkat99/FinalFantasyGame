package generator;

import enemies.*;
import items.*;
import player.AbstractPlayer;
import player.Warrior;
import player.Wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Generator {
    public static AbstractEnemy generateEnemy(int playerLevel) {
        List<AbstractEnemy> listOfAllEnemies = List.of(new GiantRat(), new Goblin(), new GraySlime(), new OrcGrunt(), new OrcWarlord(), new Ettin(), new Skeleton(), new Wyrm(), new Vampire());
        List<AbstractEnemy> listOfLevelEnemies = listOfAllEnemies.stream().filter(e -> e.getLevelAppearance().contains(playerLevel)).collect(Collectors.toList());
        Collections.shuffle(listOfLevelEnemies);
        return listOfLevelEnemies.stream().findFirst().orElse(null);
    }

    public static Usable generatePotions() {


        List<Usable> listOfAllPotions = new ArrayList<Usable>() {
            {
                add(new HealthPotion());
                add(new MinorHealthPotion());
                add(new ManaPotion());
                add(new MinorManaPotion());
            }
        };

        Collections.shuffle(listOfAllPotions);
        return listOfAllPotions.stream().findFirst().orElse(null);
    }

    public static AbstractWeapon generateWeapon(AbstractPlayer player, int floor) {
        int bonusFloor;
        int strengthBonus;
        int intellectBonus;
        int hpBonus;
        int mpBonus;
        List<String> listOfDescriptions = new ArrayList<String>() {
            {
                add("Amazing");
                add("Ancient");
                add("Deadly");
                add("Legendary");
                add("Cursed");
                add("Forbidden");
            }
        };
        Collections.shuffle(listOfDescriptions);
        if (player instanceof Warrior) {
            bonusFloor = WeaponBonusFloor.floorBonusMap(floor);
            strengthBonus = ThreadLocalRandom.current().nextInt(0, bonusFloor);
            hpBonus = bonusFloor - strengthBonus;
            return new Sword(listOfDescriptions.stream().findAny().orElse(null), hpBonus, strengthBonus);
        } else if (player instanceof Wizard) {
            bonusFloor = WeaponBonusFloor.floorBonusMap(floor);
            intellectBonus = ThreadLocalRandom.current().nextInt(0, bonusFloor);
            hpBonus = (bonusFloor - intellectBonus) / 2;
            mpBonus = hpBonus;
            return new Staff(listOfDescriptions.stream().findAny().orElse(null), hpBonus, mpBonus, intellectBonus);
        }
        return null;
    }
    public static List<Item> createItemList(int floor, AbstractPlayer player){
        List<Item> listOfFloorItems = new ArrayList<>();
        AbstractWeapon randomWeapon;
        Item randomPotion;
        for(int i =1; i <= 5; i++){
            randomWeapon = generateWeapon(player,floor);
            listOfFloorItems.add(randomWeapon);
        }
        for(int i =1; i <= 10; i++){
            randomPotion = generatePotions();
            listOfFloorItems.add(randomPotion);
        }
        return listOfFloorItems;
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
        Wizard kat = new Wizard("Kat");
        int amountOfGamePotions = ThreadLocalRandom.current().nextInt(20, 30);
        int counter = 0;
        List<Usable> listOfGamePotions = new ArrayList<Usable>();
        while (counter < amountOfGamePotions) {
            Usable tempPotion = generatePotions();
            listOfGamePotions.add(tempPotion);
            counter++;
        }
        System.out.println(listOfGamePotions);

        for(int i=0; i<=9; i++) {
            System.out.println(createItemList(i, kat));
        }
    }
}

