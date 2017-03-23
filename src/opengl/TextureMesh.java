package opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TextureMesh implements Mesh {
    
    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int texCoordsVboId;
    private final int vertexCount;
    
    private Texture texture;
    
    public TextureMesh(float[] positions, float[] textCoords, int[] indices, Texture texture) {
        this.texture = texture;
        FloatBuffer verticesBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            verticesBuffer.put(positions).flip();
            
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);
            
            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0,3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            
            texCoordsVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, texCoordsVboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            
            idxVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            
            
            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
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
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);   // indices
        glEnableVertexAttribArray(1);   // texture
        
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        
        glDisableVertexAttribArray(0);   // indices
        glDisableVertexAttribArray(1);   // texture
        glBindVertexArray(0);
    }
    
    public void cleanup() {
        glDisableVertexAttribArray(0);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);
        glDeleteBuffers(texCoordsVboId);
        
        texture.cleanup();
        
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}

