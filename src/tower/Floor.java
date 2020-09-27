package tower;


import Entity.Entity;
import items.AbstractWeapon;
import items.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Floor {
    static int BUFFERED_IMG_WIDTH = 640;
    static int BUFFERED_IMG_HEIGHT = 480;
    double X_DIMENSION = 80;
    double Y_DIMENSION = 60;
    double percentage = 0.75;
    private AbstractBlock[][] floor;
    private BufferedImage image;
    private Graphics2D g2;
    private int starty;
    private int startx;
    private int exitX;
    private int exitY;
    final int TILE_SIZE = 8;
    private int FloorCounter;
    private final int MAX_FLOOR = 9;
    private List<Item> listOfFloorItems;

    public void setListOfFloorItems(List<Item> listOfFloorItems) {
        this.listOfFloorItems = listOfFloorItems;
        setItemCoordinates();
    }

    public List<Item> getListOfFloorItems() {
        return listOfFloorItems;
    }

    public void setItemCoordinates() {
        for(Item item : listOfFloorItems) {
            int currentX = randomgenenerator((int) (X_DIMENSION - 1));
            int currentY = (randomgenenerator((int) (Y_DIMENSION - 1)));
            AbstractBlock tempBlock = floor[currentX][currentY];
            while(tempBlock instanceof Wall || tempBlock.hasItem() == true){
                currentX = randomgenenerator((int) (X_DIMENSION - 1));
                currentY = (randomgenenerator((int) (Y_DIMENSION - 1)));
                tempBlock = floor[currentX][currentY];
            }
            tempBlock.setItem(item);
        }
    }


    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public Coordinates getPlayerCoordinates() {
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                if (floor[i][j].isOccupiedByPlayer()) {
                    System.out.println(i + " " + j + "is occupied");
                    return floor[i][j].getCoordinates();
                }
            }
        }
        return null;
    }

    public void setPlayerCoordinates(int x, int y, Entity player) {
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                if (floor[i][j].isOccupiedByPlayer()) {
                    System.out.println(i + " " + j + "is occupied");
                    floor[i][j].setOccupant(null);
                }
            }
        }
        floor[x][y].setOccupant(player);
    }

    public AbstractBlock getEntranceBlock() {
        return floor[getStartx()][getStarty()];
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public void setExitX(int exitX) {
        this.exitX = exitX;
    }

    public void setExitY(int exitY) {
        this.exitY = exitY;
    }

    public AbstractBlock[][] getFloor() {
        return floor;
    }


    public Floor(int FloorLevel) {
        image =
                new BufferedImage(
                        BUFFERED_IMG_WIDTH,
                        BUFFERED_IMG_HEIGHT,
                        BufferedImage.TYPE_INT_BGR
                );
        g2 = (Graphics2D) image.getGraphics();
        FloorCounter = FloorLevel;
        generateFloor();
        addExit();
        drawFloor();
    }


    public void drawFloor() {
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[j].length; j++) {
                if (floor[i][j] instanceof Wall) {
                    drawWall(i, j);
                } else {
                    if (floor[i][j] instanceof Entrance) {
                        drawEntrance(i, j);
                    } else if (floor[i][j] instanceof Tile) {
                        drawTile(i, j);
                        if(floor[i][j].hasItem() == true){
                            drawItem(i, j);
                        }
                    } else if (floor[i][j] instanceof Exit) {
                        drawExit(i, j);
                    }
                    if (floor[i][j].isOccupiedByPlayer()) {
                        drawPlayer(i, j);
                    }

                }

            }
        }
    }

    private void drawTile(int i, int j) {
        g2.setColor(new Color(100, 60, 40));
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g2.setColor(new Color(180, 100, 100));
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
    }

    private void drawWall(int i, int j) {
        g2.setColor(new Color(100, 60, 40));
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
    private void drawItem(int i, int j) {
        g2.setColor(new Color(30, 250, 30));
        g2.fillRect(i * TILE_SIZE+TILE_SIZE-3, j * TILE_SIZE, 4, 4);
    }

    private void drawPlayer(int i, int j) {
        g2.setColor(new Color(30, 200, 200));
        g2.fillArc(i * TILE_SIZE + 1, j * TILE_SIZE + 1,
                TILE_SIZE - 3, TILE_SIZE - 3, 0, 360);
    }

    private void drawEntrance(int i, int j) {
        g2.setColor(Color.WHITE);
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawExit(int i, int j) {
        g2.setColor(new Color(251, 255, 51));
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public void generateFloor() {
        int currentx;
        int currenty;


        var filled = 0;
        floor = new AbstractBlock[(int) X_DIMENSION][(int) Y_DIMENSION];
        setStartx(randomgenenerator((int) (X_DIMENSION - 1)));
        setStarty(randomgenenerator((int) (Y_DIMENSION - 1)));

        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                floor[i][j] = new Wall(new Coordinates(i, j));
            }
        }
        if (FloorCounter != 0) {
            floor[getStartx()][getStarty()] = new Entrance(new Coordinates(getStartx(), getStarty()));
        } else {
            floor[getStartx()][getStarty()] = new Tile(new Coordinates(getStartx(), getStarty()));
        }
        filled = 1;

        while (filled / (X_DIMENSION * Y_DIMENSION) < percentage) {
            currentx = randomgenenerator((int) (X_DIMENSION - 1));
            currenty = randomgenenerator((int) (Y_DIMENSION - 1));
            if (floor[currentx][currenty] instanceof Wall) {
                floor[currentx][currenty] = new Tile(new Coordinates(currentx, currenty));
                filled = filled + 1;
            }
        }
//        print2D(floor);
    }

    public void addExit() {
        if (FloorCounter < MAX_FLOOR) {
            exitX = randomgenenerator((int) (X_DIMENSION - 1));
            exitY = randomgenenerator((int) (Y_DIMENSION - 1));
            floor[exitX][exitY] = new Exit(new Coordinates(exitX, exitY));
        }
    }

    public double getX_DIMENSION() {
        return X_DIMENSION;
    }

    public double getY_DIMENSION() {
        return Y_DIMENSION;
    }

    public void print2D(String mat[][]) {
        // Loop through all rows
        for (String[] row : mat)
            System.out.println(Arrays.toString(row)); // and then printing in a separate line // converting each row as string
    }

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }

    public int randomgenenerator(int upperBound) {
        return (int) (Math.random() * ((upperBound) + 1));
    }
}