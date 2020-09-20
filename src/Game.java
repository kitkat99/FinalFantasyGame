//import dice.Dice;
//import enemies.AbstractEnemy;
//import enemies.Goblin;
//import enemies.OrcGrunt;
//import items.Greatsword;
//import items.Warhammer;
//import items.AbstractWeapon;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import static java.lang.Integer.parseInt;
//
//public class Game {
//    public static void main(String[] args) {
//
//
//        int playerHitPoints = 50;
//        int roundHitPoints = 0;
//
//        List<AbstractEnemy> enemyList = createEnemyList();
//        List<AbstractWeapon> weaponList = createWeaponList();
//
//        printEnemyList(enemyList);
//
//        int roundCounter = 0;
//        while (playerHitPoints > 0 && enemyList.size()>0) {
//            roundCounter++;
//            System.out.println(System.lineSeparator()+"----------------ROUND " + roundCounter + "-----------------");
//            roundHitPoints = enemyAttack(enemyList);
//            playerHitPoints = playerHitPoints - roundHitPoints;
//
//            //Check Player HitPoints after damage
//            if(playerHitPoints>0)
//                System.out.println(System.lineSeparator()+"Health Points remaining: " + playerHitPoints+System.lineSeparator());
//            else
//                break;
//            //Player Attack
//            PlayerRound(enemyList, weaponList);
//
//        }
//        if(enemyList.isEmpty() && playerHitPoints > 0)
//        {
//            System.out.println(System.lineSeparator()+"All your enemies are dead. You Won");
//        }
//        else if(!enemyList.isEmpty() && playerHitPoints <= 0)
//        {
//            System.out.println(System.lineSeparator()+"You died. Tough luck");
//        }
//
//    }
//
//
//    public static List<AbstractEnemy> createEnemyList() {
//        int enemyHitPoints = 0;
//        List<AbstractEnemy> enemyList = new ArrayList<AbstractEnemy>();
//        Dice RollDice = new Dice();
//        RollDice.numberOfDices = 1;
//        RollDice.numberDicesEdges = 20;
//        RollDice.diceBonus = 0;
//
//        while (enemyHitPoints < 60) {
//            RollDice.roll();
//            Goblin Goblin = new Goblin();
//            OrcGrunt OrcGrunt = new OrcGrunt();
//            Troll Troll = new Troll();
//            if (RollDice.result < 10 && enemyHitPoints + Goblin.getHitPoints() <= 60) {
//                enemyHitPoints = enemyHitPoints + Goblin.getHitPoints();
//                enemyList.add(Goblin);
//            } else if (RollDice.result >= 10 && RollDice.result < 15 && enemyHitPoints + OrcGrunt.getHitPoints() <= 60) {
//                enemyHitPoints = enemyHitPoints + OrcGrunt.getHitPoints();
//                enemyList.add(OrcGrunt);
//            }
//            if (RollDice.result >= 15 && RollDice.result <= 20 && enemyHitPoints + Troll.getHitPoints() <= 60) {
//                enemyHitPoints = enemyHitPoints + Troll.getHitPoints();
//                enemyList.add(Troll);
//            }
//            if (enemyHitPoints > 50)
//                break;
//        }
//        return enemyList;
//    }
//
//    public static List<AbstractWeapon> createWeaponList() {
//        Greatsword Greatsword = new Greatsword();
//        Warhammer Warhammer = new Warhammer();
//        List<AbstractWeapon> weaponList = new ArrayList<AbstractWeapon>();
//        weaponList.add(Greatsword);
//        weaponList.add(Warhammer);
//        return weaponList;
//    }
//
//    public static int enemyAttack(List<AbstractEnemy> enemyList) {
//        int roundHitPoints = 0;
//        for (AbstractEnemy enemyItem : enemyList) {
//            int enemyDamage = enemyItem.getWeapon().hitDamageWeapon();
//            System.out.println(enemyItem.getEnemyName() + " attacks for " + enemyDamage + " damage!");
//            roundHitPoints = roundHitPoints + enemyDamage;
//        }
//        return roundHitPoints;
//    }
//
//    public static int PlayerSelectionEnemy(List<AbstractEnemy> enemyList) {
//        System.out.println("Select an enemy to attack:" + System.lineSeparator());
//        printEnemyList(enemyList);
//        var scanEnemy = new Scanner(System.in);
//        int indexOfEnemyElement = parseInt(scanEnemy.nextLine());
//        while (indexOfEnemyElement > enemyList.size() - 1) {
//            System.out.println("Incorrect value");
//            System.out.println("Select an enemy to attack");
//            scanEnemy = new Scanner(System.in);
//            indexOfEnemyElement = parseInt(scanEnemy.nextLine());
//        }
//
//        return indexOfEnemyElement;
//    }
//
//    public static AbstractWeapon PlayerSelectionWeapon(List<AbstractWeapon> weaponList) {
//        System.out.println("Select a weapon to use:" + System.lineSeparator());
//        int counter = 0;
//        for (AbstractWeapon weaponItem : weaponList) {
//
//            System.out.println("[" + counter + "] items.Weapon[" + weaponItem.getWeaponDescription() + ", dice.Dice[" + weaponItem.getRollDiceWeaponEdges() + "d" + weaponItem.getRollDiceWeaponNum() + "+" + weaponItem.getRollDiceWeaponBonus() + "]");
//            counter++;
//        }
//        var scanWeapon = new Scanner(System.in);
//        int indexOfWeaponElement = parseInt(scanWeapon.nextLine());
//        while (indexOfWeaponElement > weaponList.size() - 1) {
//            System.out.println("Incorrect value");
//            System.out.println("Select a weapon to attack");
//            scanWeapon = new Scanner(System.in);
//            indexOfWeaponElement = parseInt(scanWeapon.nextLine());
//        }
//
//        return weaponList.get(indexOfWeaponElement);
//    }
//
//    public static int calculateInitialDamage(AbstractEnemy enemy, AbstractWeapon weapon) {
//        int calculateDamagePlayerWeapon = weapon.hitDamageWeapon();
//        System.out.println("You attack " + enemy.getEnemyName() + "[" + enemy.getHitPoints() + "]" + " with the items.Weapon " + "[" + weapon.getWeaponDescription() + ", dice.Dice[" + weapon.getRollDiceWeaponEdges() + "d" + weapon.getRollDiceWeaponNum() + "+" + weapon.getRollDiceWeaponBonus() + "] for " +          calculateDamagePlayerWeapon + " (" + weapon.getDamageTypeWeapon() + ")");
//
//        return calculateDamagePlayerWeapon;
//
//    }
//
//    public static void printEnemyList(List<AbstractEnemy> enemyList){
//        int counter = 0;
//        for (AbstractEnemy enemyItem : enemyList) {
//
//            System.out.println("[" + counter + "] " + enemyItem.getEnemyName() + " [" + enemyItem.getHitPoints() + "]");
//            counter++;
//
//        }
//    }
//
//    public static void PlayerRound(List<AbstractEnemy> enemyList, List<AbstractWeapon> weaponList){
//        //PlayerChoices
//        int chosenEnemyIndex = PlayerSelectionEnemy(enemyList);
//        AbstractWeapon chosenWeapon = PlayerSelectionWeapon(weaponList);
//
//        System.out.println(chosenWeapon.getWeaponName());
//
//        int calculateInitialDamage = calculateInitialDamage(enemyList.get(chosenEnemyIndex),chosenWeapon);
//
//        int calculateDamageTaken = enemyList.get(chosenEnemyIndex).calculateDamageTaken(chosenWeapon.DamageTypeWeapon, calculateInitialDamage);
//
//        int enemyHitPoints = enemyList.get(chosenEnemyIndex).getHitPoints() - calculateDamageTaken;
//        if(enemyHitPoints<=0)
//        {
//            System.out.println(System.lineSeparator()+enemyList.get(chosenEnemyIndex).getEnemyName()+" has died");
//            enemyList.remove(enemyList.get(chosenEnemyIndex));
//        }
//        else
//        {
//            enemyList.get(chosenEnemyIndex).setHitPoints(enemyHitPoints);
//        }
//    }
//}
