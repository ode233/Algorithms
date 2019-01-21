/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF pe;
    private boolean[] pb;
    private int num = 0;
    private int limit;
    private boolean[] topct;
    private boolean[] botct;
    private boolean syspl = false;

    public Percolation(int n) {
        validate(n);
        pe = new WeightedQuickUnionUF(n * n);
        topct = new boolean[n * n];
        botct = new boolean[n * n];
        limit = n;
        pb = new boolean[n * n];
        for (int i = 0; i < n * n; i++) {
            pb[i] = false;
            topct[i] = false;
            botct[i] = false;
        }
    }

    private int toidx(int row, int col) {
        return (row - 1) * limit + col - 1;
    }

    private void validate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("index " + n + " should > 0 ");
        }
    }

    public void open(int row, int col) {
        validate(row, col);
        int idx = toidx(row, col);
        boolean top = false;
        boolean bot = false;
        if (!isOpen(row, col)) {
            pb[idx] = true;
            num++;
        }
        if (row == 1) {
            top = true;
        }
        if (row == limit) {
            bot = true;
        }
        if (row - 1 > 0 && isOpen(row - 1, col)) {
            if (topct[pe.find(idx - limit)] || topct[pe.find(idx)]) {
                top = true;
            }
            if (botct[pe.find(idx - limit)] || botct[pe.find(idx)]) {
                bot = true;
            }
            pe.union(idx, idx - limit);
        }
        if (row + 1 <= limit && isOpen(row + 1, col)) {
            if (topct[pe.find(idx + limit)] || topct[pe.find(idx)]) {
                top = true;
            }
            if (botct[pe.find(idx + limit)] || botct[pe.find(idx)]) {
                bot = true;
            }
            pe.union(idx, idx + limit);
        }
        if (col - 1 > 0 && isOpen(row, col - 1)) {
            if (topct[pe.find(idx - 1)] || topct[pe.find(idx)]) {
                top = true;
            }
            if (botct[pe.find(idx - 1)] || botct[pe.find(idx)]) {
                bot = true;
            }
            pe.union(idx, idx - 1);
        }
        if (col + 1 <= limit && isOpen(row, col + 1)) {
            if (topct[pe.find(idx + 1)] || topct[pe.find(idx)]) {
                top = true;
            }
            if (botct[pe.find(idx + 1)] || botct[pe.find(idx)]) {
                bot = true;
            }
            pe.union(idx, idx + 1);
        }
        topct[pe.find(idx)] = top;
        botct[pe.find(idx)] = bot;
        if (topct[pe.find(idx)] && botct[pe.find(idx)]) {
            syspl = true;
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return pb[toidx(row, col)];
    }

    public int numberOfOpenSites() {
        return num;
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int idx = toidx(row, col);
        return topct[pe.find(idx)];
    }

    public boolean percolates() {
        return syspl;
    }

    private void validate(int row, int col) {
        if (row < 1 || row > limit || col < 1 || col > limit) {
            throw new IllegalArgumentException("row or col  should between 1 and " + limit);
        }
    }

    public static void main(String[] args) {
        Percolation pl = new Percolation(3);
        pl.open(1, 1);
        pl.open(2, 1);
        pl.open(3, 1);
        // pe.open(3, 3);
        pl.open(3, 2);
        System.out.println(pl.isFull(3, 2));
    }
}
