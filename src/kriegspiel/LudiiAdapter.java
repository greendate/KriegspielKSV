package kriegspiel;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.set.hash.TIntHashSet;
import other.context.Context;
import other.state.container.ContainerState;
import uk.co.kriegspiel.Piece;
import uk.co.kriegspiel.Result;

public class LudiiAdapter {
	
	protected int player = -1;
	
	protected int opponent = -1;
	
	private final int players = 2;
		
	private int opponentsKing = 0;
	
	private ArrayList <Integer> illegalMovesFrom = new ArrayList <Integer>();
	
	private ArrayList <Integer> illegalMovesTo = new ArrayList <Integer>();
	
	public LudiiAdapter(final int playerID) {
		this.player = playerID;
		this.opponent = this.players - playerID + 1;
	}
	
	private boolean aiPlayerWhite() {
		return (player == 1);
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
	
	private boolean notIllegalMove(int from, int to) {
		for(int i = 0; i < illegalMovesFrom.size(); i++) {
			if(illegalMovesFrom.get(i) == from && illegalMovesTo.get(i) == to) return false;
		}
		return true;
	}
	
	public ArrayList<Integer> findLegalMovesFrom(Context context, int from) {
		ArrayList<Integer> wrappedList = new ArrayList <Integer>();
		from--;
		
		ContainerState board = context.containerState(0);
		TIntHashSet pendingValues = context.state().pendingValues();
		
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
			if(notIllegalMove(from + 1, wrappedList.get(i) + 1)) legalMovesFrom.add(wrappedList.get(i) + 1);
		}
		return legalMovesFrom;
	}
	
	public Piece.Value getOccupierValue(Context context, int sq) {
		ContainerState board = context.containerState(0);
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
	
	public Piece.Color getOccupierColor(Context context, int sq) {
		ContainerState board = context.containerState(0);
		sq--;
		int occupier = board.whoCell(sq);
		
		if (occupier == 0) return Piece.Color.WASH;
		else if (occupier == 1) return Piece.Color.WHITE;
		else if (occupier == 2) return Piece.Color.BLACK;
		
		return Piece.Color.GHOST;
	}
	
	public boolean isInCheck(Context context) {
		List <String> refereeMessages = context.getNotes(opponent);
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
	
	public Result getResult(Context context) {
		Result result = new Result();
		result.Reset();
		List <String> refereeMessages = context.getNotes(opponent);
		ContainerState board = context.containerState(0);
		
		if(isInCheck(context)) {
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
	
	public ArrayList<Integer> getGreenList(Context context) {
		ArrayList<Integer> greenList = new ArrayList<Integer>();
		Result result = getResult(context);
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
	
	public boolean isSquareGreen(Context context, int sq) {
		ArrayList<Integer> greenList = getGreenList(context);
		for(int i = 0; i < greenList.size(); i++) {
			if(greenList.get(i) == sq) return true;
		}
		return false;
	}
	
	public boolean wasOpponentInCheck(Context context) {
		List <String> refereeMessages = context.getNotes(player);
		for(int i = 0; i < refereeMessages.size(); i++) {
			String message = refereeMessages.get(i);
			if(message.contains("check") || message.contains("Check")) return true;
		}
		return false;
	}
	
	public Result getMyLastResult(Context context) {
		Result result = new Result();
		result.Reset();
		List <String> refereeMessages = context.getNotes(player);
		
		if(wasOpponentInCheck(context)) {
			if(aiPlayerWhite()) result.bBlackInCheck = true;
			else result.bWhiteInCheck = true;
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
		
		return result;
	}
	
	public void updateOpponentsKingLocation(Context context) {
		ContainerState board = context.containerState(0);
		for(int i = 1; i < 65; i++) {
			if((aiPlayerWhite() && board.whatCell(i - 1) == 6) || (!aiPlayerWhite() && board.whatCell(i - 1) == 5)) {
				opponentsKing = i;
				break;
			}
		}
	}
	
	public ArrayList<Integer> getMyLastGreenList (Context context) {
		ArrayList<Integer> greenList = new ArrayList<Integer>();
		Result result = getMyLastResult(context);
		if (result.bCheckCol) { 
			for (int y = 0; y < 8; y++) greenList.add(getP(getX(opponentsKing - 1), y) + 1);
		}
		else if(result.bCheckRow) {
			for (int x = 0; x < 8; x++) greenList.add(getP(x, getY(opponentsKing - 1)) + 1);
		}
		else if (result.bCheckLong || result.bCheckShort) {
			ArrayList<Integer> diagonal1 = new ArrayList<Integer>();
			diagonal1.add(opponentsKing);
			int x = getX(opponentsKing - 1) + 1; 
			int y = getY(opponentsKing - 1) + 1;
            while (x < 8 && y < 8) {
            	diagonal1.add(getP(x, y) + 1);
            	x++;
            	y++; 
            }
            x = getX(opponentsKing - 1) - 1;
            y = getY(opponentsKing - 1) - 1;
            while (x >= 0 && y >= 0) {
            	diagonal1.add(getP(x, y) + 1);
            	x--;
            	y--; 
            }
            
            ArrayList<Integer> diagonal2 = new ArrayList<Integer>();
            diagonal2.add(opponentsKing);
            x = getX(opponentsKing - 1) + 1;
            y = getY(opponentsKing - 1) - 1;
            while (x < 8 && y >= 0) {
            	diagonal2.add(getP(x, y) + 1); 
            	x++;
            	y--; 
            }
            x = getX(opponentsKing - 1) - 1;
            y = getY(opponentsKing - 1) + 1;
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
	
	public void updateIllegalMoves(int from, int to) {
		// System.out.println("Illegal Move: " + from + "-" + to);
		illegalMovesFrom.add(from);
		illegalMovesTo.add(to);
	}
	
	public void resetIllegalMoves() {
		illegalMovesFrom.clear();
		illegalMovesTo.clear();
	}
	
}
