package PID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import robot.Robot;
import robotparts.Gyroscope;
import robotparts.Motor;

public class StayStraightPID implements PID {
    private double p = 0;
    private double i = 0;
    private double d = 0;
    private double diff;
    private double integral = 0;
    private double derivative = 0;

    private final Motor leftMotor;
    private final Motor rightMotor;
    private final Gyroscope gyro;
    private final Robot robot;
    
    private double desiredHeading = 0;
    private double currentHeading = 0;
    
    public StayStraightPID(File file, Robot robot, Motor leftMotor, Motor rightMotor, Gyroscope gyro) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(file));
        for (String line = read.readLine(); line != null; line = read
                .readLine()) {
            String[] tokens = line.split("\\s+");
            String constant = tokens[0];
            double value = Double.parseDouble(tokens[1]);
            if (constant.equals("p")) {
                p = value;
            }
            if (constant.equals("i")) {
                i = value;
            }
            if (constant.equals("d")) {
                d = value;
            }
        }
        
        this.robot = robot;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.gyro = gyro;
        this.desiredHeading = robot.getDesiredHeading();
        this.currentHeading = robot.getCurrentHeading();
    }

    @Override
    public Thread thread() {
        Thread pid = new Thread(new Runnable() {
            public void run() {
                
            }
        });
        return pid;
    }
    
    
}
