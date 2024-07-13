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
	
	private Context contextCopy;
	
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
	
	private int getP(int x, int y) {
		return y * 8 + x;
	}
	
	private ArrayList <Integer> legalMovesInDirection (ContainerState board, int sq, int incrX, int incrY) {
		ArrayList <Integer> wrappedList = new ArrayList <Integer>();
		int x = getX(sq);
		int y = getY(sq);
		x += incrX;
		y += incrY;
		while(x >= 0 && y >= 0 && x < 8 && y < 8) {
			int tempSq = getP(x, y);
			if(board.whatCell(tempSq) == 0) wrappedList.add(tempSq);
			else if (board.whoCell(tempSq) != board.whoCell(sq)) {
				wrappedList.add(tempSq);
				break;
			}
			else {
				break;
			}
			x += incrX;
			y += incrY;
		}
		return wrappedList;
	}
	
	private int validToSquare(ContainerState board, int sq, int incrX, int incrY) {
		int x = getX(sq) + incrX;
		int y = getY(sq) + incrY;
		if(x >= 0 && y >= 0 && x < 8 && y < 8) {
			int to = getP(x, y);
			if((board.whoCell(to) != board.whoCell(sq)) || board.whatCell(to) == 0) {
				return to;
			}
		}
		return -1;
	}
	
	private boolean emptySquares(ContainerState board, int from, int to) {
		for(int i = from; i <= to; i++) {
			if(board.whatCell(i) != 0) return false;
		}
		return true;
	}
	
	private String getCapturePosition(String message) {
		String square = ""; 
		
		for(int i = 1; i < message.length(); i++) {
			if(Character.isUpperCase(message.charAt(i)) || Character.isDigit(message.charAt(i))) 
				square += message.charAt(i);

		}
		return square;
	}
	
	//-------------------------------------------------------------------------
	
	public ArrayList<Integer> findLegalMovesFrom(int from) {
		ArrayList<Integer> wrappedList = new ArrayList <Integer>();
		from--;
		
		ContainerState board = contextCopy.containerState(0);
		TIntHashSet pendingValues = contextCopy.state().pendingValues();
		
		int piece = board.whatCell(from);
		
		if(piece == 1) { // white pawn
			if (from >= 8 && from <= 15) {
				int to = from + 16;
				if(board.whatCell(to) == 0 && board.whatCell(from + 8) == 0) wrappedList.add(to);
			}
			if(from < 56) {
				if(getX(from) > 0) {
					int to = from + 7;
					if(board.whoCell(to) == 2 || pendingValues.contains(to)) {
						wrappedList.add(to);
					}
				}
				if(getX(from) < 7) {
					int to = from + 9;
					if(board.whoCell(to) == 2 || pendingValues.contains(to)) {
						wrappedList.add(to);
					}
				}
				int to = from + 8;
				if(board.whatCell(to) == 0) wrappedList.add(to);
			}
		}
		
		else if(piece == 2) { // black pawn
			if (from >= 48 && from <= 55) {
				int to = from - 16;
				if(board.whatCell(to) == 0 && board.whatCell(from - 8) == 0) wrappedList.add(to);
			}
			if(from > 7) {
				if(getX(from) > 0) {
					int to = from - 9;
					if(board.whoCell(to) == 1 || pendingValues.contains(to)) {
						wrappedList.add(to);
					}
				}
				if(getX(from) < 7) {
					int to = from - 7;
					if(board.whoCell(to) == 1 || pendingValues.contains(to)) {
						wrappedList.add(to);
					}
				}
				int to = from - 8;
				if(board.whatCell(to) == 0) wrappedList.add(to);
			}
		}
		
		else if(piece == 3 || piece == 4) { // rook
			wrappedList.addAll(legalMovesInDirection(board, from, 1, 0));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, 0));
			wrappedList.addAll(legalMovesInDirection(board, from, 0, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, 0, -1));
		}
		
		else if (piece == 7 || piece == 8) { // bishop
			wrappedList.addAll(legalMovesInDirection(board, from, 1, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, 1, -1));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, -1));
		}
		
		else if (piece == 5 || piece == 6) { // king
			int to;
			
			to = validToSquare(board, from, 1, 1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 1, -1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 1, 0);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -1, 1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -1, -1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -1, 0);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 0, 1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 0, -1);
			if (to != -1) wrappedList.add(to);
			
			// castling
			
			if(from == 4 && piece == 5) {
				if(board.whatCell(7) == 3 && emptySquares(board, 5, 6)) wrappedList.add(6);
				if(board.whatCell(0) == 3 && emptySquares(board, 1, 3)) wrappedList.add(2);
			}
			if(from == 60 && piece == 6) {
				if(board.whatCell(63) == 4 && emptySquares(board, 61, 62)) wrappedList.add(62);
				if(board.whatCell(56) == 4 && emptySquares(board, 57, 59)) wrappedList.add(58);
			}
		}
		
		else if (piece == 9 || piece == 10) { // knight
			int to;
			
			to = validToSquare(board, from, 2, 1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 2, -1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 1, -2);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -1, -2);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -2, -1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -2, 1);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, -1, 2);
			if (to != -1) wrappedList.add(to);
			
			to = validToSquare(board, from, 1, 2);
			if (to != -1) wrappedList.add(to);
		}
		
		else if (piece == 11 || piece == 12) { // queen
			wrappedList.addAll(legalMovesInDirection(board, from, 1, 0));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, 0));
			wrappedList.addAll(legalMovesInDirection(board, from, 0, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, 0, -1));
			wrappedList.addAll(legalMovesInDirection(board, from, 1, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, 1, -1));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, 1));
			wrappedList.addAll(legalMovesInDirection(board, from, -1, -1));
		}
		
		ArrayList<Integer> legalMovesFrom = new ArrayList <Integer>();
		for(int i = 0; i < wrappedList.size(); i++) {
			legalMovesFrom.add(wrappedList.get(i) + 1);
		}
		return legalMovesFrom;
	}
	
	public Piece.Value getOccupierValue(int sq) {
		ContainerState board = contextCopy.containerState(0);
		sq--;
		int piece = board.whatCell(sq);
		
		if (piece == 0) return Piece.Value.EMPTY;
		else if (piece == 1 || piece == 2) return Piece.Value.PAWN;
		else if (piece == 3 || piece == 4) return Piece.Value.ROOK;
		else if (piece == 5 || piece == 6) return Piece.Value.KING;
		else if (piece == 7 || piece == 8) return Piece.Value.BISHOP;
		else if (piece == 9 || piece == 10) return Piece.Value.KNIGHT;
		else if (piece == 11 || piece == 12) return Piece.Value.QUEEN;
		
		return Piece.Value.GHOST;
	}
	
	public Piece.Color getOccupierColor(int sq) {
		ContainerState board = contextCopy.containerState(0);
		sq--;
		int occupier = board.whoCell(sq);
		
		if (occupier == 0) return Piece.Color.WASH;
		else if (occupier == 1) return Piece.Color.WHITE;
		else if (occupier == 2) return Piece.Color.BLACK;
		
		return Piece.Color.GHOST;
	}
	
	public int getMovesMade() {
		if(aiPlayerWhite()) {
			return movesMade * 2;
		}
		else {
			return movesMade * 2 + 1;
		}
	}
	
	public boolean isInCheck() {
		List <String> refereeMessages = contextCopy.getNotes(opponent);
		for(int i = 0; i < refereeMessages.size(); i++) {
			String message = refereeMessages.get(i);
			if(message.contains("check") || message.contains("Check")) return true;
		}
		return false;
	}
	
	// this information is not available under Ludii Kriegspiel rules
	public ArrayList<Integer> getOldTries() {
		return new ArrayList<Integer>();
	}
	
	public Result getResult() {
		Result result = new Result();
		result.Reset();
		List <String> refereeMessages = contextCopy.getNotes(opponent);
		ContainerState board = contextCopy.containerState(0);
		
		if(isInCheck()) {
			if(aiPlayerWhite()) result.bWhiteInCheck = true;
			else result.bBlackInCheck = true;
		}
		
		for(int i = 0; i < refereeMessages.size(); i++) {
			String message = refereeMessages.get(i);
			if(message.matches("(.*) captured")) {
				String redSquare = getCapturePosition(message);
				int captureX = redSquare.charAt(0) - 'A';
				int captureY = redSquare.charAt(1) - '1';
				result.iRedSquare = getP(captureX, captureY) + 1;
			}
			else if(message.matches("Short diagonal check")) result.bCheckShort = true;		
			else if(message.matches("Long diagonal check")) result.bCheckLong = true;
			else if(message.matches("File check")) result.bCheckCol = true;
			else if(message.matches("Rank check")) result.bCheckRow = true;
		}
		
		result.iBlackRemain = 0;
		result.iWhiteRemain = 0;
		for(int i = 0; i < 64; i++) {
			if(board.whoCell(i) == 1) result.iWhiteRemain++;
			else if(board.whoCell(i) == 2) result.iBlackRemain++; 
			
			if(board.whatCell(i) == 5 && result.bWhiteInCheck) result.iCheckedKing = i + 1;
			else if(board.whatCell(i) == 6 && result.bBlackInCheck) result.iCheckedKing = i + 1;
		}
		
		return result;
	}
	
	public ArrayList<Integer> getGreenList() {
		ArrayList<Integer> greenList = new ArrayList<Integer>();
		Result result = getResult();
		if(result.iCheckedKing == 0) return greenList;
		
		if (result.bCheckCol) { 
			for (int y = 0; y < 8; y++) greenList.add(getP(getX(result.iCheckedKing - 1), y) + 1);
		}
		else if(result.bCheckRow) {
			for (int x = 0; x < 8; x++) greenList.add(getP(x, getY(result.iCheckedKing - 1)) + 1);
		}
		else if (result.bCheckLong || result.bCheckShort) {
			ArrayList<Integer> diagonal1 = new ArrayList<Integer>();
			diagonal1.add(result.iCheckedKing);
			int x = getX(result.iCheckedKing - 1) + 1; 
			int y = getY(result.iCheckedKing - 1) + 1;
            while (x < 8 && y < 8) {
            	diagonal1.add(getP(x, y) + 1);
            	x++;
            	y++; 
            }
            x = getX(result.iCheckedKing - 1) - 1;
            y = getY(result.iCheckedKing - 1) - 1;
            while (x >= 0 && y >= 0) {
            	diagonal1.add(getP(x, y) + 1);
            	x--;
            	y--; 
            }
            
            ArrayList<Integer> diagonal2 = new ArrayList<Integer>();
            diagonal2.add(result.iCheckedKing);
            x = getX(result.iCheckedKing - 1) + 1;
            y = getY(result.iCheckedKing - 1) - 1;
            while (x < 8 && y >= 0) {
            	diagonal2.add(getP(x, y) + 1); 
            	x++;
            	y--; 
            }
            x = getX(result.iCheckedKing - 1) - 1;
            y = getY(result.iCheckedKing - 1) + 1;
            while (x >= 0 && y < 8) {
            	diagonal2.add(getP(x, y) + 1);
            	x--;
            	y++;
            }
            
            if(result.bCheckLong) {
            	if(diagonal1.size() > diagonal2.size()) greenList.addAll(diagonal1);
            	else greenList.addAll(diagonal2);
            }
            else if (result.bCheckShort){
            	if(diagonal1.size() < diagonal2.size()) greenList.addAll(diagonal1);
            	else greenList.addAll(diagonal2);
            }
		}
		return greenList;
	}
	
	public boolean isSquareGreen(int sq) {
		ArrayList<Integer> greenList = getGreenList();
		for(int i = 0; i < greenList.size(); i++) {
			if(greenList.get(i) == sq) return true;
		}
		return false;
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
	
	private ArrayList<String> getLegalMoves() {
		ArrayList<String> legalMoves = new ArrayList<String>();
		for(int i = 1; i < 65; i++) {
			if((getOccupierColor(i) == Piece.Color.WHITE && aiPlayerWhite()) || (getOccupierColor(i) == Piece.Color.BLACK && !aiPlayerWhite())) {
				 ArrayList<Integer> toLocations = findLegalMovesFrom(i);
				 for(int j = 0; j < toLocations.size(); j++) {
					 int from = i;
					 int to = toLocations.get(j);
					 // System.out.println(getOccupierColor(i) + "-" + getOccupierValue(i) + " " + coordID(from) + "-" + coordID(to));
					 legalMoves.add(coordID(from) + "-" + coordID(to));
				 }
			}
		}
    	return legalMoves;
	}
	
	private ArrayList<String> getOpponentLegalMoves() {
		ArrayList<String> legalMoves = new ArrayList<String>();
		for(int i = 1; i < 65; i++) {
			if((getOccupierColor(i) == Piece.Color.WHITE && !aiPlayerWhite()) || (getOccupierColor(i) == Piece.Color.BLACK && aiPlayerWhite())) {
				 ArrayList<Integer> toLocations = findLegalMovesFrom(i);
				 for(int j = 0; j < toLocations.size(); j++) {
					 int from = i;
					 int to = toLocations.get(j);
					 System.out.println(getOccupierColor(i) + "-" + getOccupierValue(i) + " " + coordID(from) + "-" + coordID(to));
					 legalMoves.add(coordID(from) + "-" + coordID(to));
				 }
			}
		}
    	return legalMoves;
	}
	
	private Move performMove(String move, FastArrayList<Move> pseudoLegalMoves) {
		int from = (move.charAt(1) - '1') * 8 + move.charAt(0) - 'A';
		int to = (move.charAt(4) - '1') * 8 + move.charAt(3) - 'A';
		for(Move pseudoMove: pseudoLegalMoves) {
			if(pseudoMove.from() == from && pseudoMove.to() == to) 
				return pseudoMove;
		}
		return null;
	}
	
	private void testGetOccupier() {
		for(int i = 1; i < 65; i++) {
			System.out.println(coordID(i) + ":" + getOccupierColor(i) + "-" + getOccupierValue(i));
		}
	}
	
	private void testGreenList() {
		System.out.println("King in Check");
		ArrayList<Integer> greenList = getGreenList();
		System.out.println("Green List: ");
		for(int i = 0; i < greenList.size(); i++) {
			System.out.print(coordID(greenList.get(i)) + " ");
		}
		System.out.println(" ");
		for(int i = 1; i < 65; i++) {
			System.out.println(coordID(i) + " " + isSquareGreen(i));
		}
	}
	
	private void displayResult() {
		Result result = getResult();
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
		contextCopy = context;
		/*
		System.out.println("Total moves by now: " + getMovesMade());
		System.out.println("Old tries: " + getOldTries());
		*/
		
		/*
		List <String> lastTryMessages = context.getNotes(player);
		
		// TODO: Remove or mark illegal moves here
		if(lastTryMessages.size() != 0) {
			if(lastTryMessages.get(0).equals("Illegal move")) {
				return chooseRandomMove(pseudoLegalMoves);
			}
		}
		*/
		
		if(pseudoLegalMoves.get(0).actionDescriptionStringShort().contentEquals("Promote")) {
			// TODO: chose queen here
			final int r = ThreadLocalRandom.current().nextInt(pseudoLegalMoves.size());
			return pseudoLegalMoves.get(r); 
		}
		
		/*
		int oppTries = context.score(opponent);
		
		List <String> refereeMessages = context.getNotes(opponent);
		
		int pawnTries = context.score(player);
		*/ 
		/*
		if(isInCheck()) {
			testGreenList();
		}
		testGetOccupier();
		displayResult();
		*/
		// System.out.println("Legal moves list:");
		ArrayList <String> legalMoves = getLegalMoves();
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
	}
	
	//-------------------------------------------------------------------------

}