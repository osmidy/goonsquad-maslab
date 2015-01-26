package imageprocessing;

public class ImageCube {
    private final int x;
    private final int y;
    private final double distance; // Inches
    private final double heading;

    public ImageCube(int x, int y, double distance, double heading) {
        this.x = x;
        this.y = y;
        this.distance = distance;
        this.heading = heading;
    }

    public double getHeading() {
        return heading;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
