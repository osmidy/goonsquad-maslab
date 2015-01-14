package fieldobject;

import java.util.Arrays;
import java.util.List;

/**
 * A representation of a wall on the physical playing field.
 * Dimensions and materials match those specified in the game rules.
 * @author osmidy
 *
 */
public class Wall implements FieldObject {
    private final int height;
    private final int width;
    private final int depth;
    // Location on field
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    
    // TODO:  Finish Gyro C++; make file parser (parseItOut); Add location to FieldObjects; make Stack class (FieldObject?)
    public Wall(int x1, int y1, int x2, int y2) {
        height = 6;
        width = 24; // On map, each unit of wall covers 2 ft
        depth = 1;  // Won't be used for walls
        
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        return metric;
    }

    @Override
    public double getHeight() {
        return width;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getDepth() {
        return depth;
    }

    @Override
    public List<Integer> getDimensions() {
        List<Integer> dimensions = Arrays.asList(height, width, depth);
        return dimensions;
    }

    @Override
    public void setMaterial() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getColor() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getY() {
        // TODO Auto-generated method stub
        return 0;
    }

}
