package building;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum CellType {
    EMPTY(0, " ", Color.GRAY), 
    WALL(1, "X", Color.BLACK), 
    FLOOR(2, "-", Color.WHITE), 
    DOOR(3, "=", Color.GREEN),
    
    // "Special" cell types, used for rendering purposes
    WALLED_DOOR(4, "?", Color.DARK_GRAY),
    LINKED_DOOR(5, "=", Color.CYAN),
    CONNECTOR_FLOOR(6, "+", Color.ORANGE),
//    N_FLOOR(6, "-", Color.ORANGE),
//    E_FLOOR(7, "-", Color.PINK),
//    W_FLOOR(8, "-", Color.MAGENTA),
//    S_FLOOR(9, "-", Color.BLUE),
    
    GHOST(10, "?", Color.LIGHT_GRAY);
    
    public final int id;
    public final String str;
    public final Color color;
    
    private static final Map<Integer, CellType> map;
    
    static {
        map = new HashMap<Integer,CellType>();
        for (CellType type : CellType.values()) {
            map.put(type.id, type);
        }
    }
    
    private CellType(int id, String str, Color color) {
        this.id = id;
        this.str = str;
        this.color = color;
    }
    
    public static CellType get(int id) {
        return map.get(id);
        
    }
}
