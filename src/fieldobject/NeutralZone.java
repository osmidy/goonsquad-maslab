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
    private final Color color = Color.PURPLE;

    public NeutralZone(List<Integer> endpoints) {
        height = 0;
        width = 0;
        depth = 0;
        
        this.x = endpoints.get(0);
        this.y = endpoints.get(1);
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
    public int[] getDimensions() {
        int[] dimensions = new int[] {height, width, depth};
        return dimensions;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
 
    // These indicate positions of x1 and y1
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

}
