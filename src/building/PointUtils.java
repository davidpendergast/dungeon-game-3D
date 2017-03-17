package building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    
    public static int[] getExtrema(Point...points) {
        return getExtrema(Arrays.asList(points));
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
    
    public static boolean inBox(Point p, Point b1, Point b2) {
        int[] extrema = getExtrema(b1, b2);
        return extrema[0] <= p.x && p.x <= extrema[1] 
                && extrema[2] <= p.y && p.y <= extrema[3];
    }
    
    public static List<Point> floodRemove(Collection<Point> points, Point seed, Predicate<Point> filter) {
        return floodRemove(points, Arrays.asList(seed), filter);
    }
    
    /**
     * Removes the filter-satisfying points flood-adjacent to a seed point.
     */
    public static List<Point> floodRemove(Collection<Point> points, List<Point> seeds, Predicate<Point> filter) {
        List<Point> res = new ArrayList<Point>();
        Queue<Point> q = new LinkedList<Point>();
        q.addAll(seeds);
        List<Boolean> seedInPoints = seeds.stream().map(x -> points.contains(x)).collect(Collectors.toList());
        points.removeAll(seeds);
        Point p;
        while (!q.isEmpty()) {
            p = q.poll();
            res.add(p);
            Point[] neighbors = {new Point(p.x-1, p.y), new Point(p.x+1, p.y),
                    new Point(p.x, p.y-1), new Point(p.x, p.y+1)};
            for (Point n : neighbors) {
                if (points.contains(n) && filter.test(n)) {
                    points.remove(n);
                    q.add(n);
                }
            }
        }
        
        for (int i = 0; i < seeds.size(); i++) {
            Point seed = seeds.get(i);
            if (!filter.test(seed)) {
                // if seed isn't part of the flood fill, fix stuff
                res.remove(seed);
                if (seedInPoints.get(i)) {
                    points.add(seed);
                }
            }
        }
        
        return res;
    }

}
