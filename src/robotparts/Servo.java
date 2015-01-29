package robotparts;

import sensorIO.Gpio;
import sensorIO.Pwm;
import sensorIO.Shield;

/**
 * Class providing methods which represent the operations of a physical servo.
 * 
 * @author osmidy
 *
 */
public class Servo {
    private final int index;
    private final Shield shield;
    private final long pointer;
    
    static {
        System.loadLibrary("interface");
    }

    /**
     * Instantiates a Servo.
     * 
     * @param index
     *            index for Shield output for the Servo
     * @param Shield
     *            Shield object connected to the Servo
     */
    public Servo(int index, Shield shield) {
        this.index = index;
        this.shield = shield;
        this.pointer = shield.getPointer();
    }
    
    public void setPosition(double duty) {
        setPosition(pointer, index, duty);
    }
    
    private native void setPosition(long pointer, int index, double duty);
}
