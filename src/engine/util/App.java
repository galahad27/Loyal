package engine.util;

import java.io.InputStream;
import java.util.Scanner;

public class App
{
	public  static String loadReasource(String fileName) throws Exception
	{
		String result;
		try(InputStream in = App.class.getClass().getResourceAsStream(fileName); Scanner scanner = new Scanner(in, "UTF-8");)
		{
			result = scanner.useDelimiter("\\A").next();
		}
		return result;
	}
}
