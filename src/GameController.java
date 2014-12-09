import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ui.Gui;
import common.Configs;
import common.Direction;
import common.Location;
import common.LocationPair;
import common.Player;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.piece.Piece;

public class GameController
{
	private final Gui gui;
	
	private Gui getGui()
	{
		return gui;
	}
	
	private final Board board;
	
	private Board getBoard()
	{
		return board;
	}

	private Player currentPlayer = Configs.FirstPlayer;

	private Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	private void switchCurrentPlayer()
	{
		setCurrentPlayer(getCurrentPlayer().getOpponent());
	}
	
	public GameController(Board board)
	{
		this.board = board;
		this.gui = new Gui(board);
	}
	
	private boolean isOutOfMoves(Player player)
	{
		HashMap<Location, Piece> playerPieces = getBoard().getPlayerPieces(player);
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
	
	private void checkForPromotions(Player player)
	{
		HashMap<Location, Piece> playerPieces = getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(player))
			{
				Piece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					getBoard().promotePiece(location);
				}
			}
		}
	}

	private boolean canCatchFromLocation(Player player, Location location) {
		Board board = getBoard();
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
			LocationPair pair = new LocationPair(location, target);
			if(board.isLocationFree(target) && board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				return true;
			}
		}
		return false;
	}

	private boolean canMoveFromLocation(Player player, Location location, boolean includeBackwards) {
		Board board = getBoard();
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
		InputProvider inputProvider = new InputProvider();
		Board board = getBoard();
		//System.out.println(board);
		getGui().paint();
		while(true)
		{
			Player currentPlayer = getCurrentPlayer();
			if(isOutOfMoves(currentPlayer))
			{
				System.out.println(currentPlayer + " lost because there are no more possible moves.");
				break;
			}
			String move = inputProvider.getInput(getCurrentPlayer());
			Action action = ActionFactory.create(move, board.getSize());
			if(action.isValidOn(board, currentPlayer))
			{
				System.out.println("Valid action");
				action.executeOn(board, currentPlayer);
				checkForPromotions(currentPlayer);
				//check victory conditions
				switchCurrentPlayer();
				//System.out.println(board);
				getGui().paint();
				
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
			else
			{
				System.out.println("Invalid move");
			}
		}
		inputProvider.close();
	}
	
	//TODO CompositeActions: fly-catch-fly
	//TODO implement Configs
}
