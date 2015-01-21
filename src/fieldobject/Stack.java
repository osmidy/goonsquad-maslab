package fieldobject;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a stack of cubes on the playing field. Dimensions and materials
 * are as specified by the game rules.
 * 
 * @author osmidy
 *
 */
public class Stack implements FieldObject {
    private final int height;
    private final int width;
    private final int depth;
    // Location on field
    private final int x;
    private final int y;
    // Cubes in stack
    private final Cube bottomCube;
    private final Cube middleCube;
    private final Cube topCube;
    private final Color bottomColor;
    
    public Stack(int x, int y, Cube bottomCube, Cube middleCube, Cube topCube) {
        this.height = 6;
        this.width = 2;
        this.depth = 2;
        
        this.x = x;
        this.y = y;
        
        this.bottomCube = bottomCube;
        this.middleCube = middleCube;
        this.topCube = topCube;
        
        bottomColor = bottomCube.getColor();
    }
    
    
    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        return metric;
    }
    @Override
    public double getHeight() {
        return height;
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
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    
    // Returns color of bottom cube (retrieved first)
    @Override
    public Color getColor() {
        return bottomColor;
    }

}
