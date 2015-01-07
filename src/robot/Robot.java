package robot;

import java.util.List;

import computervision.Sensor;

/**
 * An interface providing methods to dictate mechanical operations of for
 * physical parts of a robot. A physical robot exists on a specified playing
 * field containing walls and blocks of varying colors. With regards to
 * direction, all angles for the robot are measured counterclockwise relative to
 * the axis passing through the center of the Robot's face.
 * 
 * @author osmidy
 *
 */
public interface Robot {
    // Datatype Definition: Robot ::= sensors:List<Sensor> +

    /**
     * Determine the direction this Robot is facing
     * 
     * @param centerSensor
     * @return the heading, in radians, of the Robot
     */
    public double getHeading(Sensor centerSensor);

    /**
     * @return the current velocity of the robot, in m/s
     */
    public double getVelocity();

    /**
     * Updates the robot's velocity
     * 
     * @param TBD
     */
    public void setVelocity();

    /**
     * @return the current velocity of the robot, in rad/s
     */
    public double getAngularVelocity();

    /**
     * Updates the robot's angular velocity
     * 
     * @param TBD
     */
    public void setAngularVelocity();

    /**
     * Determines the direction of a Sensor on the Robot.
     * 
     * @param sensor
     *            A Sensor mounted on the robot
     * @return the heading, in radians, of the sensor.
     */
    public double getSensorDirection(Sensor sensor);

    /**
     * Calls the mechanical action to be taken by the robot part
     */
    // TODO: robot parts or whole robot?
    public void action();

    /**
     * Determines the object closest to this Robot
     * 
     * @param sensors
     *            Sensors used to determine distance to objects on the playing
     *            field
     * @return the distance from this Robot to the closest object in meters
     */
    public double distanceToClosestObject(List<Sensor> sensors);

    /**
     * Searches for a Cube on the playing field
     * 
     * @param sensor
     *            Sensor used to do locate Cube on the field
     * @return a Cube discovered on the playing field.
     */
//    public Cube findCube(Sensor sensors);

    /**
     * Moves towards a Cube on the playing field in order to collect it
     * 
     * @param cube
     *            the Cube on the field targeted by the robot
     */
//    public void getCube(Cube cube);
}
