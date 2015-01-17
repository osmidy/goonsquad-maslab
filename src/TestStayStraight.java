import robotparts.Gyroscope;
import robotparts.Motor;

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
        double desired = 0.0;

        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);
        pwmPointerLeft = leftMotor.getPwmPin();
        dirPointerLeft = leftMotor.getGpioPin();
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);
        pwmPointerRight = rightMotor.getPwmPin();
        dirPointerRight = rightMotor.getGpioPin();

        // Calculating Current Heading with integration
        Thread getHeading = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                double prevBias = 0;
                while (true) {
                    long end = System.currentTimeMillis();
                    double deltaT = .001 * (end - start); // from milli to sec
                    double omega = gyro.getAngularVelocity(
                            gyro.getChipPointer(), gyro.getSpiPointer());
                    double bias = ((.1 * end) - .3373) - prevBias;
                    double total = (omega-bias) * deltaT;
                    heading += total;
                    start = end;
                    // System.out.println(heading);
                }
            }

        });
        
        // Initial Settings
        getHeading.start();
        long current = System.currentTimeMillis();
        double motorBias = .2;
        double p = .001;
        leftMotor.setSpeed(motorBias);
        rightMotor.setSpeed(motorBias);
            
        // Main loop with P control implemented
        outerloop: while (true) {
            double omega = gyro.getAngularVelocity(gyro.getChipPointer(),
                    gyro.getSpiPointer());
            double diff = desired - heading;
            // // if (heading <= -0.5) {
            // // double leftSpeed = leftMotor.getSpeed();
            // // leftSpeed += p;
            // // double rightSpeed = rightMotor.getSpeed();
            // // rightSpeed -= p;
            // // leftMotor.setSpeed(leftSpeed);
            // // rightMotor.setSpeed(rightSpeed);
            // // }
            // // if (heading >= 0.5) {
            // // double rightSpeed = rightMotor.getSpeed();
            // // rightSpeed += p;
            // // double leftSpeed = leftMotor.getSpeed();
            // // leftSpeed -= p;
            // // rightMotor.setSpeed(rightSpeed);
            // // leftMotor.setSpeed(leftSpeed);
            // // }
            double power = p * diff;
            leftMotor.setSpeed(motorBias + power);
            rightMotor.setSpeed(motorBias - power);
            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                    + rightMotor.getSpeed() + " Heading: " + heading);
            try {
            Thread.sleep(250);
            } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            long fin = System.currentTimeMillis();
            if ((fin - current) >= 5000) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                break outerloop;
            }
            // }    
        }
        getHeading.interrupt();
        System.out.println("Fin.");
    }
}