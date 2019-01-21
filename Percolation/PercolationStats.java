/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] p;
    private double mean = -1;
    private double std = -1;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("index n and trials should > 0 ");
        p = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation per = new Percolation(n);
            int t = 0;
            while (!per.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!per.isOpen(row, col)) {
                    per.open(row, col);
                    t++;
                }
            }
            p[i] = (double) t / (double) (n * n);
        }
    }

    public double mean() {
        mean = StdStats.mean(p);
        return mean;
    }

    public double stddev() {
        std = StdStats.stddev(p);
        return std;
    }

    public double confidenceLo() {
        if (mean == -1) mean();
        if (std == -1) stddev();
        double lo = mean - CONFIDENCE_95 * std / Math.sqrt(p.length);
        return lo;
    }

    public double confidenceHi() {
        if (mean == -1) mean();
        if (std == -1) stddev();
        double hi = mean + CONFIDENCE_95 * std / Math.sqrt(p.length);
        return hi;
    }

    public static void main(String[] args) {
        int n = 2;
        int t = 10;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("mean = " + ps.mean());
        System.out.println("stddv = " + ps.stddev());
        System.out.println(
                "95% confidence interval = [ " + ps.confidenceLo() + ", " + ps.confidenceHi()
                        + " ]");
        // PercolationStats ps = new PercolationStats(10, 10);
        // double k = ps.mean();
        // System.out.println(1);
    }
}
