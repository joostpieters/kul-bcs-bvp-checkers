import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ui.Gui;
import common.Configs;
import common.Direction;
import common.Location;
import common.Player;
import domain.action.Action;
import domain.action.ActionFactory;
import domain.board.Board;
import domain.piece.Piece;

public class GameController {	
	private final Gui gui;
	
	private Gui getGui()
	{
		return gui;
	}
	
	private final Board board;
	
	private Board getBoard() {
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
		setCurrentPlayer(getCurrentPlayer() == Player.White ? Player.Black : Player.White);
	}
	
	public GameController(Board board) {
		this.board = board;
		this.gui = new Gui(board);
	}
	
	private boolean isOutOfMoves(Player player)
	{
		HashMap<Location, Piece> playerPieces = getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			boolean includeBackwards = playerPieces.get(location).canMoveBackwards();
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
			if(board.isLocationFree(target) && board.isLocationOccupiedBy(player.getOpponent(), location.getCenterBetween(target)))
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
		Scanner scanner = new Scanner(System.in);
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
			String move = askMove(scanner);
			Action action = ActionFactory.create(move);
			if(action.isValidOn(board, currentPlayer))
			{
				System.out.println("Valid action");
				action.executeOn(board, currentPlayer);
				checkForPromotions(currentPlayer);
				//check victory conditions
				switchCurrentPlayer();
				//System.out.println(board);
				getGui().paint();
			}
			else
			{
				System.out.println("Invalid move");
			}
		}
		scanner.close();
	}
	
	private String askMove(Scanner scanner)
	{
		System.out.print(getCurrentPlayer() + "'s next move: ");
		return scanner.nextLine();
	}
	
	//TODO remise
	//TODO give up
	//TODO CompositeActions: fly, fly-catch-fly
	//TODO implement Configs
}
