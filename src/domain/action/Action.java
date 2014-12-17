package domain.action;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.GameUpdateSource;

public abstract class Action extends GameUpdateSource
{	
	public abstract boolean isValidOn(IReadOnlyBoard board, Player currentPlayer);
	
	public abstract void executeOn(IBoard board, Player currentPlayer);
	
	protected abstract Location getFrom();
	
	@Override
	public abstract String toString();
}
