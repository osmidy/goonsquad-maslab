package robotparts;

import sensorIO.Gpio;
import sensorIO.Spi;

/**
 * A class representing the gyroscope on a physical robot. Provides methods for
 * reading and normalizing input information from a gyroscope as necessary.
 * 
 * @author osmidy
 *
 */
public class Gyroscope {
    private final Gpio chipSelect;
    private final long chipPointer;
    private final String gpioDir = "out";
    private final int chipPin;
    private final Spi spi;
    private final long spiPointer;

    private final String DIR_OUT = "out";

    // For native methods
    static {
        System.loadLibrary("interface");
    }

    // Rep Invariant: Gpio direction is "out"
    private void checkRep() {
        assert gpioDir.equals(DIR_OUT);
    }

    /**
     * Constructor method to instantiate a Gyroscope.
     * 
     * @param gpioPin
     *            Pin number for the Gpio interface of this Gyroscoe
     */
    public Gyroscope(int chipPin) {
        this.chipPin = chipPin;
        this.chipSelect = new Gpio(chipPin, gpioDir);
        this.spi = new Spi();
        this.chipPointer = chipSelect.getPointer();
        this.spiPointer = spi.getPointer();
        checkRep();
    }

    /**
     * Determines the current angular velocity of this Gyroscope.
     * 
     * @param chipPointer
     *            a long; points to memory location of Gpio object for this
     *            Gyroscope
     * @param spiPointer
     *            a long; points to memory location of Spi object for this
     *            Gyroscope
     * @return the heading, in degrees/s, of the Gyroscope
     */
    public double getAngularVelocity() {
        return getAngularVelocity(chipPointer, spiPointer);
    }

    private native double getAngularVelocity(long chipPointer, long spiPointer);

    public int getChipPin() {
        return chipPin;
    }
}
