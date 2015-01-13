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
    private final String gpioDir = "out";
    private final int chipPin;
    private final Spi spi;

    // Rep Invariant: Gpio direction is "out"
    private void checkRep() {
        assert gpioDir.equals("out");
    }

    /**
     * Constructor method to instantiate a Gyroscope.
     * 
     * @param gpioPin
     *            Pin number for the Gpio interface of this Gyroscoe
     */
    public Gyroscope(int chipPin) {
        this.chipPin = chipPin;
        chipSelect = new Gpio(chipPin, gpioDir);
        spi = new Spi();
        checkRep();
    }
    
    /**
     * Determines the current heading of this Gyroscope.
     * 
     * @return the heading, in degrees, of the Gyroscope
     */
    public double getHeading() {
        // TODO: adjust for drifting
        return 0;
    }
}
