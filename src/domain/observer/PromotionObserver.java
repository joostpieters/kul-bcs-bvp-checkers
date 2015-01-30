package domain.observer;

import java.util.HashMap;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.update.BasicUpdateProcessor;

/**
 * This {@link IObserver} monitors the {@link IGame} 
 * for possible promotions every time the {@link Player}s switch turns.
 * If it found a promotion, it signals this to its own observers 
 * through the {@link IObserver#firePromotion(IReadOnlyBoard, Location)} update.
 */
public class PromotionObserver extends BasicUpdateProcessor
{
	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		Player previous = switchedIn.getOpponent();
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(previous);
		for(Location location : playerPieces.keySet())
		{
			if(location.isOnPromotionRow(previous))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					emitPromotion(board, location);
				}
			}
		}
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
	}
}
