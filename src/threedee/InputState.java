package threedee;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class InputState implements KeyListener {
    
    public boolean left = false;
    public boolean right = false;
    public boolean forward = false;
    public boolean backward = false;
    
    private Map<Integer, Runnable> keyDownMap;
    private Map<Integer, Runnable> keyUpMap;
    
    public InputState() {
        keyDownMap = new HashMap<Integer, Runnable>();
        keyDownMap.put(KeyEvent.VK_W, () -> forward = true);
        keyDownMap.put(KeyEvent.VK_A, () -> left = true);
        keyDownMap.put(KeyEvent.VK_S, () -> backward = true);
        keyDownMap.put(KeyEvent.VK_D, () -> right = true);
        
        keyUpMap = new HashMap<Integer, Runnable>();
        keyUpMap.put(KeyEvent.VK_W, () -> forward = false);
        keyUpMap.put(KeyEvent.VK_A, () -> left = false);
        keyUpMap.put(KeyEvent.VK_S, () -> backward = false);
        keyUpMap.put(KeyEvent.VK_D, () -> right = false);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        Runnable action = keyDownMap.get(e.getKeyCode());
        if (action != null) {
            action.run();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        Runnable action = keyUpMap.get(e.getKeyCode());
        if (action != null) {
            action.run();
        }  
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    

}
