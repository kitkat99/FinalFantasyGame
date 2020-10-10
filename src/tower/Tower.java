package tower;


import enemies.Dragon;
import generator.Generator;
import items.Item;
import player.AbstractPlayer;


import java.util.ArrayList;

import java.util.List;

import static generator.Generator.*;

public class Tower {
    private static final int numberOfFloors = 10;
    List<Floor> floors = new ArrayList<>();
private AbstractPlayer player;
    Floor currentFloor;
    Dragon dragon = new Dragon();

    public static int getNumberOfFloors() {
        return numberOfFloors;
    }

    public Floor getNextFloor() {
        var listIterate = floors.listIterator();
        if (listIterate.hasNext()) {
            return listIterate.next();
        }
        return null;
    }

    public Floor getPreviousFloor() {
        var listIterate = floors.listIterator();
        if (listIterate.hasPrevious()) {
            return listIterate.previous();
        }
        return null;
    }

    private void generateFloors() {
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i));
        }
    }

    public List<Floor> getFloors() {
        return floors;
    }


    public Floor getCurrentFloor() {
        return floors.get(floors.indexOf(currentFloor));
    }

    public int getFloorNumber(Floor floor) {
        return floors.indexOf(floor);
    }

    public void setCurrentFloor(Floor tmpFloor) {
        currentFloor = floors.get(floors.indexOf(tmpFloor));
    }

    public void goToPreviousFloor() {
        try {
            if (floors.indexOf(currentFloor) == 0) {
                throw new IndexOutOfBoundsException();
            }

        } catch (Exception e) {
            System.out.println("there is nothing below");
        }
        var tempFloor = floors.get(floors.indexOf(currentFloor));
        tempFloor.getListOfFloorEnemyTiles().stream().filter(x -> !(x.getOccupant() instanceof Dragon)).forEach(e -> e.setOccupant(null));
        setCurrentFloor(floors.get(floors.indexOf(tempFloor) - 1));

    }

    public void setDragon()
    {

            int currentX = floors.get(9).randomgenenerator((int) (floors.get(9).X_DIMENSION - 1));
            int currentY = floors.get(9).randomgenenerator((int) (floors.get(9).Y_DIMENSION - 1));
            AbstractBlock tempBlock = floors.get(9).getFloor()[currentX][currentY];
            while(!(tempBlock instanceof Tile) && tempBlock.isOccupiedByEnemy()){
                currentX = floors.get(9).randomgenenerator((int) (floors.get(9).X_DIMENSION - 1));
                currentY = floors.get(9).randomgenenerator((int) (floors.get(9).Y_DIMENSION - 1));
                tempBlock = floors.get(9).getFloor()[currentX][currentY];
            }
        floors.get(9).setEntityCoordinates(currentX,currentY, dragon);

    }

    public void goToNextFloor() {
        try {
            if (floors.indexOf(currentFloor) == 9) {
                throw new IndexOutOfBoundsException();
            }

        } catch (Exception e) {
            System.out.println("there is nothing above");
        }
        var tempFloor = floors.get(floors.indexOf(currentFloor));
        tempFloor.getListOfFloorEnemyTiles().stream().filter(x -> !(x.getOccupant() instanceof Dragon)).forEach(e -> e.setOccupant(null));
        setCurrentFloor(floors.get(floors.indexOf(tempFloor) + 1));
        if(getFloorNumber(currentFloor) == 9){
            setDragon();
        }
    }

    public Tower(AbstractPlayer player) {
        generateFloors();
        currentFloor = floors.get(0);
        currentFloor.getEntranceBlock().setOccupant(player);
        this.player = player;
        generateItems();
        generateTraps();
        setDragon();
    }

    public void generateItems() {
        for (Floor floor : floors) {
            floor.setListOfFloorItems(createItemList(getFloorNumber(floor), player));
        }
    }
    public void generateTraps() {
        for (Floor floor : floors) {
            floor.setListOfFloorItems(Generator.generateTraps());
        }
    }
}