package domain.game;

import common.Configs;
import common.Player;
import domain.board.contracts.IBoard;
import domain.game.contracts.GameState;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class Game implements IGame
{
	private final IBoard board;
	private Player currentPlayer = Configs.FirstPlayer;
	private Player winner = null;
	private GameState state = GameState.Ongoing;
	
	public Game(IBoard board)
	{
		this.board = board;
	}
	
	public Game(Game copy)
	{
		this.board = copy.board.getDeepClone();
		this.currentPlayer = copy.currentPlayer;
		this.winner = copy.winner;
		this.state = copy.state;
	}

	@Override
	public IBoard getBoard()
	{
		return board;
	}

	@Override
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	private void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void switchCurrentPlayer()
	{
		setCurrentPlayer(getCurrentPlayer().getOpponent());
	}

	@Override
	public boolean isOver()
	{
		return getGameState() != GameState.Ongoing;
	}

	@Override
	public Player getWinner()
	{
		if (!isOver())
		{
			throw new IllegalStateException("Game is not over yet.");
		}
		if (getGameState() == GameState.Remise)
		{
			throw new IllegalStateException("Remise, there is no winner.");
		}
		return winner;
	}

	private void setWinner(Player winner)
	{
		this.winner = winner;
	}

	@Override
	public GameState getGameState()
	{
		return state;
	}

	private void setGameState(GameState state)
	{
		this.state = state;
	}

	@Override
	public void remise()
	{
		if(isOver())
		{
			throw new IllegalStateException("Game is already over.");
		}
		setGameState(GameState.Remise);
	}

	@Override
	public void gameOver(Player winner)
	{
		if(isOver())
		{
			throw new IllegalStateException("Game is already over.");
		}
		setWinner(winner);
		setGameState(GameState.Finished);
	}

	@Override
	public IReadOnlyGame getReadOnlyGame()
	{
		return new ReadOnlyGame(this);
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
		if(obj instanceof Game)
		{
			Game casted = (Game)obj;
			return 	this.getGameState() == casted.getGameState() &&
					this.getCurrentPlayer() == casted.getCurrentPlayer() &&
					this.winner == casted.winner &&
					this.getBoard().equals(casted.getBoard());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int result = getGameState().hashCode();
		result = 37 * result + getCurrentPlayer().hashCode();
		result = 37 * result + (winner == null ? 0 : winner.hashCode());
		result = 37 * result + getBoard().hashCode();
		return result;
	}
}
