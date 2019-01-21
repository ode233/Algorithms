/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class BruteCollinearPoints {
    private int num = 0;
    private LineSegment[] lsStore;


    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null");
        }
        int numOfpoint = points.length;
        int len = numOfpoint;
        int maxLen = (len - 3) * (len + 2) / 6;
        if (len < maxLen) len = maxLen;
        for (int i = 0; i < numOfpoint; i++) {
            if (points[i] == null) throw new IllegalArgumentException("point is null");
            for (int j = i + 1; j < numOfpoint; j++) {
                if (points[i].equals(points[j]))
                    throw new IllegalArgumentException("point is duplicate");
            }
        }
        Point[] pointOrder = new Point[numOfpoint];
        for (int i = 0; i < numOfpoint; i++) {
            pointOrder[i] = points[i];
        }
        sort(pointOrder);
        lsStore = new LineSegment[len];
        for (int i = 0; i < numOfpoint - 3; i++)
            for (int j = i + 1; j < numOfpoint - 2; j++)
                for (int k = j + 1; k < numOfpoint - 1; k++)
                    for (int z = k + 1; z < numOfpoint; z++) {
                        if (pointOrder[i].slopeOrder().compare(pointOrder[j], pointOrder[k]) == 0
                                && pointOrder[i].slopeOrder().compare(pointOrder[k], pointOrder[z])
                                == 0) {
                            lsStore[num] = new LineSegment(pointOrder[i], pointOrder[z]);
                            num++;
                        }
                    }
    }

    private void merge(Point[] a, Point[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid + 1, hi);

        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
        assert isSorted(a, lo, hi);
    }

    private void sort(Point[] a, Point[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private void sort(Point[] a) {
        Point[] aux = new Point[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    private boolean isSorted(Point[] a, int first, int end) {
        boolean k = true;
        for (int i = first; i < end; i++) {
            if (a[i].compareTo(a[i + 1]) > 0) {
                k = false;
            }
        }
        return k;
    }


    public int numberOfSegments() {
        return num;
    }

    public LineSegment[] segments() {
        LineSegment[] ls = new LineSegment[num];
        for (int i = 0; i < num; i++) {
            ls[i] = lsStore[i];
        }
        return ls;
    }

    /*    public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            StdDraw.setScale(0, 32767);
            StdDraw.setPenRadius(0.005);
            int n = Integer.parseInt(StdIn.readString());
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(Integer.parseInt(StdIn.readString()),
                                      Integer.parseInt(StdIn.readString()));
                points[i].draw();
            }
            BruteCollinearPoints cp = new BruteCollinearPoints(points);
            for (int i = 0; i < cp.numberOfSegments(); i++) {
                cp.segments()[i].draw();
            }
            System.out.println(cp.numberOfSegments());
            long endTime = System.currentTimeMillis();
            float excTime = (float) (endTime - startTime) / 1000;
            System.out.println("执行时间：" + excTime + "s");
        }*/
    public static void main(String[] args) {

    }

}
