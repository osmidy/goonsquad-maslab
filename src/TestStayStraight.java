import robotparts.Gyroscope;
import robotparts.Motor;
import sensorIO.Gpio;
import sensorIO.Pwm;

public class TestStayStraight {
    static double heading = 0.0;
    static Gyroscope gyro = new Gyroscope(10);

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

        Pwm pwmLeft = new Pwm(pwmPinLeft);
        Gpio dirLeft = new Gpio(dirPinLeft, DIR_OUT);
        Pwm pwmRight = new Pwm(pwmPinRight);
        Gpio dirRight = new Gpio(dirPinRight, DIR_OUT);
        Motor leftMotor = new Motor(pwmLeft, pwmPinLeft, dirLeft,
                dirPinLeft, leftForward, leftReverse);
        pwmPointerLeft = leftMotor.getPwmPin();
        dirPointerLeft = leftMotor.getGpioPin();
        Motor rightMotor = new Motor(pwmRight, pwmPinRight, dirRight,
                dirPinRight, rightForward, rightReverse);
        pwmPointerRight = rightMotor.getPwmPin();
        dirPointerRight = rightMotor.getGpioPin();
        
        Thread getHeading = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                while (true) {
                    long end = System.currentTimeMillis();
                    double diff = .001 * (end - start); // from milli to sec
                    double omega = gyro.getAngularVelocity(gyro.getChipPointer(), gyro.getSpiPointer());
                    double total = omega * diff;
                    double bias = (.1 * diff) - .3373;
                    total -= bias;
                    heading = total;
                }
            }

        });

        
        getHeading.start();
        long current = System.currentTimeMillis();
        leftMotor.setSpeed(.2);
        rightMotor.setSpeed(.2);
        outerloop:
        while (true) {
            if (heading >= 0.5) {
                double leftSpeed = leftMotor.getSpeed();
                leftSpeed += .01;
                leftMotor.setSpeed(leftSpeed);
            }
            if (heading <= -0.5) {
                double rightSpeed = rightMotor.getSpeed();
                rightSpeed += .01;
                rightMotor.setSpeed(rightSpeed);
            }
            System.out.printf("Left: %d, Right: %d, Heading: %d \n", leftMotor.getSpeed(), rightMotor.getSpeed(), heading);
            long fin = System.currentTimeMillis();
            if ((fin - current) >= 7000) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                break outerloop;
            }
        }
        System.out.println("Fin.");
    }
}