package fieldobject;

import java.util.Arrays;
import java.util.List;

/**
 * A representation of a neutralization zone on the physical playing field.
 * Dimensions and materials match those specified in the game rules.
 * 
 * @author osmidy
 *
 */
public class NeutralZone implements FieldObject {
    private int height;
    // These not final; will change based camera readings.
    private int width;
    private final int depth;
    // Location on field
    private final int x;
    private final int y;

    public NeutralZone(int x, int y) {
        height = 0;
        width = 0;
        depth = 0;
        
        this.x = x;
        this.y = y;
    }

    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        return metric;
    }

    /**
     * Sets the 
     * 
     * @return
     */

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
