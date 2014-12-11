package domain.input;

import java.util.HashMap;

import common.Location;
import common.Player;
import domain.Game;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.board.Board;
import domain.piece.Piece;

public class ActionInput implements IInput
{
	private final String move;
	private final Game game;
	
	private String getMove()
	{
		return move;
	}
	
	private Game getGame()
	{
		return game;
	}
	
	public ActionInput(String move, Game game)
	{
		this.move = move;
		this.game = game;
	}

	@Override
	public boolean process()
	{
		Game game = getGame();
		Board board = game.getBoard();
		Player currentPlayer = game.getCurrentPlayer();
		
		try
		{
			Action action = ActionFactory.create(getMove(), board.getSize());
			if(action.isValidOn(board, currentPlayer))
			{
				action.executeOn(board, currentPlayer);
				checkForPromotions(currentPlayer);
				//TODO check victory conditions
				game.switchCurrentPlayer();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(IllegalArgumentException ex)
		{
			game.getUI().showWarning(ex.getMessage());
			return false;
		}
	}
	
	private void checkForPromotions(Player player) //TODO find better place
	{
		Board board = getGame().getBoard();
		HashMap<Location, Piece> playerPieces = board.getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(player))
			{
				Piece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					board.promotePiece(location);
				}
			}
		}
	}
}