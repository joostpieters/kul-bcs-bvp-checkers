package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import common.Configs;
import common.Player;
import domain.action.ActionPriorityComparator;
import domain.action.ActionRequest;
import domain.board.contracts.IBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Direction;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;

public class GameAnalyzer extends GameUpdateSource {
	private static final ActionPriorityComparator ActionPriorityComparator = new ActionPriorityComparator();
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
	
	public boolean isActionAllowed(ActionRequest request)
	{
		return !Configs.MandatoryMaximalCatching || getAllowedActions().contains(request);
	}
	
	public List<ActionRequest> getAllowedActions()
	{
		List<ActionRequest> actions = getPossibleActions();
		Collections.sort(actions, ActionPriorityComparator.reversed());
		int highest = actions.get(0).getNumberOfCatches();
		List<ActionRequest> possible = actions.stream().filter(a -> a.getNumberOfCatches() == highest).collect(Collectors.toList());
		
		return possible;
	}
	
	public List<ActionRequest> getPossibleActions() //TODO include multi-range (recursive on actionable pieces -- but enforce flying straight!)
	{
		List<ActionRequest> actions = new ArrayList<ActionRequest>();
		Player player = getGame().getCurrentPlayer();
		HashMap<Location, IPiece> playerPieces = getGame().getBoard().getPlayerPieces(player);
		for(Location location : playerPieces.keySet())
		{
			IPiece piece = playerPieces.get(location);
			List<ActionRequest> possibleSteps = getStepsFromLocation(location, piece.canStepBackward());
			List<ActionRequest> possibleCatches = getCatchesFromLocation(location, piece.canCatchBackward());
			actions.addAll(possibleSteps);
			actions.addAll(possibleCatches);
		}
		
		return actions;
	}

	public boolean isCurrentPlayerOutOfMoves()
	{
		return getPossibleActions().size() == 0;
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

	private List<ActionRequest> getCatchesFromLocation(Location location, boolean includeBackwards)
	{
		List<ActionRequest> requests = new ArrayList<ActionRequest>();
		IBoard board = getGame().getBoard();
		Player player = getGame().getCurrentPlayer();
		List<Location> targets = new ArrayList<Location>();
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
		try { targets.add(location.getRelativeLocation(player, Direction.Front, Direction.Front, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		if(includeBackwards)
		{
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Right, Direction.Right)); } catch(IllegalArgumentException outOfRange) {}
			try { targets.add(location.getRelativeLocation(player, Direction.Back, Direction.Back, Direction.Left, Direction.Left)); } catch(IllegalArgumentException outOfRange) {}
		}
		
		for(Location target : targets)
		{
			DiagonalLocationPair pair = new DiagonalLocationPair(location, target);
			if(board.isLocationFree(target) && board.isLocationOccupiedBy(player.getOpponent(), pair.getCenterBetween()))
			{
				requests.add(new ActionRequest(true, location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
	
	private List<ActionRequest> getStepsFromLocation(Location location, boolean includeBackwards)
	{
		List<ActionRequest> requests = new ArrayList<ActionRequest>();
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
				requests.add(new ActionRequest(false, location.getIndex(), target.getIndex()));
			}
		}
		return requests;
	}
}
