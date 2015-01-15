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

    /**
     * Constructor method.
     * 
     * @param trigPin
     *            pin for output from this Sensor
     * @param echoPiin
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
        // TODO Auto-generated method stub
        return 0;
    }

}
