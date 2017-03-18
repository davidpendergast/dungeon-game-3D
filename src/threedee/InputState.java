package threedee;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class InputState implements KeyListener, MouseListener, MouseMotionListener {
    
    public boolean left = false;
    public boolean right = false;
    public boolean forward = false;
    public boolean backward = false;
    
    public boolean turnLeft = false;
    public boolean turnRight = false;
    public boolean turnUp = false;
    public boolean turnDown = false;
    
    public boolean dragging = false;
    
    private Map<Integer, Runnable> keyDownMap;
    private Map<Integer, Runnable> keyUpMap;
    
    public InputState() {
        buildMaps();
    }
    
    private void buildMaps() {
        keyDownMap = new HashMap<Integer, Runnable>();
        keyDownMap.put(KeyEvent.VK_W, () -> forward = true);
        keyDownMap.put(KeyEvent.VK_A, () -> left = true);
        keyDownMap.put(KeyEvent.VK_S, () -> backward = true);
        keyDownMap.put(KeyEvent.VK_D, () -> right = true);
        keyDownMap.put(KeyEvent.VK_UP, () -> turnUp = true);
        keyDownMap.put(KeyEvent.VK_LEFT, () -> turnLeft = true);
        keyDownMap.put(KeyEvent.VK_RIGHT, () -> turnRight = true);
        keyDownMap.put(KeyEvent.VK_DOWN, () -> turnDown = true);
        
        keyUpMap = new HashMap<Integer, Runnable>();
        keyUpMap.put(KeyEvent.VK_W, () -> forward = false);
        keyUpMap.put(KeyEvent.VK_A, () -> left = false);
        keyUpMap.put(KeyEvent.VK_S, () -> backward = false);
        keyUpMap.put(KeyEvent.VK_D, () -> right = false);
        keyUpMap.put(KeyEvent.VK_UP, () -> turnUp = false);
        keyUpMap.put(KeyEvent.VK_LEFT, () -> turnLeft = false);
        keyUpMap.put(KeyEvent.VK_RIGHT, () -> turnRight = false);
        keyUpMap.put(KeyEvent.VK_DOWN, () -> turnDown = false);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragging = true;
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    

}
