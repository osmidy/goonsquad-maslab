import robotparts.Gyroscope;
import robotparts.Motor;

public class TestStayStraight {
    static double heading = 0.0;
    static Gyroscope gyro = new Gyroscope(10);

    public static void main(String[] args) {
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
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);

        // Calculating Current Heading with integration
        Thread getHeading = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                while (true) {
                    long end = System.currentTimeMillis();
                    double deltaT = .001 * (end - start); // from milli to sec
                    double omega = gyro.getAngularVelocity();
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
        double motorBias = 0;
        double p = .012;
        double i = .0005;
        double d = .03;
        long begin = System.currentTimeMillis();
        double integral = 0;
        double derivative = 0;
        double prevDiff = 0;
        leftMotor.setSpeed(motorBias);
        rightMotor.setSpeed(motorBias);

        // Main loop with PID control implemented
        outerloop: while (true) {
            double diff = desired - heading;          
            long finish = System.currentTimeMillis();
            double deltaT = .001 * (finish - begin); // from milli to sec
            integral += diff * deltaT;
            begin = finish;
            derivative = diff - prevDiff;
            prevDiff = diff;
            if (integral > 200) {
                integral = 200;
            }
            double power = p * diff + i * integral + d * derivative;
            leftMotor.setSpeed(motorBias + power);
            rightMotor.setSpeed(motorBias - power);
            System.out.println("Left: " + leftMotor.getSpeed() + " Right: "
                    + rightMotor.getSpeed() + " Heading: " + heading);
            //System.out.println("Diff: " + p * diff + " Integral: " + i * integral + " Derivative: " + d * derivative + " Power: " + power);
            System.out.println("Gyro: " + gyro.getAngularVelocity());
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            long fin = System.currentTimeMillis();
            // if ((fin - current) >= 10000) {
            //    leftMotor.setSpeed(0);
            //    rightMotor.setSpeed(0);
            //    break outerloop;
            // }
            // }
        }
        // getHeading.interrupt();
        // System.out.println("Fin.");
    }
}