package fieldobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A representation of a cube on the physical playing field. Dimensions and
 * materials match those specified in the game rules.
 * 
 * @author osmidy
 *
 */
public class Cube implements FieldObject {
    private final int height;
    private final int width;
    private final int depth;
    // Location on field
    private final int x;
    private final int y;
    
    public Cube(int x, int y) {
        height = 2;
        width = 2;
        depth = 2;
        
        this.x = x;
        this.y = y;
    }

    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        return metric;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getDepth() {
        return this.depth;
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
