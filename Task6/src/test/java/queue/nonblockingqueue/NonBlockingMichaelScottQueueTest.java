package queue.nonblockingqueue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class NonBlockingMichaelScottQueueTest {

    private final static int testQueueSize = 100;

    private final static int numThreads = 4;

    private static ArrayList<Integer> values;

    @BeforeAll
    static void beforeAll() {
        values = new ArrayList<>(testQueueSize);
        for (int i = 0; i < testQueueSize; i++) {
            values.add(i);
        }
    }

    @Test
    void oneThreadTest() {
        NonBlockingMichaelScottQueue<Integer> queue = new NonBlockingMichaelScottQueue<>();
        for (Integer value : values) {
            queue.enqueue(value);
        }
        int i = 0;
        while (true) {
            Integer value = queue.dequeue();
            if (value == null) {
                break;
            }
            else {
                assertEquals(values.get(i), value);
                i++;
            }
        }
        assertEquals(testQueueSize, i);
    }

    @Test
    void multipleThreadEnqueueTest() throws InterruptedException {
        NonBlockingMichaelScottQueue<Integer> queue = new NonBlockingMichaelScottQueue<>();
        Thread[] threads = new Thread[numThreads];
        int threadValuesSize = testQueueSize / numThreads;
        // enqueue
        for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
            int finalThreadIndex = threadIndex;
            threads[threadIndex] = new Thread(() -> {
                int start = finalThreadIndex * threadValuesSize;
                int end = start + threadValuesSize;
                if (end > testQueueSize) end = testQueueSize;
                for (int i = start; i < end; i++) {
                    queue.enqueue(values.get(i));
                }
            });
            threads[threadIndex].start();
        }

        // join
        for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
            threads[threadIndex].join();
        }

        // single thread dequeue
        int i = 0;
        while (true) {
            Integer value = queue.dequeue();
            if (value == null) {
                break;
            }
            else {
                //System.out.println(value);
                assertTrue(values.contains(value));
                i++;
            }
        }
        assertEquals(testQueueSize, i);
    }

    @Test
    void multipleThreadDequeueTest() throws InterruptedException {
        NonBlockingMichaelScottQueue<Integer> queue = new NonBlockingMichaelScottQueue<>();
        Thread[] threads = new Thread[numThreads];
        // single thread enqueue
        for (Integer value : values) {
            queue.enqueue(value);
        }

        // dequeue
        AtomicInteger i = new AtomicInteger(0);
        for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
            threads[threadIndex] = new Thread(() -> {
                while (true) {
                    Integer value = queue.dequeue();
                    if (value == null) {
                        break;
                    }
                    else {
                        //System.out.println(value);
                        assertTrue(values.contains(value));
                        i.incrementAndGet();
                    }
                }
            });
            threads[threadIndex].start();
        }
        // join
        for (int threadIndex = 0; threadIndex < numThreads; threadIndex++) {
            threads[threadIndex].join();
        }
        assertEquals(testQueueSize, i.get());
    }
}