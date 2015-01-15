package sensors;

import sensorIO.Aio;

/**
 * A class representing a physical IR sensor. Implements the Sensor interface.
 * 
 * @author osmidy
 *
 */
public class IRSensor implements Sensor {
    private final Aio aio;
    private final int aioPin;
    private final long aioPointer;

    /**
     * Constructor method.
     * 
     * @param gpioPin
     *            physical pin for the Sensor. direction for this Gpio is "in"
     */
    public IRSensor(int gpioPin) {
        this.aioPin = gpioPin;
        aio = new Aio(this.aioPin); // TODO: in or out?
        aioPointer = aio.getPointer();
    }

    @Override
    public double distanceToObject() {
        double voltage = aio.read(aioPointer);
        double distance = computeDistance(voltage);
        return distance;
    }

    /**
     * Converts voltage readings to distances.
     * 
     * @param voltage
     *            value read from this Sensor
     * @return the distance, in meters, to the nearest object
     */
    private double computeDistance(double voltage) {
        // TODO: apply formula here
        double distance = voltage * 1;
        return distance;
    }

}
