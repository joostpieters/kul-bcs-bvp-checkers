package common;

import java.awt.Color;

import domain.board.BoardSize;

public class Configs {
	public final static BoardSize Size = new BoardSize(10,10);
	public final static boolean MandatoryMaximalCatching = true;
	public final static boolean BackwardCatchingAllowed = true;
	public final static Player FirstPlayer = Player.White;
	public final static boolean FlyingDame = true;
	
	//GUI
	public final static Color DarkColor = Color.GRAY;
	public final static Color LightColor = Color.WHITE;
	public final static int SquareSizePx = 50;
	public final static String CijfersPath = "data\\cijfers32.fig";
	public final static String SchijvenPath = "data\\schijven.fig";
	
	//Inputs
	public final static String ResignInput = "resign";
	public final static String RemiseInput = "remise";
	
	//Locale
	public final static String Language = "nl";
	public final static String Country = "BE";
}
