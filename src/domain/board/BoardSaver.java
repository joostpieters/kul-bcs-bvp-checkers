package domain.board;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import domain.IGameFollower;

public class BoardSaver implements IGameFollower {
	private final Path outputDirectory;
	
	private Path getOutputDirectory() {
		return outputDirectory;
	}
	
	public BoardSaver(Path outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void save(Board board, Path filename) throws IOException
	{
		Path output = getOutputDirectory().resolve(filename);
		BufferedWriter writer = Files.newBufferedWriter(output);
		writer.write(board.toString());
		writer.close();
	}
	
	public void saveBoardByDateTime(Board board) throws IOException
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = format.format(Calendar.getInstance().getTime());
		String filename = "Board_" + timestamp + ".txt";
		save(board, Paths.get(filename));
	}
	
	@Override
	public void update(Board board) {
		try {
			saveBoardByDateTime(board);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
