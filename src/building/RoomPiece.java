package building;

import java.awt.Point;

public class RoomPiece {
    
    public RoomPieceTemplate template;
    
    public Point pos;
    
    /**
     * 0, 1, 2, or 3
     */
    public int rot = 0;
    
    public RoomPiece(RoomPieceTemplate template) {
        this.template = template;
    }
    
    public void setPosition(Point pos) {
        this.pos = pos;
    }
    
    public void shiftPosition(Point shift) {
        this.pos = new Point(pos.x + shift.x, pos.y + shift.y);
    }
    
    public void setRotation(int rot) {
        this.rot = ((rot % 4) + 4) % 4; // gotta be positive
    }
    
    public int width() {
        if (rot == 0 || rot == 2) {
            return template.width();
        } else {
            return template.height();
        }
    }
    
    public int height() {
        if (rot == 0 || rot == 2) {
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
    
    public Point rotateToTemplateCoords(Point p) {
        if (rot == 0) {
            return p;
        } else if(rot == 1) {
            return new Point(p.y, width() - p.x - 1);
        } else if (rot == 2) {
            return new Point(width() - p.x - 1, height() - p.y - 1);
        } else {
            assert rot == 3; // y'all better be 3
            return new Point(height() - p.y - 1, p.x);
        }
    }
    
    private boolean isValid(int x, int y) {
        return x >= 0 && y >= 0 && x < width() && y < height();
    }
    
    public CellType getAbsolute(int absX, int absY) {
        return get(absX - pos.x, absY - pos.y);
    }

}
