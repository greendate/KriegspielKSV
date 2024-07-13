/*
 * 
 */

package uk.co.kriegspiel;
import java.util.ArrayList;
import java.util.Random;
import uk.co.kriegspiel.KSpielUI.GameMode;
/**  RobotB
 *   used in version 3.2
 * The basic robot makes a list of all possible moves.
 * It then choses in the following order:
 * (a) hit a red square.
 * (b) accelerate pawn in last 4 rows or a pawn take, chosen at random
 * (c) move to a square adjacent to a check line, chosen at random
 *      failing any of the above it makes a random move
 * 
 * This variant allows for a pre-plannned sequence of moves which continue
 * until one of the moves proves feasible or the list is completed.  
 * In this version, the pre-planned sequence has priority over (b) and (c).
 * 
 * This feature is intended for opening moves and end-games.
 * 
 * @author denysbennett
 */
public class RobotB extends Robot{

    // variables
    
    protected ArrayList<Integer> sequence;
    private Openings openings;  //list of opening moves
    Random generator;
    
    // constructors
    
    public RobotB(){
        
    }
    
    public RobotB (KSpielApp theApp, KSpielUI theUI, Piece.Color color){
        ksApp = theApp;
        ksUI = theUI;
        myColor = color;
        myLastGreenList = new ArrayList<Integer>();
        
        // extension of Robot
        sequence = new ArrayList<Integer>(); // pre-programmed move sequence (From, to, from, to, ....)
        openings = new Openings(myColor);
        generator = new Random();

    }
    

    // methods
    
    @Override
    public void SendChallenge(String varlet, GameMode game, Piece.Color color, RuleSet rules){
        SendAcceptChallenge(true);
    }
    
    @Override
    public void SendAcceptChallenge(boolean accept){
        ksUI.OnReceiveChallengeResponse(accept);
    }
    
    private ArrayList<Integer> startSequence(){
        int choice = openings.getHowMany();

              int chosen = generator.nextInt(choice); // returns a random number between 0 and number of choices

              if (chosen == 0){
                  sequence.clear(); // let the robot choose a move at random
              }
              else {
                  chosen--; // give the robot an opening sequence
                  //chosen = 0;   // no, try first one
                  // load up a moves sequence
                  sequence = openings.getOpening(chosen);
              }
        return sequence;
    }

    private ArrayList<Integer> moveRooksToKing(ArrayList<Integer> RookLocs ){
        //* ToDo - this is incomplete
        
        int KingLoc = RookLocs.get(0);
         // Two rooks need to be adjacent to the King: count them
        ArrayList<Integer> rooksAdjacent = new ArrayList<Integer>();
        ArrayList<Integer> rooksNotAdjacent = new ArrayList<Integer>();
        for (int i=1; i<RookLocs.size(); i++){
             if ( ksApp.isAdjacent(KingLoc, RookLocs.get(i))){
                  rooksAdjacent.add(RookLocs.get(i));
             }
             else {
                  rooksNotAdjacent.add(RookLocs.get(i));
               }
                      }
                      
                      if (rooksAdjacent.size()<2){
                          boolean searching = true;
                          boolean found = false;
                        
                          for (int i=0; i<rooksNotAdjacent.size() && searching ; i++){
                              for (int j=0; j<listMyMovesFrom.size() && searching ; j++){
                                if (rooksNotAdjacent.get(i) == listMyMovesFrom.get(j)){
                                    // revert = j; // save this move pointer in case I want to move a rook anyway
                                    if (ksApp.isAdjacent(listMyMovesTo.get(j),KingLoc)){
                                        sequence.add(listMyMovesFrom.get(j));
                                        sequence.add(listMyMovesTo.get(j));
                                        found = true;
                                   //     searching = false; // force exit from loops with first entry in sequence  
                                        // just get one rook at a time
                                    }
                                }
                              }
                          }
                          
                          if (!found){
                              searching = true;
                              
                              // try to move a rook to a row or column of king
                              for (int i=0; i<rooksNotAdjacent.size() && searching ; i++){
                                  for (int j=0; j<listMyMovesFrom.size() && searching ; j++){
                                     if (rooksNotAdjacent.get(i) == listMyMovesFrom.get(j)){
                                     
                                        // test if move is to same row or col as king
                                       int x = ksApp.getX(listMyMovesTo.get(j));
                                       int y = ksApp.getY(listMyMovesTo.get(j));
                                       if (x == ksApp.getX(KingLoc) || y == ksApp.getY(KingLoc)){
                                            sequence.add(listMyMovesFrom.get(j));
                                            sequence.add(listMyMovesTo.get(j));
                                            found = true;
                                        //    searching = false; // force exit from loops with first entry in sequence  
                                        // just get one rook at a time
                                        }
                                      }
                                    }
                                }
                            }
                          
                      } 
                    
        return sequence;
    }
    
    private ArrayList<Integer> moveToSq1(ArrayList<Integer> RookLocs){
        //  This tries to make a sequence to move King to sq 9, Rook1 to sq2, Rook2 to sq17
        // then to rake across rows 1,2,3 column by column
        
        // start with King
        int KingLoc = RookLocs.get(0);
        
        int xKing = ksApp.getX(KingLoc);
        int yKing = ksApp.getY(KingLoc);
        int xTo = xKing; int yTo = yKing;
        
        while (!(xKing==1 && yKing==2)){
            if (xKing>1) 
                xTo = xKing-1;
            if (yKing>2) 
                yTo = yKing-1;
            if (yKing == 1) 
                yTo = 2;

            sequence.add(KingLoc);
            sequence.add(ksApp.getP(xTo, yTo));

            xKing = xTo; yKing = yTo;
        }
        
        sequence = moveRooksToKing(findRooksAndKing()); // quick way to put new KingLoc in old RookLocs
        
        int rook1 = 17;
        int rook2 = 2;
        
        sequence = moveRook(RookLocs, rook1);
        sequence = moveRook(RookLocs, rook2);
        
        for (int k=0; k<3; k++){  // repeat 3 times
            sequence.add(rook1); // From
            rook1++;
            sequence.add(rook1); // To

            sequence.add(KingLoc); // From
            KingLoc++;
            sequence.add(KingLoc); // To
            sequence.add(KingLoc); // From
            KingLoc++;
            sequence.add(KingLoc); // To

            sequence.add(rook1); // From
            rook1++;
            sequence.add(rook1); // To

            sequence.add(rook2); // From
            rook2 = rook2 + 2;
            sequence.add(rook2); // To
        }
        return sequence;
    }
    
    private ArrayList<Integer> moveToSq57(ArrayList<Integer> RookLocs){
        //  This tries to make a sequence to move King to sq 9, Rook1 to sq2, Rook2 to sq17
        // then to rake across rows 1,2,3 column by column
        
        // start with King
        int KingLoc = RookLocs.get(0);
        
        int xKing = ksApp.getX(KingLoc);
        int yKing = ksApp.getY(KingLoc);
        int xTo = xKing; int yTo = yKing;
        
        while (!(xKing==1 && yKing==7)){
            if (xKing>1) 
                xTo = xKing-1;
            if (yKing<7) 
                yTo = yKing+1;
            if (yKing == 8) 
                yTo = 7;

            sequence.add(KingLoc);
            sequence.add(ksApp.getP(xTo, yTo));

            xKing = xTo; yKing = yTo;
        }
        
        sequence = moveRooksToKing(findRooksAndKing()); // quick way to put new KingLoc in old RookLocs
        
        int rook1 = 41;
        int rook2 = 58;
        
        sequence = moveRook(RookLocs, rook1);
        sequence = moveRook(RookLocs, rook2);
        
        for (int k=0; k<3; k++){  // repeat 3 times
            sequence.add(rook1); // From
            rook1++;
            sequence.add(rook1); // To

            sequence.add(KingLoc); // From
            KingLoc++;
            sequence.add(KingLoc); // To
            sequence.add(KingLoc); // From
            KingLoc++;
            sequence.add(KingLoc); // To

            sequence.add(rook1); // From
            rook1++;
            sequence.add(rook1); // To

            sequence.add(rook2); // From
            rook2 = rook2 + 2;
            sequence.add(rook2); // To
        }
        return sequence;
    }
    
    private ArrayList<Integer> moveRook(ArrayList<Integer> rooklist, int dest){
        if  (! existsInList(rooklist, dest)){
            boolean searching = true;
            for (int i=1; i<rooklist.size() && searching; i++){
                for (int j=0; j<listMyMovesFrom.size() && searching; j++){
                    if (listMyMovesFrom.get(j) == rooklist.get(i) && listMyMovesTo.get(j) == dest){
                        searching = false;
                        sequence.add(listMyMovesFrom.get(j));
                        sequence.add(dest);
                    }
                } 
            }
        }
        
        return sequence;
    }
    private boolean existsInList(ArrayList<Integer> list, int target){
        boolean exists = false;
        for (int i=0; i<list.size(); i++){
            if (target == list.get(i)) exists = true;
        }
        
        return exists;
        
    }
    private ArrayList<Integer> findRooksAndKing(){
        // find the King and all rooks/queens, returning the king's loc in first (0th) element
        ArrayList<Integer> RooksAndKing = new ArrayList<Integer>(); 
        ArrayList<Integer> RookLocs = new ArrayList<Integer>(); //find my rooks and queens, and the king
        int KingLoc=0;

        for (int i=0; i<listMyPieceLocations.size(); i++){
            if (ksApp.mainBoard.getOccupier(listMyPieceLocations.get(i)).value()== Piece.Value.ROOK
                 || ksApp.mainBoard.getOccupier(listMyPieceLocations.get(i)).value()== Piece.Value.QUEEN){
                RookLocs.add(listMyPieceLocations.get(i));
            }
            if (ksApp.mainBoard.getOccupier(listMyPieceLocations.get(i)).value()== Piece.Value.KING){
                KingLoc = listMyPieceLocations.get(i);
            }
        }
        
        RooksAndKing.add(KingLoc);
        RooksAndKing.addAll(RookLocs);
        
        return RooksAndKing;
    }
    
    private ArrayList<Integer> clearbackrows(){
        
        if (myColor == Piece.Color.WHITE){
            // this adds a move to clear items from back rows: WHITE
            for (int i=0; i<listMyPieceLocations.size(); i++){
                if (listMyPieceLocations.get(i)<17){
                    Piece.Value piecevalue = ksApp.mainBoard.getOccupier(listMyPieceLocations.get(i)).value();
                    if (piecevalue == Piece.Value.PAWN || piecevalue == Piece.Value.BISHOP || piecevalue == Piece.Value.KNIGHT){
                        for (int j=0; j<listMyMovesFrom.size(); j++){
                            // the following line crashed with indexout of bounds with value 9 size 9
                            if (listMyMovesFrom.get(j)== listMyPieceLocations.get(i) && listMyMovesTo.get(j)>16){
                                sequence.add(listMyMovesFrom.get(j));
                                sequence.add(listMyMovesTo.get(j));
                                i++;    // skip to next piece  

                            }
                            //!!!! must make sure there are more pieces!
                            if (i==listMyPieceLocations.size())
                                break; 
                        }
                    }
                }
            }
        } else { 
            // this adds a move to clear items from back rows: BLACK
            for (int i=0; i<listMyPieceLocations.size(); i++){
                if (listMyPieceLocations.get(i)>48){
                    Piece.Value piecevalue = ksApp.mainBoard.getOccupier(listMyPieceLocations.get(i)).value();
                    if (piecevalue == Piece.Value.PAWN || piecevalue == Piece.Value.BISHOP || piecevalue == Piece.Value.KNIGHT){
                        for (int j=0; j<listMyMovesFrom.size(); j++){
                            
                            if (listMyMovesFrom.get(j)== listMyPieceLocations.get(i) && listMyMovesTo.get(j)<49){
                                sequence.add(listMyMovesFrom.get(j));
                                sequence.add(listMyMovesTo.get(j));
                                i++;    // skip to next piece  

                            }
                            //!!!! must make sure there are more pieces!
                            if (i==listMyPieceLocations.size())
                                break; 
                        }
                    }
                }
            }
            
        
        }
        
        return sequence;
    }
    
    private ArrayList<Integer> searchSequence(){
                     // Ask board how many pieces remain

              int Remain = ksApp.mainBoard.result.iBlackRemain+ksApp.mainBoard.result.iWhiteRemain;
              if (Remain<17){
                  // Three quarters cleared, so can we build a scanning sequence with Rooks or Queens?
                  
                  ArrayList<Integer> RookLocs = findRooksAndKing(); //find my rooks and queens, and the king
                  
                  if (RookLocs.size()>2){
                      // we have at least two still standing, so can plan a row-by-row scanning end-game
                      // choose a strategy at random
                      
                      int randomIndex = generator.nextInt(4); // 
                      
                      // test case 2
                      // randomIndex = 2;
                      ksUI.sysout("search sequence: "+String.valueOf(randomIndex));
                      switch (randomIndex){
                          
                          case 0:
                              sequence = clearbackrows();
                          case 1:
                              sequence = moveToSq1(RookLocs);
                              break;
                          case 2:
                              sequence = moveToSq57(RookLocs);
                              break;
                          case 3:
                              sequence = moveRooksToKing(RookLocs);
                              break;
                          default:
                              break;
                      }
                      
                      
                      
                  } // end if RookLoc.size()>1
                  
                clearInvalidSequence(); // dismiss created sequence if invalid  
              }
        return sequence;
    }
    
    private void clearInvalidSequence(){
        
        if (sequence.size()>0){
             int iseqFrom = sequence.get(0);
             int iseqTo = sequence.get(1);

             boolean valid = false;
             //scan the valid moves list
             for (int i=0; i<listMyMovesTo.size(); i++){
                 if (listMyMovesFrom.get(i) == iseqFrom && listMyMovesTo.get(i) == iseqTo)
                     valid = true;
                 }
             if (! valid) sequence.clear();    // clear the sequence
        }
    }
    
    @Override
      protected ArrayList<Integer> decideMove(){
      
          ArrayList<Integer> move = new ArrayList<Integer>();
          int iFrom=0;
          int iTo=0;
          
         // Clear any invalid pre-programmed sequence.
              
          if (sequence.size()>0){              
              clearInvalidSequence();
          }

          // if this is the first move, choose an opening move sequence
          if (ksApp.mainBoard.getMovesMade() < 2){
              sequence = startSequence();  // modifies sequence
              // sequence = startSequence(sequence);   
          } // end of choose opening move sequence
          
          // if game is advanced, try to make an end-game sequence if none yet exists
          if (sequence.size()==0) {            
              sequence = searchSequence(); 
          }
          
          // priorities as follows:

          // (a) pawn queened taking a piece.
          // (b) pawn queened.
          // (c) hit red square or pawn take.
          // (d) attack back row if promoted pawn
          // (e) pre-programmed sequence.
          // (f) random other move

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

          // Can I Queen a Pawn? Better still take a piece as I queen it?
          ArrayList<Integer> listPawnQueenings = new ArrayList<Integer>();
          ArrayList<Integer> listPawnQueenTakes = new ArrayList<Integer>();
          for (int j=0; j<listMyMovesFrom.size(); j++){
              if (ksApp.mainBoard.getOccupier(listMyMovesFrom.get(j)).value() == Piece.Value.PAWN) {
                  switch (myColor) {

                      case WHITE:
                          if (listMyMovesFrom.get(j)>48){
                              if (ksApp.getX(listMyMovesFrom.get(j)) != ksApp.getX(listMyMovesTo.get(j)))
                                        listPawnQueenTakes.add(j);
                              else
                                        listPawnQueenings.add(j);
                          }
                          break;

                      case BLACK:
                          if (listMyMovesFrom.get(j)<17){
                              if (ksApp.getX(listMyMovesFrom.get(j)) != ksApp.getX(listMyMovesTo.get(j)))
                                        listPawnQueenTakes.add(j);
                              else
                                        listPawnQueenings.add(j);
                          }
                          break;
                  }  // end switch
              } // end if
          }  // end for

          // Can I take with a pawn? Add to countermoves list
          // Can I promote a pawn? Accelerate if 4 or less rows from end
          int accelerateW =32; int accelerateB = 33;
          int remain = PiecesRemaining(myColor);
          switch (remain){
              case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16:
                  accelerateW =32; accelerateB = 33;
                  break;
              default:
                  accelerateW = 8; accelerateB = 57;    // accelerate all pawns
                  break;
                  
          }
          
          ArrayList<Integer> listPawnMoves = new ArrayList<Integer>();
          for (int j=0; j<listMyMovesFrom.size(); j++){
              if (ksApp.mainBoard.getOccupier(listMyMovesFrom.get(j)).value() == Piece.Value.PAWN) {
                  switch (myColor) {
                      
                      case WHITE:
                          if (listMyMovesFrom.get(j)>accelerateW)
                              listPawnMoves.add(j);
                          break;
                          
                      case BLACK:
                              if (listMyMovesFrom.get(j)<accelerateB)
                                  listPawnMoves.add(j);
                              break;
                      } // end switch
                  
                      // test for a pawn take
                      if (ksApp.getX(listMyMovesFrom.get(j)) != ksApp.getX(listMyMovesTo.get(j))){
                          listCounterMoves.add(j);
                      }
                  } // end if
              }  // end for

          // is there an announcement that an enemy pawn been promoted ?
          ArrayList<Integer> listBackNo = new ArrayList<Integer>(); // move number
          ArrayList<Integer> listBackTo = new ArrayList<Integer>(); // move to
          ArrayList<Integer> listBackFrom = new ArrayList<Integer>(); // move from
          int backRow = -1; // default selected move number
          if (ksUI.mainBoard.result.bQueenedPawn && ksUI.ksApp.myCfg.rules.bAnnounceQueens){

               // list any moves to my back (first) row
              for (int i=0; i<listMyMovesTo.size(); i++){
                  switch (myColor){
                      case WHITE:
                          if (listMyMovesTo.get(i)<9){
                              listBackNo.add(i);
                              listBackTo.add(listMyMovesTo.get(i));
                              listBackFrom.add(listMyMovesFrom.get(i));
                          }
                          break;
                      case BLACK:
                          if (listMyMovesTo.get(i)>56){
                              listBackNo.add(i);
                              listBackTo.add(listMyMovesTo.get(i));
                              listBackFrom.add(listMyMovesFrom.get(i));
                          }
                          break;
                  }
              }

              
              // Pick best move to back row: look for longest scan across row

              // failing that pick any move and try that
                 switch (listBackTo.size()) {
                     case 0: // empty
                         backRow = -1;
                         break;
                     case 1: // just select it (later)
                         backRow = 0; // point to sole entry
                         break;
                     default:   // choose the best
                         backRow = 0; // default to first entry
                         int span = 0;
                         int dist = 0;
                         for (int j=1; j<listBackTo.size(); j++){
                             dist = ksUI.ksApp.getX(listBackTo.get(j))
                                         - ksUI.ksApp.getX(listBackFrom.get(j));
                             if (ksUI.ksApp.getY(listBackTo.get(j)) == ksUI.ksApp.getY(listBackFrom.get(j))){
                                 if ( Math.abs(dist) > span){
                                     span = Math.abs(dist);
                                     backRow = j;
                                 }
                             }
                         }

                         break;
                }
              
          }

              // End of list-making
         
              
              // Now select a move from the above lists
              
              int randomIndex;
              
              if (listPawnQueenTakes.size()>0){
                  // there is at least one countermove
                  randomIndex = generator.nextInt(listPawnQueenTakes.size());
                  iFrom = listMyMovesFrom.get(listPawnQueenTakes.get(randomIndex));
                  iTo = listMyMovesTo.get(listPawnQueenTakes.get(randomIndex));
              } else {
                  
              if (listPawnQueenings.size()>0){
                  // there is at least one countermove
                  randomIndex = generator.nextInt(listPawnQueenings.size());
                  iFrom = listMyMovesFrom.get(listPawnQueenings.get(randomIndex));
                  iTo = listMyMovesTo.get(listPawnQueenings.get(randomIndex));
              } else {

              
              if (backRow>-1){
                  // there is a backrow scan move attempting to kill pawn promotion
                  iFrom = listBackFrom.get(backRow);
                  iTo = listBackTo.get(backRow);
                  
              } else {
              if (listCounterMoves.size()>0){
                  // there is at least one countermove
                  randomIndex = generator.nextInt(listCounterMoves.size());
                  iFrom = listMyMovesFrom.get(listCounterMoves.get(randomIndex));
                  iTo = listMyMovesTo.get(listCounterMoves.get(randomIndex));
              } else {

              //  Is there a valid pre-programmed move
              if (sequence.size()>0){
                   iFrom = sequence.get(0);
                   iTo = sequence.get(1);
                      
                   sequence.remove(0);   // delete from
                   sequence.remove(0);   // delete to
                      
               } else {
                  
 
               // resume without using pre-programmed move

               if (listPawnMoves.size()>0){
                    // there is at least one pawn promotion opportunity
                     randomIndex = generator.nextInt(listPawnMoves.size());
                     iFrom = listMyMovesFrom.get(listPawnMoves.get(randomIndex));
                     iTo = listMyMovesTo.get(listPawnMoves.get(randomIndex));

                } else {
                        
                   // just make a random move

                   randomIndex= generator.nextInt(listMyMovesFrom.size());
                   iFrom = listMyMovesFrom.get(randomIndex);
                   iTo = listMyMovesTo.get(randomIndex);
                }
                      
               }
              }
              }
              }
              }
                  
              
              
              move.add(iFrom);
              move.add(iTo);

          return move;
               
  }
}
