import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import bvp.Bord;
import bvp.Figuren;
import bvp.Figuur;
import common.Configs;
import common.Location;
import common.Player;
import domain.action.ActionCatch;
import domain.action.ActionMove;
import domain.board.Board;
import domain.board.BoardFactory;
import domain.board.BoardSize;
import domain.piece.Piece;
import domain.square.Square;

public class GameController {

	public static void main(String[] args) {
		File file = new File("data\\input\\default.txt");
		try {
			Board board = BoardFactory.create(file);
			GameController controller = new GameController(board);
			controller.play();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Board board;
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	private Player currentPlayer = Configs.FirstPlayer;

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	private void switchCurrentPlayer()
	{
		setCurrentPlayer(getCurrentPlayer() == Player.White ? Player.Black : Player.White);
	}
	
	private Bord guiFrame = new Bord("Checkers");
	
	public Bord getGuiFrame()
	{
		return guiFrame;
	}
	
	public GameController(Board board) {
		this.board = board;
	}
	
	public void showGui()
	{
		Bord frame = getGuiFrame();
		BoardSize size = getBoard().getSize();
		Figuren numbers = new Figuren("data\\cijfers32.fig");
		Figuren schijven = new Figuren("data\\schijven.fig");
		Figuur whitePiece = schijven.getFiguur("witteschijf"); //50 x 50
		Figuur whiteDame = schijven.getFiguur("wittedamschijf");
		Figuur blackPiece = schijven.getFiguur("zwarteschijf");
		Figuur blackDame = schijven.getFiguur("zwartedamschijf");
		Figuur background = new Figuur(size.getCols()*50, size.getRows()*50);
		Figuur whiteSquare = new Figuur(Configs.SquareSizePx, Configs.SquareSizePx);
		Figuur blackSquare = new Figuur(Configs.SquareSizePx, Configs.SquareSizePx);
		whiteSquare.vulRechthoek(0,0,Configs.SquareSizePx,Configs.SquareSizePx,Configs.LightColor);
		blackSquare.vulRechthoek(0,0,Configs.SquareSizePx,Configs.SquareSizePx,Configs.DarkColor);
		for (int row = 0; row < size.getRows(); row++)
		{
			for (int col = 0; col < size.getCols(); col++)
			{
				Location location = new Location(row, col, size);
				Square square = getBoard().getSquare(location);
				if (location.isWhite())
				{
					background.plaatsFiguur(whiteSquare, col*Configs.SquareSizePx, row*Configs.SquareSizePx);
				}
				else
				{
					background.plaatsFiguur(blackSquare, col*Configs.SquareSizePx, row*Configs.SquareSizePx);
					int hPixels = col*Configs.SquareSizePx;
					int vPixels = row*Configs.SquareSizePx;
					if(square.hasPiece())
					{
						Piece piece = square.getPiece();
						Player player = piece.getPlayer();
						if(player == Player.White) //TODO dames
						{
							background.plaatsFiguur(whitePiece, hPixels, vPixels);
						}
						else
						{
							background.plaatsFiguur(blackPiece, hPixels, vPixels);
						}
					}
					int index = location.getIndex();
    				String digits = Integer.toString(index);
    				for(int i=0; i<digits.length(); i++)
    				{
    					String digit = digits.substring(i, i+1);
    					Figuur figure = numbers.getFiguur(digit).scaleer(10,10);
						background.plaatsFiguur(figure, hPixels + Configs.SquareSizePx/3 + 10*i, vPixels + Configs.SquareSizePx/3);
    				}
				}
			}
		}
		frame.toon(background);
	}
	
	private void play()
	{
		Scanner scanner = new Scanner(System.in);
		Board board = getBoard();
		System.out.println(board);
		showGui();
		while(true)
		{
			Player currentPlayer = getCurrentPlayer();
			String move = askMove(scanner);
			
			if(ActionMove.isValidPattern(move))
			{
				ActionMove action = new ActionMove(move);
				if(action.isValidOn(board, currentPlayer))
				{
					System.out.println("Valid move");
					action.executeOn(board, currentPlayer);
					//check promotion
					//check victory conditions
					switchCurrentPlayer();
					System.out.println(board);
					showGui();
				}
				else
				{
					System.out.println("Invalid move");
				}
			}
			else if(ActionCatch.isValidPattern(move))
			{
				ActionCatch action = new ActionCatch(move);
				if(action.isValidOn(board, currentPlayer))
				{
					System.out.println("Valid catch");
					action.executeOn(board, currentPlayer);
					//check promotion
					//check victory conditions
					switchCurrentPlayer();
					System.out.println(board);
					showGui();
				}
				else
				{
					System.out.println("Invalid catch");
				}
				System.out.println("Valid catch");
			}
			else
			{
				System.out.println("Invalid pattern, try again.");
			}
		}
		//scanner.close();
	}
	
	private String askMove(Scanner scanner)
	{
		System.out.print(getCurrentPlayer() + "'s next move: ");
		return scanner.nextLine();
	}
	
	//TODO remise
	//TODO give up
	//TODO no more moves
	//TODO promotions
	//TODO Dames: flying moves, catches
	//TODO CompositeActions: multiple catches
	//TODO implement Configs
}
