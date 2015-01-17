import robotparts.Gyroscope;
import robotparts.Motor;
import sensors.IRSensor;
import sensorIO.Gpio;
import sensorIO.Pwm;

public class TestWallFollowing {
    public static void main(String[] args) {
        String DIR_OUT = "out";
        long pwmPointerLeft;
        long dirPointerLeft;
        long pwmPointerRight;
        long dirPointerRight;

        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;

        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;
        double desired = 0.0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);
        pwmPointerLeft = leftMotor.getPwmPin();
        dirPointerLeft = leftMotor.getGpioPin();
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);
        pwmPointerRight = rightMotor.getPwmPin();
        dirPointerRight = rightMotor.getGpioPin();
        IRSensor forwardSensor = new IRSensor(0);
        IRSensor rearSensor = new IRSensor(1);
        
        // Initial Settings 
        long current = System.currentTimeMillis();
        double bias = .2;
        double p = .001;
        double separation = .1;
        double diff = 1;
        leftMotor.setSpeed(bias);
        rightMotor.setSpeed(bias);

        // Main loop with P control
        parallelLoop: while (Math.abs(diff) > 0.01) {
            double frontSep = forwardSensor.distanceToObject();
            double rearSep = rearSensor.distanceToObject();  
            diff = (frontSep - separation) - (rearSep - separation);
            double power = p * diff;
            leftMotor.setSpeed(bias - power);
            rightMotor.setSpeed(bias + power);
            System.out.println("Front: " + frontSep + "Rear: " + rearSep);
            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                    + rightMotor.getSpeed());
            long fin = System.currentTimeMillis();
            if ((fin - current) >= 5000) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                break parallelLoop;
            }
        // localizingLoop: while (Math.abs(frontSep - 11) > 0.05 &&
        
            // }    
        }
        System.out.println("Fin.");
    }
}

