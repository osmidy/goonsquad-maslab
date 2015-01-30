import imageprocessing.ImageCube;
import imageprocessing.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import PID.ReversePID;
import PID.RotatePID;
import PID.StayStraightPID;
import PID.WallFollowPID;
import robot.Robot;
import robot.Robot.State;
import robotparts.Gyroscope;
import robotparts.Motor;
import robotparts.Servo;
import sensorIO.Shield;
import sensors.CameraSensor;
import sensors.IRSensor;
import sensors.Sensor;

public class Main {
    // Free Cokebot!
    private final static Robot cokebot = makeRobot();
    private final static List<Motor> motors = cokebot.getMotors();
    private final static List<Sensor> sensors = cokebot.getSensors();
    private final static Map<Sensor, Double> sensorMap = cokebot.getSensorMap();
    private final static ImageUtil imageUtil = new ImageUtil();
    private final static Motor leftMotor = motors.get(0);
    private final static Motor rightMotor = motors.get(1);
    private final static IRSensor sideIR = (IRSensor) sensors.get(0);
    private final static IRSensor diagonalIR = (IRSensor) sensors.get(1);
    private final static Gyroscope gyro = cokebot.getGyro();
    
    private final static File wallFollowPid = new File("WallFollowPID.conf");
    private final static File stayStraightPid = new File("StayStraightPID.conf");
    

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
        
        Thread imageThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    imageUtil.checkImage();
                }
            }
        });
        
        sensorThread.start();
        // TODO: when stack is hit, remove stack from list, create new cubes and
        // add to list
        State state;
        simulate: while (true) {
            state = cokebot.getState();
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

    private static void findWall() throws IOException {
    	cokebot.setSpeed(0);
    	sleep(100);
    	StayStraightPID approachWall = new StayStraightPID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
    	Thread approachThread = approachWall.thread();
        RotatePID rotateRobot = new RotatePID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
        Thread rotateThread = rotateRobot.thread();
    	
    	AtomicBoolean wallFound = new AtomicBoolean(false);
    	AtomicBoolean nearWall = new AtomicBoolean(false);
    	Thread getDistance = new Thread(new Runnable() {
    		public void run() {
    			int[] wall = new int[2];
    			double distance = Double.MAX_VALUE;
    			while (!wallFound.get()) {
    				wall = imageUtil.getWallPixel();
    				if ((wall.length != 0) && (wall != null)) {
    					imageUtil.getWallHeading(wall);
    					cokebot.setSpeed(0);
    					double newDesiredHeading = imageUtil.getWallHeading(wall) + cokebot.getCurrentHeading();
    					cokebot.setDesiredHeading(newDesiredHeading);
    					approachThread.start();
    					wallFound.set(true);
    				}
    			}
    			while (!nearWall.get()) {
    			    wall = imageUtil.getWallPixel();
    			    double distanceInches = 2129 * Math.pow(wall[1], -0.876); // Camera calibration
    			    distance = distanceInches * .0254; // inches to meters
    			    nearWall.set((distance < 0.15));
    			    sleep(30);
    			}
    		}
    	});
    	
    	getDistance.start();
    	mainloop: while (true) {
    	    if (nearWall.get()) {
    	        getDistance.interrupt();
    	        break mainloop;
    	    }
    	}
    	cokebot.setSpeed(0);
    	sleep(30);
    	double newDesiredHeading = cokebot.getCurrentHeading() - sensorMap.get(diagonalIR);
    	cokebot.setDesiredHeading(newDesiredHeading);
    	rotateThread.start();
    	sleep(200);
    	rotateThread.interrupt();
    	cokebot.setState(State.WALLFOLLOW);
    	
//    	double wallHeading; // Calculated from column that is being iterated over (column 77)
//        double newWallHeading = cokebot.getCurrentHeading() + wallHeading;
//        cokebot.setDesiredHeading(newWallHeading);
//        
//        File file = new File("StayStraightPID.conf");
//        StayStraightPID getToWall = new StayStraightPID(file, cokebot, leftMotor, rightMotor, gyro);
//        Thread getToWallThread = getToWall.thread();
//        Thread checkDistance = new Thread(new Runnable() {
//            public void run() {
//                double wallDistance = Double.MAX_VALUE;
//                // TODO: make sure interrupted correctly
//                while (wallDistance > 20) {
//                    System.out.println("Approaching wall...");
//                    wallDistance = getWallDistance(); // Iterates over column to find distance, details in notebook
//                    sleep(10);
//                }
//            }
//        });
//        
//        getToWallThread.start();
//        checkDistance.start();
//        sleep(500);
//        getToWallThread.interrupt();
//        
//        cokebot.setState(State.WALLFOLLOW);
    }

    private static void followAndSearch() throws IOException {
        WallFollowPID pid = new WallFollowPID(wallFollowPid, leftMotor, rightMotor, sideIR, diagonalIR);
        Thread pidThread = pid.thread();
        pidThread.start();

        AtomicBoolean cubeFound = new AtomicBoolean(false);
        List<int[]> centers;
        while (!cubeFound.get()) {
            centers = imageUtil.getGreenCenters(); // TODO: desired color field
            if (!centers.isEmpty()) {
                pidThread.interrupt();
                cokebot.setSpeed(0);
                ImageCube closestCube = imageUtil.getClosestCube();
                double newDesiredHeading = closestCube.getHeading() + cokebot.getCurrentHeading();
                cokebot.setDesiredHeading(newDesiredHeading);
                cubeFound.set(true);
                cokebot.setSpeed(0);
            }
        }
    
        
//        Thread findCube = new Thread(new Runnable() {
//            public void run() {
//                List<int[]> centers;
//                while (!cubeFound.get()) {
//                    imageUtil.checkImage();
//                    centers = imageUtil.getGreenCenters(); // TODO: desired color field
//                    if (!centers.isEmpty()) {
//                        pidThread.interrupt();
//                        cokebot.setSpeed(0);
//                        ImageCube closestCube = imageUtil.getClosestCube();
//                        double newDesiredHeading = closestCube.getHeading() + cokebot.getCurrentHeading();
//                        cokebot.setDesiredHeading(newDesiredHeading);
//                        cubeFound.set(true);
//                        cokebot.setSpeed(0);
//                    }
//                }
//            }
//        });
//        findCube.start();
//        mainloop: while (true) {
//            if (cubeFound.get()) {
//                findCube.interrupt();
//                cokebot.setSpeed(0);
//                break mainloop;
//            }
//            
//        }
        cokebot.setSpeed(0);
        sleep(30);
//        findCube.interrupt();
        cokebot.setState(State.DRIVETOCUBE);
    }

    private static void driveToCube() throws IOException {
        StayStraightPID getCube = new StayStraightPID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
        Thread getCubeThread = getCube.thread();
        Thread checkDistance = new Thread(new Runnable() {
            public void run() {
                double distance = Double.MAX_VALUE;
                // TODO: make sure interrupted correctly
                while (distance > .20) {
                    System.out.println("Running check...");
                    distance = imageUtil.getClosestCube().getDistance();
                    distance *= 0.0254; //in to m
                    sleep(10);
                }
                cokebot.setState(State.COLLECTCUBE);
            }
        });
        
        getCubeThread.start();
        checkDistance.start();
        sleep(1500);
//        getCubeThread.interrupt();
//        cokebot.setState(State.COLLECTCUBE);     
    }

    private static void collectCube() {
        System.out.println("SERVOS: " + sensors);
        IRSensor closeRange = (IRSensor)sensors.get(2);
        if (closeRange.getVoltage() < 500) {
            System.out.println("COLLECTED!");
            SetZero.main(new String[0]);
        }

    }

    private static void findDropZone() throws IOException {
        cokebot.setSpeed(0);
        sleep(100);
        StayStraightPID approachZone = new StayStraightPID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
        Thread approachThread = approachZone.thread();
        RotatePID rotateRobot = new RotatePID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
        Thread rotateThread = rotateRobot.thread();
        
        AtomicBoolean zoneFound = new AtomicBoolean(false);
        AtomicBoolean nearZone = new AtomicBoolean(false);
        AtomicBoolean inZone = new AtomicBoolean(false);
        Thread getDistance = new Thread(new Runnable() {
            public void run() {
                int[] zone = new int[2];
                double distance = Double.MAX_VALUE;
                while (!zoneFound.get()) {
                    imageUtil.checkImage();
                    zone = imageUtil.getDropzonePixel();
                    if ((zone.length != 0) && (zone != null)) {
                        imageUtil.getDropzoneHeading(zone);
                        cokebot.setSpeed(0);
                        double newDesiredHeading = imageUtil.getDropzoneHeading(zone) + cokebot.getCurrentHeading();
                        cokebot.setDesiredHeading(newDesiredHeading);
                        approachThread.start();
                        zoneFound.set(true);
                    }
                }
                while (!nearZone.get()) {
                    imageUtil.checkImage();
                    zone = imageUtil.getDropzonePixel();
                    double distanceInches = 2129 * Math.pow(zone[1], -0.876); // Camera calibration
                    distance = distanceInches * .0254; // in to m
                    nearZone.set(distance < 0.15);
                    sleep(30);
                }
            }
        });
        
        getDistance.start();
        mainloop: while (true) {
            if (nearZone.get()) {
                sleep(100);
                getDistance.interrupt();
                break mainloop;
            }
        }
        cokebot.setSpeed(0);
        // Rotate for reverse
        double newDesiredHeading = cokebot.getCurrentHeading() + 180.0;
        cokebot.setDesiredHeading(newDesiredHeading);
        rotateThread.start();
        sleep(200);
        rotateThread.interrupt();
        cokebot.setState(State.DROPSTACK);
//        double dropHeading; // Calculated from column that is being iterated over (column 0)
//        double newWallHeading = cokebot.getCurrentHeading() + dropHeading;
//        cokebot.setDesiredHeading(newWallHeading);
//        
//        File file = new File("StayStraightPID.conf");
//        StayStraightPID getToWall = new StayStraightPID(file, cokebot, leftMotor, rightMotor, gyro);
//        Thread getToWallThread = getToWall.thread();
//        Thread checkDistance = new Threadd(new Runnable() {
//            public void run() {
//                double wallDistance = Double.MAX_VALUE;
//                // TODO: make sure interrupted correctly
//                while (wallDistance > 20) {
//                    System.out.println("Approaching wall...");
//                    wallDistance = getWallDistance(); // Iterates over column to find distance, details in notebook
//                    sleep(10);
//                    }
//                }
//            });
//            
//            getToWallThread.start();
//            checkDistance.start();
//            sleep(500);
//            getToWallThread.interrupt();
//        }
//        
//        // Now turn to face the wall
//        double newFaceWallHeading = cokebot.getCurrentHeading() - (90 - dropHeading);
//        cokebot.setDesiredHeading(newFaceWallHeading);
//        // Turn in place to the desired heading, which should face the wall
//        
//        cokebot.setState(State.DROPSTACK);
    }
    
    private static void dropStack() throws IOException {
        cokebot.setSpeed(0);
        // TODO: actuator/servo things to actually drop blocks
        ReversePID reverse = new ReversePID(stayStraightPid, cokebot, leftMotor, rightMotor, gyro);
        Thread reverseThread = reverse.thread();
        reverseThread.start();
        sleep(2000);
        reverseThread.interrupt();
        cokebot.setSpeed(0);
        cokebot.setState(State.FINDWALL);

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

        
        // Shield
        int bus = 6;
        Shield shield = new Shield(bus);
        
        // Servos
        int pinServoPin = 3;
        Servo pinServo = new Servo(pinServoPin, shield);
        servos.add(pinServo);
        
        // Sensors
        int sideIRPin = 0;
        int diagonalIRPin = 1;
        int closeRangeIRPin = 2;
        double sideIRHeading = -90;
        double diagonalIRHeading = -30;
        double closeRangeIRHeading = 0.0;

        IRSensor sideIR = new IRSensor(sideIRPin);
        IRSensor diagonalIR = new IRSensor(diagonalIRPin);
        IRSensor closeRangeIR = new IRSensor(closeRangeIRPin);
        sensorHeadings.put(sideIR, sideIRHeading);
        sensorHeadings.put(diagonalIR, diagonalIRHeading);
        sensorHeadings.put(closeRangeIR, closeRangeIRHeading);

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
