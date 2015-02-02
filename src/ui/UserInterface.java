package ui;

import java.util.Scanner;

import ui.contracts.IUserInterface;
import common.Player;

public class UserInterface implements IUserInterface, AutoCloseable
{
	private final static Scanner INPUT_SCANNER = new Scanner(System.in);
	
	@Override
	public boolean askYesNo(String message)
	{
		return askInput(message + " (Y/N)").toLowerCase().equals("y");
	}
	
	@Override
	public String askInput(String message)
	{
		System.out.print(message);
		return INPUT_SCANNER.nextLine();
	}
	
	@Override
	public String askActionInput(Player player)
	{
		String message = LocalizationManager.getString("askActionInput");
		return askInput(String.format(message, player));
	}
	
	@Override
	public void close()
	{
		INPUT_SCANNER.close();
	}
}
