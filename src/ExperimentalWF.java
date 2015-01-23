import robotparts.Gyroscope;
import robotparts.Motor;
import sensors.IRSensor;
import sensorIO.Gpio;
import sensorIO.Pwm;
import java.util.Arrays;

public class ExperimentalWF {
    public static void main(String[] args) {

        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;

        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;

        // int frontPin = 0;
        int diagonalPin = 1;
        int sidePin = 0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);

        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        IRSensor sideSensor = new IRSensor(sidePin);
        IRSensor diagonalSensor = new IRSensor(diagonalPin);
        // IRSensor frontSensor = new IRSensor(frontPin);

        // Initial Settings
        // long current = System.currentTimeMillis();
        long begin = System.currentTimeMillis();
        boolean log = true;
        double bias = 0.2;
        double p = 0.5;
        double i = 0.005; // .005 // 0.01;
        double d = 0.5;// .3 // 0.7
        double integral = 0;
        double derivative = 0;
        double sideSep = sideSensor.distanceToObject();
        double diagonalSep = diagonalSensor.distanceToObject();
        // double frontSep = frontSensor.distanceToObject();
        double prevDiff = 0;
        double desired = 0.1;
        // double frontSep = frontSensor.getVoltage();

        // double[] sideSepData;
        // sideSepData = new double[20];
        // int sideSepCounter = 0;
        // double[] sideSepWindow;
        // sideSepWindow = new double[20];

        leftMotor.setSpeed(bias);
        rightMotor.setSpeed(bias);

        // Main loop with PID control
        mainLoop: while (true) {
            // if (sideSep > 0.5) {
            // leftMotor.setSpeed(0);
            // rightMotor.setSpeed(0);
            // System.out.println(sideSep);
            // break mainLoop;
            // }

            // frontSep = frontSensor.distanceToObject();
            diagonalSep = diagonalSensor.distanceToObject();
            sideSep = sideSensor.distanceToObject();

            if ((diagonalSep < 0.3) && (sideSep < 0.25)) {
                System.out.println("(1,1)" + "(" + sideSep + "," + diagonalSep
                        + ")");
                leftMotor.setSpeed(0.1);
                rightMotor.setSpeed(-0.1);
                // leftMotor.setSpeed(0.25); // .23 // .25
                // rightMotor.setSpeed(0.1); // .1
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            else if ((diagonalSep < 0.3) && (sideSep >= 0.25)) {
                System.out.println("(0,1)" + "(" + sideSep + "," + diagonalSep
                        + ")");
                leftMotor.setSpeed(0.05);
                rightMotor.setSpeed(0.1);
                // leftMotor.setSpeed(0.25); // .23 // .25
                // rightMotor.setSpeed(0.1); // .1
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            else if ((diagonalSep >= 0.3) && (sideSep >= 0.25)) {
                System.out.println("(0,0)" + "(" + sideSep + "," + diagonalSep
                        + ")");
                // leftMotor.setSpeed(0.05);
                // rightMotor.setSpeed(0.25);
                // // leftMotor.setSpeed(0.25); // .23 // .25
                // // rightMotor.setSpeed(0.1); // .1
                // try {
                // Thread.sleep(100);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }

                while (diagonalSep > 0.15) {
                    diagonalSep = diagonalSensor.distanceToObject();
                    leftMotor.setSpeed(0.02);
                    rightMotor.setSpeed(0.25);
                    // leftMotor.setSpeed(0.25); // .23 // .25
                    // rightMotor.setSpeed(0.1); // .1
                }
                leftMotor.setSpeed(0.05);
                rightMotor.setSpeed(0.05);
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            // // left turns
            // if ((sideSep > 0.6) && (frontSep > 0.6)) {
            // System.out.println("TURNING");
            // while (frontSep > 0.4) {
            // System.out.println("Front: " + frontSep);
            // frontSep = frontSensor.distanceToObject();
            // leftMotor.setSpeed(0.12);
            // rightMotor.setSpeed(0.22);
            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // }
            // }

            else {
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

                // approach right turns smoothly
                // if (frontSep < 0.4 && frontSep > 0.2) {
                // // double a = -1/4000;
                // // double b = 1/50;
                // // double c = -3/10;
                // // double newBias = a*frontSep*frontSep + b*frontSep + c;
                // double newBias = (frontSep - 0.2);
                // leftMotor.setSpeed(newBias + 0.05);
                // rightMotor.setSpeed(newBias - 0.05);
                // try {
                // Thread.sleep(30);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }
                // if (frontSep <= 0.2) {
                // leftMotor.setSpeed(0.1);
                // rightMotor.setSpeed(-0.1);
                // try {
                // Thread.sleep(80);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }

                // Approach turns in steps
                // if (frontSep<0.4 && frontSep>0.2){
                // leftMotor.setSpeed(0.23);
                // rightMotor.setSpeed(0.1);
                // try {
                // Thread.sleep(80);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                //
                // }
                // if (frontSep <= 0.2){
                // leftMotor.setSpeed(0.1);
                // rightMotor.setSpeed(-0.1);
                // try {
                // Thread.sleep(80);
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // }
                // only works with wall follow

                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (log == true) {
                    System.out.println("Side: " + sideSep + "Diagonal: "
                            + diagonalSep);
                    System.out.println("Left: " + leftMotor.getSpeed()
                            + " Right: " + rightMotor.getSpeed());
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
            }
            // System.out.println("Fin.");
        }
    }
}
