import sensors.UltraSonicSensor;


public class TestUltraSound {
    static UltraSonicSensor us = new UltraSonicSensor(4, 7);
    
    public static void main(String[] v) throws InterruptedException {
        while (true) {
            System.out.println(us.distanceToObject());
            Thread.sleep(2000);
        }
    }
}
