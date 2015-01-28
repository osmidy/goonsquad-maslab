import imageprocessing.ImageCube;
import imageprocessing.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import PID.StayStraightPID;
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
    private final static List<Motor> motors = cokebot.getMotors();
    private final static List<Sensor> sensors = cokebot.getSensors();
    private final static ImageUtil imageUtil = new ImageUtil();
    private final static Motor leftMotor = motors.get(0);
    private final static Motor rightMotor = motors.get(1);
    private final static IRSensor sideIR = (IRSensor) sensors.get(0);
    private final static IRSensor diagonalIR = (IRSensor) sensors.get(1);
    private final static Gyroscope gyro = cokebot.getGyro();
    

    public static void main(String[] args) throws IOException {        
        Thread sensorThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    for (Sensor sensor : sensors) {
                        sensor.distanceToObject();
                    }
                    sleep(30);
                }
            }
        });
        
        Thread imageUtilThread = new Thread(new Runnable() {
            public void run() {
                imageUtil.main(new String[0]);
            }
        });
        sensorThread.start();
        imageUtilThread.start();
        // TODO: when stack is hit, remove stack from list, create new cubes and
        // add to list
        State state;
        sleep(670);
        simulate: while (true) {
            state = cokebot.getState();
            sleep(120); 
            System.out.println(state);
            if (state.equals(State.FINDWALL)) {
                findWall();
            } else if (state.equals(State.WALLFOLLOW)) {
                followAndSearch();
            } else if (state.equals(State.DRIVETOCUBE)) {
                driveToCube();
            } else if (state.equals(State.COLLECTCUBE)) {
                collectCube();
            } else if (state.equals(State.FINDDROPZONE)) {
                findDropZone();
            } else if (state.equals(State.DROPSTACK)) {
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
            wallFound = true;
        }
        sleep(30);
        cokebot.setState(State.WALLFOLLOW);
    }

    private static void followAndSearch() throws IOException {
        WallFollowPID pid = new WallFollowPID(new File("WallFollowPID.txt"),
                leftMotor, rightMotor, sideIR, diagonalIR);
        Thread pidThread = pid.thread();
        pidThread.start();

        AtomicBoolean cubeFound = new AtomicBoolean(false);
        Thread findCube = new Thread(new Runnable() {
            public void run() {
                List<int[]> centers;
                while (!cubeFound.get()) {
                    centers = imageUtil.getGreenCenters(); // TODO: desired color field
                    if (!centers.isEmpty()) {
                        pidThread.interrupt();
                        cokebot.setSpeed(0);
                        System.out.println("FOUND");
                        ImageCube closestCube = imageUtil.getClosestCube();
                        double newDesiredHeading = closestCube.getHeading() + cokebot.getCurrentHeading();
                        cokebot.setDesiredHeading(newDesiredHeading);
                        cubeFound.set(true);
                        cokebot.setSpeed(0);
                    }
                }
            }
        });
        findCube.start();
        mainloop: while (true) {
            if (cubeFound.get()) {
//                findCube.interrupt();
//                pidThread.interrupt();
                cokebot.setSpeed(0);
                break mainloop;
            }
            
        }
        cokebot.setSpeed(0);
        sleep(30);
        findCube.interrupt();
        cokebot.setState(State.DRIVETOCUBE);
    }

    private static void driveToCube() throws IOException {
//        StayStraightPID pid = new StayStraightPID(new File("StayStraightPID.conf"), cokebot, leftMotor, rightMotor, gyro);
//        Thread thread = pid.thread();
//        thread.start();
        Thread getHeading = new Thread(new Runnable() {
            public void run() {
                double heading = cokebot.getCurrentHeading();
                long start = System.currentTimeMillis();
                while (true) {
                    long end = System.currentTimeMillis();
                    double deltaT = .001 * (end - start); // from milli to sec
                    double omega = gyro.getAngularVelocity();
                    double bias = ((.11 * end) - .3373);
                    double prevBias = ((.11 * start) - .3373);
                    double total = (omega - (bias - prevBias)) * deltaT;
                    heading += total;
                    start = end;
                    cokebot.setHeading(heading);
                }
            }

        });

        // Initial Settings
        getHeading.start();
        double motorBias = 0.2;
        double p = .012;
        double i = .0005;
        double d = .03;
        long begin = System.currentTimeMillis();
        double integral = 0;
        double derivative = 0;
        double prevDiff = 0;
        leftMotor.setSpeed(motorBias);
        rightMotor.setSpeed(motorBias);

        // Main loop with PID control implemented
        outerloop: while (true) {
            double desired = cokebot.getDesiredHeading();
            double heading = cokebot.getCurrentHeading();
            double diff = desired - heading;          
            long finish = System.currentTimeMillis();
            double deltaT = .001 * (finish - begin); // from milli to sec
            integral += diff * deltaT;
            begin = finish;
            derivative = diff - prevDiff;
            prevDiff = diff;
            if (integral > 500) {
                integral = 500;
            }
            double power = p * diff + i * integral + d * derivative;
            leftMotor.setSpeed(motorBias + power);
            rightMotor.setSpeed(motorBias - power);
//            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
//                    + rightMotor.getSpeed() + " Heading: " + heading);
//            System.out.println("Diff" + p * diff + "Integral: " + i * integral + "Derivative: " + d * derivative + "Power: " + power);
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ImageCube closestCube = imageUtil.getClosestCube();
            double distanceInches = closestCube.getDistance();
            double distance = distanceInches * 2.54; // in to cm
            if (distance <= 20) {
                break outerloop;
            }
            
            long fin = System.currentTimeMillis();
            // if ((fin - current) >= 10000) {
            //    leftMotor.setSpeed(0);
            //    rightMotor.setSpeed(0);
            //    break outerloop;
            // }
            // }
        }
        getHeading.interrupt();
        leftMotor.setSpeed(.1);
        rightMotor.setSpeed(.1);
        sleep(1000);
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
        sleep(30);
        cokebot.setState(State.FINDWALL);
        

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
     * 
     * @return the new robot.
     */
    private static Robot makeRobot() {
        final List<Motor> motors = new ArrayList<Motor>();
        final List<Servo> servos = new ArrayList<Servo>();
        final Map<Sensor, Double> sensorHeadings = new LinkedHashMap<Sensor, Double>();

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
        //sensorHeadings.put(camera, cameraHeading);

//        int trigPin = 4;
//        int echoPin = 7;
//        double ultrasonicHeading = 0.0;
//
//        UltraSonicSensor ultrasonic = new UltraSonicSensor(trigPin, echoPin);
//        sensorHeadings.put(ultrasonic, ultrasonicHeading);

        // Gyro
        int gyroPin = 10;
        Gyroscope gyro = new Gyroscope(gyroPin);

        return new Robot(motors, servos, sensorHeadings, gyro);
    }
    
    private static void print(String x) {
        System.out.println(x);
    }
}
