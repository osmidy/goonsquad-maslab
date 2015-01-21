import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robot.Robot;
import robot.Robot.State;
import robotparts.Gyroscope;
import robotparts.Motor;
import robotparts.Servo;
import sensors.IRSensor;
import sensors.Sensor;
import sensors.UltraSonicSensor;

public class Main {
    public static void main(String[] args) {
        // Enter the Squadbot
        Robot squadbot = makeRobot();
        State state = squadbot.getState();
        // TODO:  when stack is hit, remove stack from list, create new cubes and add to list
        
        while (true) {
            if (state.equals(State.FINDWALL)) {
                findWall();
            }
            if (state.equals(State.WALLFOLLOW)) {
                followAndSearch();
            }
            if (state.equals(State.DRIVETOCUBE)) {
                driveToCube();
            }
            if (state.equals(State.COLLECTCUBE)) {
                collectCube();
            }
            if (state.equals(State.FINDDROPZONE)) {
                findDropZone();
            }
            if (state.equals(State.DROPSTACK)) {
                dropStack();
            }
        }
        
    }
    
    private static void findWall() {
        // TODO Auto-generated method stub
        
    }

    private static void followAndSearch() {
        // TODO Auto-generated method stub
        
    }

    private static void driveToCube() {
        // TODO Auto-generated method stub
        
    }

    private static void collectCube() {
        // TODO Auto-generated method stub
        
    }

    private static void findDropZone() {
        // TODO Auto-generated method stub
        
    }

    private static void dropStack() {
        // TODO Auto-generated method stub
        
    }

    /**
     * Initializes the robot
     * @return the new robot.
     */
    private static Robot makeRobot() {
        final List<Motor> motors = new ArrayList<Motor>();
        final List<Servo> servos = new ArrayList<Servo>();
        final Map<Sensor, Double> sensorHeadings = new HashMap<Sensor, Double>();

        // Wheel Motors
        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int leftForward = 1;
        int leftReverse = 0;

        int pwmPinRight = 3;
        int dirPinRight = 4;
        int rightForward = 0;
        int rightReverse = 1;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);
        motors.add(leftMotor);
        motors.add(rightMotor);

        // Other Motors

        // Servos

        // Sensors
        int frontIRPin = 0;
        int rearIRPin = 1;
        double frontIRHeading = -90;
        double rearIRHeading = -90;

        IRSensor frontIR = new IRSensor(frontIRPin);
        IRSensor rearIR = new IRSensor(rearIRPin);
        sensorHeadings.put(frontIR, frontIRHeading);
        sensorHeadings.put(rearIR, rearIRHeading);

        int trigPin = 4;
        int echoPin = 7;
        double ultrasonicHeading = 0.0;

        UltraSonicSensor ultrasonic = new UltraSonicSensor(trigPin, echoPin);
        sensorHeadings.put(ultrasonic, ultrasonicHeading);

        // Gyro
        int gyroPin = 10;
        Gyroscope gyro = new Gyroscope(gyroPin);
        
        return new Robot(motors, servos, sensorHeadings, gyro);
    }
}
