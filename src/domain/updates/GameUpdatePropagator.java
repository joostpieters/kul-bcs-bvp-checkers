package domain.updates;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IGameObserver;

/**
 * Combines {@link GameUpdateSource} with {@link IGameObserver} to form a GameUpdatePropagator.
 * This class simply propagates any updates it receives from its members to its subscribers.
 */
public abstract class GameUpdatePropagator extends GameUpdateSource implements IGameObserver
{
	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
		updateObserversBoard(board, performer);
	}

	@Override
	public void gameOver(Player winner)
	{
		updateObserversGameOver(winner);		
	}
	
	@Override
	public void promotion(Location location)
	{
		updateObserversPromotion(location);
	}
	
	@Override
	public void outOfMoves(Player player)
	{
		updateObserversOutOfMoves(player);
	}
	
	@Override
	public void agreeRemise()
	{
		updateObserversAgreeRemise();
	}
	
	@Override
	public void disagreeRemise()
	{
		updateObserversDisagreeRemise();
	}
	
	@Override
	public void proposeRemise(Player proposer)
	{
		updateObserversProposeRemise(proposer);
	}
	
	@Override
	public void resign(Player resignee)
	{
		updateObserversResign(resignee);
	}
	
	@Override
	public void start(IReadOnlyBoard board, Player starter)
	{
		updateObserversStart(board, starter);
	}
	
	@Override
	public void warning(String message)
	{
		updateObserversWarning(message);
	}
	
	@Override
	public void error(String message, Exception ex)
	{
		updateObserversError(message, ex);
	}
}
