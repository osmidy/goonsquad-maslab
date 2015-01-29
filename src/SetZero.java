import robotparts.Motor;
import robotparts.Servo;
import sensorIO.Shield;


public class SetZero {
    public static void main(String[] args) {
        int leftForward = 1;
        int leftReverse = 0;
        int rightForward = 0;
        int rightReverse = 1;
        int pwmPinLeft = 9;
        int dirPinLeft = 8;
        int pwmPinRight = 3;
        int dirPinRight = 4;
        Motor leftMotor = new Motor(pwmPinLeft, dirPinLeft, leftForward,
                leftReverse);
        Motor rightMotor = new Motor(pwmPinRight, dirPinRight, rightForward,
                rightReverse);
        Servo servo1 = new Servo(0, new Shield(6));
        servo1.setPosition(0);
        leftMotor.setSpeed(0);
        rightMotor.setSpeed(0);
    }
}
