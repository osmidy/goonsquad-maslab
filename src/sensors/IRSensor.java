package sensors;

import sensorIO.Gpio;

/**
 * A class representing a physical IR sensor. Implements the Sensor interface.
 * 
 * @author osmidy
 *
 */
public class IRSensor implements Sensor {
    private final Gpio gpio;
    private final int gpioPin;
    private final long gpioPointer;

    /**
     * Constructor method.
     * 
     * @param gpioPin
     *            physical pin for the Sensor. direction for this Gpio is "in"
     */
    public IRSensor(int gpioPin) {
        this.gpioPin = gpioPin;
        gpio = new Gpio(this.gpioPin, "in"); // TODO: in or out?
        gpioPointer = gpio.getPointer();
    }

    @Override
    public double distanceToObject() {
        double voltage = gpio.read(gpioPointer);
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
