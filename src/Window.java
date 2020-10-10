import Entity.Entity;
import GameState.GameState;
import enemies.AbstractEnemy;
import enemies.Dragon;
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

import static generator.Generator.generatePotions;


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
    static AbstractPlayer player;
    static playerObserver playerData;
    GameState gameState;


    public Window(String instance, String name) {
        super("Rogue Game");
        player = decideInstancePlayer(instance, name);
        generateWindow();
        gameState = new GameState(player,floorBufferedImage,tower,textArea, playerData, leftPanel, map);
    }

    public static AbstractPlayer decideInstancePlayer(String instance, String name) {
        if (instance.equalsIgnoreCase("wizard")) {
            return new Wizard(name);
        } else if (instance.equalsIgnoreCase("warrior"))
            return new Warrior(name);
        return null;
    }

    private void generateWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        GridLayout grid = new GridLayout();
        setLayout(grid);

        map = new JLabel();
        map.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        tower = new Tower(player);
        floorBufferedImage = tower.getCurrentFloor();

        JPanel PlayerInfo = new JPanel(new BorderLayout(4, 4));
        playerData = new playerObserver(player);
        playerData.setHorizontalAlignment(SwingConstants.LEFT);
        playerData.setVerticalAlignment(SwingConstants.CENTER);
        PlayerInfo.add(playerData);

        player.notifyObserver(playerData);

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
                if (player.getCurrentHitPoints() > 0) {
                    switch (e.getKeyChar()) {
                        case 'a':
                        case 'A':
                            gameState.changePosition(-1, 0);
                            break;
                        case 'd':
                        case 'D':
                            gameState.changePosition(1, 0);
                            break;
                        case 'W':
                        case 'w':
                            gameState.changePosition(0, -1);
                            break;
                        case 'S':
                        case 's':
                            gameState.changePosition(0, 1);
                            break;
                        case 'P':
                        case 'p':
                            gameState.swapWeapon();
                            break;
                        case 'H':
                        case 'h':
                            gameState.useHealth();
                            break;
                        case 'M':
                        case 'm':
                            gameState.useMana();
                            break;
                        case 'g':
                        case 'G':
                            gameState.useMinorHealth();
                            break;
                        case 'N':
                        case 'n':
                            gameState.useMinorMana();
                            break;
                        case KeyEvent.VK_SPACE:
                            gameState.playerAttack();
                            break;
                        case 'R':
                        case 'r':
                            gameState.rest();
                            break;
                        default:
                            break;
                    }
                } else {
                    System.out.println("\n You died");
                    System.exit(0);
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



    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Missing arguments");
            System.out.println("Please run as: java rogue.Game " +
                    " player-class player-name");
            System.out.println(" where player-class is either " +
                    "\"wizard\" or \"warrior\"");
            System.out.println(" and player-name is the " +
                    "character name");
            System.exit(0);
        }
        SwingUtilities.invokeLater(new windowThread(args[0], args[1]));
    }

    private static class windowThread extends Thread {
        String instance;
        String name;

        public windowThread(String instance, String name) {
            this.instance = instance;
            this.name = name;
        }

        @Override
        public void run() {
            var window = new Window(instance, name);
        }
    }
}



