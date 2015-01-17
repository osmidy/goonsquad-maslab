package robotparts;

import sensorIO.Gpio;
import sensorIO.Pwm;

/**
 * Class representing the motor of a physical robot. Provides methods for
 * controlling speed and powering of the motor.
 * 
 * @author osmidy
 *
 */
public class Motor {
    private final Pwm pwm;
    private final Gpio dir;
    private final int pwmPin;
    private final int dirPin;
    private String DIR_OUT = "out";
    private final long pwmPointer;
    private final long dirPointer;
    private double speed;
    private final int forwardValue;
    private final int reverseValue;

    // Rep Invariant: Pwm and Gpio are on different pins; Gpio set for output
    private void checkRep() {
        assert pwmPin != dirPin;
    }

    /**
     * Instantiates a Motor. By default, the Motor run in the forward direction.
     * 
     * @param pwmPin
     *            Physical pin number of pwm
     * @param dirPin
     *            Physical pin number of dir
     * @param forwardValue
     *            Pin value that rotates the motor in the forward direction
     * @param reverseValue
     *            Pin value that rotates the motor in the reverse direction
     */
    public Motor(int pwmPin, int dirPin, int forwardValue, int reverseValue) {
        this.pwm = new Pwm(pwmPin);
        this.pwmPin = pwmPin;
        this.pwmPointer = pwm.getPointer();
        this.dir = new Gpio(dirPin, DIR_OUT);
        this.dirPin = dirPin;
        this.dirPointer = dir.getPointer();
        this.forwardValue = forwardValue;
        this.reverseValue = reverseValue;
        this.speed = 0;
        checkRep();
    }

    /**
     * Sets the speed for this Motor
     * 
     * @param speed
     *            a double in the range [0.0, 1.0], indicating the percentage of
     *            power to be applied by the motor
     */
    public synchronized void setSpeed(double speed) {
        if (speed >= 0) {
            this.setForward();
        } else {
            this.setReverse();
        }
        pwm.setSpeed(pwmPointer, speed);
        this.speed = speed;
        checkRep();
    }

    /**
     * @return current speed of this Motor
     */
    public double getSpeed() {
        return pwm.getSpeed(pwmPointer);
    }

    /**
     * Sets the Motor to run in the forward direction.
     */
    private void setForward() {
        dir.write(dirPointer, forwardValue);
        if (speed < 0) {
            speed *= -1;
        }
        checkRep();
    }

    /**
     * Sets the Motor to run in the reverse direction.
     */
    private void setReverse() {
        dir.write(dirPointer, reverseValue);
        if (speed > 0) {
            speed *= -1;
        }
        checkRep();
    }

    /**
     * @return the Pwm pin number of this Motor
     */
    public int getPwmPin() {
        checkRep();
        return pwmPin;
    }

    /**
     * @return the Gpio pin number of this Motor
     */
    public int getGpioPin() {
        checkRep();
        return dirPin;
    }
}
