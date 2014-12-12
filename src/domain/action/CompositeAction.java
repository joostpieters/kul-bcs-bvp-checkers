package domain.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.board.Board;
import domain.location.Location;

public class CompositeAction extends Action {
	private final List<Action> actions;
	
	public CompositeAction(Action... actions) {
		this.actions = Arrays.asList(actions);
	}
	
	public CompositeAction(List<Action> actions) {
		this.actions = new ArrayList<Action>(actions);
	}
	
	public CompositeAction() {
		this.actions = new ArrayList<Action>();
	}
	
	protected List<Action> getActions()
	{
		return actions;
	}
	
	protected void addAction(Action action)
	{
		getActions().add(action);
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer) {
		if(getActions().size() == 0)
		{
			throw new IllegalStateException("CompositeAction is empty.");
		}
		Board testBoard = (Board)board.getDeepClone();
		for(Action action : getActions())
		{
			if(!action.isValidOn(testBoard, currentPlayer))
			{
				return false;
			}
			action.executeOn(testBoard, currentPlayer);
		}
		return true;
	}
	
	@Override
	public void executeOn(Board board, Player currentPlayer) {
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
	public String toString() {
		List<String> list = getActions().stream().map(e -> e.toString()).collect(Collectors.toList());
		String[] strings = (String[]) list.toArray(new String[list.size()]);
		return "(" + String.join(" + ", strings) + ")";
	}

	@Override
	protected Location getFrom() {
		return getActions().get(0).getFrom();
	}
}
