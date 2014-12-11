package domain;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import common.Configs;
import common.Direction;
import common.Location;
import common.Player;
import common.RestrictedLocationPair;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.input.IInput;
import domain.input.InputProvider;
import domain.piece.Piece;

public class GameController
{
	private final Game game;
	
	private Game getGame()
	{
		return game;
	}
	
	public GameController(Game game)
	{
		this.game = game;
	}
	
	private boolean isOutOfMoves(Player player)
	{
		HashMap<Location, Piece> playerPieces = getGame().getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			boolean includeBackwards = playerPieces.get(location).canMoveBackward();
			if(canMoveFromLocation(player, location, includeBackwards) || canCatchFromLocation(player, location))
			{
				return false;
			}
		}
		return true;
	}

	private boolean canCatchFromLocation(Player player, Location location) //TODO find better place
	{
		Board board = getGame().getBoard();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		if(Configs.BackwardCatchingAllowed)
		{
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		}
		
		for(Location target : targets)
		{
			RestrictedLocationPair pair = new RestrictedLocationPair(location, target);
			if(board.isLocationFree(target) && board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				return true;
			}
		}
		return false;
	}

	private boolean canMoveFromLocation(Player player, Location location, boolean includeBackwards) //TODO find better place
	{
		Board board = getGame().getBoard();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		if(includeBackwards)
		{
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		}
		for(Location target : targets)
		{
			if(board.isLocationFree(target))
			{
				return true;
			}
		}
		return false;
	}
	
	public void play()
	{
		Game game = getGame();
		Board board = game.getBoard();
		game.getUI().visualize(board);
		InputProvider inputProvider = new InputProvider(game.getUI());
		while(!game.isOver())
		{
			Player currentPlayer = game.getCurrentPlayer();
			if(isOutOfMoves(currentPlayer))
			{
				game.getUI().outOfMoves(currentPlayer);
				game.gameOver(currentPlayer.getOpponent());
				break;
			}
			IInput input = inputProvider.askInput(game);
			boolean result = input.process();
			
			if(result)
			{
				game.getUI().visualize(board);
				saveBoardByDateTime(board);
			}
			else
			{
				game.getUI().showWarning("could not process input, try again.");
			}
		}
		game.getUI().close();
	}
	
	private static void saveBoardByDateTime(Board board)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = format.format(Calendar.getInstance().getTime());
		String filename = "data\\output\\Board_" + timestamp + ".txt";
		try {
			BoardFactory.save(board, new java.io.File(filename));
		} catch (FileNotFoundException e) {
			System.err.println("Could not save board to: " + filename);
			e.printStackTrace();
		}
	}
	
	//TODO implement Configs
}
