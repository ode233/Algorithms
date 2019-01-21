/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;
    private final int size;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("G is null");
        }
        digraph = new Digraph(G);
        size = digraph.V();
    }

    public int length(int v, int w) {
        return single(v, w)[0];
    }

    public int ancestor(int v, int w) {
        return single(v, w)[1];
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return multi(v, w)[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return multi(v, w)[1];
    }

    private int[] single(int v, int w) {
        if (v < 0 || v >= size) {
            throw new IllegalArgumentException("v is out range");
        }
        if (w < 0 || w >= size) {
            throw new IllegalArgumentException("w is out range");
        }
        int[] single = { -1, -1 };
        if (v == w) {
            single[0] = 0;
            single[1] = v;
            return single;
        }
        Queue<Integer> qv = new Queue<Integer>();
        Queue<Integer> qw = new Queue<Integer>();
        int[] markv = new int[size];
        int[] markw = new int[size];
        qv.enqueue(v);
        markv[v] = 1;
        while (!qv.isEmpty()) {
            int deq = qv.dequeue();
            for (int q : digraph.adj(deq)) {
                if (markv[q] == 0) {
                    qv.enqueue(q);
                    markv[q] = markv[deq] + 1;
                }
            }
        }
        if (markv[w] != 0) {
            single[0] = markv[w] - 1;
            single[1] = w;
        }
        qw.enqueue(w);
        markw[w] = 1;
        while (!qw.isEmpty()) {
            int deq = qw.dequeue();
            for (int q : digraph.adj(deq)) {
                if (markw[q] == 0) {
                    qw.enqueue(q);
                    markw[q] = markw[deq] + 1;
                    if (markv[q] != 0) {
                        int tmplength = markv[q] + markw[q] - 2;
                        if (tmplength < single[0] || single[0] == -1) {
                            single[0] = tmplength;
                            single[1] = q;
                        }
                    }
                }
            }
        }
        return single;
    }

    private int[] multi(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException("Itetable is null");
        }
        for (Object q : v) {
            if (q == null) {
                throw new IllegalArgumentException("v have null");
            }
        }
        for (Object q : w) {
            if (q == null) {
                throw new IllegalArgumentException("w have null");
            }
        }
        for (int q : v) {
            if (q < 0 || q >= size) {
                throw new IllegalArgumentException("v is out range");
            }
        }
        for (int q : w) {
            if (q < 0 || q >= size) {
                throw new IllegalArgumentException("w is out range");
            }
        }
        int[] multi = { -1, -1 };
        for (int q : v) {
            for (int p : w) {
                if (q == p) {
                    multi[0] = 0;
                    multi[1] = q;
                    return multi;
                }
            }
        }
        Queue<Integer> qv = new Queue<Integer>();
        Queue<Integer> qw = new Queue<Integer>();
        int[] markv = new int[size];
        int[] markw = new int[size];
        for (int q : v) {
            qv.enqueue(q);
            markv[q] = 1;
        }
        while (!qv.isEmpty()) {
            int deq = qv.dequeue();
            for (int q : digraph.adj(deq)) {
                if (markv[q] == 0) {
                    qv.enqueue(q);
                    markv[q] = markv[deq] + 1;
                }
            }
        }
        for (int q : w) {
            qw.enqueue(q);
            markw[q] = 1;
            if (markv[q] != 0) {
                int tmplength = markv[q] - 1;
                if (tmplength < multi[0] || multi[0] == -1) {
                    multi[0] = tmplength;
                    multi[1] = q;
                }
            }
        }
        while (!qw.isEmpty()) {
            int deq = qw.dequeue();
            for (int q : digraph.adj(deq)) {
                if (markw[q] == 0) {
                    qw.enqueue(q);
                    markw[q] = markw[deq] + 1;
                    if (markv[q] != 0) {
                        int tmplength = markv[q] + markw[q] - 2;
                        if (tmplength < multi[0] || multi[0] == -1) {
                            multi[0] = tmplength;
                            multi[1] = q;
                        }
                    }
                }
            }
        }
        return multi;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
