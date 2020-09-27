import tower.*;
import player.AbstractPlayer;
import player.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


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
    static AbstractPlayer player = new Warrior("balls");

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
        var playerData = new JLabel();
        playerData.setHorizontalAlignment(SwingConstants.LEFT);
        playerData.setVerticalAlignment(SwingConstants.CENTER);
        playerData.setText("PlayerInfo");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var window = new Window();

        });
    }
}