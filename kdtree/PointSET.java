/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> pointSet;

    public PointSET() {
        this.pointSet = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return pointSet.contains(p);
    }

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("argument is null");
        }
        SET<Point2D> rangeSet = new SET<Point2D>();
        for (Point2D p : pointSet) {
            if ((p.x() >= rect.xmin() && p.x() <= rect.xmax())
                    && (p.y() >= rect.ymin() && p.y() <= rect.ymax())) {
                rangeSet.add(p);
            }
        }
        return rangeSet;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (isEmpty()) return null;
        Point2D nearest = pointSet.max();
        for (Point2D point : pointSet) {
            if (point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
