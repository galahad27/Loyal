package engine.graph;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
	private final int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	private final Map<String, Integer> uniforms;

	public Shader() throws Exception
	{
		this.programId = glCreateProgram();
		if(this.programId == 0)
		{
			throw new Exception("Could not create a new Shader");
		}
		this.uniforms = new HashMap<>();
	}

	public void createUniform(String uniformName) throws Exception
	{
		int uniformLocation = glGetUniformLocation(this.programId, uniformName);
		if(uniformLocation < 0)
		{
			throw new Exception("Could not find uniform: "+uniformName);
		}
		this.uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value)
	{
		try(MemoryStack stack = MemoryStack.stackPush())
		{
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(this.uniforms.get(uniformName), false, fb);
		}
	}

	public void createVertexShader(String shaderCode) throws Exception
	{
		this.vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}
	public void createFragmentShader(String shaderCode) throws Exception
	{
		this.fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}

	public int createShader(String shaderCode, int shaderType) throws Exception
	{
		int shaderId = glCreateShader(shaderType);
		if(shaderId == 0)
		{
			throw new Exception("Error creating shader. Type: " + shaderType);
		}

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
		{
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(this.programId, shaderId);

		return shaderId;
	}

	public void link() throws Exception
	{
		glLinkProgram(this.programId);
		if(glGetProgrami(this.programId, GL_LINK_STATUS) == 0)
		{
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
		}

		if(this.vertexShaderId != 0)
		{
			glDetachShader(this.programId, this.vertexShaderId);
		}
		if(this.fragmentShaderId != 0)
		{
			glDetachShader(this.programId, this.fragmentShaderId);
		}

		//REMOVE IN PRODUCTION
		glValidateProgram(this.programId);
		if(glGetProgrami(this.programId, GL_VALIDATE_STATUS) == 0)
		{
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(this.programId, 1024));
		}
	}

	public void bind()
	{
		glUseProgram(this.programId);
	}

	public void unbind()
	{
		glUseProgram(0);
	}

	public void clean()
	{
		unbind();
		if(this.programId != 0)
		{
			glDeleteProgram(this.programId);
		}
	}
}
