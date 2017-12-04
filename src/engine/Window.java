package engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
	private final String TITLE;

	private long window;
	private int width;
	private int height;
	private boolean resized;
	private boolean vSync;

	public Window(String title, int width, int height, boolean vSync)
	{
		this.TITLE = title;
		this.width = width;
		this.height = height;
		this.vSync = vSync;
		this.resized = false;
	}

	public void init()
	{
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if(!glfwInit())
		{
			throw  new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		// Create the window
		this.window = glfwCreateWindow(this.width, this.height, TITLE, NULL, NULL);
		if(this.window == NULL)
		{
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Setup resize callback
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.resized = true;
		});

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scanCode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			{
				glfwSetWindowShouldClose(window, true);
			}
		});

		// Center our window
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(this.window, (vidMode.width() - this.width)/2, (vidMode.height() - this.height)/2);

		glfwMakeContextCurrent(this.window);

		if(this.vSync)
		{
			glfwSwapInterval(1);
		}

		glfwShowWindow(this.window);

		GL.createCapabilities();

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	public void update()
	{
		glfwSwapBuffers(this.window);
		glfwPollEvents();
	}
	public void destroy()
	{
		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	//SETTERS
	public void setClearColor(float r, float g, float b, float a)
	{
		glClearColor(r,g,b,a);
	}
	public void setResized(boolean resized)
	{
		this.resized = resized;
	}

	//GETTERS
	public int getWidth()
	{
		return this.width;
	}
	public int getHeight()
	{
		return this.height;
	}
	public boolean isKeyPressed(int keyCode)
	{
		return glfwGetKey(this.window, keyCode) == GLFW_PRESS;
	}
	public boolean isResized()
	{
		return this.resized;
	}
	public boolean isvSync()
	{
		return this.vSync;
	}
	public boolean windowShouldClose()
	{
		return glfwWindowShouldClose(this.window);
	}



}
