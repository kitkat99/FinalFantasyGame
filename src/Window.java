import enemies.AbstractEnemy;
import generator.Generator;
import items.Equippable;
import items.Item;
import player.Slot;
import tower.*;
import player.AbstractPlayer;
import player.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Random;


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
        var playerData = new playerLabel();
        playerData.setHorizontalAlignment(SwingConstants.LEFT);
        playerData.setVerticalAlignment(SwingConstants.CENTER);
        playerData.setPlayer(player);
        this.addPropertyChangeListener("downloadPanel", new playerListener(playerData));

        PlayerInfo.add(playerData);


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
        floorBufferedImage.drawFloor();
        map.setIcon(floorBufferedImage.getIcon());
        leftPanel.revalidate();
        leftPanel.repaint();
        setVisible(true);
    }

    public KeyListener mapKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
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
                    default:
                        break;
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
        var newPosition = new Coordinates(floorBufferedImage.getPlayerCoordinates().getCoordinateX() + x, floorBufferedImage.getPlayerCoordinates().getCoordinateY() + y);
        if (isMovementValid(x, y)) {
            tempTile = floorBufferedImage.getFloor()[newPosition.getCoordinateX()][newPosition.getCoordinateY()];
            if (!tempTile.isWalkable() || tempTile == null) {
                return;
            } else {
                if (tempTile instanceof Exit) {
                    tower.goToNextFloor();
                    textArea.append("\nYou are now at the " + (tower.getFloors().indexOf(tower.getCurrentFloor()) + 1) + " floor");
                    floorBufferedImage = tower.getCurrentFloor();
                    floorBufferedImage.setPlayerCoordinates(floorBufferedImage.getStartx(), floorBufferedImage.getStarty(), player);
                }
                if (tempTile instanceof Entrance) {
                    tower.goToPreviousFloor();
                    textArea.append("\nYou are now at the " + (tower.getFloors().indexOf(tower.getCurrentFloor()) + 1) + " floor");
                    floorBufferedImage = tower.getCurrentFloor();
                    floorBufferedImage.setPlayerCoordinates(floorBufferedImage.getExitX(), floorBufferedImage.getExitY(), player);
                }
                if (tempTile instanceof Tile) {
                    floorBufferedImage.setPlayerCoordinates(newPosition.getCoordinateX(), newPosition.getCoordinateY(), player);
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
                    if (calculateProbability(player.getCurrentHitPoints(), player.getMaxHP())) {
                        AbstractEnemy enemy = Generator.generateEnemy(player.getLevel(player.getExperiencePoints()));
                        AbstractBlock enemyTile = calculateEnemyCoordinates(player, enemy);
                        enemyTile.setOccupant(enemy);
                        System.out.println(enemyTile);
                    }
                }

                floorBufferedImage.drawFloor();
                map.setIcon(floorBufferedImage.getIcon());
                leftPanel.revalidate();
                leftPanel.repaint();
            }
        }
    }

    public boolean isMovementValid(int x, int y) {
        var isLegalXMovement = Math.abs(floorBufferedImage.getPlayerCoordinates().getCoordinateX() + x) < (int) floorBufferedImage.getX_DIMENSION() &&
                Math.abs(floorBufferedImage.getPlayerCoordinates().getCoordinateX() - x) > 0;
        var isLegalYMovement = Math.abs(floorBufferedImage.getPlayerCoordinates().getCoordinateY() + y) < (int) floorBufferedImage.getY_DIMENSION() &&
                Math.abs(floorBufferedImage.getPlayerCoordinates().getCoordinateY() - y) > 0;
        return isLegalXMovement && isLegalYMovement;
    }


    public void swapWeapon() {
        AbstractBlock tempTile;
        var newPosition = new Coordinates(floorBufferedImage.getPlayerCoordinates().getCoordinateX(), floorBufferedImage.getPlayerCoordinates().getCoordinateY());
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
        if (new Random().nextDouble() <= (hp / hpMax) * 0.2)
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
        Coordinates playerCoordinates = tower.getCurrentFloor().getPlayerCoordinates();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var window = new Window();

        });
    }
}

class playerLabel extends JLabel {
    private AbstractPlayer player;

    public void setPlayer(AbstractPlayer player) {
        this.player = player;
        setText(player == null ? null : player.playerStats());
    }

    public AbstractPlayer getPlayer() {
        return player;
    }
}

class playerListener implements PropertyChangeListener {
    private playerLabel playerLabel;

    public playerListener(playerLabel playerLabel) {
        this.playerLabel = playerLabel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent player) {
        AbstractPlayer value = (AbstractPlayer) player.getNewValue();
        playerLabel.setPlayer(value);
    }
}