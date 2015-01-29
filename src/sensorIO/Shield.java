package sensorIO;

/**
 * Represents a physical I2c shield.
 * 
 * @author Olivier
 *
 */
public class Shield {
    private final int bus;
    private final long pointer;
    
    static {
        System.loadLibrary("interface");
    }
    
    public Shield(int bus) {
        this.bus = bus;
        pointer = this.init(bus);
    }
    
    public long getPointer() {
        return this.pointer;
    }
    
    private native long init(int bus);

}
