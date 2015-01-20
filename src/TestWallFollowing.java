 import robotparts.Gyroscope;
import robotparts.Motor;
import sensors.IRSensor;
import sensorIO.Gpio;
import sensorIO.Pwm;

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
        int rearPin = 1;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);

        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        IRSensor forwardSensor = new IRSensor(forwardPin);
        IRSensor rearSensor = new IRSensor(rearPin);
        
        // Initial Settings 
        long current = System.currentTimeMillis();
        long begin = System.currentTimeMillis();
        double bias = 0;
        double p = 3; // .012;
        double i = 0.2; // .0005;
        double d = 5; // .03;
        double integral = 0;
        double derivative = 0;
        double separation = .1;
        double frontSep = forwardSensor.distanceToObject();
        double rearSep = rearSensor.distanceToObject();  
        double diff = frontSep - rearSep;
        double prevDiff = 0;
        leftMotor.setSpeed(bias);
        rightMotor.setSpeed(bias);

        // Main loop with PID control
        mainLoop: while (true) {
            if ((frontSep > 0.5) || (rearSep > 0.5)) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                break mainLoop;
            }
            long end = System.currentTimeMillis();
            frontSep = forwardSensor.distanceToObject();
            rearSep = rearSensor.distanceToObject(); 
            diff = frontSep - rearSep; 
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
            System.out.println("Front: " + frontSep + "Rear: " + rearSep);
            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                    + rightMotor.getSpeed());
            System.out.println("Integral: " + integral + "Derivative: " + derivative + "Power: " + power);
            // Use slight turns in place to change distance from wall
//            if (!(Math.abs(diff) > 0.01)) {
//                if (frontSep > separation) { 
//                    leftMotor.setSpeed(-0.02);
//                    rightMotor.setSpeed(0.02);
//                }
//                else {
//                    leftMotor.setSpeed(0.02);
//                    rightMotor.setSpeed(-0.02);
//                }
//            }
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
//            long fin = System.currentTimeMillis();
//            if ((fin - current) >= 10000) {
//                leftMotor.setSpeed(0);
//                rightMotor.setSpeed(0);
//                break mainLoop;
//            }
//        
        }
//        System.out.println("Fin.");
    }
}

