package building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import building.RoomPiece.Door;
import building.WorldFactory.Options;

public class World {
    
    public List<RoomPiece> pieces;
    public List<RoomPiece> ghostPieces;
    public Integer[] bounds = {null, null, null, null};
    public boolean isGenerating = false;
    
    public World() {
        this.pieces = new CopyOnWriteArrayList<RoomPiece>(); // lololol
        this.ghostPieces = new CopyOnWriteArrayList<RoomPiece>();
    }
    
    public RoomPiece getPieceAt(int x, int y) {
        for (RoomPiece rp : pieces) {
            if (rp.get(x, y) != CellType.EMPTY) {
                return rp;
            }
        }
        return null;
    }
    
    public RoomPiece getPieceAt(Point p) {
        return getPieceAt(p.x, p.y);
    }
    
    public RoomPiece getGhostAt(int x, int y) {
        for (RoomPiece ghost : ghostPieces) {
            if (ghost.get(x, y) != CellType.EMPTY) {
                return ghost;
            }
        }
        return null;
    }
    
    public CellType getCellType(int x, int y) {
        RoomPiece rp = getPieceAt(x,y);
        if (rp != null) {
            return rp.get(x,y);
        } else {
            return CellType.EMPTY;        
        }
    }
    
    public CellType getCellType(Point p) {
        return getCellType(p.x, p.y);
    }
    
    public CellType getSpecialCellType(int x, int y) {
        RoomPiece ghost = getGhostAt(x,y);
        if (ghost != null) {
            return CellType.GHOST;
        }
        RoomPiece rp = getPieceAt(x,y);
        if (rp != null) {
            return rp.getSpecial(x,y);
        } else {
            return CellType.EMPTY;        
        }
    }
    
    public CellType getSpecialCellType(Point p) {
        return getSpecialCellType(p.x, p.y);
    }
    
    public boolean add(RoomPiece piece) {
        if (collides(piece) || outOfBounds(piece)) {
            return false;
        } else {
            pieces.add(piece);
            for (Door door : piece.getDoors()) {
                Door connectingDoor = doorThatConnectsTo(door);
                if (connectingDoor != null) {
                    piece.setNeighbor(door, connectingDoor.roomPiece);
                    connectingDoor.roomPiece.setNeighbor(connectingDoor, piece);
                }
            }
            return true;
        }
    }
    
    public boolean collides(RoomPiece piece) {
        for (Point p : piece.points()) {
            CellType worldCellType = getCellType(p);
            if (worldCellType != CellType.EMPTY 
                    && piece.get(p.x, p.y) != worldCellType) {
                return true;
            }
        }
        return false;
    }
    
    public List<Door> availableDoors() {
        List<Door> result = new ArrayList<Door>();
        for (RoomPiece piece : pieces) {
            result.addAll(piece.getAvailableDoors());
        }
        return result;
    }
    
    public Door doorThatConnectsTo(Door d) {
        // could be more efficient but ehhh...
        for (RoomPiece rp : pieces) {
            for (Door rpDoor : rp.getDoors()) {
                if (d.isCompatible(rpDoor) && d.left().equals(rpDoor.right())) {
                    return rpDoor;
                }
            }
        }
        
        return null;
    }
    
    public boolean outOfBounds(RoomPiece rp) {
        if (bounds[0] != null && rp.pos.x < bounds[0]) {
            return true;
        }
        
        if (bounds[1] != null && rp.pos.x + rp.width() > bounds[1]) {
            return true;
        }
        
        if (bounds[2] != null && rp.pos.y < bounds[2]) {
            return true;
        }
        
        if (bounds[3] != null && rp.pos.y + rp.height() > bounds[3]) {
            return true;
        }
        
        return false;
    }

}
