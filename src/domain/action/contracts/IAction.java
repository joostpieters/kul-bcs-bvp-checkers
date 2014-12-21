package domain.action.contracts;

import common.Player;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IBasicGameUpdateSource;

public interface IAction extends IBasicGameUpdateSource
{	
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer);
	
	public void executeOn(IBoard board, Player currentPlayer);
	
	public Location getFrom();
}
