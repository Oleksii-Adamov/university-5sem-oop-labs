package queue.nonblockingqueue;

import java.util.concurrent.atomic.AtomicReference;

public class Node<E> {
    private E value;
    private AtomicReference<Node<E>> next;

    public Node() {
        this.next = new AtomicReference<>(null);
    }

    public Node(E value) {
        this.value = value;
        this.next = new AtomicReference<>(null);
    }

    public Node(E value, Node next) {
        this.value = value;
        this.next = new AtomicReference<>(next);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public AtomicReference<Node<E>> getNext() {
        return next;
    }
}
