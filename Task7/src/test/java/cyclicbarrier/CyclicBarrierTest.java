package cyclicbarrier;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class CyclicBarrierTest {

    private final static int numParties = 5;

    private final static int breakTimes = 3;

    private final static int baseSleepTimeMs = 500;

    private static ArrayList<Integer> expectedSortedArr = new ArrayList<>(numParties);

    @Test
    void test() throws InterruptedException {
        for (int i = 0; i < numParties; i++) {
            expectedSortedArr.add(i);
        }
        Thread[] threads = new Thread[numParties];
        AtomicBoolean barrierActionExecuted = new AtomicBoolean(false);
        // creating barrier
        ArrayList<Integer> arr = new ArrayList<>(numParties);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numParties, () -> {
            barrierActionExecuted.set(true);
            Collections.sort(arr);
            assertEquals(expectedSortedArr, arr);
        });
        for (int t = 0; t < breakTimes; t++) {
            // starting threads
            for (int i = 0; i < numParties; i++) {
                int finalI = i;
                threads[i] = new Thread(() -> {
                    try {
                        Thread.sleep(baseSleepTimeMs * (finalI + 1));
                        arr.add(finalI);
                        System.out.println(finalI);
                        cyclicBarrier.await();
                        // should be done after assertion in barrier action
                        arr.add(finalI);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                });
                threads[i].start();
            }

            // join
            for (Thread thread: threads) thread.join();

            // reset
            arr.clear();
            cyclicBarrier.reset();
        }
        assertTrue(barrierActionExecuted.get());
    }
}