/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n = 0;

    private class Node {
        private Item item;
        private Node next;
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("not support remove");
        }

        @Override
        public Item next() {
            if (!hasNext() || isEmpty()) throw new NoSuchElementException("Deque is exhausted");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void argep(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
    }

    private void noel() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
    }


    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void addFirst(Item item) {
        argep(item);
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) {
            last = first;
        }
        else first.next = oldfirst;
        n++;
    }

    public void addLast(Item item) {
        argep(item);
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        }
        else oldlast.next = last;
        n++;
    }

    public Item removeFirst() {
        noel();
        Item item = first.item;
        first = first.next;
        if (n == 1) {
            last = null;
        }
        n--;
        return item;
    }

    public Item removeLast() {
        noel();
        Node frontLast = first;
        Item item = last.item;
        for (int i = 2; i < n; i++) {
            frontLast = frontLast.next;
        }
        last = frontLast;
        last.next = null;
        if (n == 1) {
            first = null;
            last = null;
        }
        n--;
        return item;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Deque<String> deque = new Deque<String>();
        while (!in.isEmpty()) {
            deque.addFirst(in.readString());
        }
        while (!deque.isEmpty())
            StdOut.println(deque.removeLast());
    }
}
