package domain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.Player;
import domain.board.Board;
import domain.input.IInput;
import domain.input.InputProvider;

public class GameController
{
	private final Game game;
	private final List<IGameFollower> followers = new ArrayList<IGameFollower>();
	
	private Game getGame()
	{
		return game;
	}
	
	private List<IGameFollower> getFollowers()
	{
		return followers;
	}

	private void updateFollowers() {
		Board board = getGame().getBoard();
		for(IGameFollower follower : getFollowers())
		{
			follower.update(board);
		}
	}
	
	public GameController(Game game)
	{
		this.game = game;
	}
	
	public void subscribe(IGameFollower follower)
	{
		followers.add(follower);
	}
	
	public void unsubscribe(IGameFollower follower)
	{
		followers.remove(follower);
	}
	
	public void play() throws IOException
	{
		Game game = getGame();
		updateFollowers();
		InputProvider inputProvider = new InputProvider(game.getUI());
		while(!game.isOver())
		{
			if(game.isCurrentPlayerOutOfMoves()) //TODO check move priority
			{
				Player currentPlayer = game.getCurrentPlayer();
				game.getUI().outOfMoves(currentPlayer);
				game.gameOver(currentPlayer.getOpponent());
				break;
			}
			IInput input = inputProvider.askInput(game);
			boolean success = input.process();
			
			if(success)
			{
				updateFollowers();
			}
			else
			{
				game.getUI().showWarning("Could not process input, try again.");
			}
		}
		game.getUI().close();
	}
	
	//TODO implement Configs
}
