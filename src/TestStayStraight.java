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
        double desired = 90.0;

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
                while (true) {
                    long end = System.currentTimeMillis();
                    double deltaT = .001 * (end - start); // from milli to sec
                    double omega = gyro.getAngularVelocity(
                            gyro.getChipPointer(), gyro.getSpiPointer());
                    double bias = ((.11 * end) - .3373);
                    double prevBias = ((.11 * start) - .3373);
                    double total = (omega - (bias - prevBias)) * deltaT;
                    heading += total;
                    start = end;
                }
            }

        });

        // Initial Settings
        getHeading.start();
        long current = System.currentTimeMillis();
        double motorBias = .2;
        double p = .001;
        double i = .001;
        double d = -.001;
        long begin = System.currentTimeMillis();
        double integral = 0;
        double derivative = 0;
        leftMotor.setSpeed(motorBias);
        rightMotor.setSpeed(motorBias);

        // Main loop with PID control implemented
        outerloop: while (true) {
            double omega = gyro.getAngularVelocity(gyro.getChipPointer(),
                    gyro.getSpiPointer());
            double diff = desired - heading;          
            long finish = System.currentTimeMillis();
            double deltaT = .001 * (finish - begin); // from milli to sec
            integral += diff * deltaT;
            begin = finish;
            derivative = omega; // - ((.11 * (.001 * finish) - .3373));
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
            double power = p * diff + i * integral + d * derivative;
            leftMotor.setSpeed(motorBias + power);
            rightMotor.setSpeed(motorBias - power);
            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                    + rightMotor.getSpeed() + " Heading: " + heading);
            System.out.println("Integral: " + integral + "Derivative: " + derivative + "Power: " + power);
            try {
                Thread.sleep(100);
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