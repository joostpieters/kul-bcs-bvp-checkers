package domain.action;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.BasicGameUpdateSource;

public abstract class Action extends BasicGameUpdateSource
{	
	public abstract boolean isValidOn(IReadOnlyBoard board, Player currentPlayer);
	
	public abstract void executeOn(IBoard board, Player currentPlayer);
	
	protected abstract Location getFrom();
	
	@Override
	public abstract String toString();
}
