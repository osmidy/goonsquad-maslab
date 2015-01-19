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
    private final int aioPin;ojgh 
    private final long aioPointer;

    /**
     * Constructor method.
     * 
     * @param aioPin
     *            physical pin for the Sensor.
     */
    public IRSensor(int aioPin) {
        this.aioPin = aioPin;
        aio = new Aio(this.aioPin);
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
     * @return the approximate distance, in meters, to the nearest object
     */
    private double computeDistance(double voltage) {
        double exp = 1.257;
        double cmToM = .01;
        double distance = 8592.3 * Math.pow(voltage, exp) * cmToM;
        return distance;
    }

}
