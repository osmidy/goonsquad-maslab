package robotparts;

import sensorIO.Gpio;
import sensorIO.Pwm;

/**
 * Class providing methods which represent the operations of a physical servo.
 * 
 * @author osmidy
 *
 */
public class Servo {
    private final Pwm pwm;
    private final Gpio dir;
    private final int pwmPin;
    private final int dirPin;
    private final long pwmPointer;
    private final long dirPointer;

    // Rep Invariant: Pwm and Gpio are on different pins
    private void checkRep() {
        assert pwmPin != dirPin;
    }

    /**
     * Instantiates a Servo.
     * 
     * @param pwm
     *            A Pwm object controlling power to, and speed of, the servo
     * @param pwmPin
     *            Physical pin number of pwm
     * @param dir
     *            A Gpio object controlling direction of the servo
     * @param dirPin
     *            Physical pin number of dir
     */
    public Servo(Pwm pwm, int pwmPin, Gpio dir, int dirPin) {
        this.pwm = pwm;
        this.pwmPin = pwmPin;
        this.pwmPointer = pwm.getPointer();
        this.dir = dir;
        this.dirPin = dirPin;
        this.dirPointer = dir.getPointer();
        checkRep();
    }

    /**
     * Sets the speed for this Servo
     * 
     * @param speed
     *            a double in the range [0.0, 1.0], indicating the percentage of
     *            power to be applied by the servo
     */
    public void setSpeed(double speed) {
        pwm.setSpeed(pwmPointer, speed);
        checkRep();
    }

    /**
     * Sets the direction of motion for this Servo
     * 
     * @param value
     *            integer value used to determine direction of motion. Must be 0
     *            or 1
     */
    public void setDirection(int value) {
        dir.write(dirPointer, value);
        checkRep();
    }

    /**
     * Reverses the direction of motion for this Servo
     */
    public void reverseDirection() {
        int value = dir.read(dirPointer);
        if (value == 0) {
            dir.write(dirPointer, 1);
        } else {
            dir.write(dirPointer, 0);
        }
        checkRep();
    }

    /**
     * @return the Pwm pin number of this Servo
     */
    public int getPwmPin() {
        checkRep();
        return pwmPin;
    }

    /**
     * @return the Gpio pin number of this Servo
     */
    public int getGpioPin() {
        checkRep();
        return dirPin;
    }
}
