package kriegspiel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import game.Game;
import gnu.trove.set.hash.TIntHashSet;
import main.collections.FastArrayList;
import other.AI;
import other.context.Context;
import other.move.Move;
import other.state.container.ContainerState;
import uk.co.kriegspiel.Piece;
import uk.co.kriegspiel.Result;

public class Agent extends AI
{
	
	//-------------------------------------------------------------------------
	
	protected int player = -1;
	
	protected int opponent = -1;
	
	private final int players = 2;
	
	private int lastFrom = 0;
	
	private int lastTo = 0;
	
	// private Context contextCopy;
	
	LudiiAdapter adapter;
	
	private int movesMade;
	
	//-------------------------------------------------------------------------
	
	public Agent()
	{
		this.friendlyName = "KriegspielKSV";
	}
	
	//-------------------------------------------------------------------------
	
	// private functions
	
	private boolean aiPlayerWhite() {
		return (player == 1);
	}
	
	private String chooseRandomMove(ArrayList <String> legalMoves) {
		final int r = ThreadLocalRandom.current().nextInt(legalMoves.size());
		return legalMoves.get(r); 
	}
	
	private int getX(int sq) {
		return sq % 8;
	}
	
	private int getY(int sq) {
		return sq / 8;
	}
	
	private int getMovesMade() {
		if(aiPlayerWhite()) {
			return movesMade * 2;
		}
		else {
			return movesMade * 2 + 1;
		}
	}
	
	//-------------------------------------------------------------------------
	// following functions will be used only for testing purposes
	
	private String coordID(int sq) {
		int x = getX(sq - 1);
		int y = getY(sq - 1) + 1;
		StringBuilder coordID = new StringBuilder();
		coordID.append((char) ('A' + x));
		coordID.append((char) ('0' + y));
		return coordID.toString();
	}
	
	private ArrayList<String> getLegalMoves(Context context) {
		ArrayList<String> legalMoves = new ArrayList<String>();
		for(int i = 1; i < 65; i++) {
			if((adapter.getOccupierColor(context, i) == Piece.Color.WHITE && aiPlayerWhite()) || (adapter.getOccupierColor(context, i) == Piece.Color.BLACK && !aiPlayerWhite())) {
				 ArrayList<Integer> toLocations = adapter.findLegalMovesFrom(context, i);
				 for(int j = 0; j < toLocations.size(); j++) {
					 int from = i;
					 int to = toLocations.get(j);
					 // System.out.println(adapter.getOccupierColor(context, i) + "-" + adapter.getOccupierValue(context, i) + " " + coordID(from) + "-" + coordID(to));
					 legalMoves.add(coordID(from) + "-" + coordID(to));
				 }
			}
		}
    	return legalMoves;
	}
	
	private ArrayList<String> getOpponentLegalMoves(Context context) {
		ArrayList<String> legalMoves = new ArrayList<String>();
		for(int i = 1; i < 65; i++) {
			if((adapter.getOccupierColor(context, i) == Piece.Color.WHITE && !aiPlayerWhite()) || (adapter.getOccupierColor(context, i) == Piece.Color.BLACK && aiPlayerWhite())) {
				 ArrayList<Integer> toLocations = adapter.findLegalMovesFrom(context, i);
				 for(int j = 0; j < toLocations.size(); j++) {
					 int from = i;
					 int to = toLocations.get(j);
					 System.out.println(adapter.getOccupierColor(context, i) + "-" + adapter.getOccupierValue(context, i) + " " + coordID(from) + "-" + coordID(to));
					 legalMoves.add(coordID(from) + "-" + coordID(to));
				 }
			}
		}
    	return legalMoves;
	}
	
	private Move performMove(String move, FastArrayList<Move> pseudoLegalMoves) {
		int from = (move.charAt(1) - '1') * 8 + move.charAt(0) - 'A';
		int to = (move.charAt(4) - '1') * 8 + move.charAt(3) - 'A';
		lastFrom = from + 1;
		lastTo = to + 1;
		for(Move pseudoMove: pseudoLegalMoves) {
			if(pseudoMove.from() == from && pseudoMove.to() == to) 
				return pseudoMove;
		}
		return null;
	}
	
	private void testGetOccupier(Context context) {
		for(int i = 1; i < 65; i++) {
			System.out.println(coordID(i) + ":" + adapter.getOccupierColor(context, i) + "-" + adapter.getOccupierValue(context, i));
		}
	}
	
	private void testGreenList(Context context) {
		System.out.println("King in Check");
		ArrayList<Integer> greenList = adapter.getGreenList(context);
		System.out.println("Green List: ");
		for(int i = 0; i < greenList.size(); i++) {
			System.out.print(coordID(greenList.get(i)) + " ");
		}
		System.out.println(" ");
		for(int i = 1; i < 65; i++) {
			System.out.println(coordID(i) + " " + adapter.isSquareGreen(context, i));
		}
	}
	
	private void testMyGreenList(Context context) {
		System.out.println("Opponent's King in Check");
		ArrayList<Integer> greenList = adapter.getMyLastGreenList(context);
		System.out.println("My Green List: ");
		for(int i = 0; i < greenList.size(); i++) {
			System.out.print(coordID(greenList.get(i)) + " ");
		}
	}
	
	private void displayResult(Context context) {
		Result result = adapter.getResult(context);
		System.out.println("bCheckmate: " + result.bCheckmate);
		System.out.println("bStalemate: " + result.bStalemate);
		System.out.println("bWhiteInCheck: " + result.bWhiteInCheck);
		System.out.println("bBlackInCheck: " + result.bBlackInCheck);
		System.out.println("bWhiteResigns: " + result.bWhiteResigns);
		System.out.println("bBlackResigns: " + result.bBlackResigns);
		System.out.println("bAgreedDraw: " + result.bAgreedDraw);
		System.out.println("bQueenedPawn: " + result.bQueenedPawn);
		System.out.println("iRedSquare: " + result.iRedSquare);
		System.out.println("iCheckedKing: " + result.iCheckedKing);
		System.out.println("iCheckByKnight: " + result.iCheckByKnight);
		System.out.println("bCheckLong: " + result.bCheckLong);
		System.out.println("bCheckShort: " + result.bCheckShort);
		System.out.println("bCheckCol: " + result.bCheckCol);
		System.out.println("bCheckRow: " + result.bCheckRow);
		System.out.println("iRedisplayButton: " + result.iRedisplayButton);
		System.out.println("iWhiteRemain: " + result.iWhiteRemain);
		System.out.println("iBlackRemain: " + result.iBlackRemain);
		System.out.println("error: " + result.error);
		System.out.println("");
	}
	
	private void displayMyLastResult(Context context) {
		Result result = adapter.getMyLastResult(context);
		System.out.println("bWhiteInCheck: " + result.bWhiteInCheck);
		System.out.println("bBlackInCheck: " + result.bBlackInCheck);
		System.out.println("iRedSquare: " + result.iRedSquare);
		System.out.println("bCheckLong: " + result.bCheckLong);
		System.out.println("bCheckShort: " + result.bCheckShort);
		System.out.println("bCheckCol: " + result.bCheckCol);
		System.out.println("bCheckRow: " + result.bCheckRow);
		System.out.println("");
	}
	
	//-------------------------------------------------------------------------
	
	
	@Override
	public Move selectAction
	(
		final Game game, 
		final Context context, 
		final double maxSeconds,
		final int maxIterations,
		final int maxDepth
	)
	{
		FastArrayList<Move> pseudoLegalMoves = game.moves(context).moves();
		// contextCopy = context;
		// System.out.println("Total moves by now: " + getMovesMade());
		// System.out.println("Old tries: " + adapter.getOldTries());
		
		List <String> lastTryMessages = context.getNotes(player);
		
		// TODO: Remove or mark illegal moves here
		if(lastTryMessages.size() != 0) {
			if(lastTryMessages.get(0).equals("Illegal move")) {
				adapter.updateIllegalMoves(lastFrom, lastTo);
				// final int r = ThreadLocalRandom.current().nextInt(pseudoLegalMoves.size());
				// return pseudoLegalMoves.get(r); 
			}
			else adapter.resetIllegalMoves();
		} 
		else adapter.resetIllegalMoves();
		
		if(pseudoLegalMoves.get(0).actionDescriptionStringShort().contentEquals("Promote")) {
			// TODO: choose queen here
			final int r = ThreadLocalRandom.current().nextInt(pseudoLegalMoves.size());
			return pseudoLegalMoves.get(r); 
		}
		
		/*
		int oppTries = context.score(opponent);
		
		List <String> refereeMessages = context.getNotes(opponent);
		
		int pawnTries = context.score(player);
		*/ 
		/*
		if(adapter.isInCheck(context)) {
			testGreenList(context);
		}
		System.out.println("");
		*/
		if(adapter.wasOpponentInCheck(context)) {
			testMyGreenList(context);
			System.out.println("");
		}
		// testGetOccupier(context);
		// System.out.println("");
		// displayResult(context);
		// System.out.println("");
		displayMyLastResult(context);
		System.out.println("");
		adapter.updateOpponentsKingLocation(context);
		
		// System.out.println("Legal moves list:");
		ArrayList <String> legalMoves = getLegalMoves(context);
		// System.out.println("");
		String chosenMove = chooseRandomMove(legalMoves);
		// System.out.println("Legal human moves list:");
		// getOpponentLegalMoves();
		
		movesMade++;
		return performMove(chosenMove, pseudoLegalMoves);
	}
	
	@Override
	public void initAI(final Game game, final int playerID)
	{
		this.player = playerID;
		this.opponent = this.players - playerID + 1;
		this.adapter = new LudiiAdapter(playerID);
	}
	
	//-------------------------------------------------------------------------

}