import robotparts.Gyroscope;


public class TestGyro {
    static Gyroscope gyro = new Gyroscope(10);
    static long chip = gyro.getChipPointer();
    static long spi = gyro.getSpiPointer();
    
    public static void main(String[] f) throws InterruptedException {
        while (true) {
            gyro.getHeading(chip, spi);
            Thread.sleep(2000);
        }
    }
}
