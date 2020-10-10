package GameState;

import Entity.Entity;
import enemies.AbstractEnemy;
import enemies.Dragon;
import generator.Generator;
import items.Equippable;
import items.Item;
import items.Trap;
import observers.playerObserver;
import player.AbstractPlayer;
import player.Slot;
import player.Warrior;
import player.Wizard;
import tower.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static generator.Generator.generatePotions;

public class GameState {
    AbstractPlayer player;
    Floor floorBufferedImage;
    Tower tower;
    JTextArea textArea;
    playerObserver playerData;
    ArrayList<AbstractBlock> listOfEnemyTiles = new ArrayList<>();
    JPanel leftPanel;
    JLabel map;

    public GameState(AbstractPlayer tempPlayer, Floor tempFloor, Tower tempTower, JTextArea tempArea, playerObserver tempObserver, JPanel tempPanel, JLabel tempMap) {
        this.player = tempPlayer;
        this.floorBufferedImage = tempFloor;
        this.tower = tempTower;
        this.textArea = tempArea;
        this.playerData = tempObserver;
        this.leftPanel = tempPanel;
        this.map = tempMap;
    }

    public void changePosition(int x, int y) {
        AbstractBlock tempTile;
        var newPosition = new Coordinates(floorBufferedImage.getEntityCoordinates(player).getCoordinateX() + x, floorBufferedImage.getEntityCoordinates(player).getCoordinateY() + y);
        if (isMovementValid(x, y, player)) {
            tempTile = floorBufferedImage.getFloor()[newPosition.getCoordinateX()][newPosition.getCoordinateY()];
            if (!tempTile.isWalkable() || tempTile == null) {
                return;
            } else {
                if (tempTile instanceof Exit) {
                    tower.goToNextFloor();
                    textArea.append("\nYou are now at the " + (tower.getFloors().indexOf(tower.getCurrentFloor()) + 1) + " floor");
                    floorBufferedImage = tower.getCurrentFloor();
                    floorBufferedImage.setEntityCoordinates(floorBufferedImage.getStartx(), floorBufferedImage.getStarty(), player);
                }
                if (tempTile instanceof Entrance) {
                    tower.goToPreviousFloor();
                    textArea.append("\nYou are now at the " + (tower.getFloors().indexOf(tower.getCurrentFloor()) + 1) + " floor");
                    floorBufferedImage = tower.getCurrentFloor();
                    floorBufferedImage.setEntityCoordinates(floorBufferedImage.getExitX(), floorBufferedImage.getExitY(), player);
                }
                if (tempTile instanceof Tile && !tempTile.isOccupiedByEnemy()) {
                    floorBufferedImage.setEntityCoordinates(newPosition.getCoordinateX(), newPosition.getCoordinateY(), player);
                    if (tempTile.hasItem() == true) {
                        if (tempTile.IsItemPotion()) {
                            textArea.append("\n You have found " + tempTile.getItemDescription());
                            player.pickUp(tempTile.getItem());
                            tempTile.setItem(null);
                            player.printInventory();
                        } else if (tempTile.IsItemEquippable()) {
                            textArea.append("\n You have found " + tempTile.getItemDescription() + ". Press P to swap Weapons");
                        } else if (tempTile.getItem() instanceof Trap) {
                            textArea.append("\n You stepped on a " + tempTile.getItemDescription());
                            player.useTrap((Trap) tempTile.getItem());
                            tempTile.setItem(null);
                            if (player.getCurrentHitPoints() <= 0) {
                                textArea.append("\nYou died");
                            }
                        }
                    }
                    decideIfEnemyMoves();
                    listOfEnemyTiles = floorBufferedImage.getListOfFloorEnemyTiles();
                    createEnemy(player, listOfEnemyTiles, 0.2);

                }

                floorBufferedImage.drawFloor(player);
                map.setIcon(floorBufferedImage.getIcon());
                leftPanel.revalidate();
                leftPanel.repaint();
            }
        }
    }

    public void decideIfEnemyMoves() {
        List<AbstractBlock> movableEnemiesList = defineEnemiesWithinEnemyVisibilityRadius(floorBufferedImage.getListOfFloorEnemyTiles(), player);
        changeEnemyPosition(movableEnemiesList);
    }

    public void createEnemy(AbstractPlayer player, ArrayList<AbstractBlock> listOfEnemyTiles, double probability) {
        if (calculateProbability(player.getCurrentHitPoints(), player.getMaxHP(), probability) && listOfEnemyTiles.size() <= 30) {
            AbstractEnemy enemy = Generator.generateEnemy(player.getLevel(player.getExperiencePoints()));
            AbstractBlock enemyTile = calculateEnemyCoordinates(player, enemy);
            enemyTile.setOccupant(enemy);
            listOfEnemyTiles = floorBufferedImage.getListOfFloorEnemyTiles();
            textArea.append("\n A new " + ((AbstractEnemy) enemyTile.getOccupant()).getEnemyName() + " has appeared");
            textArea.append("\n Current Enemies Number on the floor:  " + listOfEnemyTiles.size());
        }
    }


    public boolean isMovementValid(int x, int y, Entity entity) {
        var isLegalXMovement = (floorBufferedImage.getEntityCoordinates(entity).getCoordinateX() + x) < (int) floorBufferedImage.getX_DIMENSION() &&
                (floorBufferedImage.getEntityCoordinates(entity).getCoordinateX() + x) >= 0;
        var isLegalYMovement = (floorBufferedImage.getEntityCoordinates(entity).getCoordinateY() + y) < (int) floorBufferedImage.getY_DIMENSION() &&
                (floorBufferedImage.getEntityCoordinates(entity).getCoordinateY() + y) >= 0;
        return isLegalXMovement && isLegalYMovement;
    }


    public void swapWeapon() {
        AbstractBlock tempTile;
        var newPosition = new Coordinates(floorBufferedImage.getEntityCoordinates(player).getCoordinateX(), floorBufferedImage.getEntityCoordinates(player).getCoordinateY());
        tempTile = floorBufferedImage.getFloor()[newPosition.getCoordinateX()][newPosition.getCoordinateY()];
        if (tempTile.hasItem() == true) {

            if (tempTile.IsItemEquippable()) {
                boolean isEquipped = false;
                Item tempItem = null;
                for (Slot slot : player.getPlayerSlotList()) {
                    if (!player.getEquippedItems().isEmpty() && (((Equippable) tempTile.getItem()).getSlotType() == slot.getSlotType())) {

                        tempItem = player.getEquippedItems().get(0);
                        textArea.append("\n You dropped the " + tempItem.toString());
                        slot.remove(tempItem);
                        player.drop(tempItem);

                    }
                    if ((((Equippable) tempTile.getItem()).getSlotType() == slot.getSlotType())) {
                        slot.equip(tempTile.getItem());
                        textArea.append("\n You equipped the " + tempTile.getItem().toString());
                        player.notifyObserver(playerData);
                        isEquipped = true;
                        break;
                    }
                }
                if (!isEquipped) {
                    textArea.append("\n Item doesn't fit in Player's Slot ");
                }
                tempTile.setItem(tempItem);

            }
        }
    }

    public boolean calculateProbability(int hp, int hpMax, double probability) {
        double ratio = (((double) hp / hpMax) * probability);
        return Math.random() <= ratio;
    }

    public AbstractBlock calculateEnemyCoordinates(AbstractPlayer player, AbstractEnemy enemy) {
        int maxRadiusforEnemyCircle = Math.max(AbstractPlayer.getPlayerVisibility(), enemy.getVisibilityRadius()) + 1;
        ArrayList<AbstractBlock> possibleEnemyTilesList = calculateVisibleTiles(maxRadiusforEnemyCircle);
        return possibleEnemyTilesList.stream().filter(e -> e instanceof Tile && e.isOccupiedByEnemy() != true).findAny().orElse(null);
    }

    public ArrayList<AbstractBlock> calculateVisibleTiles(int maxRadius) {
        ArrayList<AbstractBlock> possibleEnemyTilesList = new ArrayList<AbstractBlock>();
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        if (playerCoordinates != null) {
            for (int i = 0; i < tower.getCurrentFloor().getFloor().length; i++) {
                for (int j = 0; j < tower.getCurrentFloor().getFloor()[j].length; j++) {
                    if ((Math.abs(playerCoordinates.getCoordinateX() - tower.getCurrentFloor().getFloor()[i][j].getCoordinates().getCoordinateX())) +
                            ((Math.abs(playerCoordinates.getCoordinateY() - tower.getCurrentFloor().getFloor()[i][j].getCoordinates().getCoordinateY()))) == maxRadius) {
                        possibleEnemyTilesList.add(tower.getCurrentFloor().getFloor()[i][j]);
                    }
                }
            }
        }
        return possibleEnemyTilesList;
    }

    public List<AbstractBlock> defineEnemiesWithinEnemyVisibilityRadius(List<AbstractBlock> listOfEnemyTiles, AbstractPlayer player) {
        List<AbstractBlock> movableEnemiesList = new ArrayList<>();
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        if (playerCoordinates != null) {
            for (AbstractBlock enemyTile : listOfEnemyTiles) {
                if ((Math.abs(playerCoordinates.getCoordinateX() - enemyTile.getCoordinates().getCoordinateX())) * (Math.abs(playerCoordinates.getCoordinateX() - enemyTile.getCoordinates().getCoordinateX())) +
                        ((Math.abs(playerCoordinates.getCoordinateY() - enemyTile.getCoordinates().getCoordinateY())) * (Math.abs(playerCoordinates.getCoordinateY() - enemyTile.getCoordinates().getCoordinateY()))) <
                        ((AbstractEnemy) enemyTile.getOccupant()).getVisibilityRadius() * ((AbstractEnemy) enemyTile.getOccupant()).getVisibilityRadius()) {
                    movableEnemiesList.add(enemyTile);
                }
            }
        }
        return movableEnemiesList;
    }

    public List<AbstractBlock> defineEnemiesWithinPlayerVisibilityRadius(List<AbstractBlock> listOfEnemyTiles, AbstractPlayer player) {
        List<AbstractBlock> attackableEnemiesList = new ArrayList<>();
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        if (playerCoordinates != null) {
            for (AbstractBlock enemyTile : listOfEnemyTiles) {
                if ((Math.abs(playerCoordinates.getCoordinateX() - enemyTile.getCoordinates().getCoordinateX())) * (Math.abs(playerCoordinates.getCoordinateX() - enemyTile.getCoordinates().getCoordinateX())) +
                        ((Math.abs(playerCoordinates.getCoordinateY() - enemyTile.getCoordinates().getCoordinateY())) * (Math.abs(playerCoordinates.getCoordinateY() - enemyTile.getCoordinates().getCoordinateY()))) <
                        AbstractPlayer.getPlayerVisibility() * AbstractPlayer.getPlayerVisibility()) {
                    attackableEnemiesList.add(enemyTile);
                }
            }
        }
        return attackableEnemiesList;
    }

    public double calculateMinimumDistance(int entityCoordinateX, int neighborCoordinateX, int entityCoordinateY, int neighborCoordinateY) {
        return (Math.abs(entityCoordinateX - neighborCoordinateX)) + (Math.abs(entityCoordinateY - neighborCoordinateY));
    }

    public void changeEnemyPosition(List<AbstractBlock> movableEnemiesList) {
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        if (playerCoordinates != null && movableEnemiesList.size() != 0) {
            for (AbstractBlock enemyTile : movableEnemiesList) {
                ArrayList<Coordinates> neighborCoordinatesList = new ArrayList<>();
                if (isMovementValid(-1, 0, enemyTile.getOccupant())) {
                    var westNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX() - 1, enemyTile.getCoordinates().getCoordinateY());
                    neighborCoordinatesList.add(westNeighbor);
                }
                if ((isMovementValid(1, 0, enemyTile.getOccupant()))) {
                    var eastNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX() + 1, enemyTile.getCoordinates().getCoordinateY());
                    neighborCoordinatesList.add(eastNeighbor);
                }
                if (isMovementValid(0, -1, enemyTile.getOccupant())) {
                    var northNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX(), enemyTile.getCoordinates().getCoordinateY() - 1);
                    neighborCoordinatesList.add(northNeighbor);
                }
                if (isMovementValid(0, 1, enemyTile.getOccupant())) {
                    var southNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX(), enemyTile.getCoordinates().getCoordinateY() + 1);
                    neighborCoordinatesList.add(southNeighbor);
                }

                double minDistance = calculateMinimumDistance(playerCoordinates.getCoordinateX(), enemyTile.getCoordinates().getCoordinateX(), playerCoordinates.getCoordinateY(), enemyTile.getCoordinates().getCoordinateY());

                for (Coordinates neighbor : neighborCoordinatesList) {
                    AbstractBlock tempTile;
                    tempTile = floorBufferedImage.getFloor()[neighbor.getCoordinateX()][neighbor.getCoordinateY()];
                    if ((Math.abs(playerCoordinates.getCoordinateX() - neighbor.getCoordinateX())) + (Math.abs(playerCoordinates.getCoordinateY() - neighbor.getCoordinateY())) <= minDistance
                            && tempTile instanceof Tile && tempTile.getOccupant() == null) {
                        minDistance = calculateMinimumDistance(playerCoordinates.getCoordinateX(), neighbor.getCoordinateX(), playerCoordinates.getCoordinateY(), neighbor.getCoordinateY());

                        floorBufferedImage.setEntityCoordinates(neighbor.getCoordinateX(), neighbor.getCoordinateY(), enemyTile.getOccupant());

                    }
                }
            }
        }
    }

    public void playerAttack() {
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        List<AbstractBlock> attackableEnemiesList = createAttackableEnemiesList(player);
        if (player instanceof Warrior) {
            if (attackableEnemiesList.size() != 0) {
                engageBattle(attackableEnemiesList);
            }
        } else if (player instanceof Wizard && player.getCurrentManaPoints() >= 5 && playerCoordinates != null) {
            if (attackableEnemiesList.size() != 0) {
                player.setCurrentManaPoints(player.getCurrentManaPoints() - 5);
                engageBattle(attackableEnemiesList);
            }
        }
    }


    public List<AbstractBlock> createAttackableEnemiesList(AbstractPlayer player) {
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        List<AbstractBlock> attackableEnemiesList = new ArrayList<>();
        if (player instanceof Warrior) {
            attackableEnemiesList = calculateNeighborTiles();

        } else if (player instanceof Wizard && playerCoordinates != null) {
            attackableEnemiesList = defineEnemiesWithinPlayerVisibilityRadius(floorBufferedImage.getListOfFloorEnemyTiles(), player);
            Collections.sort(attackableEnemiesList, Comparator.comparingDouble((AbstractBlock a) -> calculateMinimumDistance(playerCoordinates.getCoordinateX(), a.getCoordinates().getCoordinateX(), playerCoordinates.getCoordinateY(), a.getCoordinates().getCoordinateY())));
        }
        return attackableEnemiesList;
    }

    public void enemyAttack(List<AbstractBlock> attackableEnemiesList) {
        if (attackableEnemiesList.size() != 0) {
            for (AbstractBlock enemyTile : attackableEnemiesList) {
                int enemyDamage = ((AbstractEnemy) enemyTile.getOccupant()).getWeapon().hitDamageWeapon();
                if (player.getCurrentHitPoints() - enemyDamage > 0) {
                    player.setCurrentHitPoints(player.getCurrentHitPoints() - enemyDamage);
                    textArea.append("\n" + ((AbstractEnemy) enemyTile.getOccupant()).getEnemyName() + " hit you for " + enemyDamage + " damage");
                } else {
                    player.setCurrentHitPoints(0);
                    textArea.append("\nYou died");
                }
            }
        }
    }

    public void engageBattle(List<AbstractBlock> attackableEnemiesList) {
        AbstractBlock enemyTile = attackableEnemiesList.stream().findFirst().orElse(null);
        AbstractEnemy enemyToAttack = (AbstractEnemy) attackableEnemiesList.stream().findFirst().orElse(null).getOccupant();
        textArea.append("\n You hit " + enemyToAttack.getEnemyName() + " with " + player.getAttackDamage() + " damage");
        enemyToAttack.setHitPoints(enemyToAttack.getHitPoints() - player.getAttackDamage());
        if (enemyToAttack.getHitPoints() > 0) {
            textArea.append("\n Remaining HP for " + enemyToAttack.getEnemyName() + " is " + enemyToAttack.getHitPoints());
        } else if (enemyToAttack.getHitPoints() <= 0) {

            listOfEnemyTiles = floorBufferedImage.getListOfFloorEnemyTiles();
            listOfEnemyTiles.remove(enemyTile);
            textArea.append("\n Current Enemies Number on the floor:  " + listOfEnemyTiles.size());

            enemyTile.setOccupant(null);
            int previousLevel = player.getLevel(player.getExperiencePoints());
            player.addXP(enemyToAttack.getEnemyXP());
            textArea.append("\n You Gained " + enemyToAttack.getEnemyXP() + " XP ");
            if (previousLevel != player.getLevel(player.getExperiencePoints())) {
                textArea.append("\n You are now in " + player.getLevel(player.getExperiencePoints()) + " Level ");
            }
            attackableEnemiesList.remove(enemyTile);

            if (enemyToAttack instanceof Dragon) {
                textArea.append("\nYou won.");
                System.out.println("\nYou won.");
                System.exit(0);
            } else {
                if (calculateProbability(player.getCurrentHitPoints(), player.getMaxHP(), 0.2)) {
                    enemyTile.setItem(generatePotions());
                }
            }


        }
            enemyAttack(calculateNeighborTiles());
            decideIfEnemyMoves();

        repaint();
    }

    public List<AbstractBlock> calculateNeighborTiles() {
        Coordinates playerCoordinates = tower.getCurrentFloor().getEntityCoordinates(player);
        List<AbstractBlock> floorTiles = new ArrayList<>();
        if (playerCoordinates != null) {
            floorTiles = floorBufferedImage.getListOfFloorEnemyTiles().stream().filter(e ->
                    calculateMinimumDistance(playerCoordinates.getCoordinateX(), e.getCoordinates().getCoordinateX(), playerCoordinates.getCoordinateY(), e.getCoordinates().getCoordinateY()) == 1).collect(Collectors.toList());
            Collections.sort(floorTiles, new Comparator<AbstractBlock>() {
                @Override
                public int compare(AbstractBlock abstractBlock1, AbstractBlock abstractBlock2) {
                    return ((AbstractEnemy) abstractBlock1.getOccupant()).getHitPoints() - ((AbstractEnemy) abstractBlock2.getOccupant()).getHitPoints();
                }
            });
        }
        return floorTiles;
    }

    public void repaint() {
        floorBufferedImage.drawFloor(player);
        map.setIcon(floorBufferedImage.getIcon());
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public void useHealth() {
        player.useHealthPotion();
        enemyAttack(calculateNeighborTiles());
        decideIfEnemyMoves();
        repaint();
    }

    public void useMinorHealth() {
        player.useMinorHealthPotion();
        enemyAttack(calculateNeighborTiles());
        decideIfEnemyMoves();
        repaint();
    }

    public void useMana() {
        player.useManaPotion();
        enemyAttack(calculateNeighborTiles());
        decideIfEnemyMoves();
        repaint();
    }
    public void useMinorMana() {
        player.useMinorManaPotion();
        enemyAttack(calculateNeighborTiles());
        decideIfEnemyMoves();
        repaint();
    }

    public void rest() {
        player.rest();
        enemyAttack(calculateNeighborTiles());
        decideIfEnemyMoves();
        createEnemy(player, listOfEnemyTiles, 0.05);
        repaint();
    }

}
