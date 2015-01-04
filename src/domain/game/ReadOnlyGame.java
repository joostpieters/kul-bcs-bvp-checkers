package domain.game;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class ReadOnlyGame implements IReadOnlyGame
{
	private final IGame game;
	
	private IGame getGame()
	{
		return game;
	}
	
	public ReadOnlyGame(IGame game)
	{
		this.game = game;
	}
	
	@Override
	public IReadOnlyBoard getBoard()
	{
		return getGame().getBoard().getReadOnlyBoard();
	}

	@Override
	public Player getCurrentPlayer()
	{
		return getGame().getCurrentPlayer();
	}

	@Override
	public boolean isOver()
	{
		return getGame().isOver();
	}

	@Override
	public Player getWinner()
	{
		return getGame().getWinner();
	}

	@Override
	public GameState getGameState()
	{
		return getGame().getGameState();
	}

	@Override
	public IReadOnlyGame getReadOnlyGame()
	{
		return this;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj == this)
		{
			return true;
		}
		if(obj instanceof ReadOnlyGame)
		{
			ReadOnlyGame casted = (ReadOnlyGame)obj;
			
			boolean result = 
					this.getGameState() == casted.getGameState() &&
					this.getCurrentPlayer() == casted.getCurrentPlayer() &&
					this.getBoard().equals(casted.getBoard());
			
			if(getGameState() == GameState.Finished)
			{
				result &= this.getWinner() == casted.getWinner();
			}
			return result;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return getGame().hashCode();
	}
}
