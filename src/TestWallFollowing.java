import robotparts.Gyroscope;
import robotparts.Motor;
import sensors.IRSensor;
import sensorIO.Gpio;
import sensorIO.Pwm;
import java.util.Arrays;

public class TestWallFollowing {
    public static void main(String[] args) {

        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;

        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;

        int forwardPin = 0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);

        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        IRSensor forwardSensor = new IRSensor(forwardPin);

        // Initial Settings
        // long current = System.currentTimeMillis();
        long begin = System.currentTimeMillis();
        boolean log = true;
        double bias = 0.15;
        double p = 0.1; // .012;
        double i = 0; // 0.005; // 0.0005;
        double d = 0; // 0.05; // 0.03;
        double integral = 0;
        double derivative = 0;
        double frontSep = forwardSensor.distanceToObject();
        double prevDiff = 0;
        double desired = 0.2;

        // double[] frontSepData;
        // frontSepData = new double[20];
        // int frontSepCounter = 0;
        // double[] frontSepWindow;
        // frontSepWindow = new double[20];

        leftMotor.setSpeed(bias);
        rightMotor.setSpeed(bias);

        // Main loop with PID control
        mainLoop: while (true) {
            if (frontSep > 0.5) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                System.out.println(frontSep);
                break mainLoop;
            }

            // frontSepWindow[frontSepCounter] = forwardSensor
            // .distanceToObject();
            // rearSepWindow[rearSepCounter] = rearSensor.distanceToObject();
            // frontSepCounter++;
            // rearSepCounter++;
            // //else {
            // frontSepData = frontSepWindow;
            // rearSepData = rearSepWindow;
            // Arrays.sort(frontSepData);
            // Arrays.sort(rearSepData);
            // frontSep = frontSepData[10];
            // rearSep = rearSepData[10];
            //
            // if (frontSepCounter > 19){
            // frontSepCounter = 0;
            // rearSepCounter = 0;

            {

                long end = System.currentTimeMillis();
                frontSep = forwardSensor.distanceToObject();
                double diff = frontSep - desired;
                double deltaT = .001 * (end - begin);
                integral += diff * deltaT;
                derivative = diff - prevDiff;

                if (integral > 500) {
                    integral = 500;
                }

                double power = p * diff + i * integral + d * derivative;
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
                    System.out.println("Diff: " + diff);
                    System.out.println("Front: " + frontSep);
                    System.out.println("Left: " + leftMotor.getSpeed()
                            + " Right: " + rightMotor.getSpeed());
                    System.out.println("Integral: " + integral + "Derivative: "
                            + derivative + "Power: " + power);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // }
                    // Use slight turns in place to change distance from wall
                    // if (!(Math.abs(diff) > 0.01)) {
                    // if (frontSep > separation) {
                    // leftMotor.setSpeed(-0.02);
                    // rightMotor.setSpeed(0.02);
                    // }
                    // else {
                    // leftMotor.setSpeed(0.02);
                    // rightMotor.setSpeed(-0.02);
                    // }
                    // }


                    // long fin = System.currentTimeMillis();
                    // if ((fin - current) >= 10000) {
                    // leftMotor.setSpeed(0);
                    // rightMotor.setSpeed(0);
                    // break mainLoop;
                    // }
                    //
                }
                // System.out.println("Fin.");
            }
        }
    }
}
