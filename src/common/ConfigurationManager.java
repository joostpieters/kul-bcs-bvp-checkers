package common;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import domain.board.BoardSize;
import domain.board.contracts.IBoardSize;

/**
 * This Singleton facilitates reading configuration settings from a properties file.
 */
public class ConfigurationManager
{
	private static ConfigurationManager instance;
	private final Properties properties = new Properties();
	
	private ConfigurationManager(String fileName) throws IOException
	{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		
		if (inputStream == null)
		{
			throw new FileNotFoundException(String.format("Property file '%s' not found in the classpath", fileName));
		}
		
		properties.load(inputStream);
	}
	
	/**
	 * Returns the singleton instance of this class.
	 */
	public static ConfigurationManager getInstance()
	{
		if(instance == null)
		{
			try
			{
				instance = new ConfigurationManager("config.properties");
			}
			catch (IOException ex)
			{
				throw new ExceptionInInitializerError(ex);
			}
		}
		
		return instance;
	}
	
	private String getValue(String key)
	{
		return properties.getProperty(key);
	}
	
	private void setValue(String key, String value)
	{
		properties.setProperty(key, value);
	}
	
	public IBoardSize getBoardSize() throws NumberFormatException
	{
		int x = Integer.parseInt(getValue("boardSizeX"));
		int y = Integer.parseInt(getValue("boardSizeY"));
		return new BoardSize(x, y);
	}
	
	public boolean getMandatoryMaximalCatching()
	{
		return Boolean.parseBoolean(getValue("MandatoryMaximalCatching"));
	}
	
	public void setMandatoryMaximalCatching(boolean enable)
	{
		setValue("MandatoryMaximalCatching", Boolean.toString(enable));
	}
	
	public boolean getBackwardCatchingAllowed()
	{
		return Boolean.parseBoolean(getValue("BackwardCatchingAllowed"));
	}
	
	public void setBackwardCatchingAllowed(boolean enabled)
	{
		setValue("BackwardCatchingAllowed", Boolean.toString(enabled));
	}
	
	public Player getFirstPlayer()
	{
		return Player.valueOf(getValue("FirstPlayer"));
	}
	
	public boolean getFlyingDame()
	{
		return Boolean.parseBoolean(getValue("FlyingDame"));
	}
	
	public int getRemiseThreshold()
	{
		return Integer.parseInt(getValue("RemiseThreshold"));
	}
	
	public Color getDarkColor()
	{
		return Color.decode(getValue("DarkColor"));
	}
	
	public Color getLightColor()
	{
		return Color.decode(getValue("LightColor"));
	}
	
	public int getSquareSizePx()
	{
		return Integer.parseInt(getValue("SquareSizePx"));
	}
	
	public String getCijfersPath()
	{
		return getValue("CijfersPath");
	}
	
	public String getSchijvenPath()
	{
		return getValue("SchijvenPath");
	}
	
	public int getPaintDelayMs()
	{
		return Integer.parseInt(getValue("PaintDelayMs"));
	}
	
	public String getLanguage()
	{
		return getValue("Language");
	}
	
	public String getCountry()
	{
		return getValue("Country");
	}
}
