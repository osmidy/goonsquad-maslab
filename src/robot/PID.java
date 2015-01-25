package robot;

/**
 * The name says it all.
 * 
 * @author osmidy
 *
 */
public class PID {
    private final double p;
    private final double i;
    private final double d;
    private final double diff;

    public PID(double p, double i, double d, double error) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.diff = error;
    }

    public double computePower(double integral, double derivative) {
        return p * diff + i * integral + d * derivative;
    }
}
