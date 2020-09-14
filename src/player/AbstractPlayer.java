package player;

import items.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPlayer {
    private String playerName;
    private int hitPoints;
    private int manaPoints;
    private int baseStrength;
    private int baseIntelligence;
    private int experiencePoints;
    private List<Item> Inventory = new ArrayList<>();
    public List<Slot> playerSlotList = new ArrayList<>();

    public String getPlayerName() {
        return playerName;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getBaseIntelligence() {
        return baseIntelligence;
    }

    public int getBaseStrength() {
        return baseStrength;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public AbstractPlayer(int baseStrength, int baseIntelligence, int hitPoints, int manaPoints) {
        this.baseIntelligence = baseIntelligence;
        this.baseStrength = baseStrength;
        this.hitPoints = hitPoints;
        this.manaPoints = manaPoints;
    }

    public int getLevel(int experiencePoints) {
        int[] levelArray = new int[]{0, 299, 899, 2699, 6499, 13999};
        int counter = 1;
        if (experiencePoints > 13999) {
            return 6;
        }
        for (int i = 0; i < levelArray.length; i++) {

            if (levelArray[i] < experiencePoints && experiencePoints < levelArray[i + 1]) {

                counter = i + 1;
            }
        }
        return counter;
    }
    public void rest(){
        if(getMaxHP() - getHitPoints()>4){
            setHitPoints(getHitPoints() + 4);
        }
        else if (getMaxHP() - getHitPoints()<4 && getMaxHP() - getHitPoints()>0){
            setHitPoints(getMaxHP());
        }
        if(getMaxMana() - getManaPoints()>4){
            setManaPoints(getManaPoints() + 4);
        }
        else if (getMaxMana() - getManaPoints()<4 && getMaxMana() - getManaPoints()>0){
            setManaPoints(getMaxMana());
        }


    }

    public void setManaPoints(int manaPoints) {
        this.manaPoints = manaPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public abstract LevelBonus  levelBonusMap(int level) ;

    public abstract List<Slot> setPlayerSlotList();

    public void pickUp(Item item) {
        Inventory.add(item);
    }

    public void drop(Item item) {
        if (!isItemEquipped(item))
            Inventory.remove(item);
        else {
            System.out.println("Cannot drop the item, it is equipped");
        }
    }

    public boolean isItemEquipped(Item item) {
        List<Item> equippedItems = new ArrayList<>();
        this.getPlayerSlotList().forEach(e -> equippedItems.addAll(e.getListOfItems()));
        return equippedItems.stream().anyMatch(x -> x.equals(item));
    }

    public List<Slot> getPlayerSlotList() {
        return playerSlotList;
    }

    public void printInventory() {
        System.out.println("Items in "+getPlayerName()+" Inventory");
        for (Item i : Inventory) {
            System.out.println(i.getItemName());
        }
    }

    public List<Item> getEquippedItems() {
        List<Item> equippedItems = new ArrayList<>();
        this.getPlayerSlotList().forEach(e -> equippedItems.addAll(e.getListOfItems()));
        return equippedItems;
    }

    public List<ItemEffect> getEquippedItemEffect() {
        List<ItemEffect> equippedItemEffect = new ArrayList<>();
        getEquippedItems().forEach(e -> equippedItemEffect.addAll(e.getItemEffects()));
        return equippedItemEffect;
    }

    public int getBoostfromEquippedItems(EffectType effectType) {
        Integer acc = 0;
        for (ItemEffect e : getEquippedItemEffect()) {
            if (e.getEffectType() == effectType) {
                Integer amountEffect = e.getAmountEffect();
                acc = acc + amountEffect;
            }
        }
        return acc;
    }

    public abstract int getLevelHP();

    public abstract int getLevelMP();

    public abstract int getLevelInt();

    public abstract int getLevelStr();

    public abstract int getIntelligence();

    public abstract int getStrength();

    public abstract int getMaxHP();

    public abstract int getMaxMana();

    public abstract int getAttackDamage();

    public void addXP(int XPoints) {
        experiencePoints += XPoints;
    }

    public void removeXP(int XPoints) {
        experiencePoints -= XPoints;
    }

    public String playerStats() {
        return " Hit Points: " + getMaxHP()
                + " Mana Points: " + getMaxMana()
                + " Strength: " + getStrength()
                + " Damage: " + getAttackDamage()
                + " Intelligence: " + getIntelligence();

    }

    public List<Item> getInventory() {
        return Inventory;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
