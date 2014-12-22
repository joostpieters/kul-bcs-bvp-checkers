package domain.observers;

import java.util.HashMap;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.game.contracts.IGame;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.UpdateSource;
import domain.updates.contracts.IBasicUpdateProcessor;

/**
 * This {@link IObserver} monitors the {@link IGame} 
 * for possible promotions every time the {@link Player}s switch turns.
 * If it found a promotion, it signals this to its own observers 
 * through the {@link IObserver#promotion(IReadOnlyBoard, Location)} update.
 */
public class PromotionObserver extends UpdateSource implements IBasicUpdateProcessor
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
	public void executeAction(IAction action)
	{
	}

	@Override
	public void linkBasic(IBasicUpdateProcessor propagator)
	{
		this.subscribeBasic(propagator);
		propagator.subscribeBasic(this);
	}
	
	@Override
	public void unlinkBasic(IBasicUpdateProcessor processor)
	{
		this.unsubscribeBasic(processor);
		processor.unsubscribeBasic(this);
	}
}
