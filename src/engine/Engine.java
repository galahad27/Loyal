package engine;

public class Engine implements Runnable
{
	public static final int Target_FPS = 60;
	public static final int Target_UPS = 30;

	private final Thread gameLoop;
	private final Window window;
	private final IGame game;
	private final Timer timer;

	public Engine(String title, int width, int height, boolean vSync, IGame game) throws Exception
	{
		this.gameLoop = new Thread(this, "GAME_THREAD");
		this.window = new Window(title, width, height, vSync);
		this.game = game;
		this.timer = new Timer();
	}

	public void start()
	{
		String osName = System.getProperty(("os.name"));
		if(osName.contains("Mac"))
		{
			this.gameLoop.run();
		}
		else
		{
			this.gameLoop.start();
		}
	}

	protected void init() throws Exception
	{
		this.window.init();
		this.timer.init();
		this.game.init();
	}

	protected void gameLoop()
	{
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / Target_FPS;

		boolean running = true;
		while(running && !this.window.windowShouldClose())
		{
			elapsedTime = this.timer.getElapsedTime();
			accumulator += elapsedTime;

			input();

			while(accumulator >= interval)
			{
				update(interval);
				accumulator -= interval;
			}

			render();

			if(!this.window.isvSync())
			{
				sync();
			}
		}
	}

	private void sync()
	{
		float loopSlot = 1f / Target_FPS;
		double endTime = this.timer.getLastLoopTime() + loopSlot;
		while (this.timer.getTime() < endTime)
		{
			try
			{
				Thread.sleep(1);
			}
			catch(InterruptedException e)
			{}
		}
	}

	protected void input()
	{
		this.game.input(this.window);
	}

	protected  void update(float interval)
	{
		this.game.update(interval);
	}

	protected void render()
	{
		this.game.render(this.window);
		this.window.update();
	}
	protected  void clean()
	{
		this.game.clean();
		this.window.destroy();
	}

	@Override
	public void run()
	{
		try
		{
			init();
			gameLoop();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			clean();
		}
	}
}
