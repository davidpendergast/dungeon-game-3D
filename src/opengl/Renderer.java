package opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
    
    private ShaderProgram shaderProgram;
    
    private static final float FOV = (float)Math.toRadians(60.0);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    
    private Transformation transformation;
    
    public Renderer() {
        transformation = new Transformation();
    }
    
    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("shaders/fragment.fs"));
        shaderProgram.link();
        
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
    }
    
    public void render(Window window, GameItem[] gameItems) {
        clear();
        
        if (window.getResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        
        shaderProgram.bind();
        
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, 
                window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        
        for (GameItem item : gameItems) {
            Matrix4f worldMatrix = transformation.getWorldMatrix(item.getPosition(), 
                    item.getRotation(), item.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            item.getMesh().render();
        }
        
        shaderProgram.unbind();
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

}
