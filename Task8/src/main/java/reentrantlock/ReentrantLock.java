package reentrantlock;

public class ReentrantLock {

    private long lockedTreadId = -1;
    private int holdCount = 0;

    public synchronized void lock() throws InterruptedException {
        if (holdCount == 0) {
            lockedTreadId = Thread.currentThread().getId();
            holdCount++;
        }
        else if (Thread.currentThread().getId() == lockedTreadId) {
            holdCount++;
        }
        else {
            while (holdCount > 0)
                wait();
            lockedTreadId = Thread.currentThread().getId();
            holdCount++;
        }
    }

    public synchronized void unlock() {
        holdCount--;
        if (holdCount == 0) {
            lockedTreadId = -1;
            notify();
        }
    }

    public synchronized boolean tryLock() {
        if (holdCount == 0) {
            lockedTreadId = Thread.currentThread().getId();
            holdCount++;
            return true;
        }
        else if (Thread.currentThread().getId() == lockedTreadId) {
            holdCount++;
            return true;
        }
        else {
            return false;
        }
    }

    public synchronized int getHoldCount() {
        return holdCount;
    }

    public synchronized boolean isHeldByCurrentThread() {
        return Thread.currentThread().getId() == lockedTreadId;
    }
}
