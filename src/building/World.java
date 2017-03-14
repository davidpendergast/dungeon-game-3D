package building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    
    List<RoomPiece> pieces;
    
    public World() {
        this.pieces = new CopyOnWriteArrayList<RoomPiece>(); // lololol
    }
    
    public CellType getCellType(int x, int y) {
        // TODO : make O(1)
        for (RoomPiece p : pieces) {
            if (p.getAbsolute(x, y) != CellType.EMPTY) {
                return p.getAbsolute(x, y);
            }
        }
        return CellType.EMPTY;
    }
    
    public CellType getCellType(Point p) {
        return getCellType(p.x, p.y);
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
        for (Point p : piece.pointsAbsolute()) {
            CellType worldCellType = getCellType(p);
            if (worldCellType != CellType.EMPTY 
                    && piece.getAbsolute(p.x, p.y) != worldCellType) {
                return true;
            }
        }
        return false;
    }

}
