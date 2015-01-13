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
    public List<Double> getDimensions();

    /**
     * Assigns a lighting profile identifying the Material making up this
     * FieldObject.
     * 
     * @param TBD
     */
    public void setMaterial();

    /**
     * @return the rgb color of this FieldObject
     */
    // TODO: rgb or hex?
    public int getColor();
}