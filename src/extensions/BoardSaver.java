package extensions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import common.Player;
import domain.action.contracts.IAction;
import domain.board.contracts.IBoard;
import domain.board.contracts.IReadOnlyBoard;
import domain.update.contracts.IBasicObserver;

/**
 * Saves the {@link IBoard} state to a file with every update.
 */
public class BoardSaver implements IBasicObserver
{
	private final Path outputDirectory;
	
	private Path getOutputDirectory()
	{
		return outputDirectory;
	}
	
	public BoardSaver(Path outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}

	public void save(IReadOnlyBoard board, Path filename) throws IOException
	{
		Path output = getOutputDirectory().resolve(filename);
		BufferedWriter writer = Files.newBufferedWriter(output);
		writer.write(board.toString());
		writer.close();
	}
	
	public void saveBoardByDateTime(IReadOnlyBoard board) throws IOException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = format.format(Calendar.getInstance().getTime());
		String filename = "Board_" + timestamp + ".txt";
		save(board, Paths.get(filename));
	}
	
	@Override
	public void fireUpdateBoard(IReadOnlyBoard board, Player performer)
	{
		try
		{
			saveBoardByDateTime(board);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void fireSwitchPlayer(IReadOnlyBoard board, Player switchedIn)
	{
	}

	@Override
	public void fireExecuteAction(IAction action)
	{
	}
}