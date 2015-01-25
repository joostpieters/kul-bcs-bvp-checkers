package domain.board;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import domain.board.contracts.IBoard;
import domain.board.contracts.IBoardSize;

public class BoardFactoryTest
{

	@Test
	public void testBoardSize() throws IOException
	{
		Path input = Paths.get("data", "input", "testPromotion.txt");
		IBoardSize size = new BoardSize(10, 10);
		IBoard board = BoardFactory.create(input);
		assertEquals(size, board.getSize());
	}

}
