package building;

import java.util.HashMap;
import java.util.Map;

public enum CellType {
    EMPTY(0, " "), 
    WALL(1, "X"), 
    FLOOR(2, "-"), 
    DOOR(3, "=");
    
    public final int id;
    public final String str;
    
    private static final Map<Integer, CellType> map;
    
    static {
        map = new HashMap<Integer,CellType>();
        for (CellType type : CellType.values()) {
            map.put(type.id, type);
        }
    }
    
    private CellType(int id, String str) {
        this.id = id;
        this.str = str;
    }
    
    public static CellType get(int id) {
        return map.get(id);
        
    }
}
