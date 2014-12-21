package domain.game;

import common.Configs;
import common.Player;
import domain.board.contracts.IBoard;
import domain.game.contracts.IGame;
import domain.game.contracts.IReadOnlyGame;

public class Game implements IGame
{
	private final IBoard board;
	private Player currentPlayer = Configs.FirstPlayer;
	private Player winner = null;
	private GameState state = GameState.Ongoing;

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
		setGameState(GameState.Remise);
	}

	@Override
	public void gameOver(Player winner)
	{
		setWinner(winner);
		setGameState(GameState.Finished);
	}

	public Game(IBoard board)
	{
		this.board = board;
	}

	@Override
	public IReadOnlyGame getReadOnlyGame()
	{
		return new ReadOnlyGame(this);
	}
}
