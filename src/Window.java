import Entity.Entity;
import enemies.AbstractEnemy;
import generator.Generator;
import items.*;
import observers.playerObserver;
import player.Slot;
import player.Wizard;
import tower.*;
import player.AbstractPlayer;
import player.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Window extends JFrame {

    static int WINDOW_WIDTH = 800;
    static int WINDOW_HEIGHT = 600;
    static int BUFFERED_IMG_WIDTH = 640;
    static int BUFFERED_IMG_HEIGHT = 480;
    static Floor floorBufferedImage;
    static JPanel leftPanel;
    static JLabel map;
    static Tower tower;
    static JTextArea textArea;
    static AbstractPlayer player = new Warrior("Katerina");
    static playerObserver playerData = new playerObserver(player);


    public Window() {
        super("GridLayoutTest");
        generateWindow();
    }

    private void generateWindow() {
        // the frame that contains the components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // set the size of the frame
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        // set the rows and cols of the grid, as well the distances between them
        GridLayout grid = new GridLayout();
        // what layout we want to use for our frame
        setLayout(grid);

        // add Map
        map = new JLabel();
        map.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        tower = new Tower(player);
        floorBufferedImage = tower.getCurrentFloor();

        //create PlayerInfo panel
        JPanel PlayerInfo = new JPanel(new BorderLayout(4, 4));

        playerData.setHorizontalAlignment(SwingConstants.LEFT);
        playerData.setVerticalAlignment(SwingConstants.CENTER);
//        player.register(playerData);
        PlayerInfo.add(playerData);

        player.notifyObserver(playerData);

        //split screen in right and left
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPanel = new JPanel(new BorderLayout(4, 4));
        map.setVerticalAlignment(JLabel.NORTH);
        leftPanel.add(map);
        leftPanel.setFocusable(true);
        leftPanel.addKeyListener(mapKeyListener());
        verticalSplitPane.setLeftComponent(leftPanel);
        verticalSplitPane.setRightComponent(PlayerInfo);
        verticalSplitPane.setDividerLocation(BUFFERED_IMG_WIDTH + 1);
        verticalSplitPane.setEnabled(false);
        verticalSplitPane.setDividerSize(1);
        add(verticalSplitPane);

        //split left side in top/bottom

        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        horizontalSplitPane.setDividerLocation(BUFFERED_IMG_HEIGHT + 1);
        horizontalSplitPane.setEnabled(false);
        horizontalSplitPane.setDividerSize(1);

        horizontalSplitPane.setTopComponent(map);

        textArea = new JTextArea();
        textArea.setText("Game_Story");
        textArea.setEditable(false);
        textArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        horizontalSplitPane.setBottomComponent(scrollPane);
        leftPanel.add(horizontalSplitPane, BorderLayout.CENTER);
        floorBufferedImage.drawFloor(player);
        map.setIcon(floorBufferedImage.getIcon());
        leftPanel.revalidate();
        leftPanel.repaint();
        setVisible(true);
    }

    public KeyListener mapKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(player.getCurrentHitPoints() > 0) {
                    switch (e.getKeyChar()) {
                        case 'a':
                        case 'A':
                            changePosition(-1, 0);
                            break;
                        case 'd':
                        case 'D':
                            changePosition(1, 0);
                            break;
                        case 'W':
                        case 'w':
                            changePosition(0, -1);
                            break;
                        case 'S':
                        case 's':
                            changePosition(0, 1);
                            break;
                        case 'P':
                        case 'p':
                            swapWeapon();
                            break;
                        case 'H':
                        case 'h':
                            player.useHealthPotion();
                            break;
                        case 'M':
                        case 'm':
                            player.useManaPotion();
                            break;
                        case 'L':
                        case 'l':
                            playerAttack();
                            break;
                        case 'R':
                        case 'r':
                            player.rest();
                            break;
                        default:
                            break;
                    }
                }
                else{
                    setVisible(false); //you can't see me!
                    dispose();
                }

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };
    }

    public void changePosition(int x, int y) {
        AbstractBlock tempTile;
        var newPosition = new Coordinates(floorBufferedImage.getEntityCoordinates(player).getCoordinateX() + x, floorBufferedImage.getEntityCoordinates(player).getCoordinateY() + y);
        ArrayList<AbstractBlock> listOfEnemyTiles = new ArrayList<>();
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
                        }
                    }
                    List<AbstractBlock> movableEnemiesList = defineEnemiesWithinEnemyVisibilityRadius(floorBufferedImage.getListOfFloorEnemyTiles(), player);
                    changeEnemyPosition(movableEnemiesList);
                    if (calculateProbability(player.getCurrentHitPoints(), player.getMaxHP())) {
                        AbstractEnemy enemy = Generator.generateEnemy(player.getLevel(player.getExperiencePoints()));
                        AbstractBlock enemyTile = calculateEnemyCoordinates(player, enemy);
                        enemyTile.setOccupant(enemy);

                        listOfEnemyTiles.add(enemyTile);

                        System.out.println(enemyTile);
                    }
                }

                floorBufferedImage.drawFloor(player);
                map.setIcon(floorBufferedImage.getIcon());
                leftPanel.revalidate();
                leftPanel.repaint();
            }
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
                textArea.append("\n " + player.playerStats());
                boolean isEquipped = false;
                Item tempItem = null;
                for (Slot slot : player.getPlayerSlotList()) {
                    if (!player.getEquippedItems().isEmpty()) {
                        tempItem = player.getEquippedItems().get(0);
                        slot.remove(tempItem);
                        player.drop(tempItem);

                    }
                    if ((((Equippable) tempTile.getItem()).getSlotType() == slot.getSlotType())) {
                        slot.equip(tempTile.getItem());
                        player.notifyObserver(playerData);
                        isEquipped = true;
                        break;
                    }
                }
                if (!isEquipped) {
                    textArea.append("\n Item doesn't fit in Player's Slot ");
                }
                tempTile.setItem(tempItem);
                textArea.append("\n " + player.playerStats());
            }
        }
    }

    public boolean calculateProbability(int hp, int hpMax) {
        double ratio = (( (double)hp / hpMax) * 0.8);
        if ( Math.random() <= ratio )
            return true;
        return false;
    }

    public AbstractBlock calculateEnemyCoordinates(AbstractPlayer player, AbstractEnemy enemy) {
        int maxRadiusforEnemyCircle = Math.max(player.getPlayerVisibility(), enemy.getVisibilityRadius()) + 1;
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
        System.out.println(possibleEnemyTilesList);
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
                        player.getPlayerVisibility() * player.getPlayerVisibility()) {
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
        if (playerCoordinates != null) {
            for (AbstractBlock enemyTile : movableEnemiesList) {
                ArrayList<Coordinates> neighborCoordinatesList = new ArrayList<>();
                if(isMovementValid(enemyTile.getCoordinates().getCoordinateX() - 1, enemyTile.getCoordinates().getCoordinateY(), enemyTile.getOccupant())) {
                    var westNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX() - 1, enemyTile.getCoordinates().getCoordinateY());
                    neighborCoordinatesList.add(westNeighbor);
                }
                if((isMovementValid(enemyTile.getCoordinates().getCoordinateX() + 1, enemyTile.getCoordinates().getCoordinateY(), enemyTile.getOccupant() ))) {
                    var eastNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX() + 1, enemyTile.getCoordinates().getCoordinateY());
                    neighborCoordinatesList.add(eastNeighbor);
                }
                if(isMovementValid(enemyTile.getCoordinates().getCoordinateX(), enemyTile.getCoordinates().getCoordinateY() - 1, enemyTile.getOccupant())) {
                    var northNeighbor = new Coordinates(enemyTile.getCoordinates().getCoordinateX(), enemyTile.getCoordinates().getCoordinateY() - 1);
                    neighborCoordinatesList.add(northNeighbor);
                }
                if(isMovementValid(enemyTile.getCoordinates().getCoordinateX(), enemyTile.getCoordinates().getCoordinateY() + 1, enemyTile.getOccupant() )) {
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
        if (player instanceof Warrior) {
            if (playerCoordinates != null) {
                List<AbstractBlock> enemyNeighborTiles = floorBufferedImage.getListOfFloorEnemyTiles().stream().filter(e ->
                        calculateMinimumDistance(playerCoordinates.getCoordinateX(), e.getCoordinates().getCoordinateX(), playerCoordinates.getCoordinateY(), e.getCoordinates().getCoordinateY()) == 1).collect(Collectors.toList());
                Collections.sort(enemyNeighborTiles, new Comparator<AbstractBlock>() {
                    @Override
                    public int compare(AbstractBlock abstractBlock1, AbstractBlock abstractBlock2) {
                        return ((AbstractEnemy) abstractBlock1.getOccupant()).getHitPoints() - ((AbstractEnemy) abstractBlock2.getOccupant()).getHitPoints();
                    }
                });
                if( enemyNeighborTiles.size() != 0) {
                    engageBattle(enemyNeighborTiles);
                }
            }
        } else if (player instanceof Wizard && player.getCurrentManaPoints() >= 5 && playerCoordinates != null) {
            player.setCurrentManaPoints(player.getCurrentManaPoints() - 5);
            List<AbstractBlock> attackableEnemiesList = defineEnemiesWithinPlayerVisibilityRadius(floorBufferedImage.getListOfFloorEnemyTiles(), player);
            Collections.sort(attackableEnemiesList, Comparator.comparingDouble((AbstractBlock a) -> calculateMinimumDistance(playerCoordinates.getCoordinateX(), a.getCoordinates().getCoordinateX(), playerCoordinates.getCoordinateY(), a.getCoordinates().getCoordinateY())));
            if( attackableEnemiesList.size() != 0) {
                engageBattle(attackableEnemiesList);
            }
        }
    }

    public void enemyAttack(List<AbstractBlock> attackableEnemiesList) {
        for(AbstractBlock enemyTile : attackableEnemiesList) {
            if(player.getCurrentHitPoints() - ((AbstractEnemy)enemyTile.getOccupant()).getWeapon().hitDamageWeapon() > 0)
                player.setCurrentHitPoints(player.getCurrentHitPoints() - ((AbstractEnemy)enemyTile.getOccupant()).getWeapon().hitDamageWeapon());
            else{
                player.setCurrentHitPoints(0);
                textArea.append("You died");
            }
        }
    }

    public void engageBattle(List<AbstractBlock> attackableEnemiesList){
        AbstractBlock enemyTile =  attackableEnemiesList.stream().findFirst().orElse(null);
        AbstractEnemy enemyToAttack = (AbstractEnemy) attackableEnemiesList.stream().findFirst().orElse(null).getOccupant();
        System.out.println("HP before"+enemyToAttack.getHitPoints());
        System.out.println(attackableEnemiesList);
        enemyToAttack.setHitPoints(enemyToAttack.getHitPoints() - player.getAttackDamage());
        if(enemyToAttack.getHitPoints() <= 0)
        {

            enemyTile.setOccupant(null);
            System.out.println("xp points before enemy dies "+player.getExperiencePoints());
            player.addXP(enemyToAttack.getEnemyXP());
            attackableEnemiesList.remove(enemyTile);
            System.out.println(attackableEnemiesList);
            System.out.println("xp points after enemy dies "+player.getExperiencePoints());
            System.out.println("player level after enemy dies "+player.getLevel(player.getExperiencePoints()));
            floorBufferedImage.drawFloor(player);
            map.setIcon(floorBufferedImage.getIcon());
            leftPanel.revalidate();
            leftPanel.repaint();
        }

        enemyAttack(attackableEnemiesList);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var window = new Window();

        });
    }
}


