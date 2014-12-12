package ui;

import java.util.Scanner;

import common.Player;

public class UserInterface { //TODO localize strings
	private final static Scanner inputScanner = new Scanner(System.in);
	
	public boolean askYesNo(String message)
	{
		return askInput(message).toLowerCase().equals("y");
	}
	
	public String askInput(String message)
	{
		//showMessage(message);
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
	
	public void showWarning(String message)
	{
		System.out.println("Warning! " + message);
	}
	
	public void showError(String message)
	{
		System.err.println(message);
	}
	
	public boolean askRemise(Player player)
	{
		String message = player + " proposes remise. Accept (y/n)?";
		return askYesNo(message);
	}
	
	public void agreeRemise()
	{
		showMessage("Remise.");
	}
	
	public void disagreeRemise()
	{
		showMessage("Remise denied.");
	}
	
	public void resign(Player player)
	{
		showMessage(player + " resigns.");
	}
	
	public void gameOver(Player winner)
	{
		showMessage(winner + " wins!");
	}
	
	public void outOfMoves(Player player)
	{
		showMessage(player + " lost because there are no more possible moves.");
	}
	
	public void close()
	{
		inputScanner.close();
	}
}
