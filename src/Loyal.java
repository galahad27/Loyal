import engine.Engine;
import engine.IGame;
import engine.Window;
import game.Renderer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Loyal implements IGame
{
	private float color = 0.0f;
	private final Renderer renderer;

	private int direction = 0;

	public Loyal()
	{
		this.renderer = new Renderer();
	}

	@Override
	public void init() throws Exception
	{
		this.renderer.init();
	}
	@Override
	public void input(Window window)
	{
		if(window.isKeyPressed(GLFW_KEY_UP))
		{
			direction = 1;
		}
		else if(window.isKeyPressed(GLFW_KEY_DOWN))
		{
			direction = -1;
		}
		else
		{
			direction = 0;
		}
	}
	@Override
	public void update(float interval)
	{
		color += direction * 0.01f;
		if(color > 1)
		{
			color = 1.0f;
		}
		else if(color < 0)
		{
			color = 0.0f;
		}
	}
	@Override
	public void render(Window window)
	{
		if(window.isResized())
		{
			glViewport(0,0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		window.setClearColor(color, color, color, 0.0f);
		renderer.clear();
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
