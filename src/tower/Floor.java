package tower;


import Entity.Entity;
import enemies.AbstractEnemy;
import items.Item;
import items.Trap;
import states.Unknown;
import states.Visible;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

    public void defineBlockState(AbstractBlock block){
        if(block.isVisible){
            block.setStateBlock(block.getVisible());
        }
        else if(block.hasBeenStepped && !block.isVisible){
            block.setStateBlock(block.getFogged());
        }
        else if(!block.hasBeenStepped){
            block.setStateBlock(block.getUnknown());
        }
    }


    public int getStartx() {
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public Coordinates getEntityCoordinates(Entity entity) {
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                if (floor[i][j].getOccupant() != null && floor[i][j].getOccupant().equals(entity)) {
                    return floor[i][j].getCoordinates();
                }
            }
        }
        return null;
    }

    public void setEntityCoordinates(int x, int y, Entity entity) {
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                if (floor[i][j].getOccupant() != null && floor[i][j].getOccupant().equals(entity)) {
                    floor[i][j].setOccupant(null);
                }
            }
        }
        floor[x][y].setOccupant(entity);
    }

    public ArrayList<AbstractBlock> getListOfFloorEnemyTiles() {
        ArrayList<AbstractBlock> listOfFloorEnemyTiles = new ArrayList<>();
        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[0].length; j++) {
                if (floor[i][j].getOccupant() != null && floor[i][j].getOccupant() instanceof AbstractEnemy) {
                    listOfFloorEnemyTiles.add( floor[i][j]);
                }
            }
        }
        return listOfFloorEnemyTiles;
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
        drawFloor(null);
    }


    public void drawFloor(Entity entity) {

        for (int i = 0; i < floor.length; i++) {
            for (int j = 0; j < floor[j].length; j++) {
                if(entity != null) {
                    if ((Math.abs(getEntityCoordinates(entity).getCoordinateX() - floor[i][j].getCoordinates().getCoordinateX())) +
                            ((Math.abs(getEntityCoordinates(entity).getCoordinateY() - floor[i][j].getCoordinates().getCoordinateY()))) < 6) {
                        floor[i][j].isVisible = true;
                    }
                    else if((Math.abs(getEntityCoordinates(entity).getCoordinateX() - floor[i][j].getCoordinates().getCoordinateX())) +
                            ((Math.abs(getEntityCoordinates(entity).getCoordinateY() - floor[i][j].getCoordinates().getCoordinateY()))) > 6 && floor[i][j].isVisible == true){
                        floor[i][j].hasBeenStepped = true;
                        floor[i][j].isVisible = false;
                    }
                }
                defineBlockState(floor[i][j]);
                if (!(floor[i][j].getStateBlock() instanceof Unknown)) {
                    if (floor[i][j] instanceof Wall) {
                        drawWall(i, j);
                    } else {
                        if (floor[i][j] instanceof Entrance) {
                            drawEntrance(i, j);
                        } else if (floor[i][j] instanceof Tile) {
                            drawTile(i, j, floor[i][j].getStateBlock().stateColor());

                            if (floor[i][j].hasItem() == true && !(floor[i][j].getItem() instanceof Trap)) {
                                drawItem(i, j);
                            }
                            if (floor[i][j].hasItem() == true && (floor[i][j].getItem() instanceof Trap)) {
                                drawTrap(i, j, floor[i][j].getStateBlock().stateColor());
                            }
                            if (floor[i][j].isOccupiedByEnemy() == true) {
                                drawEnemy(i, j, (AbstractEnemy) floor[i][j].getOccupant());
                            }
                        } else if (floor[i][j] instanceof Exit) {
                            drawExit(i, j);
                        }
                        if (floor[i][j].isOccupiedByPlayer()) {
                            drawPlayer(i, j);
                        }

                    }

                }
                else{
                    g2.setColor(floor[i][j].getStateBlock().stateColor());
                    g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
//
                }
            }
        }
    }

    private void drawTile(int i, int j, Color color) {
        g2.setColor(new Color(100, 60, 40));
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g2.setColor(color);
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
    private void drawTrap(int i, int j, Color color) {
        g2.setColor(color);
        g2.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
    }
    private void drawEnemy(int i, int j, AbstractEnemy enemy) {
        g2.setColor(enemy.getColor());
        g2.fillArc(i * TILE_SIZE + 1, j * TILE_SIZE + 1,
                TILE_SIZE - 3, TILE_SIZE - 3, 0, 360);
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

    public ImageIcon getIcon() {
        return new ImageIcon(image);
    }

    public int randomgenenerator(int upperBound) {
        return (int) (Math.random() * ((upperBound) + 1));
    }

    public void calculateVisibleTiles(Entity entity) {
        ArrayList<AbstractBlock> visibleTilesList = new ArrayList<AbstractBlock>();
        if(getEntityCoordinates(entity) != null) {
            for (int i = 0; i < floor.length; i++) {
                for (int j = 0; j < floor[j].length; j++) {
                    if ((Math.abs(getEntityCoordinates(entity).getCoordinateX() - floor[i][j].getCoordinates().getCoordinateX())) +
                            ((Math.abs(getEntityCoordinates(entity).getCoordinateY() - floor[i][j].getCoordinates().getCoordinateY()))) < 6) {
                        floor[i][j].isVisible = true;
                    }
                }
            }
        }
    }
}