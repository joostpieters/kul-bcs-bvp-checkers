package domain.action;

import common.Player;
import domain.board.Board;

public abstract class Action {
	
	//public abstract boolean isValidPattern(String pattern);
	
	public abstract boolean isValidOn(Board board, Player currentPlayer);
	
	public abstract void executeOn(Board board, Player currentPlayer);
	
	@Override
	public abstract String toString();
}
