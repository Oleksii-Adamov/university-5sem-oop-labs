package queue.nonblockingqueue;

import queue.Queue;

import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingMichaelScottQueue<E> implements Queue<E> {

    AtomicReference<Node<E>> head;
    AtomicReference<Node<E>> tail;

    public NonBlockingMichaelScottQueue() {
        Node<E> dummy = new Node<>();
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    @Override
    public void enqueue(E value) {
        Node<E> node = new Node<>(value);
        while (true) {
            Node<E> localTail = tail.get();
            if (localTail.getNext().compareAndSet(null, node)) {
                tail.compareAndSet(localTail, node);
                return;
            }
            else {
                // this thead could help to update tail
                tail.compareAndSet(localTail, localTail.getNext().get());
            }
        }
    }

    @Override
    public E dequeue() {
        while (true) {
            Node<E> localHead = head.get();
            Node<E> localTail = tail.get();
            Node<E> nextHead = localHead.getNext().get();
            if (localHead == localTail) {
                if (nextHead == null) {
                    // queue is empty
                    return null;
                }
                else {
                    // this thead could help to update tail
                    tail.compareAndSet(localTail, nextHead);
                }
            }
            else {
                E result = nextHead.getValue();
                if (head.compareAndSet(localHead, nextHead)) {
                    // nextHead now dummy element
                    return result;
                }
            }
        }
    }
}
