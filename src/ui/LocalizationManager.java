package ui;

import java.util.Locale;
import java.util.ResourceBundle;

import common.Configs;

public class LocalizationManager
{
	private final static ResourceBundle localizations;
	
	public static String getString(String key)
	{
		return localizations.getString(key);
	}
	
	static
	{
		Locale currentLocale = new Locale(Configs.Language, Configs.Country);
		localizations = ResourceBundle.getBundle("Localizations", currentLocale);
	}
}
