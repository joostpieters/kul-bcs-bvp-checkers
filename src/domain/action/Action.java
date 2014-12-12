package domain.action;

import common.Player;
import domain.board.Board;
import domain.location.Location;

public abstract class Action
{
	public abstract boolean isValidOn(Board board, Player currentPlayer);
	
	public abstract void executeOn(Board board, Player currentPlayer);
	
	protected abstract Location getFrom();
	
	@Override
	public abstract String toString();
}
