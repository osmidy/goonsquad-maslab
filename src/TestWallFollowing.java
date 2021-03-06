import robotparts.Gyroscope;
import robotparts.Motor;
import sensors.IRSensor;
import sensorIO.Gpio;
import sensorIO.Pwm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TestWallFollowing {
    public static void main(String[] args) throws IOException {
        File pid = new File("WallFollowPID.conf");
        double p = 0;
        double i = 0;
        double d = 0;
        BufferedReader read = new BufferedReader(new FileReader(pid));
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

        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;

        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;

        int frontPin = 2;
        int diagonalPin = 1;
        int sidePin = 0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);

        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        IRSensor sideSensor = new IRSensor(sidePin);
        IRSensor diagonalSensor = new IRSensor(diagonalPin);
        IRSensor frontSensor = new IRSensor(frontPin);

        // Initial Settings
        // long current = System.currentTimeMillis();
        long begin = System.currentTimeMillis();
        boolean log = true;
        double bias = 0.2;
        double integral = 0;
        double derivative = 0;
        double sideSep = sideSensor.distanceToObject();
        double diagonalSep = diagonalSensor.distanceToObject();
        double frontSep = frontSensor.distanceToObject();
        double prevDiff = 0;
        double desired = 0.2;

        leftMotor.setSpeed(bias);
        rightMotor.setSpeed(bias);

        // Main loop with PID control
        mainLoop: while (true) {
            if (sideSep > 0.5) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                System.out.println(sideSep);
                break mainLoop;
            }

            // frontSep = frontSensor.distanceToObject();
            diagonalSep = diagonalSensor.distanceToObject();
            sideSep = sideSensor.distanceToObject();

            while (diagonalSep < 0.25) {
                System.out.println("Too Close: " + diagonalSep);
                diagonalSep = diagonalSensor.distanceToObject();
                sideSep = sideSensor.distanceToObject();
                leftMotor.setSpeed(0.1);
                rightMotor.setSpeed(-0.1);
                // leftMotor.setSpeed(0.25); // .23 // .25
                // rightMotor.setSpeed(0.1); // .1
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            long end = System.currentTimeMillis();
            double diff = sideSep - desired;
            double deltaT = .001 * (end - begin);
            integral += diff * deltaT;
            derivative = diff - prevDiff;

            if (integral > 500) {
                integral = 500;
            }

            double power = p * diff + i * integral + d * derivative;
            bias = 0.1;
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
                System.out.println("Side: " + sideSep + "Diagonal: "
                        + diagonalSep);
                System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                        + rightMotor.getSpeed());
                System.out.println("Diff: " + p * diff + "Integral: " + i
                        * integral + "Derivative: " + d * derivative
                        + "Power: " + power);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // long fin = System.currentTimeMillis();
                // if ((fin - current) >= 5000) {
                // leftMotor.setSpeed(0);
                // rightMotor.setSpeed(0);
                // System.out.println("Time.");
                // break mainLoop;
                // }
                //
            }
            // System.out.println("Fin.");
        }
    }
}
