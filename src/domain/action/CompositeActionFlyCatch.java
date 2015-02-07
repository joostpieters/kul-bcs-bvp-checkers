package domain.action;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.DiagonalLocationPair;
import domain.location.Location;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

/**
 * Represents a {@link CompositeAction}. More specifically one comprised of multiple {@link AtomicActionStep} and {@link AtomicActionCatch} subactions, albeit all on the same diagonal.
 */
public class CompositeActionFlyCatch extends CompositeAction
{
	/**
	 * Creates a new {@link CompositeActionFlyCatch} based on the given {@link DiagonalLocationPair}.
	 * 
	 * @param	opponentPieceLocations
	 * 			The {@link Location}s of all opponent {@link IPiece}s.
	 * @param 	currentPlayer
	 * 			The player that will execute this {@link IAction}.
	 * @param 	pair
	 * 			The from and to {@link Location}s of this pair will determine the start- and endpoints of this {@link CompositeActionFlyCatch}.
	 */
	public CompositeActionFlyCatch(Set<Location> opponentPieceLocations, Player currentPlayer, DiagonalLocationPair pair) 
	{		
		if(pair.getDiagonalDistance() < 2)
		{
			fireWarning("Distance is too short to fly.");
			throw new IllegalStateException("Distance is too short to fly.");
		}
		
		Location opponentLocation = findOpponentPieceLocation(opponentPieceLocations, pair);
		
		generateSubActions(pair, opponentLocation);
	}

	private Location findOpponentPieceLocation(Set<Location> opponentPieceLocations, DiagonalLocationPair pair)
	{
		List<Location> opponentPiecesInBetween = opponentPieceLocations.stream().filter(l -> pair.isBetweenStrict(l)).collect(Collectors.toList());
		int nbOpponentPiecesInBetween = opponentPiecesInBetween.size();
		if(nbOpponentPiecesInBetween == 0)
		{
			fireWarning("Could not find a piece to catch.");
			throw new IllegalStateException("Could not find a piece to catch.");
		}
		else if(nbOpponentPiecesInBetween > 1)
		{
			fireWarning("Can only catch one piece at a time.");
			throw new IllegalStateException("Can only catch one piece at a time.");
		}
		Location opponentLocation = opponentPiecesInBetween.get(0);
		return opponentLocation;
	}

	private void generateSubActions(DiagonalLocationPair pair, Location opponentLocation)
	{
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
			addSubAction(subAction);
		}
	}
	
	/**
	 * Returns true if this {@link IAction} is valid on the given {@link IBoard} and for the given {@link Player},
	 * false otherwise.
	 * 
	 * {@link CompositeActionFlyCatch}'s are valid iff their subactions are valid when executed sequentially and the piece in question can fly.
	 */
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
