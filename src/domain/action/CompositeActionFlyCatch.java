package domain.action;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.contracts.IReadOnlySquare;

public class CompositeActionFlyCatch extends CompositeAction
{
	public CompositeActionFlyCatch(Set<Location> opponentPieceLocations, Player currentPlayer, DiagonalLocationPair pair) 
	{		
		if(pair.getDiagonalDistance() < 2)
		{
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		List<Location> opponentPiecesInBetween = opponentPieceLocations.stream().filter(l -> pair.isBetweenStrict(l)).collect(Collectors.toList());
		int nbOpponentPiecesInBetween = opponentPiecesInBetween.size();
		if(nbOpponentPiecesInBetween == 0)
		{
			throw new IllegalStateException("Could not find a piece to catch.");
		}
		else if(nbOpponentPiecesInBetween > 1)
		{
			throw new IllegalStateException("Can only catch one piece at a time.");
		}
		Location opponentLocation = opponentPiecesInBetween.get(0);
		
		List<DiagonalLocationPair> pairs = pair.getStepsBetween();
		IAction subAction;
		for(int i=0; i < pairs.size(); i++)
		{
			DiagonalLocationPair stepPair = pairs.get(i);
			if(stepPair.getTo().equals(opponentLocation)) //replace 2 steps with catch
			{
				assert(i+1 < pairs.size()); //because opponentLocation is strictly between pair end-points
				Location catchFrom = stepPair.getFrom();
				Location catchTo = pairs.get(i+1).getTo();
				DiagonalLocationPair catchPair = new DiagonalLocationPair(catchFrom, catchTo);
				subAction = new AtomicActionCatch(catchPair);
				i++; //skip next step
			}
			else //regular step
			{
				subAction = new AtomicActionStep(stepPair);
			}
			addAction(subAction);
		}
	}
	
	@Override
	public boolean isValidOn(IReadOnlyBoard board, Player currentPlayer)
	{		
		if(!super.isValidOn(board, currentPlayer))
		{
			return false;
		}
		Location from = getFrom();
		IReadOnlySquare fromSquare = board.getSquare(from);
		return fromSquare.getPiece().canFly(); //square now surely hasPiece
	}
}
