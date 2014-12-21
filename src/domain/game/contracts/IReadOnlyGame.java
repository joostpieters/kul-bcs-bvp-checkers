package domain.game.contracts;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.GameState;

public interface IReadOnlyGame
{
	public IReadOnlyBoard getBoard();

	public Player getCurrentPlayer();

	public boolean isOver();

	public Player getWinner();

	public GameState getGameState();
	
	public IReadOnlyGame getReadOnlyGame();
}