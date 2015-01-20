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
 * robot are measured clockwise, in degrees, relative to the axis passing
 * through the center of the Robot's face.
 * 
 * @author osmidy
 *
 */
public class Robot {
    // Datatype Definition: Robot ::= sensors:Set<Sensor> + gyro:Gyroscope +
    // motors:List<Motor> + servos:List<Servo>

    // Rep Invariant: robot has at least two motors
    private void checkRep() {
        assert motors.size() >= 2;
    }

    private final List<Motor> motors;
    private final List<Servo> servos;
    private final Set<Sensor> sensors;
    private final Map<Sensor, Double> sensorHeadings;
    private final Gyroscope gyro;

    private double heading = 0.0;
    private final double radius = 0.1524; // 6 inches
    private State state = State.FINDWALL;

    public enum State {
        FINDWALL, WALLFOLLOW, DRIVETOCUBE, COLLECTCUBE, FINDDROPZONE, DROPSTACK;
    }

    /**
     * Constructor method
     * 
     * @param motors
     *            a List of Motor mounted on this Robot
     * @param servos
     *            a List of Servos mounted on this Robot
     * @param sensorMap
     *            a mapping of Sensors mounted on this Robot to the angle at
     *            which they are aligned
     * 
     * 
     * @param gyro
     *            a Gyroscope mounted on this Robot
     */
    public Robot(List<Motor> motors, List<Servo> servos,
            Map<Sensor, Double> sensorMap, Gyroscope gyro) {
        this.motors = motors;
        this.servos = servos;
        this.sensors = sensorMap.keySet();
        this.sensorHeadings = sensorMap;
        this.gyro = gyro;

    }

    /**
     * Determine the direction this Robot is facing
     * 
     * @return the heading, in radians, of the Robot
     */
    public double getHeading() {
        return this.heading;
    }

    /**
     * Updates the current heading of this Robot
     * 
     * @param angle
     *            the new value for the Robot heading
     */
    public synchronized void setHeading(double angle) {
        this.heading = angle;
    }

    /**
     * Determine the direction a given Sensor is facing
     * 
     * @param sensor
     *            Sensor mounted on the robot
     * @return the heading, in degrees, of the Sensor
     */
    public double getSensorHeading(Sensor sensor) {
        double angle = sensorHeadings.get(sensor);
        return angle;
    }

    /**
     * @return the current speed of the robot
     */
    public double getSpeed() {
        return 0.0;
    }

    /**
     * Updates the robot's velocity
     * 
     * @param speed
     *            double in range [0.0, 1.0]
     */
    public void setVelocity(double speed) {

    }

    /**
     * @return the current angular velocity of the robot, in deg/s
     */
    public double getAngularVelocity() {
        double omega = gyro.getAngularVelocity();
        return omega;
    }

    /**
     * Calls the mechanical action to be taken by the robot part
     */
    // TODO: robot parts or whole robot?
    public void action() {

    }

    /**
     * Determines the object closest to this Robot
     * 
     * @param sensors
     *            Sensors used to determine distance to objects on the playing
     *            field
     * @return the distance from this Robot to the closest object in meters
     */
    public double distanceToClosestObject(Set<Sensor> sensors) {
        double closest = Double.MAX_VALUE;
        for (Sensor sensor : sensors) {
            double dist = sensor.distanceToObject();
            if (dist < closest) {
                closest = dist;
            }
        }
        return closest;
    }

    /**
     * Determines the angle of the object closest to this Robot
     * 
     * @param sensorMap
     *            Sensors used to determine distance to objects on the playing
     *            field
     * @return the angle, relative to the robot, at which the closest object is
     * 
     */
    public double angleOfClosestObject(Map<Sensor, Double> sensorMap) {
        double angle = Double.MAX_VALUE;
        double closest = Double.MAX_VALUE;
        for (Sensor sensor : sensorMap.keySet()) {
            double dist = sensor.distanceToObject();
            double dir = sensorMap.get(sensor);
            if (dist < closest) {
                closest = dist;
                angle = dir;
            }
        }
        return angle;
    }

    /**
     * @return the current state of the Robot
     */
    public State getState() {
        return this.state;
    }

    /**
     * Sets the current state of the Robot
     * 
     * @param state
     *            One of the enumerated states for the Robot class
     */
    public void setState(State state) {
        this.state = state;
    }

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
    public double getRadius() {
        return this.radius;
    }
}