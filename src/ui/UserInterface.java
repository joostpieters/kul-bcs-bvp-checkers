package ui;

import java.util.Scanner;

import common.Player;

public class UserInterface
{
	//TODO localize strings
	//TODO restrict usage to Inputs?
	private final static Scanner inputScanner = new Scanner(System.in);
	
	public boolean askYesNo(String message)
	{
		return askInput(message + " (Y/N)").toLowerCase().equals("y");
	}
	
	public String askInput(String message)
	{
		System.out.print(message);
		return inputScanner.nextLine();
	}
	
	public String askMoveInput(Player player)
	{
		return askInput(player + "'s next move: ");
	}
	
	public void showMessage(String message)
	{
		System.out.println(message);
	}
	
	public void close()
	{
		inputScanner.close();
	}
}
