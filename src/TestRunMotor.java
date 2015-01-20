import sensorIO.Gpio;
import sensorIO.Pwm;
import robotparts.Gyroscope;

public class TestRunMotor {
    public static void main (String[] args) {
        // Run the motor
        int pwmPin = 9;
        int dirPin = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;
        Pwm pwm = new Pwm(pwmPin);
        Pwm pwmRight = new Pwm(pwmPinRight);
        Gpio dir = new Gpio(8, "out");
        Gpio dirRight = new Gpio(dirPinRight,"out");
        long pwmPointer = pwm.getPointer();
        long dirPointer = dir.getPointer();
        long pwmPointerRight = pwmRight.getPointer();
        long dirPointerRight = dirRight.getPointer();
        print("Starting...2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        print("Starting...1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        print("Speed!");
        pwm.setSpeed(pwmPointer, 0.15);
        pwmRight.setSpeed(pwmPointerRight, 0.15);
        dirRight.write(dirPointerRight, 0);
        /**
        //make gyro
        final Gyroscope gyro = new Gyroscope(10);
        
        // PID(just P for now) loop for 5 seconds
        long startTime = System.currentTimeMillis(); //fetch starting time
        while(false||(System.currentTimeMillis()-startTime)<5000){
        	double bias = 0.1*System.currentTimeMillis()-0.33;
        	double power = gyro.getAngularVelocity() - bias;
        	pwm.setSpeed(pwmPointer, 0.15-0.03*power);
        	pwmRight.setSpeed(pwmPointerRight, 0.15+0.03*power);
        }
        */
        // Stop after 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        pwm.setSpeed(pwmPointer, 0.0);
        pwmRight.setSpeed(pwmPointerRight, 0.0);
    }
    
    private static void print(String str) {
        System.out.println(str);
    }
}
