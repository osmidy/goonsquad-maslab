package robot;

import java.util.List;
import java.util.Map;
import java.util.Set;

import robotparts.Gyroscope;
import robotparts.Motor;
import robotparts.Servo;
import sensors.Sensor;

/**
 * An class providing methods to dictate mechanical operations of a physical
 * robot. A physical robot exists on a specified playing field containing walls
 * and blocks of varying colors. With regards to direction, all angles for the
 * robot are measured clockwise, in degrees, relative to the axis passing through the center
 * of the Robot's face.
 * 
 * @author osmidy
 *
 */
public class Robot {
    private final Set<Sensor> sensors;
    private final Map<Sensor, Double> sensorHeadings;
    private final List<Motor> motors;
    private final List<Servo> servos;
    private final Gyroscope gyro;
    private State state = FINDWALL;
    // Datatype Definition: Robot ::= sensors:Set<Sensor> + gyro:Gyroscope + motors:List<Motor> + servos:List<Servo>
    // Rep Invariant:  robot has at least two motors
    private void checkRep() {
        assert motors.size() >= 2;
    }
    
    enum State {
        WALLFOLLOW, FINDWALL, DROPSTACK, FINDDROPZONE, DRIVETOCUBE, COLLECTCUBE;
    }
    
    /**
     * Constructo method
     * 
     * @parm sensorMap a mapping of Sensors mounted on this Robot to the angle at which they are aligned
     * @param motors a List of Motor mounted on this Robot
     * @param servos a List of Servos mounted on this Robot
     * @param gyro a Gyroscope mounted on this Robot
     */
    public Robot(Map<Sensor, Double> sensorMap, List<Motor> motors, List<Servo> servos, Gyroscope gyro) {
        this.sensorHeadings = sensorMap;
        this.sensors = sensorMap.keySet();
    }
    /**
     * Determine the direction this Robot is facing
     * 
     * @param centerSensor
     *            Sensor positioned at the front of the Robot, along the axis
     *            passing through its face
     * @return the heading, in radians, of the Robot
     */
    public double getHeading(Sensor centerSensor);
    
    /**
     * Assigns directional heading to Sensors mounted on the Robot
     * @param sensor Sensor mounted on the robot
     * @param heading Heading, in radians, to be assigned to the Sensor
     */
    public void setSensorHeading(Sensor sensor, double heading);
    
    /**
     * Determine the direction a given Sensor is facing
     * 
     * @param sensor  Sensor mounted on the robot
     * @return the heading, in radians, of the Sensor
     */
    public double getSensorHeading(Sensor sensor);

    /**
     * @return the current velocity of the robot, in m/s
     */
    public double getVelocity();

    /**
     * Updates the robot's velocity
     * 
     * @param speed double in range [0.0, 1.0]
     */
    public void setVelocity(double speed);

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
     *            CameraSensor used to locate Cube on the field
     * @return the Cube of closest distance discovered on the playing field.
     */
    // public Cube findCube(CameraSensor sensor);

    /**
     * Moves towards a Cube on the playing field in order to collect it
     * 
     * @param cube
     *            the Cube on the field targeted by the robot
     */
    // public void getCube(Cube cube);

    /**
     * Searches for walls on the playing field.
     * 
     * @param sensors
     *            List of Sensors used to detect the walls
     * @return a List of Walls detected by the Sensors
     */
    // public List<Walls> findWall(List<Sensor> sensors);

    /**
     * Causes the Robot to follow and move along the closest Wall
     * 
     * @param walls
     *            a List of Walls on the playing field
     */
    // public void followWall(List<Wall> walls);
    
    /**
     * @return the radius, in meters, of this Robot
     */
    public double getRadius();
}