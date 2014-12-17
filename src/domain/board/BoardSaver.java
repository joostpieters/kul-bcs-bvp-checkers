package domain.board;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import common.Player;
import domain.board.contracts.IReadOnlyBoard;
import domain.location.Location;
import domain.updates.contracts.IGameFollower;

public class BoardSaver implements IGameFollower {
	private final Path outputDirectory;
	
	private Path getOutputDirectory() {
		return outputDirectory;
	}
	
	public BoardSaver(Path outputDirectory) {
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
	public void update(IReadOnlyBoard board, Player performer) {
		try {
			saveBoardByDateTime(board);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameOver(Player winner) { }

	@Override
	public void promotion(Location location) { }

	@Override
	public void outOfMoves(Player player) { }
}
