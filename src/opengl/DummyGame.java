package opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class DummyGame implements IGameLogic {

    float color = 0.0f;
    float direction = 0;
    
    private final Renderer renderer;
    private GameItem item;
    private GameItem[] items = {null};
    
    public DummyGame() {
        renderer = new Renderer();
    }
    
    @Override
    public void init() throws Exception {
        renderer.init();
        float[] positions = new float[] {
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f, 
        };
        
        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                0, 1, 6, 4, 0, 6,
                // Bottom face
                6, 1, 2, 7, 6, 2,
                // Back face
                4, 6, 7, 5, 4, 7, 
        };
        
        float[] colors =   new float[]{
                0.5f,   0.0f,   0.0f,
                0.0f,   0.5f,   0.0f,
                0.0f,   0.0f,   0.5f,
                0.0f,   0.5f,   0.5f,
                0.5f,   0.0f,   0.0f,
                0.0f,   0.5f,   0.0f,
                0.0f,   0.0f,   0.5f,
                0.0f,   0.5f,   0.5f,
        };
        
        Mesh mesh = new Mesh(positions, colors, indices);
        item = new GameItem(mesh);
        item.setPosition(0, 0, -2);
        
        items[0] = item;
    }

    @Override
    public void input(Window window) {
        
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        if (direction != 0) {
            color += direction * 0.0001f;
            color = Math.max(0, Math.min(1, color));
        }
        
        float rotation = item.getRotation().x + 0.0005f;
        if (rotation > 360.0) {
            rotation = 0;
        }
        item.setRotation(rotation, rotation, rotation);
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, items);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem item : items) {
            if (item != null) {
                item.cleanup();
            }
        }
    }


}
