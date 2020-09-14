import items.*;
import player.Slot;
import player.Warrior;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FantasyGame {
    public static void main(String[] args) {
        Warrior firstWarrior = new Warrior("Katerina");
        Warrior secondWarrior = new Warrior("Dimitris");
        List<Warrior> warriorList = List.of(firstWarrior, secondWarrior);

        warriorList.forEach(warrior ->
                System.out.println(warrior.getPlayerName() +
                        " Initial Player Stats : "
                        + warrior.playerStats())
        );
        RingOfHealth ringOfHealth = new RingOfHealth();
        FlimsyLeatherArmor flimsyLeatherArmor = new FlimsyLeatherArmor();
        RelicOfTheAncients relicOfAncients = new RelicOfTheAncients();
        WristbandOfMana wristbandOfMana = new WristbandOfMana();
        List<Item> listOfItems = List.of(ringOfHealth, flimsyLeatherArmor, relicOfAncients, wristbandOfMana);
        listOfItems.forEach(item -> {
            if(listOfItems.indexOf(item) % 2 != 0)
            firstWarrior.pickUp(item);
            else
            secondWarrior.pickUp(item);});
        for (Warrior warrior : warriorList) {
            for (Item elem : warrior.getInventory()) {
                if (elem instanceof Equippable) {
                    Slot slot = null;
                    for (Slot x : warrior.getPlayerSlotList()) {
                        if (x.getSlotType() == ((Equippable) elem).getSlotType()) {
                            slot = x;
                            break;
                        }
                    }
                    if (slot != null) {
                        slot.equip(elem);
                    }
                }
            }
        }


        warriorList.forEach(warrior -> {
            System.out.println(warrior.getPlayerName() + " Stats After Equip " + warrior.playerStats());
            warrior.printInventory();
            System.out.println(warrior.getPlayerName() + " Starting Level " + warrior.getLevel(warrior.getExperiencePoints())+". Initial Stats are:"+ warrior.playerStats());
            while (warrior.getLevel(warrior.getExperiencePoints()) < 5) {
                int experiencePoints = ThreadLocalRandom.current().nextInt(200, 1000 + 1);
                System.out.println(warrior.getPlayerName() + " Gained " + experiencePoints + " XP");
                warrior.addXP(experiencePoints);
                System.out.println(warrior.getPlayerName() + " is now level "
                        + warrior.getLevel(warrior.getExperiencePoints()) + ". Her/His/Them stats are :" + warrior.playerStats());

            }
        });
    }
}
