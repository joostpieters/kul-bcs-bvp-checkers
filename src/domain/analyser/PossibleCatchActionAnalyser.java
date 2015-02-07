package domain.analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import common.Player;
import domain.action.ActionFactory;
import domain.action.contracts.IAction;
import domain.action.request.CatchActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.contracts.IPiece;

/**
 * This {@link IAnalyser} find all possible catches on a given board.
 */
public class PossibleCatchActionAnalyser implements IAnalyser<CatchActionRequest>
{
	private final IReadOnlyBoard board;
	
	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}

	/**
	 * Constructs a new {@link PossibleCatchActionAnalyser} with the given {@link IReadOnlyBoard}.
	 * 
	 * @param 	board
	 * 			The {@link IReadOnlyBoard} to analyse.
	 */
	public PossibleCatchActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}
	
	/**
	 * Finds the possible {@link CatchActionRequest}s the given player can choose from, 
	 * using the {@link IPiece} at the given pieceLocation. 
	 */
	public List<CatchActionRequest> find(Player currentPlayer, Location pieceLocation)
	{
		IReadOnlyBoard board = getBoard();
		IAnalyser<CatchActionRequest> specialisedAnalyser = getSpecialisedAnalyser(board, pieceLocation);
		
		Collection<CatchActionRequest> catchRequests = specialisedAnalyser.find(currentPlayer, pieceLocation);
		
		List<CatchActionRequest> result = new ArrayList<CatchActionRequest>();
		for(CatchActionRequest catchRequest : catchRequests)
		{
			IBoard testBoard = board.getDeepClone();
			try
			{
				IAction action = ActionFactory.create(catchRequest, testBoard, currentPlayer);
				if(action.isValidOn(testBoard, currentPlayer)) //this should be OK in most cases
				{
					action.executeOn(testBoard, currentPlayer);
					result.add(catchRequest);
					
					List<CatchActionRequest> allNext = new PossibleCatchActionAnalyser(testBoard).find(currentPlayer, catchRequest.getEnd(testBoard.getSize()));
					for(CatchActionRequest next : allNext)
					{
						result.add(new CatchActionRequest(catchRequest, next));
					}
				}
			}
			catch (LocationOutOfRangeException e)
			{
				assert false;
			}
		}
		
		return result;
	}

	private IAnalyser<CatchActionRequest> getSpecialisedAnalyser(IReadOnlyBoard board, Location pieceLocation)
	{
		if(!board.getSquare(pieceLocation).hasPiece())
		{
			throw new IllegalArgumentException("Given pieceLocation did not contain a piece: " + pieceLocation);
		}
		
		boolean includeFly = board.getSquare(pieceLocation).getPiece().canFly();
		return includeFly
			? new PossibleFlyCatchActionAnalyser(board)
			: new PossibleAtomicCatchActionAnalyser(board);
	}
}
