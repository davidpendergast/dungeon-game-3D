package opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class DummyGame implements IGameLogic {

    float color = 0.0f;
    float direction = 0;
    
    private final Renderer renderer;
    private Mesh mesh;
    
    public DummyGame() {
        renderer = new Renderer();
    }
    
    @Override
    public void init() throws Exception {
        renderer.init();
        float[] positions = new float[] { 
                -0.5f,  0.5f, -1.05f, 
                -0.5f, -0.5f, -1.05f,
                 0.5f, -0.5f, -1.05f, 
                 0.5f,  0.5f, -1.05f };
        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2
        };
        float[] colors = new float[] {
                0.5f,   0.0f,   0.0f,
                0.0f,   0.5f,   0.0f,
                0.0f,   0.0f,   0.5f,
                0.0f,   0.5f,   0.5f
        };
        mesh = new Mesh(positions, colors, indices);
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
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }


}
