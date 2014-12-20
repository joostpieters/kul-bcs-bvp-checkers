package domain.observers;

import java.util.HashMap;

import common.Player;
import domain.Game;
import domain.action.Action;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;
import domain.updates.contracts.IBasicGameObserver;

/**
 * This {@link IGameObserver} monitors the {@link Game} 
 * for possible promotions every time the {@link Player}s switch turns.
 * If it found a promotion, it signals this to its own observers 
 * through the {@link IGameObserver#promotion(IReadOnlyBoard, Location)} event.
 */
public class PromotionObserver extends GameUpdateSource implements IBasicGameObserver
{
	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer)
	{
	}

	@Override
	public void switchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
		Player previous = switchedIn.getOpponent();
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(previous);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(previous))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					updateObserversPromotion(board, location);
				}
			}
		}
	}

	@Override
	public void executeAction(Action action)
	{
	}
}
