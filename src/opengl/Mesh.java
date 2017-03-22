package opengl;

import  static  org.lwjgl.opengl.GL11.*;
import  static  org.lwjgl.opengl.GL15.*;
import  static  org.lwjgl.opengl.GL20.*;
import  static  org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    
    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int colorVboId;
    private final int vertexCount;
    
    public Mesh(float[] positions, float[] colors, int[] indices) {
        FloatBuffer verticesBuffer = null;
        FloatBuffer colorBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            
            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0,3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
            
            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            
            
            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }
    
    public int getVaoId() {
        return vaoId;
    }
    
    public int getVertexCount() {
        return vertexCount;
    }
    
    public void render() {
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);   // indices
        glEnableVertexAttribArray(1);   // color
        
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);   // indices
        glDisableVertexAttribArray(1);   // color
        glBindVertexArray(0);
    }
    
    public void cleanup() {
        glDisableVertexAttribArray(0);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);
        
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}
