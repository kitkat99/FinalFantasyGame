package tower;

public class Coordinates {
    private int CoordinateX;
    private int CoordinateY;

    public int getCoordinateX() {
        return CoordinateX;
    }

    public int getCoordinateY() {
        return CoordinateY;
    }

    public void setCoordinateX(int coordinateX) {
        CoordinateX = coordinateX;
    }

    public void setCoordinateY(int coordinateY) {
        CoordinateY = coordinateY;
    }

    public Coordinates(int X, int Y) {
        CoordinateX = X;
        CoordinateY = Y;
    }
}