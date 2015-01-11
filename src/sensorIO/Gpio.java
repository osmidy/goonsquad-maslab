package sensorIO;

/**
 * A class which handles transfer of data to and from physical robot sensors
 * over the Gpio interface. Uses JNI to call for implementation of methods in
 * C++, utilizing methods in the libmraa library.
 * 
 * @author osmidy
 *
 */
public class Gpio {
    private final int pin;

    // Library for input methods
    static {
        System.loadLibrary("gpio");
    }

    /**
     * Constructor method
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     */
    public Gpio(int pin) {
        this.pin = pin;
        this.init(pin);
    }

    /**
     * Creates an instance of the Gpio input pin. Default Gpio direction is set
     * to DIR_OUT
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     */
    // First create fields for pinInterface and pin, then run this method
    public native void init(int pin);

    /**
     * Reads input value from the Gpio pin
     * 
     * @return the current value on the Gpio
     */
    public native int read();

    /**
     * Writes output value to the Gpio pin
     * 
     */
    public native void write();

    /**
     * Changes direction on the pin
     * 
     * @param dir
     *            String indicating the direction to be set, either "in" or
     *            "out"
     */
    public native void setDirection(String dir);

}
