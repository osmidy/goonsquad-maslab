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
     * @param pwm
     *            A Pwm object controlling power to, and speed of, the motor
     * @param pwmPin
     *            Physical pin number of pwm
     * @param dir
     *            A Gpio object controlling direction of the motor
     * @param dirPin
     *            Physical pin number of dir
     */
    public Motor(Pwm pwm, int pwmPin, Gpio dir, int dirPin, int fv, int rv) {
        this.pwm = pwm;
        this.pwmPin = pwmPin;
        this.pwmPointer = pwm.getPointer();
        this.dir = dir;
        this.dirPin = dirPin;
        this.dirPointer = dir.getPointer();
        this.speed = 0;
        this.forwardValue = fv;
        this.reverseValue = rv;
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
        checkRep();
    }

    /**
     * Sets the Motor to run in the reverse direction.
     */
    private void setReverse() {
        dir.write(dirPointer, reverseValue);
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
