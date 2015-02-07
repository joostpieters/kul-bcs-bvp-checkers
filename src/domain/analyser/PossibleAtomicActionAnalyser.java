package domain.analyser;

import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.action.contracts.IActionRequest;
import domain.action.request.CatchActionRequest;
import domain.action.request.MoveActionRequest;
import domain.analyser.contracts.IAnalyser;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;

/**
 * Finds all possible atomic actions (steps and catches). 
 */
public class PossibleAtomicActionAnalyser implements IAnalyser<IActionRequest>
{
	private final IReadOnlyBoard board;

	@Override
	public IReadOnlyBoard getBoard()
	{
		return board;
	}

	public PossibleAtomicActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}

	@Override
	public List<IActionRequest> find(Player currentPlayer, Location pieceLocation)
	{
		PossibleAtomicStepActionAnalyser stepActionAnalyser = new PossibleAtomicStepActionAnalyser(board);
		PossibleAtomicCatchActionAnalyser catchActionAnalyser = new PossibleAtomicCatchActionAnalyser(board);
		
		List<MoveActionRequest> possibleAtomicSteps = stepActionAnalyser.find(currentPlayer, pieceLocation);
		List<CatchActionRequest> possibleAtomicCatches = catchActionAnalyser.find(currentPlayer, pieceLocation);
		
		List<IActionRequest> result = new ArrayList<IActionRequest>();
		result.addAll(possibleAtomicSteps);
		result.addAll(possibleAtomicCatches);
		
		return result;
	}
}
