package fieldobject;

/**
 * An interface providing methods for retrieving the specifications for the objects 
 * on the playing field that the robot will be interacting with.  
 * @author George
 *
 */
public interface FieldObject {

    /** 
     * Retrieves the dimensions for the object
     * 
     * @param FieldObject
     * @return the dimensions of specified object (width, length, height)
     *
     */
    public double getDimensions();
    
    /**
     * Retrieves the color for the object
     * 
     * @param FieldObject
     * @return the color of the specified object (Red, Green, Blue)
     * 
     */
    public int getColor();
}
