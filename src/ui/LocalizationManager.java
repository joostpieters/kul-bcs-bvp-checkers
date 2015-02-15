package ui;

import java.util.Locale;
import java.util.ResourceBundle;

import common.ConfigurationManager;

public class LocalizationManager
{
	private final static ResourceBundle localizations;
	
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
