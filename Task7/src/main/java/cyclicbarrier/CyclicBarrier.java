package cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;

public class CyclicBarrier {
    int numParties;

    int parties = 0;
    Runnable barrierAction;

    boolean barrierBroken = false;

    public CyclicBarrier(int numParties) {
        if (numParties < 1) {
            throw new IllegalArgumentException("numParties < 1");
        }
        this.numParties = numParties;
        barrierAction = null;
    }

    public CyclicBarrier(int numParties, Runnable barrierAction) {
        if (numParties < 1) {
            throw new IllegalArgumentException("numParties < 1");
        }
        this.numParties = numParties;
        this.barrierAction = barrierAction;
    }

    public synchronized void await() throws InterruptedException, BrokenBarrierException {
        if (barrierBroken) {
            throw new BrokenBarrierException();
        }
        parties++;
        if (parties == numParties) {
            runBarrierAction();
            breakBarrier();
            notifyAll();
        }
        else {
            while (!barrierBroken) {
                wait();
            }
        }
    }

    private void breakBarrier() {
        barrierBroken = true;
    }

    private void runBarrierAction() {
        if (barrierAction != null)
            barrierAction.run();
    }

    public void reset() {
        barrierBroken = false;
        parties = 0;
    }

    public int getNumParties() {
        return numParties;
    }
}
