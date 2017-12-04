package engine;

public class Timer
{
	private double lastLoopTime;

	public void init()
	{
		this.lastLoopTime = getTime();
	}

	public float getElapsedTime()
	{
		double time = getTime();
		float elapsedTime = (float) (time - this.lastLoopTime);
		lastLoopTime = time;
		return elapsedTime;
	}

	public double getTime()
	{
		return System.nanoTime() / 1000_000_000.0;
	}
	public double getLastLoopTime()
	{
		return lastLoopTime;
	}
}
