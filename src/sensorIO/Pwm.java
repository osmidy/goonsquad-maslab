package sensorIO;

/**
 * A class which handles transfer of data to and from physical robot sensors
 * over the Pwm interface. Uses JNI to call for implementation of methods in
 * C++, utilizing methods in the libmraa library.
 * 
 * @author osmidy
 *
 */
public class Pwm {
    private final int pin;

    // Library for input methods
    static {
        System.loadLibrary("pwm");
    }

    /**
     * Constructor method
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     */
    public Pwm(int pin) {
        this.pin = pin;
        this.init(pin);
    }

    /**
     * Creates an instance of a Pwm object. Default enable status and speed of
     * Pwm are respectively set to true and 0.0.
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     */
    public native void init(int pin);

    /**
     * Writes a value for the speed over the Pwm interface
     * 
     * @param speed
     *            value for the speed to be assigned to the sensor on this Pwm
     *            pin. Requires a value in the range [0.0, 1.0]
     */
    public native void setSpeed(double speed);

    /**
     * @return the current speed on the Pwm sensor
     */
    public native double getSpeed();

}
