package domain.action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IGameFollower;

public class CompositeAction extends Action implements IGameFollower {
	private final List<Action> actions = new ArrayList<Action>();
	
	public CompositeAction(Action... actions)
	{
		for(Action action : actions)
		{
			addAction(action);
		}
	}
	
	public CompositeAction() { }
	
	protected List<Action> getActions()
	{
		return actions;
	}
	
	protected void addAction(Action action)
	{
		getActions().add(action);
		action.subscribe(this);
	}
	
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{
		if(getActions().size() == 0)
		{
			throw new IllegalStateException("CompositeAction is empty.");
		}
		IBoard testBoard = board.getDeepClone();
		for(Action action : getActions())
		{
			if(!action.isValidOn(testBoard, currentPlayer))
			{
				return false;
			}
			disableUpdateFollowers();
			action.executeOn(testBoard, currentPlayer);
			enableUpdateFollowers();
		}
		return true;
	}
	
	@Override
	public void executeOn(IBoard board, Player currentPlayer)
	{
		if(!isValidOn(board, currentPlayer))
		{
			throw new IllegalStateException(String.format("%s is invalid.", this));
		}
		
		for(Action action : getActions())
		{
			action.executeOn(board, currentPlayer);
		}
		
	}
	
	@Override
	public String toString()
	{
		List<String> list = getActions().stream().map(e -> e.toString()).collect(Collectors.toList());
		String[] strings = (String[]) list.toArray(new String[list.size()]);
		return "(" + String.join(" + ", strings) + ")";
	}

	@Override
	protected Location getFrom()
	{
		return getActions().get(0).getFrom();
	}

	@Override
	public void update(IReadOnlyBoard board, Player performer) //propagate updates from actions to own followers
	{
		updateFollowers(board, performer);		
	}

	@Override
	public void gameOver(Player winner)
	{
		updateFollowersGameOver(winner);
	}

	@Override
	public void promotion(Location location)
	{
		updateFollowersPromotion(location);		
	}

	@Override
	public void outOfMoves(Player player)
	{
		updateFollowersOutOfMoves(player);
	}
}
