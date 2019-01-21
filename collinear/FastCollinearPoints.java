/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;


public class FastCollinearPoints {
    private int num = 0;
    private LineSegment[] lsMid;

    public FastCollinearPoints(Point[] points) {
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
        lsMid = new LineSegment[len];
        Point[] pointOrder = new Point[numOfpoint];
        for (int i = 0; i < numOfpoint; i++) {
            pointOrder[i] = points[i];
        }
        MergeX.sort(pointOrder);
        int[][] duplAfter = new int[numOfpoint][numOfpoint];
        int[] duplDistance = new int[numOfpoint];
        for (int i = 0; i < numOfpoint - 3; i++) {
            Point[] pointTemp;
            int lenOfTemp;
/*            System.out.println(i);
            for (int j = 0; j < numOfpoint; j++) {
                for (int k = 0; k < duplDistance[j]; k++) System.out.print(duplAfter[j][k]);
                System.out.println();
            }*/
            if (duplDistance[i] > 0) {
                lenOfTemp = numOfpoint - i - 1 - duplDistance[i];
                pointTemp = new Point[lenOfTemp];
                for (int j = i + 1, k = 0; j < numOfpoint; j++) {
                    boolean noDupl = true;
                    for (int z = 0; z < duplDistance[i]; z++) {
                        if (j == duplAfter[i][z]) {
                            noDupl = false;
                            break;
                        }
                    }
                    if (noDupl) {
                        pointTemp[k] = pointOrder[j];
                        k++;
                    }
                }
            }
            else {
                lenOfTemp = numOfpoint - i - 1;
                pointTemp = new Point[lenOfTemp];
                for (int j = i + 1; j < numOfpoint; j++) {
                    pointTemp[j - i - 1] = pointOrder[j];
                }
            }
            MergeX.sort(pointTemp, pointOrder[i].slopeOrder());
            for (int k = 0; k < lenOfTemp - 2; k++) {
                double slope = pointOrder[i].slopeTo(pointTemp[k]);
                if (pointOrder[i].slopeTo(pointTemp[k + 2]) == slope) {
                    int count = 2;
                    while (k + count < lenOfTemp - 1
                            && pointOrder[i].slopeTo(pointTemp[k + count + 1]) == slope) {
                        count++;
                    }
                    if (count > 2) {
                        int[] idx = new int[count + 1];
                        for (int z = 0; z < count + 1; z++) {
                            for (int z1 = i + 1; z1 < numOfpoint; z1++) {
                                if (pointOrder[z1].equals(pointTemp[k + z])) {
                                    idx[z] = z1;
                                }
                            }
                        }
                        for (int z = 0; z < count - 2; z++) {
                            for (int z2 = 0; z2 < count - z; z2++) {
                                duplAfter[idx[z]][duplDistance[idx[z]] + z2] = idx[z2 + z + 1];
                            }
                            duplDistance[idx[z]] += count - z;
                        }
                    }
                    k = k + count;
                    lsMid[num] = new LineSegment(pointOrder[i], pointTemp[k]);
                    num++;
                }
            }
        }
    }

    public int numberOfSegments() {
        return num;
    }

    public LineSegment[] segments() {
        LineSegment[] ls = new LineSegment[num];
        for (int i = 0; i < num; i++) {
            ls[i] = lsMid[i];
        }
        return ls;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        StdDraw.setScale(-1000, 32767);
        StdDraw.setPenRadius(0.005);
        int n = Integer.parseInt(StdIn.readString());
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(Integer.parseInt(StdIn.readString()),
                                  Integer.parseInt(StdIn.readString()));
            points[i].draw();
        }
        FastCollinearPoints cp = new FastCollinearPoints(points);
        for (int i = 0; i < cp.numberOfSegments(); i++) {
            cp.segments()[i].draw();
        }
        System.out.println(cp.numberOfSegments());
        long endTime = System.currentTimeMillis();
        double excTime = (double) (endTime - startTime) / 1000;
        System.out.println("执行时间：" + excTime + "s");
    }

/*    public static void main(String[] args) {

    }*/

}
