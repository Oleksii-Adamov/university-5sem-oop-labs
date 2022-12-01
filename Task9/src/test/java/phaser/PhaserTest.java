package phaser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhaserTest {

    private final static int baseSleepTimeMs = 500;

    @Test
    void arriveAndAwaitAdvanceTest() throws InterruptedException {
        int numThreads = 4;
        ArrayList<Integer> expectedSortedArr = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            expectedSortedArr.add(i);
        }
        List<Integer> arr = Collections.synchronizedList(new ArrayList<>());
        Phaser phaser = new Phaser();
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
               try {
                   phaser.register();
                   Thread.sleep(baseSleepTimeMs * (finalI + 1));
                   arr.add(finalI);
                   phaser.arriveAndAwaitAdvance();
                   Collections.sort(arr);
                   assertEquals(expectedSortedArr, arr);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            });
        }
        join(threads);
    }

    @Test
    void arriveTest() throws InterruptedException {
//        System.out.println("arriveTest");
        int numThreads = 2;
        List<Integer> arr = Collections.synchronizedList(new ArrayList<Integer>());
        Phaser phaser = new Phaser();
        Thread[] threads = new Thread[numThreads];
        threads[0] = new Thread(() -> {
            try {
                phaser.register();
//                System.out.println("Registered 0");
                Thread.sleep(100);
                arr.add(0);
                assertEquals(0, phaser.arrive());
//                System.out.println("Arrived 0");
                arr.add(0);
                assertEquals(new ArrayList<>(Arrays.asList(0, 0)), arr);
//                System.out.println("Blocked 0");
                phaser.arriveAndAwaitAdvance();
                assertEquals(new ArrayList<>(Arrays.asList(0, 0, 1, 1)), arr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads[1] = new Thread(() -> {
            try {
                phaser.register();
//                System.out.println("Registered 1");
                Thread.sleep(1000);
                arr.add(1);
//                System.out.println("Blocked 1");
                phaser.arriveAndAwaitAdvance();
                assertEquals(new ArrayList<>(Arrays.asList(0, 0, 1)), arr);
                arr.add(1);
//                System.out.println("Blocked 1");
                phaser.arriveAndAwaitAdvance();
                assertEquals(new ArrayList<>(Arrays.asList(0, 0, 1, 1)), arr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        join(threads);
    }

    @Test
    void arriveAndDeregisterAndAwaitAdvanceTest() throws InterruptedException {
//        System.out.println("arriveAndDeregisterAndAwaitAdvanceTest");
        int numThreads = 3;
        List<Integer> arr = Collections.synchronizedList(new ArrayList<>());
        Phaser phaser = new Phaser();
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(() -> {
            try {
                phaser.register();
                Thread.sleep(baseSleepTimeMs);
//                System.out.println("Registered 0");
                arr.add(0);
                Integer phase = phaser.arriveAndDeregister();
                assertNotNull(phase);
//                System.out.println("Blocking 0");
                phaser.awaitAdvance(phase);
                assertTrue(new ArrayList<>(Arrays.asList(0, 1, 2)).equals(arr) || new ArrayList<>(Arrays.asList(0, 2, 1)).equals(arr));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 1; i < numThreads; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    phaser.register();
//                    System.out.println("Registered " + finalI);
                    Thread.sleep(baseSleepTimeMs * (finalI + 1));
                    arr.add(finalI);
//                    System.out.println("Blocking " + finalI);
                    phaser.arriveAndAwaitAdvance();
                    assertTrue(new ArrayList<>(Arrays.asList(0, 1, 2)).equals(arr) || new ArrayList<>(Arrays.asList(0, 2, 1)).equals(arr));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }
        join(threads);
    }

    private static void join(Thread[] threads) throws InterruptedException {
        for (Thread thread: threads) thread.join();
    }
}