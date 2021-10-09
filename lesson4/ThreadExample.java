import java.util.HashSet;
import java.util.List;

public class ThreadExample {
    private final Object monitor = new Object();
    private volatile char orderedLetter = 'A';
    private final HashSet<Character> validCharSet = new HashSet<>(List.of('A', 'B', 'C'));

    public static void main(String[] args) {
        ThreadExample instance = new ThreadExample();
        Thread a = new Thread(() -> instance.printLetter('A'));
        Thread b = new Thread(() -> instance.printLetter('B'));
        Thread c = new Thread(() -> instance.printLetter('C'));

        a.start();
        b.start();
        c.start();
    }

    private void printLetter(char letter) {
        synchronized (this.monitor) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (letter != this.orderedLetter) {
                        this.monitor.wait();
                    }
                    System.out.print(letter);
                    if (!validCharSet.contains(++this.orderedLetter)) {
                        this.orderedLetter = 'A';
                    }
                    this.monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
