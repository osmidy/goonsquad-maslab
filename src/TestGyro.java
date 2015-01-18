import robotparts.Gyroscope;


public class TestGyro {
    static Gyroscope gyro = new Gyroscope(10);
    
    public static void main(String[] f) throws InterruptedException {
        while (true) {
            System.out.println(gyro.getAngularVelocity());
            Thread.sleep(2000);
        }
    }
}
