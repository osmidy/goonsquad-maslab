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
    
    private final Color color;
    
    public Cube(int x, int y, Color color) {
        height = 2;
        width = 2;
        depth = 2;
        
        this.x = x;
        this.y = y;
        this.color = color;
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
    public Color getColor() {
        return this.color;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

}
