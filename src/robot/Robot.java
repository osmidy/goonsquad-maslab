package robot;

import computervision.Sensor;

/**
 * An interface providing methods to dictate mechanical operations of a physical robot.
 * @author osmidy
 *
 */
public interface Robot {
    // Datatype Definition:  Robot ::= sensors:List<Sensor> + 
   
    
    /**
     * Determines the direction of a Sensor on the Robot.
     * @param sensor   A Sensor mounted on the robot
     * @return the angle, degrees, of the sensor.  Angles are measured
     *          relative to the axis passing through the Robot's center.
     */
    public double getSensorDirection(Sensor sensor);
    
    
}
