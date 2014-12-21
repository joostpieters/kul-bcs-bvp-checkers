package domain;

import common.Configs;
import common.Player;
import domain.board.Board;

public class Game
{
	private final Board board;
	private Player currentPlayer = Configs.FirstPlayer;
	private Player winner = null;
	private GameState state = GameState.Ongoing;

	public Board getBoard()
	{
		return board;
	}

	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	private void setCurrentPlayer(Player currentPlayer)
	{
		this.currentPlayer = currentPlayer;
	}

	public void switchCurrentPlayer()
	{
		setCurrentPlayer(getCurrentPlayer().getOpponent());
	}

	public boolean isOver()
	{
		return getGameState() != GameState.Ongoing;
	}

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

	public GameState getGameState()
	{
		return state;
	}

	private void setGameState(GameState state)
	{
		this.state = state;
	}

	public void remise()
	{
		setGameState(GameState.Remise);
	}

	public void gameOver(Player winner)
	{
		setWinner(winner);
		setGameState(GameState.Finished);
	}

	public Game(Board board)
	{
		this.board = board;
	}
}
