import java.util.Scanner;

import common.Player;


public class InputProvider
{
	private final Scanner scanner;
	
	private Scanner getScanner() {
		return scanner;
	}
	
	public InputProvider()
	{
		scanner = new Scanner(System.in);
	}
	
	public void close()
	{
		getScanner().close();
	}

	public String getInput(Player player) //TODO remise, give up
	{
		System.out.print(player + "'s next move: ");
		return scanner.nextLine();
	}
	
	interface IInput
	{
		public void process(Player player);
	}
	
	class GiveUpInput implements IInput
	{
		@Override
		public void process(Player player) {
			System.out.println(player + " gives up. " + player.getOpponent() + " win!");
			System.exit(0);
		}
	}
	
	class RemiseInput implements IInput
	{
		@Override
		public void process(Player player) {
			System.out.println(player + " proposes remise. Accept? y/n");
			if(true) //TODO ask confirmation
			{
				System.out.println("Remise.");
			}
			else
			{
				//TODO ask input again somehow
			}
		}		
	}
	
	class MoveInput implements IInput
	{
		@Override
		public void process(Player player) {
			// TODO process move
		}		
	}
}
