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
    private final long pointer;

    // Library for input methods
    static {
        System.loadLibrary("interface");
    }

    /**
     * Constructor method
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     * @param direction
     *            a String, either "in" or "out", designating the Gpio for
     *            either input or output
     */
    public Gpio(int pin, String dir) {
        this.pin = pin;
        pointer = this.init(pin, dir);
    }

    /**
     * Creates an instance of the Gpio input pin. Default Gpio direction is set
     * to DIR_OUT for output.  Default Gpio vlue is 1.
     * 
     * @param pin
     *            an integer representing the physical pin number of the sensor
     * @param direction
     *            a String, either "in" or "out", designating the Gpio for
     *            either input or output
     * @return a long value for the stored pointer to this object in C++
     */
    // First create fields for pinInterface and pin, then run this method
    private native long init(int pin, String direction);

    /**
     * Reads input value from the Gpio pin
     * 
     * @param pointer
     *            long representing pointer to Gpio object in C++
     * @return the current value on the Gpio
     */
    public native int read(long pointer);

    /**
     * Writes output value to the Gpio pin
     * 
     * @param pointer
     *            long representing pointer to Gpio object in C++
     * @param value
     *            int value to be written to the Gpio. Must be either 0 (low) or
     *            1 (high)
     */
    public native void write(long pointer, int value);

    /**
     * @return the stored pointer for this object in C++
     */
    public long getPointer() {
        return this.pointer;
    }

}
