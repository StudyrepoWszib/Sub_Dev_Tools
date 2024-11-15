public class ProblemWyscigu {
    private static int licznik = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread watek1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                licznik++;
            }
        });

        Thread watek2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                licznik++;
            }
        });

        watek1.start();
        watek2.start();

        watek1.join();
        watek2.join();

        System.out.println("Oczekiwana wartość licznika: 2000");
        System.out.println("Rzeczywista wartość licznika: " + licznik);
    }
}
