package building;

import java.awt.Point;
import java.util.Collection;

public class PointUtils {
    
    /**
     * @param points a collection of points.
     * @return minX, maxX, minY, maxY
     */
    public static int[] getExtrema(Collection<Point> points) {
        if (points.isEmpty()) {
            return null;
        } else {
            Point[] extrema = getExtremePoints(points);
            return new int[] {extrema[0].x, extrema[1].x, 
                    extrema[2].y, extrema[3].y};
        }
    }
    
    /**
     * @param points a collection of points.
     * @return minX, maxX, minY, maxY
     */
    public static Point[] getExtremePoints(Collection<Point> points) {
        if (points.isEmpty()) {
            return new Point[]{null, null, null, null};
        } else {
            Point p1  = points.iterator().next();
            Point[] extrema = {p1, p1, p1, p1};
            for (Point p : points) {
                extrema[0] = (p.x < extrema[0].x) ? p : extrema[0];
                extrema[1] = (p.x > extrema[1].x) ? p : extrema[1];
                extrema[2] = (p.y < extrema[2].y) ? p : extrema[2];
                extrema[3] = (p.y > extrema[3].y) ? p : extrema[3];
            }
            return extrema;
        }
    }
    
    public static Point subtract(Point p1, Point p2) {
        return new Point(p1.x - p2.x, p1.y - p2.y);
    }
    
    public static Point add(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

}
