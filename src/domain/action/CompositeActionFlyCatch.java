package domain.action;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import common.Player;
import domain.board.contracts.IBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.square.contracts.ISquare;

public class CompositeActionFlyCatch extends CompositeAction
{
	
	public CompositeActionFlyCatch(IBoard board, Player currentPlayer, DiagonalLocationPair pair) //already requires board during construction 
	{		
		if(pair.getDiagonalDistance() < 2)
		{
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		Set<Location> locations = board.getPlayerPieces(currentPlayer.getOpponent()).keySet();
		List<Location> locationsBetween = locations.stream().filter(l -> pair.isBetweenExclusive(l)).collect(Collectors.toList());
		if(locationsBetween.size() != 1)
		{
			throw new IllegalStateException("Can only catch one piece at a time.");
		}
		Location enemyLocation = locationsBetween.get(0);
		
		List<DiagonalLocationPair> pairs = pair.getPairsBetweenInclusive();
		Action subAction;
		for(int i=0; i < pairs.size(); i++)
		{
			DiagonalLocationPair stepPair = pairs.get(i);
			if(stepPair.getTo().equals(enemyLocation)) //replace 2 steps with catch
			{
				if(i+1 == pairs.size())
				{
					throw new IllegalStateException("Must jump over piece to catch.");
				}
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
	public boolean isValidOn(IBoard board, Player currentPlayer)
	{		
		if(super.isValidOn(board, currentPlayer))
		{
			Location from = getFrom();
			ISquare fromSquare = board.getSquare(from);
			return fromSquare.getPiece().canFly(); //square now surely hasPiece
		}
		else
		{
			return false;
		}
	}
}
