package phaser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Phaser {
    Map<Integer, Integer> phasePartiesAwait = new HashMap<>();
    Map<Long, Integer> threadPhase = new HashMap<>();
    volatile Integer curPhase = 0;

    public synchronized void register() {
        threadPhase.put(Thread.currentThread().getId(), curPhase);
        incPartiesAwait(curPhase);
    }

    private synchronized void incPartiesAwait(Integer phase) {
        if (phasePartiesAwait.containsKey(phase)) {
            phasePartiesAwait.put(phase, phasePartiesAwait.get(phase) + 1);
        }
        else {
            phasePartiesAwait.put(phase, 1);
        }
    }

    private synchronized Integer arriveWithoutPhaseChange() {
        Integer phase = threadPhase.get(Thread.currentThread().getId());
        phasePartiesAwait.put(phase, phasePartiesAwait.get(phase) - 1);
        if (phasePartiesAwait.get(phase) == 0 && Objects.equals(curPhase, phase)) {
            phasePartiesAwait.remove(curPhase);
            curPhase++;
            notifyAll();
        }
        return phase;
    }

    private synchronized void moveToNextPhase() {
        Integer phase = threadPhase.get(Thread.currentThread().getId());
        threadPhase.put(Thread.currentThread().getId(), phase + 1);
        incPartiesAwait(phase + 1);
    }


    public synchronized Integer arrive() {
        if (threadPhase.containsKey(Thread.currentThread().getId())) {
            Integer arrivedPhase = arriveWithoutPhaseChange();
            moveToNextPhase();
            return arrivedPhase;
        }
        return null;
    }

    public synchronized Integer arriveAndDeregister() {
        System.out.println("arriveAndDeregister");
        if (threadPhase.containsKey(Thread.currentThread().getId())) {
            Integer arrivedPhase = arriveWithoutPhaseChange();
            threadPhase.remove(Thread.currentThread().getId());
            return arrivedPhase;
        }
        return null;
    }

    public synchronized void awaitAdvance(int phase) throws InterruptedException {
        while (phase >= curPhase) {
            wait();
        }
    }

    public synchronized void arriveAndAwaitAdvance() throws InterruptedException {
        if (threadPhase.containsKey(Thread.currentThread().getId())) {
            Integer arrivedPhase = arriveWithoutPhaseChange();
            awaitAdvance(arrivedPhase);
            moveToNextPhase();
        }
    }

}
