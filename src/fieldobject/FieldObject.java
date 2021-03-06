package fieldobject;

import java.util.List;

/**
 * An interface representing the physical playing field that a Robot will be
 * interacting with. Dimensions for FieldObjects are given in inches, unless
 * specified otherwise.
 * 
 * @author George
 *
 */
public interface FieldObject {

    public enum Color {
        RED, GREEN, BLUE, YELLOW, PURPLE
    }
    
    /**
     * Covnert measurements from inches to meters.
     * 
     * @param measurement
     *            a double, given in inches
     * @return the value of measurement in meters
     */
    public double toMeters(double measurement);

    /**
     * @return the height of this FieldObject
     *
     */
    public double getHeight();

    /**
     * @return the width of this FieldObject
     *
     */
    public double getWidth();

    /**
     * @return the depth of this FieldObject
     *
     */
    public double getDepth();

    /**
     * @return a List of the dimensions of this FieldObject, in the order
     *         <height, width, depth>.
     */
    public int[] getDimensions();
    
    /**
     * @return the x-coordinate for this field object
     */
    public int getX();
    
    /**
     * @return the y-coordinate for this field object
     */
    public int getY();

    /**
     * @return the Color of this FieldObject
     */
    public Color getColor();
}
