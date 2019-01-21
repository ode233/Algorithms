/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node first, last;
    private int n = 0;

    private class Node {
        private Item item;
        private Node next;
    }

    private class ListIterator implements Iterator<Item> {
        private int k = 0;
        private int[] rd = StdRandom.permutation(n);

        @Override

        public boolean hasNext() {
            return k != n;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("not support remove");
        }

        @Override
        public Item next() {
            Node current = first;
            if (!hasNext() || isEmpty())
                throw new NoSuchElementException("RandomizedQueue is exhausted");
            for (int i = 0; i < rd[k]; i++) {
                current = current.next;
            }
            k++;
            return current.item;
        }
    }

    private void noel() {
        if (n == 0) throw new NoSuchElementException("RandomizedQueue is empty");
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

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        }
        else oldlast.next = last;
        n++;
    }

    public Item dequeue() {
        noel();
        Item item;
        Node denodeFront = first;
        int rn = StdRandom.uniform(n);
        if (rn == 0) {
            item = first.item;
            first = first.next;
            if (isEmpty()) {
                last = null;
            }
        }
        else if (rn == n - 1) {
            Node frontLast = first;
            item = last.item;
            for (; rn > 1; rn--) {
                frontLast = frontLast.next;
            }
            last = frontLast;
        }
        else {
            for (; rn > 1; rn--) {
                denodeFront = denodeFront.next;
            }
            item = denodeFront.next.item;
            denodeFront.next = denodeFront.next.next;
        }
        n--;
        return item;
    }

    public Item sample() {
        noel();
        Item item;
        Node sample = first;
        int rn = StdRandom.uniform(n);
        for (; rn > 0; rn--) {
            sample = sample.next;
        }
        item = sample.item;
        return item;
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        for (int i = 0; i < args.length; i++) {
            rq.enqueue(args[i]);
        }
        for (String s : rq) StdOut.println(s);
        StdOut.println(rq.size());
        StdOut.println(rq.dequeue());
    }
}
