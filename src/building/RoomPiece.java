package building;

import static building.Direction.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import building.RoomPieceTemplate.DoorTemplate;

public class RoomPiece {
    
    public RoomPieceTemplate template;
    
    public Point pos;
    public Direction rot;
    public boolean flipped;
    
    public World world;
    
    /**
     * Rooms whose doors are connected to this one.
     * Door id -> RoomPiece
     */
    private Map<Integer, RoomPiece> neighbors;
    private Object neighborLock = new Object();
    
    public RoomPiece(RoomPieceTemplate template) {
        this.template = template;
        this.pos = new Point(0,0);
        this.rot = Direction.NORTH;
        this.flipped = false;
        this.neighbors = new HashMap<Integer, RoomPiece>();
    }
    
    public void setNeighbor(Door door, RoomPiece other) {
        synchronized (neighborLock) {
            if (other == null) {
                neighbors.remove(door.id);
            } else {
                neighbors.put(door.id, other);
            }
        }
    }
    
    public List<RoomPiece> getNeighbors() {
        synchronized (neighborLock) {
            List<RoomPiece> result = new ArrayList<RoomPiece>();
            for (RoomPiece neigh : neighbors.values()) {
                if (neigh != null && !result.contains(neigh)) { // O(n^2) but ehh
                    result.add(neigh);
                }
            }
            return result;
        }
    }
    
    public RoomPiece getNeighbor(Door d) {
        assert d.roomPiece == this;
        synchronized (neighborLock) {
            return neighbors.get(d.id);
        }
    }
    
    public boolean isWalledOff(Door d) {
        assert d.roomPiece == this;
        synchronized (neighborLock) {
            return neighbors.containsKey(d.id) && neighbors.get(d.id) == null;
        }
    }
    
    public void setWalledOff(Door d, boolean walledOff) {
        assert d.roomPiece == this;
        synchronized (neighborLock) {
            if (walledOff) {
                neighbors.put(d.id, null);
            } else {
                neighbors.remove(d.id);
            }
        }
    }
    
    public void removeNeighbor(Door d) {
        assert d.roomPiece == this;
        synchronized (neighborLock) { 
            neighbors.remove(d.id);
        }
    }
    
    public boolean isLeaf() {
        return getNeighbors().size() == 1;
    }
    
    public boolean isConnector() {
        return template.isConnector();
    }
    
    public void addToWorld(World w) {
        this.world = w;
    }
    
    public void removeFromWorld() {
        this.world = null;
    }
    
    private void makeSureNotInWorld() {
        if (this.world != null) {
            throw new RuntimeException("Cannot alter RoomPiece after it's"
                    + " been added to a world.");
        }
    }
    
    public void setPosition(Point pos) {
        makeSureNotInWorld();
        this.pos = pos;
    }
    
    public Point getPosition() {
        return pos;
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public boolean getFlipped() {
        return this.flipped;
    }
    
    public void shiftPosition(Point shift) {
        makeSureNotInWorld();
        this.pos = new Point(pos.x + shift.x, pos.y + shift.y);
    }
    
    public void setRotation(Direction rot) {
        makeSureNotInWorld();
        this.rot = rot;
    }
    
    public void setRotation(int r) {
        makeSureNotInWorld();
        this.rot = Direction.values()[((r % 4) + 4) % 4];
    }
    
    public Direction getRotation() {
        return this.rot;
    }
    
    public int width() {
        if (rot ==  EAST || rot == WEST) {
            return template.height();
        } else {
            return template.width();
        }
    }
    
    public int height() {
        if (rot ==  EAST || rot == WEST) {
            return template.width();
        } else {
            return template.height();
        }
    }
    
    public CellType get(int x, int y) {
        if (!isValid(x,y)) {
            return CellType.EMPTY;
        } else {
            Point t = worldToTemplate(new Point(x,y));
            return template.get(t.x, t.y);
        }
    }
    
    public CellType get(Point p) {
        return get(p.x, p.y);
    }
    
    public CellType getSpecial(int x, int y) {
        CellType templateType = get(x, y);
        if (templateType == CellType.FLOOR) {
            if (isLeaf()) {
                return CellType.PINK;
            }
            if (isConnector()) {
                return CellType.CONNECTOR_FLOOR;
            } 

            return templateType;
        } else if (templateType == CellType.DOOR) {
            Door d = getDoor(x, y);
            if (d == null) {
                return CellType.DOOR; // uhhh
            }
            if (d.isWalledOff()) {
                return CellType.WALLED_DOOR;
            } else if (d.isAvailable()) {
                return CellType.DOOR;
            } else {
                return CellType.LINKED_DOOR;
            }
        } else {
            return templateType;
        }
    }
    
    public Point centerPoint() {
        return new Point(pos.x + width()/2, pos.y + height()/2);
    }
    
    public CellType getSpecial(Point p) {
        return getSpecial(p.x, p.y);
    }
    
    public Point worldToTemplate(Point p) {
        Point res = new Point(p.x - pos.x, p.y - pos.y);
        switch(rot) {
            case EAST:
                res = new Point(res.y, width() - res.x - 1);
                break;
            case WEST:
                res = new Point(height() - res.y - 1, res.x);
                break;
            case SOUTH:
                res = new Point(width() - res.x - 1, height() - res.y - 1);
                break;
        }
        
        if (flipped) {
            res = new Point(template.width() - res.x - 1, res.y);
        }
        
        return res;
    }
    
    public Point templateToWorld(Point p) {
        Point res = p;
        if (flipped) {
            res = new Point(template.width() - res.x - 1, res.y);
        }
        switch(rot) {
            case EAST:
                res = new Point(template.height() - res.y - 1, res.x);
                break;
            case WEST:
                res = new Point(res.y, template.width() - res.x - 1);
                break;
            case SOUTH:
                res = new Point(template.width() - res.x - 1, template.height() - res.y - 1);
                break;
        }
        
        return new Point(res.x + pos.x, res.y + pos.y);
    }
    
    private boolean isValid(int x, int y) {
        return x >= pos.x && y >= pos.y 
                && x < pos.x + width() && y < pos.y + height();
    } 
    
    public List<Point> points() {
        List<Point> result = new ArrayList<Point>();
        for (int x = pos.x; x < pos.x + width(); x++) {
            for (int y = pos.y; y < pos.y + height(); y++) {
                if (get(x,y) != CellType.EMPTY) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }
    
    public boolean sameAs(RoomPiece other) {
        if (this.width() != other.width() || this.height() != other.height()) {
            return false;
        } else {
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    if (get(x,y) != other.get(x, y)) {
                        return false;
                    }
                }
            }
            
            return true;
        }
    }
    
    public boolean collides(RoomPiece other) {
        for (Point p : points()) {
            CellType otherType = other.get(p);
            if (otherType != CellType.EMPTY && get(p) != otherType) {
                return true;
            }
        }
        return false;
    }
    
    public int numDoors() {
        return template.numDoors();
    }
    
    public Point[] doorPosition(int i) {
        DoorTemplate d = template.getDoor(i);
        Point left = templateToWorld(d.left);
        Point right = templateToWorld(d.right);
        if (flipped) {
            Point temp = left;
            left = right;
            right = temp;
        }
        return new Point[] {left, right};
    }
    
    public Direction doorDirection(int i) {
        Point[] position = doorPosition(i);
        Point left = position[0];
        Point right = position[1];
        
        if (left.x == right.x) {
            return (left.y > right.y) ? WEST : EAST;
        } else {
            return (left.x > right.x) ? SOUTH : NORTH;
        }
    }
    
    public List<Door> getDoors() {
        List<Door> res = new ArrayList<Door>();
        for (int i = 0; i < numDoors(); i++) {
            res.add(new Door(this, i));
        }
        return res;
    }
    
    public List<Door> getAvailableDoors() {
        return getDoors().stream()
                .filter(door -> door.isAvailable())
                .collect(Collectors.toList());
        
    }
    
    public Door getDoor(Point p) {
        for (Door d : getDoors()) {
            if (PointUtils.inBox(p, d.left(), d.right())) {
                return d;
            }
        }
        return null;
    }
    
    public Door getDoor(int x, int y) {
        return getDoor(new Point(x, y));
    }
    
    public String toString() {
        return "RoomPiece[w="+width()+",h="+height()+",dir="+getRotation().name()+"]";
    }
    
    public String toDisplayString() {
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
        RoomPiece roomPiece;
        int id;
        
        public Door(RoomPiece rp, int id) {
            this.roomPiece = rp;
            this.id = id;
        }
        
        public Point left() {
            return roomPiece.doorPosition(id)[0];
        }
        
        public Point right() {
            return roomPiece.doorPosition(id)[1];
        }
        
        public int width() {
            Point left = left();
            Point right = right();
            return Math.max(Math.abs(left.x - right.x), Math.abs(left.y - right.y));
        }
        
        public boolean isCompatible(Door other) {
            return width() == other.width() && dir().isOpposite(other.dir());
        }
        
        public Direction dir() {
            return roomPiece.doorDirection(id);
        }
        
        public boolean equals(Object other) {
            if (other instanceof Door) {
                return Objects.equals(roomPiece, ((Door) other).roomPiece)
                        && id == ((Door) other).id;
            } else {
                return false;
            }
        }
        
        public RoomPiece getNeighbor() {
            return roomPiece.getNeighbor(this);
        }
        
        public boolean isWalledOff() {
            return roomPiece.isWalledOff(this);
        }
        
        public boolean isAvailable() {
            return getNeighbor() == null && !isWalledOff();
        }
        
        public int hashCode() {
            return Objects.hash(id, roomPiece);
        }
        
        public String toString() {
            return "Door[left=("+left().x+","+left().y+"), right=("+right().x+
                    ","+right().y+"), dir="+dir().name()+"]";
        }
    }
    
    public static List<RoomPiece> allOrientations(RoomPieceTemplate template) {
        boolean[] flips = template.isMirrored ? new boolean[] {false} : new boolean[] {false, true};
        List<Direction> directions = new ArrayList<Direction>();
        directions.add(Direction.NORTH);
        if (!template.is90Symmetric) {
            directions.add(Direction.EAST);
            if (!template.is180Symmetric) {
                directions.add(Direction.WEST);
                directions.add(Direction.SOUTH);
            }
        } else if (!template.is180Symmetric) {
            directions.add(Direction.SOUTH);
        }
        
        List<RoomPiece> res = new ArrayList<RoomPiece>();
        for (boolean flip : flips) {
            for (Direction dir : directions) {
                RoomPiece rp = new RoomPiece(template);
                rp.setFlipped(flip);
                rp.setRotation(dir);
                res.add(rp);
            }
        }
        
        return res;
    }
}
