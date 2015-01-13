import sensorIO.Gpio;
import sensorIO.Pwm;


public class TestRunMotor {
    public static void main (String[] args) {
        // Run the motor
        int pwmPin = 9;
        int dirPin = 8;
        Pwm pwm = new Pwm(pwmPin);
        Gpio dir = new Gpio(8, "out");
        long pwmPointer = pwm.getPointer();
        long dirPointer = dir.getPointer();
        print("Starting...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        print("Speed!");
        pwm.setSpeed(pwmPointer, 0.5);
    }
    
    private static void print(String str) {
        System.out.println(str);
    }
}
