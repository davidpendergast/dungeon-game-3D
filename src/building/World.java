package building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import building.RoomPiece.Door;

public class World {
    
    List<RoomPiece> pieces;
    Integer[] bounds = {null, null, null, null};
    
    public World() {
        this.pieces = new CopyOnWriteArrayList<RoomPiece>(); // lololol
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
        if (collides(piece)) {
            return false;
        } else {
            pieces.add(piece);
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

}
