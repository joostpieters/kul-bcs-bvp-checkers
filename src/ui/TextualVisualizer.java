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
	public void close()
	{
		//do nothing
	}

	@Override
	public void update(IReadOnlyBoard board, Player performer)
	{
		System.out.println(performer + "'s move:");
		paint(board);
	}

	@Override
	public void gameOver(Player winner)
	{
		close();		
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
}
