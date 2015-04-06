package ui;

import java.util.Locale;
import java.util.ResourceBundle;

import common.ConfigurationManager;

/**
 * Simple class that facilitates reading input from the relevant localization file.
 */
public class LocalizationManager
{
	private final static ResourceBundle localizations;
	
	/**
	 * Reads and returns the corresponding text in the localization file of the given key. 
	 * @param 	key
	 * 			The lookup key to use.
	 */
	public static String getString(String key)
	{
		return localizations.getString(key);
	}
	
	static
	{
		Locale currentLocale = new Locale(ConfigurationManager.getInstance().getLanguage(), ConfigurationManager.getInstance().getCountry());
		localizations = ResourceBundle.getBundle("Localizations", currentLocale);
	}
}
