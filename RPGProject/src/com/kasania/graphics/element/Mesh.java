package com.kasania.graphics.element;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private final int vaoID;
	private final int posVboID;
	private final int idxVboID;
	private final List<Integer> vboIDList;
	
	private final int vertexCount;
	private final Texture texture;

	public Mesh(float[] pos, float[] textCoords, int[] indices,Texture texture) {
		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			this.texture = texture;
			vertexCount = indices.length;
			vboIDList = new ArrayList<>();
			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);

			posVboID = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(pos.length);
			posBuffer.put(pos).flip();
			glBindBuffer(GL_ARRAY_BUFFER, posVboID);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            int vboId = glGenBuffers();
            vboIDList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			idxVboID = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboID);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);

			}
		}

	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void render(){
        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        // Draw the mesh
        glBindVertexArray(getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
	}

	public void cleanUp() {

        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIDList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        texture.cleanup();

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

	}

}
