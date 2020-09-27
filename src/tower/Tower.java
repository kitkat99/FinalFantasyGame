package tower;


import player.AbstractPlayer;


import java.util.ArrayList;

import java.util.List;

import static generator.Generator.createItemList;

public class Tower {
    private static final int numberOfFloors = 10;
    List<Floor> floors = new ArrayList<>();
private AbstractPlayer player;
    Floor currentFloor;

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
        setCurrentFloor(floors.get(floors.indexOf(tempFloor) - 1));
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
        setCurrentFloor(floors.get(floors.indexOf(tempFloor) + 1));
    }

    public Tower(AbstractPlayer player) {
        generateFloors();
        currentFloor = floors.get(0);
        currentFloor.getEntranceBlock().setOccupant(player);
        this.player = player;
        generateItems();
    }

    public void generateItems() {
        for (Floor floor : floors) {
            floor.setListOfFloorItems(createItemList(getFloorNumber(floor), player));
        }
    }
}