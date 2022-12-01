package reentrantlock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReentrantLockTest {

    private final static int numThreads = 3;

    @Test
    void lockUnlockTest() throws InterruptedException {
        Thread[] threads = new Thread[numThreads];
        ReentrantLock reentrantLock = new ReentrantLock();
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                try {
                    reentrantLock.lock();
                    assertTrue(reentrantLock.isHeldByCurrentThread());
                    assertEquals(1, reentrantLock.getHoldCount());
                    reentrantLock.lock();
                    assertTrue(reentrantLock.isHeldByCurrentThread());
                    assertEquals(2, reentrantLock.getHoldCount());
                    reentrantLock.unlock();
                    assertTrue(reentrantLock.isHeldByCurrentThread());
                    assertEquals(1, reentrantLock.getHoldCount());
                    reentrantLock.unlock();
                    assertFalse(reentrantLock.isHeldByCurrentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
        join(threads);
    }

    @Test
    void tryLockTest() throws InterruptedException {
        Thread[] threads = new Thread[2];
        ReentrantLock reentrantLock = new ReentrantLock();
        threads[0] = new Thread(() -> {
            try {
                reentrantLock.lock();
                Thread.sleep(500);
                reentrantLock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads[1] = new Thread(() -> {
            try {
                Thread.sleep(100);
                assertFalse(reentrantLock.tryLock());
                Thread.sleep(500);
                assertTrue(reentrantLock.tryLock());
                reentrantLock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads[0].start();
        threads[1].start();
        join(threads);
    }

    private void join(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }
}