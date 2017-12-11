package game;

import engine.Item;
import engine.Window;
import engine.graph.Mesh;
import engine.graph.Shader;
import engine.graph.Transformation;
import engine.util.App;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer
{
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_Near = 0.01f;
	private static final float Z_Far = 1000.f;

	private final Transformation transformation;

	private Shader shader;

	public Renderer()
	{
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception
	{
		shader = new Shader();
		shader.createVertexShader(App.loadReasource("/vertex.vs"));
		shader.createFragmentShader(App.loadReasource("/fragment.fs"));
		shader.link();

		shader.createUniform("projectionMatrix");
		shader.createUniform("worldMatrix");

		window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void render(Window window, Item[] items)
	{
		clear();

		if(window.isResized())
		{
			glViewport(0,0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shader.bind();

		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_Near, Z_Far);
		shader.setUniform("projectionMatrix", projectionMatrix);

		for(Item item : items)
		{
			Matrix4f worldMatrix = transformation.getWorldMatrix(item.getPosition(), item.getRotation(), item.getScale());
			shader.setUniform("worldMatrix", worldMatrix);
			item.getMesh().render();
		}
		shader.unbind();
	}

	public void clean()
	{
		if(shader != null)
		{
			shader.clean();
		}
	}

	public void clear()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}
