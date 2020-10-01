package tower;

import Entity.Entity;
import enemies.AbstractEnemy;

import items.Equippable;
import items.Item;
import items.Trap;
import items.Usable;
import player.AbstractPlayer;


public abstract class AbstractBlock {
    Coordinates Coordinates;
    private boolean isWalkable;
    private Entity Occupant;
    private Item item;

    public Coordinates getCoordinates() {
        return Coordinates;
    }

    public void setOccupant(Entity occupant) {
        Occupant = occupant;
    }

    public Entity getOccupant() {
        return Occupant;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public boolean isOccupiedByPlayer() {
        if (isOccupied()) {
            return Occupant instanceof AbstractPlayer;
        }
        return false;
    }

    public boolean isOccupiedByEnemy() {
        if (isOccupied()) {
            return Occupant instanceof AbstractEnemy;
        }
        return false;
    }

    private boolean isOccupied() {
        return Occupant != null;
    }

    public AbstractBlock(Coordinates coordinates, boolean IsWalkable) {
        Coordinates = coordinates;
        isWalkable = IsWalkable;
    }
    public boolean hasItem(){
        return item != null;
    }

    public Item getItem() {
        return item;
    }
    public String getItemDescription(){
        return item.getItemName();
    }

    public boolean IsItemPotion(){
        if (item instanceof Usable){
            if(item instanceof Trap )
                return false;
            else
                return true;
        }
        else
            return false;
    }
    public boolean IsItemEquippable(){
        if (item instanceof Equippable){
            return true;
        }
        else
            return false;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}