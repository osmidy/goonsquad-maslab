import robotparts.Servo;
import sensorIO.Shield;


public class TestServo {
    static int bus = 6; // Always for shield on Edison
    static Shield shield = new Shield(bus);
    static Servo servo = new Servo(0, shield);
    public static void main(String[] args) {
        while (true) {
            servo.setPosition(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            servo.setPosition(.8);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
