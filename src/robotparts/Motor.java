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
    public Motor(Pwm pwm, int pwmPin, Gpio dir, int dirPin) {
        this.pwm = pwm;
        this.pwmPin = pwmPin;
        this.pwmPointer = pwm.getPointer();
        this.dir = dir;
        this.dirPin = dirPin;
        this.dirPointer = dir.getPointer();
        checkRep();
    }

    /**
     * Sets the speed for this Motor
     * 
     * @param speed
     *            a double in the range [0.0, 1.0], indicating the percentage of
     *            power to be applied by the motor
     */
    public void setSpeed(double speed) {
        if (speed >= 0) {
            this.setForward();
        } else {
            this.setReverse();
        }
        pwm.setSpeed(pwmPointer, speed);
        checkRep();
    }

    /**
     * Sets the Motor to run in the forward direction.
     */
    public void setForward() {
        dir.write(dirPointer, 0);
        checkRep();
    }

    /**
     * Sets the Motor to run in the reverse direction.
     */
    public void setReverse() {
        dir.write(dirPointer, 1);
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
