package domain.updates;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.updates.contracts.IGameFollower;

/**
 * Combines {@link GameUpdateSource} with {@link IGameFollower} to form a GameUpdatePropagator.
 * This class simply propagates any updates it receives from its members to its subscribers.
 */
public abstract class GameUpdatePropagator extends GameUpdateSource implements IGameFollower
{

	@Override
	public void update(IReadOnlyBoard board)
	{
		updateFollowers(board);
	}

	@Override
	public void gameOver(Player winner)
	{
		updateFollowersGameOver(winner);		
	}
}
