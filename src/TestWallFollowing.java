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

        int frontPin = 1;
        int sidePin = 0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);

        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        IRSensor sideSensor = new IRSensor(sidePin);
        IRSensor frontSensor = new IRSensor(frontPin);

        // Initial Settings
        // long current = System.currentTimeMillis();
        long begin = System.currentTimeMillis();
        boolean log = true;
        double bias = 0.2;
        double p = 0.15; // .012;
        double i = 0; // 0.005; // 0.0005;
        double d = 0.7; // 0.03;
        double integral = 0;
        double derivative = 0;
        double sideSep = sideSensor.distanceToObject();
        double prevDiff = 0;
        double desired = 0.2;
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
            if (sideSep > 0.5) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                System.out.println(sideSep);
                break mainLoop;
            }

            // sideSepWindow[sideSepCounter] = forwardSensor
            // .distanceToObject();
            // rearSepWindow[rearSepCounter] = rearSensor.distanceToObject();
            // sideSepCounter++;
            // rearSepCounter++;
            // //else {
            // sideSepData = sideSepWindow;
            // rearSepData = rearSepWindow;
            // Arrays.sort(sideSepData);
            // Arrays.sort(rearSepData);
            // sideSep = sideSepData[10];
            // rearSep = rearSepData[10];
            //
            // if (sideSepCounter > 19){
            // sideSepCounter = 0;
            // rearSepCounter = 0;

            {

                long end = System.currentTimeMillis();
                double frontSep = frontSensor.distanceToObject();
                sideSep = sideSensor.distanceToObject();
                double diff = sideSep - desired;
                double deltaT = .001 * (end - begin);
                integral += diff * deltaT;
                derivative = diff - prevDiff;

                if (integral > 500) {
                    integral = 500;
                }
                
                double power = p * diff + i * integral + d * derivative;
                
                if (frontSep < 0.15) {
                    power -= 0.1;
                }

                leftMotor.setSpeed(bias - power);
                rightMotor.setSpeed(bias + power);
                begin = end;
                prevDiff = diff;
//                if (frontSep < 0.15) {
//                    System.out.println("Too Close: " + frontSep);
//                    leftMotor.setSpeed(0);
//                    rightMotor.setSpeed(0);
//                    leftMotor.setSpeed(.1);
//                    rightMotor.setSpeed(-.1);
//                    try {
//                        Thread.sleep(80);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }

                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (log == true) {
                    System.out.println("Diff: " + diff);
                    System.out.println("Front; " + frontSep);
                    System.out.println("Side: " + sideSep);
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
