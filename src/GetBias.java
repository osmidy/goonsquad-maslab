import robotparts.Gyroscope;
import robotparts.Motor;


public class GetBias {
    private static double heading = 0.0;
    public static void main(String[] arg) throws InterruptedException {
        String DIR_OUT = "out";
        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;
        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;
        int gyroPin = 10;
        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);
        Gyroscope gyro = new Gyroscope(gyroPin);
        long chip = gyro.getChipPointer();
        long spi = gyro.getSpiPointer();
        
        Thread getHeading = new Thread(new Runnable() {
            public void run() {
                long start = System.currentTimeMillis();
                while (true) {
                    long end = System.currentTimeMillis();
                    double deltaT = .001 * (end - start); // milli to sec
                    double angVel = gyro.getAngularVelocity(chip, spi);
                    heading = angVel * deltaT;
                    heading -= ((.1*deltaT) - .3373);
                }
            }
        });
        
        getHeading.start();
        long start = System.currentTimeMillis();
        long end = 0;
        double leftSpeed = .2;
        double rightSpeed = .2;
        
        outerloop: while (true) {
            if (end != 0) {
                Thread.sleep(end - start);
                end = 0;
            }
            leftMotor.setSpeed(leftSpeed);
            rightMotor.setSpeed(rightSpeed);
            if (heading >= 5.0) {
                end = System.currentTimeMillis();
                leftMotor.setSpeed(-1 * leftSpeed);
                rightMotor.setSpeed(-1 * rightSpeed);
                leftSpeed -= .001;
            }
            if (heading <= -5.0) {
                end = System.currentTimeMillis();
                leftMotor.setSpeed(-leftSpeed);
                rightMotor.setSpeed(-rightSpeed);
                rightSpeed -= .001;
            }
            System.out.println(leftMotor.getSpeed() + " " + rightMotor.getSpeed() + " " + heading);
            if (System.currentTimeMillis() - start >= 10000 && Math.abs(heading) < 5.0) {
                leftMotor.setSpeed(0);
                rightMotor.setSpeed(0);
                break outerloop;
            }
        }
    }
}
