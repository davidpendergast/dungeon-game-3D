package building;

import static building.Direction.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RoomPiece {
    
    public RoomPieceTemplate template;
    
    public Point pos;
    
    public Direction rot;
    
    /**
     * Rooms whose doors are connected to this one.
     */
    public List<RoomPiece> neighbors;
    
    public RoomPiece(RoomPieceTemplate template) {
        this.template = template;
        this.pos = new Point(0,0);
        this.rot = Direction.NORTH;
        this.neighbors = new ArrayList<RoomPiece>();
    }
    
    public void setPosition(Point pos) {
        this.pos = pos;
    }
    
    public Point getPosition() {
        return pos;
    }
    
    public void shiftPosition(Point shift) {
        this.pos = new Point(pos.x + shift.x, pos.y + shift.y);
    }
    
    public void setRotation(Direction rot) {
        this.rot = rot;
    }
    
    public void setRotation(int r) {
        this.rot = Direction.values()[((r % 4) + 4) % 4];
    }
    
    public Direction getRotation() {
        return this.rot;
    }
    
    public int width() {
        if (rot ==  NORTH || rot == SOUTH) {
            return template.width();
        } else {
            return template.height();
        }
    }
    
    public int height() {
        if (rot == NORTH || rot == SOUTH) {
            return template.height();
        } else {
            return template.width();
        }
    }
    
    public CellType get(int x, int y) {
        if (!isValid(x,y)) {
            return CellType.EMPTY;
        } else {
            Point t = rotateToTemplateCoords(new Point(x,y));
            return template.get(t.x, t.y);
        }
    }
    
    public CellType get(Point p) {
        return get(p.x, p.y);
    }
    
    public CellType getAbsolute(int absX, int absY) {
        return get(absX - pos.x, absY - pos.y);
    }
    
    public CellType getAbsolute(Point p) {
        return getAbsolute(p.x, p.y);
    }
    
    public Point rotateToTemplateCoords(Point p) {
        switch(rot) {
            case NORTH:
                return p;
            case EAST:
                return new Point(p.y, width() - p.x - 1);
            case WEST:
                return new Point(height() - p.y - 1, p.x);
            case SOUTH:
                return new Point(width() - p.x - 1, height() - p.y - 1);
            default:
                throw new RuntimeException("strange new direction encountered...");
        }
    }
    
    private boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < width() && y < height();
    }
    
    
    public List<Point> points() {
        List<Point> result = new ArrayList<Point>();
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (get(x,y) != CellType.EMPTY) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }
    
    public List<Point> pointsAbsolute() {
        return points().stream()
                .map(p -> new Point(p.x + pos.x, p.y + pos.y))
                .collect(Collectors.toList());
    }
    
    public void addNeighbor(RoomPiece rp) {
        this.neighbors.add(rp);
    }
    
    public void removeNeighbor(RoomPiece rp) {
        this.neighbors.remove(rp);
    }
    
    public boolean collides(RoomPiece other) {
        for (Point p : pointsAbsolute()) {
            CellType otherType = other.getAbsolute(p);
            if (otherType != CellType.EMPTY && getAbsolute(p) != otherType) {
                return true;
            }
        }
        return false;
    }
    
    public List<Door> getDoors() {
        Set<Point> doorPoints = pointsAbsolute().stream()
                .filter(p -> getAbsolute(p) == CellType.DOOR)
                .collect(Collectors.toSet());
        List<Door> result = new ArrayList<Door>();
        while (!doorPoints.isEmpty()) {
            Point seed = doorPoints.iterator().next();
            List<Point> thisDoor = floodRemove(doorPoints, seed, 
                    p -> getAbsolute(p) == CellType.DOOR);
            
            Door door = getDoor(thisDoor);
            if (door != null) {
                result.add(door);
            }
        }
        
        return result;
    }
    
    private Door getDoor(Collection<Point> doorPoints) {
        Point[] extrema = PointUtils.getExtremePoints(doorPoints);
        int width = extrema[1].x - extrema[0].x + 1;
        int height = extrema[3].y - extrema[2].y + 1;
        if ((width > 1 && height > 1) || (width == 0 && height == 0)) {
            return null;
        } else {
            if (width > 1) {
                // horizontal door
                Point p1 = extrema[0];
                Point p2 = extrema[1];
                if (getAbsolute(p1.x, p1.y-1) == CellType.EMPTY) {
                    return new Door(p1, p2);
                } else {
                    return new Door(p2, p1);
                }
            } else {
                // vertical door
                Point p1 = extrema[2];
                Point p2 = extrema[3];
                if (getAbsolute(p1.x-1, p1.y) == CellType.EMPTY) {
                    return new Door(p1, p2);
                } else {
                    return new Door(p2, p1);
                }
            }
        }
    }
    
    /**
     * Removes the filter-satisfying points flood-adjacent to a seed point.
     */
    private static List<Point> floodRemove(Set<Point> points, Point seed, Predicate<Point> filter) {
        List<Point> res = new ArrayList<Point>();
        Queue<Point> q = new LinkedList<Point>();
        q.add(seed);
        boolean seedInPoints = points.contains(seed);
        points.remove(seed);
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
        
        if (!filter.test(seed)) {
            // if seed isn't part of the flood fill, fix stuff
            res.remove(seed);
            if (seedInPoints) {
                points.add(seed);
            }
        }
        
        return res;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                sb.append(get(x,y).str);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static class Door {
        /**
         * Leftmost point of the door you'd see when standing inside the room
         * and looking out.
         */
        final Point left;
        /**
         * Rightmost point of the door you'd see when standing inside the room
         * and looking out.
         */
        final Point right;
        
        public Door(Point left, Point right) {
            assert (left.x == right.x || left.y == right.y);
            assert !(left.equals(right));
            this.left = left;
            this.right = right;
        }
        
        public boolean isHorz() {
            return left.y == right.y;
        }
        
        public boolean isVert() {
            return left.x == left.y;
        }
        
        public int width() {
            if (isHorz()) {
                return (int)Math.abs(left.x - right.x);
            } else {
                return (int)Math.abs(left.y - right.y);
            }
        }
        
        /**
         * Direction door opens away from the room.
         */
        public Direction dir() {
            if (isHorz()) {
                return left.x < right.x ? NORTH : SOUTH;
            } else {
                return left.y < right.y ? EAST : WEST;
            }
        }
        
        public boolean isCompatible(Door other) {
            return width() == other.width() && dir().isOpposite(other.dir());
        }
        
        public String toString() {
            return "(left:"+left.x+", "+left.y+", right:"+right.x+", "+right.y+", dir:"+dir()+")";
        }
    }

}
