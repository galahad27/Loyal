import engine.Engine;
import engine.IGame;
import engine.Item;
import engine.Window;
import engine.graph.Mesh;
import game.Renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Loyal implements IGame
{
	private final Renderer renderer;

	private Item[] items;

	private int displxInc = 0;
	private int displyInc = 0;
	private int displzInc = 0;
	private int scaleInc = 0;

	private float color = 0.0f;

	public Loyal()
	{
		this.renderer = new Renderer();
	}

	@Override
	public void init(Window window) throws Exception
	{
		this.renderer.init(window);
		float[] positions = new float[]
		{
			-0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f,
			0.5f, -0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
		};
		float[] colors = new float[]
		{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		int[] indices = new int[]{0,1,3,3,1,2};

		Mesh mesh = new Mesh(positions, colors, indices);
		Item item = new Item(mesh);
		item.setPosition(0,0, -2);
		items = new Item[]{item};
	}
	@Override
	public void input(Window window)
	{
		displyInc = 0;
		displxInc = 0;
		displzInc = 0;
		scaleInc = 0;

		if(window.isKeyPressed(GLFW_KEY_UP))
		{
			displyInc = 1;
		}
		else if(window.isKeyPressed(GLFW_KEY_DOWN))
		{
			displyInc = -1;
		}
		else if(window.isKeyPressed(GLFW_KEY_LEFT))
		{
			displxInc = -1;
		}
		else if(window.isKeyPressed(GLFW_KEY_RIGHT))
		{
			displxInc = 1;
		}
		else if(window.isKeyPressed(GLFW_KEY_A))
		{
			displzInc = -1;
		}
		else if(window.isKeyPressed(GLFW_KEY_Q))
		{
			displzInc = 1;
		}
		else if(window.isKeyPressed(GLFW_KEY_Z))
		{
			scaleInc = -1;
		}
		else if(window.isKeyPressed(GLFW_KEY_X))
		{
			scaleInc = 1;
		}
	}
	@Override
	public void update(float interval)
	{
		for(Item item : Items)
		{

		}
	}
	@Override
	public void render(Window window)
	{
		window.setClearColor(color, color, color, 0.0f);
		renderer.render(window, mesh);
	}
	@Override
	public void clean()
	{
		renderer.clean();
		mesh.clean();
	}

	public static void main(String[] args)
	{
		try
		{
			boolean vSync = true;
			IGame game = new Loyal();
			Engine gameEngine = new Engine("Loyal", 600, 480, vSync, game);
			gameEngine.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
