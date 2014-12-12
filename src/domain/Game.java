package domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ui.UserInterface;
import common.Configs;
import common.Player;
import domain.board.Board;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.piece.Piece;


public class Game {
	private final Board board;
	private final UserInterface ui;
	private Player currentPlayer = Configs.FirstPlayer;
	private Player winner = null;
	private GameState state = GameState.Ongoing;
	
	public Board getBoard()
	{
		return board;
	}
	
	public UserInterface getUI()
	{
		return ui;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public void switchCurrentPlayer()
	{
		setCurrentPlayer(getCurrentPlayer().getOpponent());
	}
	
	public boolean isOver()
	{
		return getGameState() != GameState.Ongoing;
	}
	
	public Player getWinner()
	{
		if(!isOver())
		{
			throw new IllegalStateException("Game is not over yet.");
		}
		if(getGameState() == GameState.Remise)
		{
			throw new IllegalStateException("Remise, there is no winner.");
		}
		return winner;
	}
	
	private void setWinner(Player winner)
	{
		this.winner = winner;
	}
	
	public GameState getGameState()
	{
		return state;
	}
	
	private void setGameState(GameState state)
	{
		this.state = state;
	}
	
	public void remise()
	{
		setGameState(GameState.Remise);
	}
	
	public void gameOver(Player winner)
	{
		setWinner(winner);
		setGameState(GameState.Finished);
		getUI().gameOver(winner);
	}
	
	public Game(Board board, UserInterface ui) {
		this.board = board;
		this.ui = ui;
	}
	
	public boolean isCurrentPlayerOutOfMoves()
	{
		Player player = getCurrentPlayer();
		HashMap<Location, Piece> playerPieces = getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			boolean includeBackwards = playerPieces.get(location).canMoveBackward();
			if(canMoveFromLocation(location, includeBackwards) || canCatchFromLocation(location))
			{
				return false;
			}
		}
		return true; //true by default when player has no more pieces
	}

	private boolean canCatchFromLocation(Location location) //TODO find better place
	{
		Board board = getBoard();
		Player player = getCurrentPlayer();
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
			DiagonalLocationPair pair = new DiagonalLocationPair(location, target);
			if(board.isLocationFree(target) && board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				return true;
			}
		}
		return false;
	}

	private boolean canMoveFromLocation(Location location, boolean includeBackwards) //TODO find better place
	{
		Board board = getBoard();
		Player player = getCurrentPlayer();
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
}
