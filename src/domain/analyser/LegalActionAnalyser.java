package domain.analyser;

import java.util.List;

import common.ConfigurationManager;
import common.Player;
import domain.action.contracts.IAction;
import domain.action.contracts.IActionRequest;
import domain.action.request.CatchActionRequest;
import domain.board.contracts.IReadOnlyBoard;

/**
 * This class validates {@link IActionRequest}s in a broader scope than {@link IAction#isValidOn(IReadOnlyBoard, Player)} does.
 * More specifically, it validates whether actions follow the priority rules.
 */
public class LegalActionAnalyser
{
	private final IReadOnlyBoard board;

	private IReadOnlyBoard getBoard()
	{
		return board;
	}
	
	public LegalActionAnalyser(IReadOnlyBoard board)
	{
		this.board = board;
	}
	
	public boolean isActionLegal(IActionRequest request)
	{
		if(!ConfigurationManager.getInstance().getMandatoryMaximalCatching())
		{
			return request.isCatch();
		}
		
		Player currentPlayer = request.getPlayer();
		MaximalCatchActionAnalyser analyser = new MaximalCatchActionAnalyser(getBoard());
		List<CatchActionRequest> maximalCatchActions = analyser.find(currentPlayer);
		if(maximalCatchActions.size() > 0) //there is a (maximal) catch
		{
			return maximalCatchActions.contains(request);
		}
		else //there are only valid move actions
		{
			assert(!request.isCatch());
			return true;
		}
	}
}