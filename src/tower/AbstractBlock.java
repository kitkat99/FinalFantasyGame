package tower;

import Entity.Entity;
import enemies.AbstractEnemy;

import items.Equippable;
import items.Item;
import items.Trap;
import items.Usable;
import player.AbstractPlayer;
import states.Fogged;
import states.StateBlock;
import states.Unknown;
import states.Visible;

import java.awt.*;


public abstract class AbstractBlock {
    StateBlock stateBlock;
    StateBlock Unknown;
    StateBlock Fogged;
    StateBlock Visible;
    Boolean hasBeenStepped = false;
    Boolean isVisible = false;
    Coordinates Coordinates;
    private boolean isWalkable;
    private Entity Occupant;
    private Item item;

    public Coordinates getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(tower.Coordinates coordinates) {
        Coordinates = coordinates;
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
        Unknown = new Unknown(this);
        Fogged = new Fogged(this);
        Visible = new Visible(this);
        stateBlock = Unknown;
    }

    public void setStateBlock(StateBlock stateBlock) {
        this.stateBlock = stateBlock;
    }

    public StateBlock getFogged() {
        return Fogged;
    }

    public StateBlock getUnknown() {
        return Unknown;
    }

    public StateBlock getVisible() {
        return Visible;
    }

    public Color stateColor(){
        return stateBlock.stateColor();
    }

    public StateBlock getStateBlock() {
        return stateBlock;
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