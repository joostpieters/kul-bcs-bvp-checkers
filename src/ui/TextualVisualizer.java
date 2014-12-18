package ui;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

public class TextualVisualizer implements IVisualizer
{

	@Override
	public void paint(IReadOnlyBoard board)
	{
		System.out.println(board);
	}

	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
		System.out.println(performer + "'s move:");
		paint(board);
	}

	@Override
	public void gameOver(Player winner)
	{
		System.out.println(String.format("Game over. %s won!", winner));
	}

	@Override
	public void promotion(Location location)
	{
		System.out.println("Promotion!");
	}

	@Override
	public void outOfMoves(Player player)
	{
		System.out.println(player + " is out of moves.");
	}

	@Override
	public void proposeRemise(Player proposer)
	{
		System.out.println(proposer + " proposes remise.");
	}

	@Override
	public void agreeRemise()
	{
		System.out.println("Remise.");
	}

	@Override
	public void disagreeRemise()
	{
		System.out.println("No remise.");
	}

	@Override
	public void resign(Player resignee)
	{
		System.out.println(resignee + " resigned.");
	}

	@Override
	public void start(IReadOnlyBoard board, Player starter)
	{
		System.out.println(String.format("Game started with %s.", starter));
	}

	@Override
	public void warning(String message)
	{
		System.out.println("Warning: " + message);
	}

	@Override
	public void error(String message, Exception ex)
	{
		System.out.println("Error: " + message);
		ex.printStackTrace();
	}
}
