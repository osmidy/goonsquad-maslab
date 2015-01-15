package sensorIO;

/**
 * A class which handles transfer of data to and from physical robot sensors
 * over the Aio interface. Uses JNI to call for implementation of methods in
 * C++, utilizing methods in the libmraa library.
 * 
 * @author osmidy
 *
 */
public class Aio {
    private final int pin;
    private final long pointer;

    // Library for interfaces
    static {
        System.loadLibrary("interface");
    }

    /**
     * Constructor method
     * 
     * @param pin
     *            an integer representing the physical pin number for this Aio
     *            object
     */
    public Aio(int pin) {
        this.pin = pin;
        this.pointer = this.init(pin);
    }

    /**
     * Instantiates the Aio object.
     * 
     * @param pin
     *            physical pin number
     * @return a long value for the stored pointer to this object in C++
     */
    private native long init(int pin);

    /**
     * Retrieves the value on the Aio pin.
     * 
     * @param pointer
     *            long representing pointer to Aio object in C++
     * @return the voltage value read from the Aio pin
     */
    public native int read(long pointer);
    
    /**
     * Implements setBit, as specified in mraa docs.
     */
    public native void setBit(long pointer, int bits);
    
    /**
     * Implements getBit, as specified in mraa docs.
     */
    public native int getBit(long pointer);
    
    /**
     * @return the C++ pointer for this object
     */
    public long getPointer() {
        return this.pointer;
    }

}
