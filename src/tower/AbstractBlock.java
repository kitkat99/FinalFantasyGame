package tower;

import Entity.Entity;
import enemies.AbstractEnemy;

import items.Item;
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

    public void setItem(Item item) {
        this.item = item;
    }
}