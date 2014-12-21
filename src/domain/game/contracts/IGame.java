package domain.game.contracts;

import common.Player;
import domain.board.contracts.IBoard;

public interface IGame extends IReadOnlyGame
{
	public void switchCurrentPlayer();

	public void remise();

	public void gameOver(Player winner);
	
	@Override
	public IBoard getBoard();
}