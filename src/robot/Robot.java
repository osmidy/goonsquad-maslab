package robot;

import computervision.Sensor;

/**
 * An interface providing methods to dictate mechanical operations of for
 * physical parts of a robot. A physical robot exists on a specified playing
 * field containing walls and blocks of varying colors.
 * @author osmidy
 *
 */
public interface Robot {
    // Datatype Definition: Robot ::= sensors:List<Sensor> +

    /**
     * Determines the direction of a Sensor on the Robot.
     * 
     * @param sensor
     *            A Sensor mounted on the robot
     * @return the heading, in degrees, of the sensor. Angles are measured
     *         counterclockwise relative to the axis passing through the center
     *         of the Robot's face.
     */
    public double getSensorDirection(Sensor sensor);

    /**
     * Calls the mechanical action to be taken by the robot part
     */
    public void action();

    /**
     * Determines the object closest to this Robot
     * 
     * @param sensor
     *            Sensors used to determine distance to objects on the playing
     *            field
     * @return the distance from this Robot to the closest object in meters
     */
    public double distanceToClosestObject();
    
    /**
     * Searches for a Cube on the playing field
     * 
     * @param sensors
     * @return Cube
     */
    //public Cube findCube(List<Sensor> sensors);

    /**
     * Moves towards a Cube on the playing field in order to collect it
     * 
     * @param Cube
     */
    //public void getCube(Cube cube);
}


