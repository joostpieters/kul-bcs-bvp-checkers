package domain;

import java.util.HashMap;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.updates.GameUpdateSource;
import domain.updates.contracts.IGameFollower;

public class PromotionChecker extends GameUpdateSource implements IGameFollower
{

	@Override
	public void update(IReadOnlyBoard board, Player performer) //TODO don't promote when leaving in same turn
	{
		HashMap<Location, IPiece> playerPieces = board.getPlayerPieces(performer);
		for(Location location : playerPieces.keySet())
		{
			if(location.isPromotionRow(performer))
			{
				IPiece piece = playerPieces.get(location);
				if(piece.canPromote())
				{
					updateFollowersPromotion(location);
					//board.promotePiece(location);
				}
			}
		}
	}

	@Override
	public void gameOver(Player winner) { }

	@Override
	public void promotion(Location location)
	{
		//self-generated, ignore
	}

	@Override
	public void outOfMoves(Player player) { }
}
