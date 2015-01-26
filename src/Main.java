import imageprocessing.ImageCube;
import imageprocessing.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PID.WallFollowPID;
import robot.Robot;
import robot.Robot.State;
import robotparts.Gyroscope;
import robotparts.Motor;
import robotparts.Servo;
import sensors.CameraSensor;
import sensors.IRSensor;
import sensors.Sensor;
import sensors.UltraSonicSensor;

public class Main {
    // Free Cokebot!
    private final static Robot cokebot = makeRobot();
    private final static ImageUtil imageUtil = new ImageUtil();
    
    public static void main(String[] args) throws IOException {
        // TODO:  when stack is hit, remove stack from list, create new cubes and add to list
        
        while (true) {
            State state = cokebot.getState();
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
            sleep(30);
        }
        
    }
    
    private static void findWall() {
        boolean wallFound = false;
        while (!wallFound) {
            Sensor wallSensor = cokebot.findClosestWallSensor();
            cokebot.followWall(wallSensor);
            sleep(30);
            wallFound = true;
        }
        sleep(30);
        cokebot.setState(State.WALLFOLLOW);
    }

    private static void followAndSearch() throws IOException {
        List<Motor> motors = cokebot.getMotors();
        List<Sensor> sensors = cokebot.getSensors();
        Motor leftMotor = motors.get(0);
        Motor rightMotor = motors.get(1);
        IRSensor sideIR = (IRSensor)sensors.get(0);
        IRSensor diagonalIR = (IRSensor)sensors.get(1);
        WallFollowPID pid = new WallFollowPID(new File("WallFollowPID.txt"), leftMotor, rightMotor, sideIR, diagonalIR);
        Thread pidThread = pid.thread();
        pidThread.start();
        
        boolean cubeFound = false;
        CameraSensor camera = (CameraSensor)sensors.get(3);
        Thread findCube = new Thread(new Runnable() {
            public void run() {
                while (!cubeFound) {
                    List<int[]> centers = imageUtil.getCubeCenters();
                    if (!centers.isEmpty()) {
                        ImageCube closestCube = imageUtil.getClosestCube();
                        double newDesiredHeading = closestCube.getHeading();
                        sleep(30);
                        cubeFound = true;
                    }
                }
            }
        });
        findCube.start();
        if (cubeFound) {
            findCube.interrupt();
            pidThread.interrupt();
            sleep(30);
        }
        cokebot.setState(State.DRIVETOCUBE);
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
    
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        int sideIRPin = 0;
        int diagonalIRPin = 1;
        double sideIRHeading = -90;
        double diagonalIRHeading = -30;

        IRSensor sideIR = new IRSensor(sideIRPin);
        IRSensor diagonalIR = new IRSensor(diagonalIRPin);
        sensorHeadings.put(sideIR, sideIRHeading);
        sensorHeadings.put(diagonalIR, diagonalIRHeading);
        
        CameraSensor camera = new CameraSensor(imageUtil);
        double cameraHeading = 0.0;
        sensorHeadings.put(camera, cameraHeading);

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
