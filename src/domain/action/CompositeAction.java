package domain.action;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import common.Player;
import domain.board.Board;

public class CompositeAction extends Action {
	private final List<Action> actions;
	
	public CompositeAction(Action... actions) {
		this.actions = Arrays.asList(actions);
	}
	
	@Override
	public boolean isValidOn(Board board, Player currentPlayer) {
		Board testBoard = (Board)board.getDeepClone();
		for(Action action : actions)
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
		
		for(Action action : actions)
		{
			action.executeOn(board, currentPlayer);
		}
		
	}
	
	@Override
	public String toString() {
		List<String> list = actions.stream().map(e -> e.toString()).collect(Collectors.toList());
		String[] strings = (String[]) list.toArray(new String[list.size()]);
		return String.join(" + ", strings);
	}
}
