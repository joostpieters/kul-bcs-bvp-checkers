package domain;

import java.util.HashMap;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;
import domain.updates.contracts.IBasicGameObserver;

public class PromotionObserver extends GameUpdateSource implements IBasicGameObserver
{

	@Override
	public void updateBoard(IReadOnlyBoard board, Player performer) //TODO don't promote when leaving in same turn
	{
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(performer);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(performer))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					updateObserversPromotion(location);
				}
			}
		}
	}
}
