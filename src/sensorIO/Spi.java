package sensorIO;

/**
 * Class handling trasnfer of data to and from the physical Spi interface.
 * Implements native methods in C++ by JNI.
 * 
 * @author osmidy
 *
 */
public class Spi {
    private final int bus = 0;
    private final long pointer;
    
    static {
        System.loadLibrary("interface");
    }

    /**
     * Constructor method
     */
    public Spi() {
        pointer = this.init(bus);
    }

    /**
     * Instantiates a Spi object. Default bits per word is set to 32.
     * 
     * @param bus
     *            integer representing the bus for the physical Spi object
     * @return a long value for the stored pointer to this object in C++
     */
    private native long init(int bus);

    /**
     * Writes single byte to Spi
     * 
     * @param pointer
     *            long representing pointer to Gpio object in C++
     * @param data
     *            char representing the byte to be written
     * @return data recevied via MISO
     */
    public native char write(long pointer, char data);

    /**
     * Writes buffer of bytes to Spi, as specified in mraa docs. length must be
     * nonnegative.
     */
    public native char write(long pointer, char data, int length);

    /**
     * Sets the bit per word for this Spi, as specified in mraa docs. bits must
     * be nonnegative.
     */
    public native void bitPerWord(long pointer, int bits);
    
    /**
     * @return the stored pointer for this object in C++
     */
    public long getPointer() {
        return this.pointer;
    }

}
