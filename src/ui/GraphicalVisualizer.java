package ui;

import java.awt.event.WindowEvent;
import ui.contracts.IVisualizer;
import bvp.Bord;
import bvp.Figuren;
import bvp.Figuur;
import common.ConfigurationManager;
import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoardSize;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.location.LocationOutOfRangeException;
import domain.piece.contracts.IPiece;
import domain.square.contracts.IReadOnlySquare;

public class GraphicalVisualizer implements IVisualizer
{
	private final Bord frame = new Bord("Checkers");
	
	private Bord getFrame()
	{
		return frame;
	}
	
	public GraphicalVisualizer() { }

	private Figuur getFigure(Figuren pieces, IPiece piece) //50 x 50
	{
		if(!piece.canPromote())
		{
			if(piece.getPlayer() == Player.White)
			{
				return pieces.getFiguur("wittedamschijf");
			}
			else
			{
				return pieces.getFiguur("zwartedamschijf");
			}
		}
		else
		{
			if(piece.getPlayer() == Player.White)
			{
				return pieces.getFiguur("witteschijf");
			}
			else
			{
				return pieces.getFiguur("zwarteschijf");
			}
		}
	}
	
	@Override
	public void paint(IReadOnlyBoard board)
	{
		Bord frame = getFrame();
		IBoardSize size = board.getSize();
		 System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Figuren numbers = new Figuren(ConfigurationManager.getInstance().getCijfersPath());
		Figuren pieces = new Figuren(ConfigurationManager.getInstance().getSchijvenPath());
		Figuur background = new Figuur(size.getCols()*50, size.getRows()*50);
		int squareSizePx = ConfigurationManager.getInstance().getSquareSizePx();
		Figuur whiteSquare = new Figuur(squareSizePx, squareSizePx);
		Figuur blackSquare = new Figuur(squareSizePx, squareSizePx);
		whiteSquare.vulRechthoek(0,0,squareSizePx, squareSizePx, ConfigurationManager.getInstance().getLightColor());
		blackSquare.vulRechthoek(0,0,squareSizePx, squareSizePx, ConfigurationManager.getInstance().getDarkColor());
		for (int row = 0; row < size.getRows(); row++)
		{
			for (int col = 0; col < size.getCols(); col++)
			{
				try
				{
					Location location = new Location(row, col, size);
					IReadOnlySquare square = board.getSquare(location);
					if (location.isWhite())
					{
						background.plaatsFiguur(whiteSquare, col*squareSizePx, row*squareSizePx);
					}
					else
					{
						background.plaatsFiguur(blackSquare, col*squareSizePx, row*squareSizePx);
						int hPixels = col*squareSizePx;
						int vPixels = row*squareSizePx;
						if(square.hasPiece())
						{
							IPiece piece = square.getPiece();
							Figuur figure = getFigure(pieces, piece);
							background.plaatsFiguur(figure, hPixels, vPixels);
						}
						int index = location.getIndex();
						String digits = Integer.toString(index);
						for(int i=0; i<digits.length(); i++)
						{
							String digit = digits.substring(i, i+1);
							Figuur figure = numbers.getFiguur(digit).scaleer(10,10);
							background.plaatsFiguur(figure, hPixels + squareSizePx/3 + 10*i, vPixels + squareSizePx/3);
						}
					}
				}
				catch (LocationOutOfRangeException e)
				{
					assert false;
				}
			}
		}
		frame.toon(background);
		try {Thread.sleep(ConfigurationManager.getInstance().getPaintDelayMs());} catch (InterruptedException e) { }
	}
	
	private void close()
	{
		Bord frame = getFrame();
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		paint(board);
	}

	@Override
	public void fireGameOver(Player winner)
	{
		close();		
	}

	@Override
	public void firePromotion(IReadOnlyBoard board, Location location)
	{
		paint(board);
	}

	@Override
	public void fireOutOfMoves(Player player)
	{
	}

	
	@Override
	public void fireProposeRemise(Player proposer)
	{
	}

	@Override
	public void fireAcceptRemise()
	{
	}

	@Override
	public void fireDeclineRemise()
	{
	}

	@Override
	public void fireResign(Player resignee)
	{
	}

	@Override
	public void fireStart(IReadOnlyBoard board, Player starter)
	{
		paint(board);
	}

	@Override
	public void fireWarning(String message)
	{
	}

	@Override
	public void fireError(String message, Exception ex)
	{
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
	}

	@Override
	public void fireForcedRemise()
	{
	}
}
