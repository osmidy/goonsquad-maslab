package Test;

public class MIGOS extends Thread {

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 8; i++) {
                System.out.println("HANNAH MONTANA!");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            System.out.println("\n");
            for (int i = 0; i < 2; i++) {
                System.out.println("I GOT MOLLY!");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("I GOT WHITE!");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            System.out.println("\n");
            System.out.println("I BEEN");
            for (int i = 0; i < 4; i++) {
                sleep(375);
                System.out.println("TRAPPIN'!");
                // sleep(100);

            }
            System.out.println("\n");
            sleep(375);
            System.out.println("ALL");
            sleep(375);
            System.out.println("DAMN");
            sleep(375);
            System.out.println("NIGHT!!!");
            System.out.println("\n");
            new MIGOS().start();
            sleep(375);
//            System.out.print("\033[H\033[2J");
            

        }
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void run() {
//        System.out.print("\033[H\033[2J");
        System.out.println("MIGOS!");
    }
}
