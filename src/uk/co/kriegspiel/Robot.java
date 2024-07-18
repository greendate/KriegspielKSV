/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import javax.swing.*;
import other.context.Context;
import kriegspiel.Agent;
import kriegspiel.LudiiAdapter;

/**  The Robot class simulates a remote opponent
 *
 * The basic robot makes a list of all possible moves.
 * It then choses in the following order of priority:
 * (a) hit a red square.
 * (b) accelerate pawn in last 4 rows or a pawn take, chosen at random
 * (c) move to a square adjacent to a check line, chosen at random
 *     failing any of the above it makes a random move.
 *
 *
 * @author denysbennett
 */


public class Robot extends Remote{
    
 // private int iFromSent;
 // private int iToSent;
  protected Timer timer;
  protected Result result;  // result of his move
  protected Result myLastResult; // result of my last move
  protected Context context;
  protected LudiiAdapter adapter;
  protected int movesMade;
  ArrayList<Integer> myLastGreenList; // from my last move
  
  // Declare lists
  ArrayList<Integer> listMyPieceLocations; // = new ArrayList<Integer>();
          
          // hold the possible moves as a matching set of arrays
  ArrayList<Integer> listMyMovesFrom; // = new ArrayList<Integer>();
  ArrayList<Integer> listMyMovesTo; // = new ArrayList<Integer>();
  ArrayList<Integer> listMyMovesValue; // = new ArrayList<Integer>(); // a list for  valuing the moves
          
  ArrayList<ArrayList<Integer>> wrappedList; // container for receiving lists of possible legal & illuminated moves

  
    // constructors
  
  public Robot(){
      
  }
  	
    public Robot (KSpielApp theApp, KSpielUI theUI, Piece.Color color){
        ksApp = theApp;
        ksUI = theUI;
        myColor = color;
        if(myColor == Piece.Color.WHITE) {
        	adapter = new LudiiAdapter(1);
        }
        else {
        	adapter = new LudiiAdapter(2);
        }
        myLastGreenList = new ArrayList<Integer>();
        

    }

    @Override
    public void SendOfferDraw(){
        if (timer != null){ //V3.02
            timer.stop();
            ReceiveAcceptDraw(true);
        }
    }
    // Send Methods - the local UI is sending to the remote/robot
    @Override
    public void SendResign(){
        // opponent has resigned, turn off the timer to stop making moves
        if (timer != null){ //V3.02
            timer.stop();
        }
    }

    @Override
    public void SendError(){
        // program has detected an error
        String Error ="";
        switch (result.error){

            case KING_NOT_FOUND:
                Error = "King not Found!";
                break;
            case OUT_OF_RANGE:
                Error = "Out of Range!";
                break;
            case KING_TAKEN:
                Error = "King Taken!";
                break;
            default:
                Error = "AAArghhhh!";
        }
        ksUI.errorMsg(Error);
        ksUI.sysout(Error);
    }
    
    public void receiveContext(Context context) {
    	this.context = context;
    }
    
  @Override
    public void SendMove(int iFrom, int iTo, int iMovesMade){
      
      // receives notification that the opponent's move has been made, then replies
      // via ReceiveMove() with a move of its own
      
      // get result of opponent's move
	   this.movesMade = iMovesMade;
       result = adapter.getResult(context);
       myLastResult = adapter.getMyLastResult(context);
       myLastGreenList = adapter.getMyLastGreenList(context);
       adapter.updateOpponentsKingLocation(context);
       
       if (result.error != Result.ErrorCode.NO_ERROR ){
           // an error has occurred in the game logic
           SendError();
           
       } else {
       
       if (result.bCheckmate || result.bStalemate || result.bBlackResigns || result.bWhiteResigns ){
           // game over
           timer.stop();
       } else {
           
            // Continue playing    

            // set the delay to simulate thinking time (use Recorder slider as control)
           // int delay = ksApp.ksUI.recorder.getDelay();
    	   int delay = 3000;
    	   
           if (result.bBlackInCheck || result.bWhiteInCheck)  // v3.02
              delay = delay * 2;   //V3.02

           /*
           ActionListener alClick = new ActionListener(){
        
           public void actionPerformed(ActionEvent ae){
           */
          
          // In response to the clock-timer, generate a new move to despatch to the board vi ReceiveMove()
          
          
          // Initialise lists
          listMyPieceLocations = new ArrayList<Integer>();
          
          // hold the possible moves as a matching set of arrays
          listMyMovesFrom = new ArrayList<Integer>();
          listMyMovesTo = new ArrayList<Integer>();
          listMyMovesValue = new ArrayList<Integer>(); // a list for  valuing the moves
          
          //ArrayList<ArrayList<Integer>> wrappedList; // container for receiving lists of possible legal & illuminated moves
          
          // make a list of the locations of all my pieces
          for (int i = 1; i<65; i++){
            if (adapter.getOccupierColor(context, i) == myColor){
              listMyPieceLocations.add(i);
            }     
          }
                    
          // List all available legal moves as matching Integer pairs in listMovesFrom, listMovesTo
          
          listMyMovesFrom.clear();
          listMyMovesTo.clear();
          // interrogate Pieces one by one:
          for (int i = 0; i<listMyPieceLocations.size(); i++){
              // Piece by piece
              // ksApp.mainBoard.clearLegalMoves();
              // get all illuminated and legal moves for the piece located at get(i)
        	  ArrayList<Integer> tempWrappedList = adapter.findLegalMovesFrom(context, listMyPieceLocations.get(i));
              
              // just using the legal moves list referenced at (1) ...
              for (int j =0; j<tempWrappedList.size(); j++){
                // move by move
                 listMyMovesFrom.add(listMyPieceLocations.get(i)); // this piece
                 listMyMovesTo.add(tempWrappedList.get(j)); // this piece's legal move
                 listMyMovesValue.add(0); // put an initial value on the move
              }
          }
          // The game should still be in play, but test for a legal move
          if (listMyMovesTo.size()== 0){ // Checkmated or Stalemated...
              
          } else { // still in play
              
              ArrayList<Integer> move = decideMove(); // this method overridden by mutable robot types

              // Relay the move to the UI via the Remote.ReceiveMove() call
              ReceiveMove(move.get(0), move.get(1));
              // record the result
              
              // myLastResult = ksApp.mainBoard.getResult().copy();      
              // myLastGreenList = ksApp.copyArray(ksApp.mainBoard.getGreenList());

            }
      /*
        }
      };
      */
      /*
      timer = new Timer(delay, alClick);
      timer.setRepeats(false);
      timer.start();
      */
       }
       
  }
    }
  protected ArrayList<Integer> decideMove(){
      
      ArrayList<Integer> move = new ArrayList<Integer>();
      int iFrom=0;
      int iTo=0;
      // Did the opponent's move result in a Red square? If so, list counter-moves.
              //  [a remote player gets this from the UI: the robot just asks the board]

              ArrayList<Integer> listCounterMoves = new ArrayList<Integer>();
              if (result.iRedSquare !=0){
                  for (int j=0; j<listMyMovesTo.size(); j++){
                      if (listMyMovesTo.get(j)== result.iRedSquare){
                          listCounterMoves.add(j);
                      }
                  }
              }

              // Can I promote a pawn? Accelerate if 4 or less rows from end
              ArrayList<Integer> listPawnMoves = new ArrayList<Integer>();
              for (int j=0; j<listMyMovesFrom.size(); j++){
                  if (adapter.getOccupierValue(context, listMyMovesFrom.get(j)) == Piece.Value.PAWN) {
                      switch (myColor) {
                          case WHITE:
                              if (listMyMovesFrom.get(j)>32)
                                  listPawnMoves.add(j);
                              break;
                          case BLACK:
                              if (listMyMovesFrom.get(j)<33)
                                  listPawnMoves.add(j);
                              break;
                      }
                      // test for a pawn take
                      if (ksApp.getX(listMyMovesFrom.get(j)) != ksApp.getX(listMyMovesTo.get(j))){
                          listPawnMoves.add(j);
                      }
                  }
              }

              // Follow up check?
              ArrayList<Integer> listCheckMoves = new ArrayList<Integer>();
              if (myLastGreenList.size()>0) {

                  // have I got any moves which attack a square adjacent to a green I saw after my last move?
                  for (int j=0; j<listMyMovesTo.size(); j++){
                      for (int k=0; k<myLastGreenList.size(); k++)
                        if (ksApp.isAdjacent(listMyMovesTo.get(j), myLastGreenList.get(k) )){
                          listCheckMoves.add(j);
                      }
                  }
              }

              // Now select a move from the above lists
              Random generator = new Random();
              int randomIndex;
              if (listCounterMoves.size()>0){
                  // there is at least one countermove
                  randomIndex = generator.nextInt(listCounterMoves.size());
                  iFrom = listMyMovesFrom.get(listCounterMoves.get(randomIndex));
                  iTo = listMyMovesTo.get(listCounterMoves.get(randomIndex));
              } else {

                  if (listPawnMoves.size()>0){
                      // there is at least one pawn promotion opportunity
                      randomIndex = generator.nextInt(listPawnMoves.size());
                      iFrom = listMyMovesFrom.get(listPawnMoves.get(randomIndex));
                      iTo = listMyMovesTo.get(listPawnMoves.get(randomIndex));

                  } else {

                      if (listCheckMoves.size()>0){
                          // there's at least one check follow-up move
                          randomIndex = generator.nextInt(listCheckMoves.size());
                          iFrom = listMyMovesFrom.get(listCheckMoves.get(randomIndex));
                          iTo = listMyMovesTo.get(listCheckMoves.get(randomIndex));

                      } else {


                          randomIndex= generator.nextInt(listMyMovesFrom.size());
                          iFrom = listMyMovesFrom.get(randomIndex);
                          iTo = listMyMovesTo.get(randomIndex);
                      }
                  }

              }
              
              move.add(iFrom);
              move.add(iTo);

          return move;
  }
  public int PiecesRemaining(Piece.Color color){
      int remain=0;
      
      for (int i=1; i<65; i++){
          // Piece occupier = ksApp.mainBoard.getOccupier(i);
          if (adapter.getOccupierColor(context, i) == color)
              remain++;
      }
      
      return remain;
  }
  
  public int PieceRemain(Piece.Color color, Piece.Value value){
      // returns the number of that piece remaining of that color
      int remain=0;
      
      for (int i=1; i<65; i++){
          // Piece occupier = ksApp.mainBoard.getOccupier(i);
          if (adapter.getOccupierColor(context, i)  == color && adapter.getOccupierValue(context, i)  == value)
              remain++;
      }
      
      return remain;
      
  }
}
