package player;

import Entity.Entity;
import items.*;
import observers.Observer;
import observers.Subject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Entity, Subject {
    private String playerName;
    private int baseHitPoints;
    private int baseManaPoints;
    private int baseStrength;
    private int baseIntelligence;
    private int experiencePoints;
    private List<Item> Inventory = new ArrayList<>();
    public List<Slot> playerSlotList = new ArrayList<>();
    private int currentHitPoints = getMaxHP();
    private int currentManaPoints = getMaxMana();
    private static final int playerVisibility = 6;

    public static int getPlayerVisibility() {
        return playerVisibility;
    }

    public int getCurrentManaPoints() {
        return currentManaPoints;
    }

    public void setCurrentManaPoints(int currentManaPoints) {
        this.currentManaPoints = currentManaPoints;
        observers.forEach( x -> notifyObserver(x));
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
        observers.forEach( x -> notifyObserver(x));
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getBaseHitPoints() {
        return baseHitPoints;
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

    public int getBaseManaPoints() {
        return baseManaPoints;
    }

    public AbstractPlayer(int baseStrength, int baseIntelligence, int baseHitPoints, int baseManaPoints) {
        this.baseIntelligence = baseIntelligence;
        this.baseStrength = baseStrength;
        this.baseHitPoints = baseHitPoints;
        this.baseManaPoints = baseManaPoints;
        playerSlotList = setPlayerSlotList();
        observers = new ArrayList<Observer>();
    }


    public List<Slot> setPlayerSlotList() {
        return List.of(new Slot(SlotType.CHEST, 1), new Slot(SlotType.NECK, 1), new Slot(SlotType.HAND, 2), new Slot(SlotType.FINGER, 10), new Slot(SlotType.LEGS, 2));
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

    public void rest() {
        if (getMaxHP() - getCurrentHitPoints() > 4) {
            setCurrentHitPoints(getCurrentHitPoints() + 4);
        } else if (getMaxHP() - getCurrentHitPoints() <= 4 && getMaxHP() - getCurrentHitPoints() > 0) {
            setCurrentHitPoints(getMaxHP());
        }
        if (getMaxMana() - getCurrentManaPoints() > 4) {
            setCurrentManaPoints(getCurrentManaPoints() + 4);
        } else if (getMaxMana() - getCurrentManaPoints() <= 4 && getMaxMana() - getCurrentManaPoints() > 0) {
            setCurrentManaPoints(getMaxMana());
        }
        observers.forEach( x -> notifyObserver(x));
    }

    public void setBaseManaPoints(int baseManaPoints) {
        this.baseManaPoints = baseManaPoints;
    }

    public void setBaseHitPoints(int baseHitPoints) {
        this.baseHitPoints = baseHitPoints;
    }

    public abstract LevelBonus levelBonusMap(int level);

    public void pickUp(Item item) {
        Inventory.add(item);
        observers.forEach( x -> notifyObserver(x));
    }

    public void drop(Item item) {
        if (!isItemEquipped(item))
            Inventory.remove(item);
        observers.forEach( x -> notifyObserver(x));
    }

    public void useHealthPotion() {
        HealthPotion healthPotion = (HealthPotion) Inventory.stream().filter(e -> e instanceof HealthPotion).findAny().orElse(null);
        calculateHealthPoints(healthPotion);
        observers.forEach( x -> notifyObserver(x));
    }
    public void useMinorHealthPotion() {
        MinorHealthPotion minorHealthPotion = (MinorHealthPotion) Inventory.stream().filter(e -> e instanceof MinorHealthPotion).findAny().orElse(null);
        calculateHealthPoints(minorHealthPotion);
        observers.forEach( x -> notifyObserver(x));
    }

    public void useManaPotion() {
        ManaPotion manaPotion = (ManaPotion) Inventory.stream().filter(e -> e instanceof ManaPotion).findAny().orElse(null);
        calculateManaPoints(manaPotion);
        observers.forEach( x -> notifyObserver(x));
    }
    public void useMinorManaPotion() {
        MinorManaPotion minorManaPotion = (MinorManaPotion) Inventory.stream().filter(e -> e instanceof MinorManaPotion).findAny().orElse(null);
        calculateManaPoints(minorManaPotion);
        observers.forEach( x -> notifyObserver(x));
    }

    public void calculateHealthPoints(Usable potion){
        if (potion != null) {
            if (getMaxHP() - getCurrentHitPoints() > potion.use()) {
                setCurrentHitPoints(getCurrentHitPoints() + potion.use());
            } else if (getMaxHP() -getCurrentHitPoints() <= potion.use() && getMaxHP() - getCurrentHitPoints() > 0) {
                setCurrentHitPoints(getMaxHP());
            }
            Inventory.remove(potion);
        }
    }

    public void calculateManaPoints(Usable potion){
        if (potion != null) {
            if (getMaxMana() - getCurrentManaPoints() > potion.use()) {
                setCurrentManaPoints(getCurrentManaPoints() + potion.use());
            } else if (getMaxMana() -getCurrentManaPoints() <= potion.use() && getMaxMana() - getCurrentManaPoints() > 0) {
                setCurrentManaPoints(getMaxMana());
            }
            Inventory.remove(potion);
        }
    }

    public void useTrap( Trap trap) {
        if (trap != null) {
            if (getCurrentHitPoints() + trap.use() <= 0)
                setCurrentHitPoints(0);
            else
                setCurrentHitPoints(getCurrentHitPoints() + trap.use());
        }
        observers.forEach( x -> notifyObserver(x));
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
        System.out.println("Items in " + getPlayerName() + " Inventory");
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

    public int getLevelHP() {
        return levelBonusMap(getLevel(getExperiencePoints())).getHP();
    }

    public int getLevelMP() {
        return levelBonusMap(getLevel(getExperiencePoints())).getMP();
    }

    public int getLevelInt() {
        return levelBonusMap(getLevel(getExperiencePoints())).getInt();
    }

    public int getLevelStr() {
        return levelBonusMap(getLevel(getExperiencePoints())).getStr();
    }

    public int getIntelligence() {
        return getBaseIntelligence() + getLevelInt() ;
    }

    public int getStrength() {
        return getBaseStrength() + getLevelStr() ;
    }


    public int getMaxHP() {
        return getBaseHitPoints() + getLevelHP() + getBoostfromEquippedItems(EffectType.HP_BOOST);
    }


    public int getMaxMana() {
        return getBaseManaPoints() + getLevelMP() + getBoostfromEquippedItems(EffectType.MANA_BOOST);
    }


    public abstract int getAttackDamage();

    public void addXP(int XPoints) {
        experiencePoints += XPoints;
        observers.forEach( x -> notifyObserver(x));
    }

    public void removeXP(int XPoints) {
        experiencePoints -= XPoints;
    }

    @Override
    public String toString() {
        if(this instanceof Warrior)
            return "Warrior";
        else if(this instanceof Wizard)
            return "Wizard";
        return "";
    }

    public String playerStats() {

        return "<html>Hit Points: " + getCurrentHitPoints()+"<br/>" +
                "Mana Points: " + getCurrentManaPoints()+"<br/> " +
                "Strength: " + getStrength()+"<br/> " +
                "Damage: " + getAttackDamage()+"<br/> " +
                "Intelligence: " + getIntelligence()+"<br/> " +
                "</html>";

    }

    public List<Item> getInventory() {
        return Inventory;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private ArrayList<Observer> observers;



    @Override
    public void register(Observer newObserver) {
        observers.add(newObserver);
    }

    @Override
    public void notifyObserver(Observer o) {
        for(Observer observer : observers){
            observer.update(this);
        }
    }

}
