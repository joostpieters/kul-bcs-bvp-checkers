package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Configs;
import common.Player;
import domain.board.contracts.IBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;

public class GameAnalyzer extends GameUpdateSource {
	private final Game game;
	
	private Game getGame() {
		return game;
	}
	
	public GameAnalyzer(Game game) {
		this.game = game;
	}
	
	public void findAndPerformPromotions()
	{
		Player player = getGame().getCurrentPlayer();
		IBoard board = getGame().getBoard();
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(player))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					board.promotePiece(location);
					updateFollowers(board.getReadOnlyBoard());
				}
			}
		}
	}

	public boolean isCurrentPlayerOutOfMoves()
	{
		Player player = getGame().getCurrentPlayer();
		HashMap<Location, IPiece> playerPieces = getGame().getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			boolean includeBackwards = playerPieces.get(location).canStepBackward();
			if(canMoveFromLocation(location, includeBackwards) || canCatchFromLocation(location))
			{
				return false;
			}
		}
		return true; //true by default when player has no more pieces
	}
	
	public void processCurrentPlayerOutOfMoves()
	{
		if(!isCurrentPlayerOutOfMoves())
		{
			throw new IllegalStateException("Current player is not out of moves.");
		}
		Game game = getGame();
		Player currentPlayer = game.getCurrentPlayer();
		Player winner = currentPlayer.getOpponent();
		game.getUI().outOfMoves(currentPlayer);
		game.gameOver(winner);
		updateFollowersGameOver(winner);
	}

	private boolean canCatchFromLocation(Location location)
	{
		IBoard board = getGame().getBoard();
		Player player = getGame().getCurrentPlayer();
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

	private boolean canMoveFromLocation(Location location, boolean includeBackwards)
	{
		IBoard board = getGame().getBoard();
		Player player = getGame().getCurrentPlayer();
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
