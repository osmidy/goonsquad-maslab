package PID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import robotparts.Motor;
import sensors.IRSensor;

/**
 * The name says it all.
 * 
 * @author osmidy
 *
 */
public class WallFollowPID implements PID {
    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double diff;
    private double integral = 0;
    private double derivative = 0;

    private final Motor leftMotor;
    private final Motor rightMotor;
    private final IRSensor sideSensor;
    private final IRSensor diagonalSensor;

    public WallFollowPID(File file, Motor leftMotor, Motor rightMotor,
            IRSensor sideIR, IRSensor diagonalIR)
            throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(file));
        for (String line = read.readLine(); line != null; line = read
                .readLine()) {
            String[] tokens = line.split("\\s+");
            String constant = tokens[0];
            double value = Double.parseDouble(tokens[1]);
            if (constant.equals("p")) {
                p = value;
            }
            if (constant.equals("i")) {
                i = value;
            }
            if (constant.equals("d")) {
                d = value;
            }
        }

        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.sideSensor = sideIR;
        this.diagonalSensor = diagonalIR;
    }

    @Override
    public Thread thread() {
        Thread pid = new Thread(new Runnable() {
            public void run() {
                long begin = System.currentTimeMillis();
                boolean log = false;
                double bias = 0.2;
                double integral = 0;
                double derivative = 0;
                double sideSep = sideSensor.distanceToObject();
                double diagonalSep = diagonalSensor.distanceToObject();
                double prevDiff = 0;
                double desired = 0.1;
                leftMotor.setSpeed(bias);
                rightMotor.setSpeed(bias);

                mainLoop: while (true) {
                    diagonalSep = diagonalSensor.distanceToObject();
                    sideSep = sideSensor.distanceToObject();
                    if ((diagonalSep < 0.25) && (sideSep < 0.25)) { // diagonalSep
                                                                    // < 0.3
                        System.out.println("(1,1)" + "(" + sideSep + ","
                                + diagonalSep + ")");
                        leftMotor.setSpeed(0.1);
                        rightMotor.setSpeed(-0.1);
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if ((diagonalSep < 0.25) && (sideSep >= 0.25)) { // diagonalSep
                        // < 0.3
                        System.out.println("(0,1)" + "(" + sideSep + ","
                                + diagonalSep + ")");
                        if (diagonalSep < 0.1) {
                            System.out.println("Too Close");
                            // diagonalSep = diagonalSensor.distanceToObject();
                            leftMotor.setSpeed(0.1);
                            rightMotor.setSpeed(-0.1);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            leftMotor.setSpeed(0.05); // 0.05
                            rightMotor.setSpeed(0.1); // 0.1
                            // leftMotor.setSpeed(0.25); // .23 // .25
                            // rightMotor.setSpeed(0.1); // .1
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else if ((diagonalSep >= 0.25) && (sideSep >= 0.25)) {
                        System.out.println("(0,0)" + "(" + sideSep + ","
                                + diagonalSep + ")");

                        while (diagonalSep > 0.25) { // .25
                            diagonalSep = diagonalSensor.distanceToObject();
                            leftMotor.setSpeed(0.1);
                            rightMotor.setSpeed(0.25);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        leftMotor.setSpeed(0.05);
                        rightMotor.setSpeed(0.05);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        long end = System.currentTimeMillis();
                        double diff = sideSep - desired;
                        double deltaT = .001 * (end - begin);
                        integral += diff * deltaT;
                        derivative = diff - prevDiff;

                        if (integral > 500) {
                            integral = 500;
                        }

                        double power = p * diff + i * integral + d * derivative;
                        bias = 0.2;
                        leftMotor.setSpeed(bias - power);
                        rightMotor.setSpeed(bias + power);
                        begin = end;
                        prevDiff = diff;

                        try {
                            Thread.sleep(33);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (log == true) {
                            System.out.println("Side: " + sideSep
                                    + "Diagonal: " + diagonalSep);
                            System.out.println("Left: " + leftMotor.getSpeed()
                                    + " Right: " + rightMotor.getSpeed());
                            System.out.println("Diff: " + p * diff
                                    + "Integral: " + i * integral
                                    + "Derivative: " + d * derivative
                                    + "Power: " + power);
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        });
        return pid;
    }
}
