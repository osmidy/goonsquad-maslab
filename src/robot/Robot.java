package robot;

import java.util.ArrayList;
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

    // Rep Invariant: robot has at least two motors; at least one servo; each
    // sensor has a heading
    private void checkRep() {
        assert motors.size() >= 2;
        assert !servos.isEmpty();
        assert sensors.size() == sensorHeadings.size();
    }

    private final List<Motor> motors;
    private final List<Servo> servos;
    private final List<Sensor> sensors;
    private final Map<Sensor, Double> sensorHeadings;
    private final Gyroscope gyro;

    private double currentHeading = 0.0;
    private double desiredHeading = 0.0;
    private final double radius = 7.5;
    private State state = State.WALLFOLLOW;

    public enum State {
        FINDWALL("FINDWALL"), WALLFOLLOW("WALLFOLLOW"), DRIVETOCUBE(
                "DRIVETOCUBE"), COLLECTCUBE("COLLECTCUBE"), FINDDROPZONE(
                "FINDDROPZONE"), DROPSTACK("DROPSTACK");
        
        private String state;

        private State(String state) {
            this.state = state;
        }
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
        this.sensorHeadings = sensorMap;
        this.sensors = new ArrayList<Sensor>(sensorMap.keySet());
        this.gyro = gyro;
        checkRep();
    }

    /**
     * Determine the direction this Robot is facing
     * 
     * @return the heading, in radians, of the Robot
     */
    public synchronized double getCurrentHeading() {
        checkRep();
        return this.currentHeading;
    }

    /**
     * Updates the current heading of this Robot
     * 
     * @param angle
     *            the new heading
     */
    public synchronized void updateHeading(double angle) {
        checkRep();
        this.currentHeading = angle;
    }

    /**
     * @return the desired heading of the robot
     */
    public synchronized double getDesiredHeading() {
        checkRep();
        return this.desiredHeading;
    }

    /**
     * Updates the desired heading for this Robot
     * 
     * @param desired
     *            heading desired by this Robot
     */
    public synchronized void setDesiredHeading(double desired) {
        checkRep();
        this.desiredHeading = desired;
    }

    /**
     * Updates the current heading of this Robot
     * 
     * @param angle
     *            the new value for the Robot heading
     */
    public synchronized void setHeading(double angle) {
        checkRep();
        this.currentHeading = angle;
    }

    public synchronized Map<Sensor, Double> getSensorMap() {
        checkRep();
        return this.sensorHeadings;
    }

    /**
     * Determine the direction a given Sensor is facing
     * 
     * @param sensor
     *            Sensor mounted on the robot
     * @return the heading, in degrees, of the Sensor
     */
    public synchronized double getSensorHeading(Sensor sensor) {
        double angle = sensorHeadings.get(sensor);
        checkRep();
        return angle;
    }

    public synchronized Gyroscope getGyro() {
        checkRep();
        return this.gyro;
    }

    public synchronized List<Sensor> getSensors() {
        checkRep();
        return this.sensors;
    }

    public synchronized List<Motor> getMotors() {
        checkRep();
        return this.motors;
    }

    /**
     * @return the current speed of the robot (motor bias)
     */
    public double getSpeed() {
        checkRep();
        return motors.get(0).getSpeed();
    }

    /**
     * Updates the robot's velocity
     * 
     * @param speed
     *            double in range [0.0, 1.0]
     */
    public synchronized void setSpeed(double speed) {
        motors.get(0).setSpeed(speed);
        motors.get(1).setSpeed(speed);
        checkRep();
    }

    /**
     * @return the current angular velocity of the robot, in deg/s
     */
    public synchronized double getAngularVelocity() {
        double omega = gyro.getAngularVelocity();
        checkRep();
        return omega;
    }

    /**
     * Calls the mechanical action to be taken by the robot part
     */
    // TODO: robot parts or whole robot?
    public void action() {
        checkRep();
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
        checkRep();
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
        checkRep();
        return angle;
    }

    /**
     * @return the current state of the Robot
     */
    public State getState() {
        checkRep();
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
        checkRep();
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
     * @return the Sensor on the Robot which detects the nearest wall.
     */
    public Sensor findClosestWallSensor() {
        Sensor closestSensor = sensors.get(0);
        double closestDistance = closestSensor.distanceToObject();
        int length = sensors.size();
        for (Sensor sensor : sensors.subList(1, length)) {
            double distance = sensor.distanceToObject();
            if (distance < closestDistance) {
                closestDistance = distance;
                closestSensor = sensor;
            }
        }
        checkRep();
        return closestSensor;
    }

    /**
     * Causes the Robot to follow and move along the closest Wall
     * 
     * @param sensor
     *            Sensor pointed towards the closest Wall
     */
    public void followWall(Sensor sensor) {
        double heading = sensorHeadings.get(sensor);
        this.setDesiredHeading(heading);
        checkRep();
    }

    /**
     * @return the radius, in inches, of this Robot
     */
    public double getRadius() {
        checkRep();
        return this.radius;
    }
}