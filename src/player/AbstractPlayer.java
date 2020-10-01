package player;

import Entity.Entity;
import items.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPlayer implements Entity {
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
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
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
        if (getMaxHP() - getBaseHitPoints() > 4) {
            setBaseHitPoints(getBaseHitPoints() + 4);
        } else if (getMaxHP() - getBaseHitPoints() < 4 && getMaxHP() - getBaseHitPoints() > 0) {
            setBaseHitPoints(getMaxHP());
        }
        if (getMaxMana() - getBaseManaPoints() > 4) {
            setBaseManaPoints(getBaseManaPoints() + 4);
        } else if (getMaxMana() - getBaseManaPoints() < 4 && getMaxMana() - getBaseManaPoints() > 0) {
            setBaseManaPoints(getMaxMana());
        }
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
    }

    public void drop(Item item) {
        if (!isItemEquipped(item))
            Inventory.remove(item);
        else {
            System.out.println("Cannot drop the item, it is equipped");
        }
    }

    public void useHealthPotion() {
        HealthPotion healthPotion = (HealthPotion) Inventory.stream().filter(e -> e instanceof HealthPotion).findAny().orElse(null);
        if (healthPotion != null) {
            System.out.println(playerStats());
            if (getMaxHP() - getCurrentHitPoints() > healthPotion.use()) {
                setCurrentHitPoints(getCurrentHitPoints() + healthPotion.use());
            } else if (getMaxHP() -getCurrentHitPoints() < healthPotion.use() && getMaxHP() - getCurrentHitPoints() > 0) {
                setCurrentHitPoints(getMaxHP());
            }
            Inventory.remove(healthPotion);
            System.out.println(playerStats());
        }
    }
    public void useManaPotion() {
        HealthPotion manaPotion = (HealthPotion) Inventory.stream().filter(e -> e instanceof ManaPotion).findAny().orElse(null);
        if (manaPotion != null) {
            System.out.println(playerStats());
            if (getMaxMana() - getCurrentManaPoints() > manaPotion.use()) {
                setCurrentManaPoints(getCurrentManaPoints() + manaPotion.use());
            } else if (getMaxMana() -getCurrentManaPoints() < manaPotion.use() && getMaxMana() - getCurrentManaPoints() > 0) {
                setCurrentManaPoints(getMaxMana());
            }
            Inventory.remove(manaPotion);
            System.out.println(playerStats());
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
        return getBaseIntelligence() + getLevelInt() + getBoostfromEquippedItems(EffectType.INTELLECT_BOOST);
    }

    public int getStrength() {
        return getBaseStrength() + getLevelStr() + getBoostfromEquippedItems(EffectType.STRENGTH_BOOST);
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
    }

    public void removeXP(int XPoints) {
        experiencePoints -= XPoints;
    }

    public String playerStats() {

        return "<html>Hit Points: " + getMaxHP()+"<br/>" +
                "Mana Points: " + getMaxMana()+"<br/> " +
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
}
