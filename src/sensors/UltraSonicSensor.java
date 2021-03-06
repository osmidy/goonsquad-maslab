package sensors;

import sensorIO.Gpio;

/**
 * A class representing a physical ultrasound sensor. Implements the Sensor
 * interface.
 * 
 * @author osmidy
 *
 */

public class UltraSonicSensor implements Sensor {
    private final Gpio trig;
    private final int trigPin;
    private final long trigPointer;
    private final Gpio echo;
    private final int echoPin;
    private final long echoPointer;

    private final String DIR_IN = "in";
    private final String DIR_OUT = "out";
    
    static {
        System.loadLibrary("interface");
    }

    /**
     * Constructor method.
     * 
     * @param trigPin
     *            pin for output from this Sensor
     * @param echoPin
     *            pin for input from this Sensor
     */
    public UltraSonicSensor(int trigPin, int echoPin) {
        trig = new Gpio(trigPin, DIR_OUT);
        echo = new Gpio(echoPin, DIR_IN);

        this.trigPin = trigPin;
        this.echoPin = echoPin;
        this.trigPointer = trig.getPointer();
        this.echoPointer = echo.getPointer();
    }

    @Override
    public double distanceToObject() {
        return echoHandle(echoPointer, trigPointer);
    }

    /**
     * Handles rising and falling edges of the echo to obtain a distance
     * reading.
     * 
     * @param echoPointer
     *            pointer in C++ to an echo Gpio
     * @param trigPointer
     *            pointer in C++ to a trig Gpio
     * @return distance to nearest object in meters
     */
    private native double echoHandle(long echoPointer, long trigPointer);

}
