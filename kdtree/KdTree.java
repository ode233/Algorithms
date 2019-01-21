/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int count;

    public KdTree() {

    }

    private static class Node {
        private Point2D point;
        private Node left, right;

        private Node(Point2D point) {
            this.point = point;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return count;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (!contains(p)) {
            root = put(root, p, 1);
            count++;
        }
    }

    private Node put(Node x, Point2D p, int layer) {
        if (x == null) return new Node(p);
        int cmp = -1;
        if (layer == 1) {
            if (p.x() >= x.point.x()) {
                cmp = 1;
            }
            layer = 2;
        }
        else if (layer == 2) {
            if (p.y() >= x.point.y()) {
                cmp = 1;
            }
            layer = 1;
        }
        if (cmp < 0) {
            x.left = put(x.left, p, layer);
        }
        else {
            x.right = put(x.right, p, layer);
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        Node x = root;
        for (int i = 1; x != null; i++) {
            int cmp = 0;
            if (i % 2 != 0) {
                if (p.x() > x.point.x()) {
                    cmp = 1;
                }
                if (p.x() < x.point.x()) {
                    cmp = -1;
                }
            }
            else {
                if (p.y() > x.point.y()) {
                    cmp = 1;
                }
                if (p.y() < x.point.y()) {
                    cmp = -1;
                }
            }
            if (cmp < 0) {
                x = x.left;
            }
            else if (cmp > 0) {
                x = x.right;
            }
            else {
                if (p.equals(x.point)) {
                    return true;
                }
                x = x.right;
            }
        }
        return false;
    }

    public void draw() {
        fordraw(root);
    }

    private void fordraw(Node x) {
        if (x != null) {
            x.point.draw();
            fordraw(x.left);
            fordraw(x.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("argument is null");
        }
        Queue<Point2D> queue = new Queue<Point2D>();
        searchRange(root, rect, queue, 1);
        return queue;
    }

    private void searchRange(Node x, RectHV rect, Queue<Point2D> queue, int layer) {
        if (x == null) {
            return;
        }
        if (x.point.x() <= rect.xmax() && x.point.x() >= rect.xmin()
                && x.point.y() <= rect.ymax() && x.point.y() >= rect.ymin()) {
            queue.enqueue(x.point);
        }
        if (layer == 1) {
            if (x.point.x() <= rect.xmin()) {
                searchRange(x.right, rect, queue, 2);
            }
            else if (x.point.x() > rect.xmax()) {
                searchRange(x.left, rect, queue, 2);
            }
            else {
                searchRange(x.left, rect, queue, 2);
                searchRange(x.right, rect, queue, 2);
            }
        }
        else if (layer == 2) {
            if (x.point.y() <= rect.ymin()) {
                searchRange(x.right, rect, queue, 1);
            }
            else if (x.point.y() > rect.ymax()) {
                searchRange(x.left, rect, queue, 1);
            }
            else {
                searchRange(x.left, rect, queue, 1);
                searchRange(x.right, rect, queue, 1);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (isEmpty()) return null;
        return searchNearest(root, p, root.point, 1);
    }

    private Point2D searchNearest(Node x, Point2D p, Point2D nearest, int layer) {
        if (x == null) {
            return nearest;
        }
        if (x.point.equals(p)) {
            return x.point;
        }
        double xp = x.point.distanceSquaredTo(p);
        if (xp < nearest.distanceSquaredTo(p)) {
            nearest = x.point;
        }
        if (x.left == null && x.right == null) {
            return nearest;
        }
        double dx = (x.point.x() - p.x()) * (x.point.x() - p.x());
        double dy = (x.point.y() - p.y()) * (x.point.y() - p.y());
        if (layer == 1) {
            if (p.x() < x.point.x()) {
                if (x.left == null) {
                    if (xp <= dx) {
                        return nearest;
                    }
                    else nearest = searchNearest(x.right, p, nearest, 2);
                }
                else if (x.left.point.distanceSquaredTo(p) <= dx) {
                    nearest = searchNearest(x.left, p, nearest, 2);
                }
                else {
                    nearest = searchNearest(x.left, p, nearest, 2);
                    if (nearest.distanceSquaredTo(p) <= dx)
                        return nearest;
                    nearest = searchNearest(x.right, p, nearest, 2);
                }
            }
            else if (p.x() > x.point.x()) {
                if (x.right == null) {
                    if (xp <= dx) {
                        return nearest;
                    }
                    else nearest = searchNearest(x.left, p, nearest, 2);
                }
                else if (x.right.point.distanceSquaredTo(p) <= dx) {
                    nearest = searchNearest(x.right, p, nearest, 2);
                }
                else {
                    nearest = searchNearest(x.right, p, nearest, 2);
                    if (nearest.distanceSquaredTo(p) <= dx)
                        return nearest;
                    nearest = searchNearest(x.left, p, nearest, 2);
                }
            }
            else {
                nearest = searchNearest(x.left, p, nearest, 2);
                if (nearest.equals(p)) {
                    return nearest;
                }
                nearest = searchNearest(x.right, p, nearest, 2);
            }
        }
        else {
            if (p.y() < x.point.y()) {
                if (x.left == null) {
                    if (xp <= dy) {
                        return nearest;
                    }
                    else nearest = searchNearest(x.right, p, nearest, 1);
                }
                else if (x.left.point.distanceSquaredTo(p) <= dy) {
                    nearest = searchNearest(x.left, p, nearest, 1);
                }
                else {
                    nearest = searchNearest(x.left, p, nearest, 1);
                    if (nearest.distanceSquaredTo(p) <= dy)
                        return nearest;
                    nearest = searchNearest(x.right, p, nearest, 1);
                }
            }
            else if (p.y() > x.point.y()) {
                if (x.right == null) {
                    if (xp <= dy) {
                        return nearest;
                    }
                    else nearest = searchNearest(x.left, p, nearest, 1);
                }
                else if (x.right.point.distanceSquaredTo(p) <= dy) {
                    nearest = searchNearest(x.right, p, nearest, 1);
                }
                else {
                    nearest = searchNearest(x.right, p, nearest, 1);
                    if (nearest.distanceSquaredTo(p) <= dy)
                        return nearest;
                    nearest = searchNearest(x.left, p, nearest, 1);
                }
            }
            else {
                nearest = searchNearest(x.left, p, nearest, 1);
                if (nearest.equals(p)) {
                    return nearest;
                }
                nearest = searchNearest(x.right, p, nearest, 1);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            Point2D query = new Point2D(0.37, 0.99);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();


            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
