package PID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import robot.Robot;
import robotparts.Gyroscope;
import robotparts.Motor;

public class RotatePID implements PID{
    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double diff;
    private double integral = 0;
    private double derivative = 0;

    private final Motor leftMotor;
    private final Motor rightMotor;
    private final Gyroscope gyro;
    private final Robot robot;

    private double desiredHeading = 0;
    private double currentHeading = 0;
    
    public RotatePID(File file, Robot robot, Motor leftMotor,
            Motor rightMotor, Gyroscope gyro) throws IOException {
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

        this.robot = robot;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.gyro = gyro;
        this.desiredHeading = robot.getDesiredHeading();
        this.currentHeading = robot.getCurrentHeading();
    }
    
    @Override
    public Thread thread() {
        Thread pid = new Thread(new Runnable() {
            public void run() {
                // Calculating Current Heading with integration
//                Thread getHeading = new Thread(new Runnable() {
//                    public void run() {
//                        long start = System.currentTimeMillis();
//                        double heading = robot.getCurrentHeading();
//                        while (true) {
//                            long end = System.currentTimeMillis();
//                            double deltaT = .001 * (end - start); // from milli
//                                                                  // to sec
//                            double omega = gyro.getAngularVelocity();
//                            double bias = ((.11 * end) - .3373);
//                            double prevBias = ((.11 * start) - .3373);
//                            double total = (omega - (bias - prevBias)) * deltaT;
//                            heading += total;
//                            start = end;
//                        }
//                    }
//
//                });
//
//                // Initial Settings
//                getHeading.start();
                double desired = 0;
                double heading = 0;
                double bias = 0.0;
//                double p = .012;
//                double i = .0005;
//                double d = .03;
                long begin = System.currentTimeMillis();
                double integral = 0;
                double derivative = 0;
                double prevDiff = 0;
                leftMotor.setSpeed(bias);
                rightMotor.setSpeed(bias);

                // Main loop with PID control implemented
                try {
                    outerloop: while (true) {
                        desired = robot.getDesiredHeading();
                        heading = robot.getCurrentHeading();
                        double diff = desired - heading;
                        long finish = System.currentTimeMillis();
                        double deltaT = .001 * (finish - begin); // from milli
                                                                 // to sec
                        integral += diff * deltaT;
                        begin = finish;
                        derivative = diff - prevDiff;
                        prevDiff = diff;
                        if (integral > 200) {
                            integral = 200;
                        }
                        double power = p * diff + i * integral + d * derivative;
                        leftMotor.setSpeed(bias + power);
                        rightMotor.setSpeed(bias - power);
                        System.out.println("Left: " + leftMotor.getSpeed()
                                + " Right: " + rightMotor.getSpeed()
                                + " Heading: " + heading);
                        System.out.println("Diff: " + p * diff + " Integral: "
                                + i * integral + " Derivative: " + d
                                * derivative + " Power: " + power);
                        Thread.sleep(33);

                        long fin = System.currentTimeMillis();
                        // if ((fin - current) >= 10000) {
                        // leftMotor.setSpeed(0);
                        // rightMotor.setSpeed(0);
                        // break outerloop;
                        // }
                        // }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return pid;
    }
        
}
