package engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

public class Mesh
{
	private final int vaoId;
	private final int posVboId;
	private final int colorVboId;
	private final int idxVboId;
	private final int vertexCount;

	public Mesh(float[] positions, float[] colors, int[] indices)
	{
		FloatBuffer posBuffer = null;
		FloatBuffer colorBuffer = null;
		IntBuffer indicesBuffer = null;

		try
		{
			this.vertexCount = indices.length;

			this.vaoId = glGenVertexArrays();
			glBindVertexArray(this.vaoId);

			//Position VBO
			this.posVboId = glGenBuffers();
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, this.posVboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0,3,GL_FLOAT, false, 0,0);

			//Color VBO
			this.colorVboId = glGenBuffers();
			colorBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, this.colorVboId);
			glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1,3,GL_FLOAT, false, 0,0);

			//Index VBO)
			this.idxVboId = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.idxVboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		}
		finally
		{
			if(posBuffer != null)
			{
				MemoryUtil.memFree(posBuffer);
			}
			if(colorBuffer != null)
			{
				MemoryUtil.memFree(colorBuffer);
			}
			if(indicesBuffer != null)
			{
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public int getVaoId()
	{
		return this.vaoId;
	}

	public int getVertexCount()
	{
		return this.vertexCount;
	}

	public void render()
	{
		//Draw Mesh
		glBindVertexArray(this.vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		//Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	public void clean()
	{
		glDisableVertexAttribArray(0);

		// Delete VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(this.posVboId);
		glDeleteBuffers(this.colorVboId);
		glDeleteBuffers(this.idxVboId);

		// Delete VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(this.vaoId);
	}
}
