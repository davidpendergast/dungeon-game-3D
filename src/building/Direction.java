package building;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Direction {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);
    
    public final int id;
    public static final Map<Integer, Direction> map;
    
    static {
        Map<Integer, Direction> m = new HashMap<Integer, Direction>();
        for (Direction d : values()) {
            m.put(d.id, d);
        }
        map = Collections.unmodifiableMap(m);
    }
    
    private Direction(int id) {
        this.id = id;
    }
    
    public Direction rotate(int num) {
        num = ((id +num) % 4 + 4) % 4;
        return map.get(num);
    }
    
    public boolean isOpposite(Direction other) {
        return other.id != id && (other.id + id) % 2 == 0;
    }

}
