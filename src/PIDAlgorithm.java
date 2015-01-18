import robotparts.Gyroscope;
import robotparts.Motor;


public class PIDAlgorithm {
    private static double currentHeading = 0.0;
    private final static double desiredHeading = 0.0;
    private static double integral = 0.0;
    private static double power = 0.0;
    
    private synchronized static void setAngle(double angle, double deltaT) {
        double bias = (.1 * deltaT) - .3373;
        currentHeading += angle - bias;
    }
    
    private static void setPower(double p, double angleDiff, double i, double d, double angVel) {
        power = p*angleDiff + i*integral + d*angVel;
    }
    
    public static void main(String[] v) {
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
                    double angVel = gyro.getAngularVelocity();
                    setAngle(angVel * deltaT, deltaT);
                    double diff = desiredHeading - currentHeading;
                    integral += diff * deltaT;
                }
            }
        });
        
        getHeading.start();
        
    }

}
