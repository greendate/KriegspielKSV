package kriegspiel;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import main.collections.FastArrayList;
import other.AI;
import other.context.Context;
import other.move.Move;
import uk.co.kriegspiel.Piece;
import uk.co.kriegspiel.RobotC;

public class Agent extends AI
{
	
	//-------------------------------------------------------------------------
	
	protected int player = -1;
	
	protected int opponent = -1;
	
	private final int players = 2;
	
	private RobotC robot;
	
	private int movesMade;
	
	//-------------------------------------------------------------------------
	
	public Agent()
	{
		this.friendlyName = "KriegspielKSV";
	}
	
	//-------------------------------------------------------------------------
	
	private boolean aiPlayerWhite() {
		return (player == 1);
	}
	
	private int getMovesMade() {
		if(aiPlayerWhite()) {
			return movesMade * 2;
		}
		else {
			return movesMade * 2 + 1;
		}
	}
	
	private Move performMove(ArrayList <Integer> move, FastArrayList<Move> pseudoLegalMoves) {
		for(Move pseudoMove: pseudoLegalMoves) {
			if(pseudoMove.from() == move.get(0) - 1 && pseudoMove.to() == move.get(1) - 1) 
				return pseudoMove;
		}
		return null;
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
		
		List <String> lastTryMessages = context.getNotes(player);
		
		if(lastTryMessages.size() != 0) {
			if(lastTryMessages.get(0).equals("Illegal move")) {
				robot.communicateIllegalMove();
			}
			else robot.communicateLegalMove();
		} 
		else robot.communicateLegalMove();
		
		if(pseudoLegalMoves.get(0).actionDescriptionStringShort().contentEquals("Promote")) {
			for(Move promotionMove:pseudoLegalMoves) {
				if(promotionMove.what() == 11 || promotionMove.what() == 12) return promotionMove;
			}
		}
		
		robot.receiveContext(context);
		robot.SendMove(0, 0, getMovesMade());
		ArrayList <Integer> move = robot.getLastMove();
		
		movesMade++;
		return performMove(move, pseudoLegalMoves);
	}
	
	@Override
	public void initAI(final Game game, final int playerID)
	{
		this.player = playerID;
		this.opponent = this.players - playerID + 1;
		if(playerID == 1) 
			this.robot = new RobotC(null, null, Piece.Color.WHITE);
		else 
			this.robot = new RobotC(null, null, Piece.Color.BLACK);
	}
	
	//-------------------------------------------------------------------------

}