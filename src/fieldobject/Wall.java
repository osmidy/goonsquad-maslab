package fieldobject;

import java.util.Arrays;
import java.util.List;

/**
 * A representation of a wall on the physical playing field. Dimensions and
 * materials match those specified in the game rules.
 * 
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
    // Assumed to be a normal wall at start
    private Color color = Color.BLUE;
    private boolean shared = false;

    // Rep Invariant: shared walls are yellow, non-shared are blue
    private void checkRep() {
        assert !(shared && color.equals(Color.BLUE));
        assert !(!shared && color.equals(Color.YELLOW));
    }

    // TODO: make file parser (parseItOut); make Stack class (FieldObject?)
    public Wall(int x1, int y1, int x2, int y2) {
        height = 6;
        width = 24; // On map, each unit of wall covers 2 ft
        depth = 1; // Won't be used for walls

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        checkRep();
    }

    @Override
    public double toMeters(double measurement) {
        double ratio = 0.0254; // .0254 m/in
        double metric = ratio * measurement;
        checkRep();
        return metric;
    }

    @Override
    public double getHeight() {
        checkRep();
        return width;
    }

    @Override
    public double getWidth() {
        checkRep();
        return width;
    }

    @Override
    public double getDepth() {
        checkRep();
        return depth;
    }

    @Override
    public int[] getDimensions() {
        int[] dimensions = new int[] {height, width, depth};
        checkRep();
        return dimensions;
    }

    @Override
    public Color getColor() {
        checkRep();
        return this.color;
    }

    /**
     * Identifies this Wall as representing a shared wall
     */
    public void markShared() {
        this.shared = true;
        this.color = Color.YELLOW;
        checkRep();
    }

    @Override
    public int getX() {
        checkRep();
        return x1;
    }

    @Override
    public int getY() {
        checkRep();
        return y1;
    }

    public int getX2() {
        checkRep();
        return x2;
    }

    public int getY2() {
        checkRep();
        return y2;
    }

}
