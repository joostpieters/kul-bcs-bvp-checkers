package domain.update;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.update.contracts.IUpdate;

/**
 * This class directly propagates all {@link IUpdate}s from source to observer, 
 * thereby limiting the direct dependency of many classes outside this 
 * package on the often-changed IGameObserver interface.
 */
public abstract class UpdatePropagator extends UpdateProcessor
{
	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		emitUpdateBoard(board, performer);
	}

	@Override
	public void fireGameOver(Player winner)
	{
		emitGameOver(winner);
	}
	
	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
		emitPromotion(board, location);
	}
	
	@Override
	public void fireOutOfMoves(Player player)
	{
		emitOutOfMoves(player);
	}
	
	@Override
	public void fireAcceptRemise()
	{
		emitAcceptRemise();
	}
	
	@Override
	public void fireDeclineRemise()
	{
		emitDeclineRemise();
	}
	
	@Override
	public void fireProposeRemise(Player proposer)
	{
		emitProposeRemise(proposer);
	}
	
	@Override
	public void fireResign(Player resignee)
	{
		emitResign(resignee);
	}
	
	@Override
	public void fireStart(IReadOnlyBoard board, Player starter)
	{
		emitStart(board, starter);
	}
	
	@Override
	public void fireExecuteAction(IAction action)
	{
		emitExecuteAction(action);
	}
	
	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		emitSwitchPlayer(board, switchedIn);
	}
	
	@Override
	public void fireForcedRemise()
	{
		emitForcedRemise();
	}
	
	@Override
	public void fireWarning(String message)
	{
		emitWarning(message);
	}
	
	@Override
	public void fireError(String message, Exception ex)
	{
		emitError(message, ex);
	}
}
