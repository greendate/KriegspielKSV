/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.util.ArrayList;
import java.util.Random;

import kriegspiel.Referee;
import uk.co.kriegspiel.KSpielUI.GameMode;

/**  RobotC
 * used in version 3.3
 * A development of RobotB, preferring King for takes
 *
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
public class RobotC extends Robot{
       // variables

    protected ArrayList<Integer> sequence;
    private Openings openings;  //list of opening moves
    private ArrayList<Integer> triesList; //opponents tries on last move
    Random generator;

    // constructors

    public RobotC(){

    }

    public RobotC (KSpielApp theApp, KSpielUI theUI, Piece.Color color){
        ksApp = theApp;
        ksUI = theUI;
        myColor = color;
        myLastGreenList = new ArrayList<Integer>();
        
        if(myColor == Piece.Color.WHITE) {
        	referee = new Referee(1);
        }
        else {
        	referee = new Referee(2);
        }
        
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
    
    public String toLetter(int i){
        String a="";
        switch (i){
                  case 1: a="a"; break;
                  case 2: a="b"; break;
                  case 3: a="c"; break;
                  case 4: a="d"; break;
                  case 5: a="e"; break;
                  case 6: a="f"; break;
                  case 7: a="g"; break;
                  case 8: a="h"; break;             
              }           
        
        return a;
    }
    
    private ArrayList<Integer> startSequence(){
        int choice = openings.getHowMany();

              int chosen = generator.nextInt(choice); // returns a random number between 0 and number of choices

              // debug only
             // chosen = 2;

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
             if ( isAdjacent(KingLoc, RookLocs.get(i))){
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
                            if (isAdjacent(listMyMovesTo.get(j),KingLoc)){
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
                                int x = getX(listMyMovesTo.get(j));
                                int y = getY(listMyMovesTo.get(j));
                                if (x == getX(KingLoc) || y == getY(KingLoc)){
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

    private boolean IsSqInBackRows(int sq){
        boolean inBackRows=false;

        if (myColor == Piece.Color.WHITE){
            if (sq<17) inBackRows=true;
        } else {
            if (sq>48) inBackRows=true;
        }
        return inBackRows;
    }


    private ArrayList<Integer> moveRook(ArrayList<Integer> rooklist, int dest){
       
        if  (! existsInList(rooklist, dest)){ // only do this if no rook there already            
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
            if (searching){  // no direct move available
                // create a two move step
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

    private String coordID(int sq){
        return toLetter(getX(sq))+(getY(sq));
    }

    private ArrayList<Integer> findRooksAndKing(){
        // find the King and all rooks/queens, returning the king's loc in first (0th) element
        ArrayList<Integer> RooksAndKing = new ArrayList<Integer>();
        ArrayList<Integer> RookLocs = new ArrayList<Integer>(); //find my rooks and queens, and the king
        int KingLoc=0;

        for (int i=0; i<listMyPieceLocations.size(); i++){
            if (referee.getOccupierValue(context, listMyPieceLocations.get(i))== Piece.Value.ROOK
                 || referee.getOccupierValue(context, listMyPieceLocations.get(i))== Piece.Value.QUEEN){
                RookLocs.add(listMyPieceLocations.get(i));
            }
            if (referee.getOccupierValue(context, listMyPieceLocations.get(i))== Piece.Value.KING){
                KingLoc = listMyPieceLocations.get(i);
            }
        }

        RooksAndKing.add(KingLoc);
        RooksAndKing.addAll(RookLocs);

        return RooksAndKing;
    }



    private ArrayList<Integer> clearbackrows(){

        // examine my pieces for a Pawn or Bishop or Knight in back rows
        // ask all such pieces to move if possible
        for (int i=0; i<listMyPieceLocations.size(); i++){
            if (IsSqInBackRows(listMyPieceLocations.get(i))){
                Piece.Value piecevalue = referee.getOccupierValue(context, listMyPieceLocations.get(i));
                if (piecevalue == Piece.Value.PAWN || piecevalue == Piece.Value.BISHOP || piecevalue == Piece.Value.KNIGHT){
                    // find this piece's valid moves
                    for (int j=0; j<listMyMovesFrom.size(); j++){
                        // test if piece has a move out of back row
                        if (listMyMovesFrom.get(j)== listMyPieceLocations.get(i) && !IsSqInBackRows(listMyMovesTo.get(j))){
                            sequence.add(listMyMovesFrom.get(j));
                            sequence.add(listMyMovesTo.get(j));
                            j=999; // break out of this for-loop
                        }
                    }

                }

            }

        }

        return sequence;
    }

    private ArrayList<Integer> KingToCorner(){


        ArrayList<Integer> rookLocs = findRooksAndKing();
        int KingLoc = rookLocs.get(0);

        int corner = chooseCorner();

        //ksUI.sysout("corner "+corner);
        if (KingLoc != corner){

            // is corner already occupied by piece of mine ? But not the king!
            if (referee.getOccupierColor(context, corner)==myColor && referee.getOccupierValue(context, corner) != Piece.Value.KING){
                // give it its marching order
                for (int i=0; i<listMyMovesFrom.size(); i++){
                    // scan for a move out of the square
                    if (listMyMovesFrom.get(i)==corner){
                        sequence.add(listMyMovesFrom.get(i));
                        sequence.add(listMyMovesTo.get(i));
                    }
                }
            }

            // now move the king
            
            int xDest = getX(corner);
            int yDest = getY(corner);

            int xKing = getX(KingLoc);
            int yKing = getY(KingLoc);
            int xTo = xKing; int yTo = yKing;
            int pTo = KingLoc;

            while (!(xKing==xDest && yKing==yDest)){
                if (xKing>xDest)
                    xTo = xKing-1;
                if (yKing>yDest)
                    yTo = yKing-1;
                if (xKing == xDest)
                    xTo = xDest;
                if (yKing == yDest)
                    yTo = yDest;
                if (xKing<xDest)
                    xTo = xKing+1;
                if (yKing<yDest)
                    yTo = yKing+1;

                pTo = getP(xTo, yTo);
                // before adding this move, check if a piece of mine is blocking
                if (referee.getOccupierColor(context, pTo)==myColor){
                    // find a move for this piece if possible
                    for (int i=0; i<listMyMovesFrom.size(); i++){
                        if (listMyMovesFrom.get(i)==pTo){
                            sequence.add(listMyMovesFrom.get(i));
                            sequence.add(listMyMovesTo.get(i));
                            i=999; // break out of for loop
                        }
                    }
                }


                sequence.add(KingLoc);
                sequence.add(pTo);

                xKing = xTo; yKing = yTo; KingLoc = pTo;
            }
        }

        // set up rook moves to king

        /*
        rookmoves(rookLocs.get(1),getRook1(corner));
        rookmoves(rookLocs.get(2),getRook2(corner));
        
         */
        rookLocs.set(0, corner);
        sequence = moveRooksToKing(rookLocs);
        sequence = moveRook(rookLocs, getRook1(corner));
        sequence = moveRook(rookLocs, getRook2(corner));
        

        sequence = scan(corner);

        return sequence;

    }

    private int setDirection(int corner){
        int randomIndex = generator.nextInt(4); // choose vert or horiz
        int incr = 1;
        if (randomIndex < 3){
            if (corner == 1 || corner == 57)
                incr = 1; // step across cols
            else
                incr = -1; // step reverse cols
        }
        else {
            if (corner == 1 || corner == 8)
                incr = 8; // step up rows
            else
                incr = -8; // step down rows
        }
        return incr;

    }
    private ArrayList<Integer> scan(int corner){

        int inside=2;
        int wing=9;
        int incr = setDirection(corner);

        int kingLoc = corner;

        switch (corner){
            case 1:
                if (incr == 8){
                    wing = 9;
                    inside = 2;
                } else {
                    wing = 2;
                    inside = 9;
                }
                break;
            case 8:
                if (incr == 8){
                    wing = 16;
                    inside = 7;
                } else {
                    wing = 7;
                    inside = 2;
                }
                break;
            case 57:
                if (incr == -8){
                    wing = 49;
                    inside = 58;
                } else {
                    wing = 58;
                    inside = 49;
                }
                break;
            case 64:
                if (incr == -8){
                    wing = 56;
                    inside = 63;

                } else {
                    wing = 63;
                    inside = 56;
                }
                break;
        }

        for (int k=0; k<6; k++ ){
            // loop this 6 times

            
            // clear any pieces from inside+incr cols & rows (and not to inside)

            // do i have any pieces that need moving?
            // get target rows & cols and rows cols to avoid
            int tx; int ty; int ax; int ay;
            tx = getX(inside+incr); ty = getY(inside+incr);
            ax = getX(inside); ay = getY(inside);

            for (int i=0; i<listMyPieceLocations.size(); i++){
                int p = listMyPieceLocations.get(i);
                int x = getX(p);
                int y = getY(p);
                // ignore the scanner king and rooks
                if ( wing != p && kingLoc != p && inside !=p
                     &&   (x==tx || y==ty)){
                    // move this, avoiding rows & cols if poss
                  //  sequence = avoid(p,ax,ay,tx,ty);
                }
            }
            
            
            sequence.add(inside);
            inside = inside+incr;
            sequence.add(inside);

            sequence.add(wing);
            wing = wing + incr;
            sequence.add(wing);

            sequence.add(kingLoc);
            kingLoc = kingLoc+incr;
            sequence.add(kingLoc);
        }



        return sequence;
    }
    
    private ArrayList<Integer> avoid (int p, int ax, int ay, int tx, int ty){
        // move piece from p, avoiding squares on cols ax and tx, and on rows tx and ty
        
        // temp code, just schedule a move for the piece
        if (existsInList(listMyMovesFrom,p)){
            int i=0;
            while (i<listMyMovesFrom.size() && listMyMovesFrom.get(i) != p){ // find first occurrence of p
                i++;
            }
            sequence.add(p);
            sequence.add(listMyMovesTo.get(i));
        }
        
        return sequence;
    }
    
/*
    private ArrayList<Integer> avoid (int p, int ax, int ay, int tx, int ty){
        // move piece from p, avoiding squares on cols ax and tx, and on rows tx and ty
        int To=0;
        if (existsInList(listMyMovesFrom,p)){

            int i=0;
            while (i<listMyMovesFrom.size() && listMyMovesFrom.get(i) != p){ // find first occurrence of p
                i++;
            }

            To = listMyMovesTo.get(i);  // accept the first move provisionally
            while (i<listMyMovesTo.size() && listMyMovesFrom.get(i)==p){  // scan to last occurrence of p
                if (IsAvoiding(To,ax,ay,tx,ty)){
                    To = listMyMovesTo.get(i);  // update move to known good position
                }
                i++;                
            }
            sequence.add(p);
            sequence.add(To);
        }

        return sequence;
    }
    */
    private boolean IsAvoiding(int p, int ax, int ay, int tx, int ty){
        boolean ok=false;
            // see if this satisfies avoidance criteria
            if (getX(p)!= ax && getX(p) != tx && getY(p) != ay && getY(p) != ty){
                ok = true;
            }
        return ok;
    }
    
    private int getRook1(int corner){
        int r1=9;
        switch (corner){
            case 1:
                r1=9; break;
            case 8:
                r1=16; break;
            case 57:
                r1=49; break;
            case 64:
                r1=56; break;
        }

        return r1;
    }
private int getRook2(int corner){
        int r1=2;
        switch (corner){
            case 1:
                r1=2; break;
            case 8:
                r1=7; break;
            case 57:
                r1=58; break;
            case 64:
                r1=63; break;
        }

        return r1;
    }


    private int chooseCorner(){
        int corner = 1;
        int randomIndex = generator.nextInt(3); //
        if (myColor == Piece.Color.WHITE){
            if (randomIndex<3)
                corner = 1;
            else
                corner = 8; // not 8, keep same corner for time being
        } else {
            if (randomIndex<3)
                corner = 64;
            else
                corner = 57;  // not 57 for the moment

        }
        return corner;

    }

    private ArrayList<Integer> unblockPawns(){
        // are any of my pawns stymied?
        // if so, can I hit the blocking piece?
        ArrayList<Integer> listBlockCleaners = new ArrayList<Integer>();
        for (int i=0; i<listMyPieceLocations.size(); i++){
            if (   getY(listMyPieceLocations.get(i)) <8
                && getY(listMyPieceLocations.get(i)) >1
                && referee.getOccupierValue(context, listMyPieceLocations.get(i)) == Piece.Value.PAWN){
                // check if this pawn is blocked
                if (! existsInList(listMyMovesFrom,listMyPieceLocations.get(i))){
                    // it's blocked, so can I hit the block?
                    int inc=1;
                    if (myColor == Piece.Color.WHITE) inc = 1; else inc = -1;
                    int target = getP(getX(listMyPieceLocations.get(i)), getY(listMyPieceLocations.get(i))+1);
                    for (int j=0; j<listMyMovesTo.size(); j++){
                        if (listMyMovesTo.get(j)==target){
                            listBlockCleaners.add(j);
                        }
                    }
                }

            }
        }

        if (listBlockCleaners.size()>0){
            //ksUI.sysout(myColor+" seq: unblock "+listBlockCleaners.size()+" pawns");
            for (int i=0; i<listBlockCleaners.size(); i++){
                sequence.add(listMyMovesFrom.get(listBlockCleaners.get(i)));
                sequence.add(listMyMovesTo.get(listBlockCleaners.get(i)));
            }
        }  else {
            // blocked pawn but no direct move
         // try a rook/queen attack
            ArrayList<Integer> rooklist = findRooksAndKing();
            if (rooklist.size()>1){
                // OK, we have a rook or queen
              
                // tackle the blocks in order, most advanced first.
                // WHITE robot has list in reverse order, BLACK in normal
                boolean searching = true; int rookloc=0;
                if (myColor == Piece.Color.BLACK){
                    for (int i=0; i<listBlockCleaners.size(); i++){
                        // find a rook which has a legal move to the target row
                        int ty = getY(listBlockCleaners.get(i));

                        if (searching){
                        for (int j=1; j<rooklist.size();j++){
                            for (int k=0; k<listMyMovesFrom.size() && searching; k++){
                                if (getY(listMyMovesTo.get(k))==ty){
                                    searching = false;
                                    sequence.add(listMyMovesFrom.get(k));
                                    sequence.add(listMyMovesTo.get(k));

                                    sequence.add(listMyMovesTo.get(k));
                                    rookloc = listBlockCleaners.get(i);
                                    sequence.add(rookloc);

                                }
                            }
                        }
                        } else{
                            // task the rook to this next target
                            sequence = rookmoves(rookloc,listBlockCleaners.get(i));
                        }

                    }
                } else {
                    // Robot is WHITE
                    for (int i=listBlockCleaners.size()-1; i>-1; i--){

                        // find a rook which has a legal move to the target row
                        int ty = getY(listBlockCleaners.get(i));

                        if (searching){
                        for (int j=1; j<rooklist.size();j++){
                            for (int k=0; k<listMyMovesFrom.size() && searching; k++){
                                if (getY(listMyMovesTo.get(k))==ty){
                                    searching = false;
                                    sequence.add(listMyMovesFrom.get(k));
                                    sequence.add(listMyMovesTo.get(k));

                                    sequence.add(listMyMovesTo.get(k));
                                    rookloc = listBlockCleaners.get(i);
                                    sequence.add(rookloc);

                                }
                            }
                        }
                        } else{
                            // task the rook to this next target
                            sequence = rookmoves(rookloc,listBlockCleaners.get(i));
                        }
                    }
                //listBlockCleaners.
                }
            }
        }

        return sequence;
    }

    private ArrayList<Integer> rookmoves(int From, int To){
        // this makes a sequence of one or two rook moves From To
        if (getX(From) == getX(To) || getY(From) == getY(To)){
            // single move
            sequence.add(From);
            sequence.add(To);
        }
        else{
            int inter = getP((getX(To)), getY(From));
            sequence.add(From);
            sequence.add(inter);
            sequence.add(inter);
            sequence.add(To);
        }
        return sequence;
    }
    private ArrayList<Integer> searchSequence(){

        // Ask board how many pieces remain
        int Remain = referee.getResult(context).iBlackRemain+referee.getResult(context).iWhiteRemain;
        if (Remain<17){
        // Three quarters cleared, so can we build a scanning sequence with Rooks or Queens?
        ArrayList<Integer> RookLocs = findRooksAndKing(); //find my rooks and queens, and the king

        if (RookLocs.size()>2){
            // we have at least two still standing, so can plan a row-by-row scanning end-game
            // choose a strategy at random

            int randomIndex = generator.nextInt(3); //
           // test case 5
           // randomIndex = 0;
           // ksUI.sysout(myColor+" search sequence: "+String.valueOf(randomIndex));
            switch (randomIndex){
                case 0:
                        sequence = unblockPawns();
                        if (sequence.size()>0)
                            break; // else try a kingtocorner strategy
                case 1:
                case 2:
                case 3:
                        sequence = clearbackrows();
                        sequence = KingToCorner();
                        break;
                    
                 default:
                        break;
                }
            } // end if RookLoc.size()>2
        else {
            if (RookLocs.size()==2){
                // only one rook, so see if we can free a pawn for promotion
                sequence = unblockPawns();
                
            }
        }

        if (sequence.size()>0) {
             //   ksUI.sysout(myColor+" seq:"+strSequence(sequence));
            }
            clearInvalidSequence(); // dismiss created sequence if invalid
        }


       return sequence;
    }

    private String strSequence(ArrayList<Integer> seq){
        String str = "";
        for (int i=0; i<seq.size(); i++){
            str = str+coordID(seq.get(i))+">";
            i++;
            str = str+coordID(seq.get(i))+",";
        }
        return str;
    }

    private void clearInvalidSequence(){

        if (sequence.size()>0){
             int iseqFrom = sequence.get(0);
             int iseqTo = sequence.get(1);

             boolean valid = false;
             //scan the valid moves list for the matching move
             for (int i=0; i<listMyMovesTo.size(); i++){
                 if (listMyMovesFrom.get(i) == iseqFrom && listMyMovesTo.get(i) == iseqTo)
                     valid = true;
                 }
             if (! valid) { // no move matching this one in valid moves list
                 sequence.clear();    // clear the sequence
              //   ksUI.sysout(myColor+" invalid seq "+coordID(iseqFrom)+"->"+coordID(iseqTo));
             }
        }
    }

    private int KingHitsGreen(){ // tests if robot in check, and if so, is there a move in which king hits green (therefore checking) square
        int mark = -1; //default, no condition
        if (referee.isInCheck(context)){
            for (int i=0; i<listMyMovesFrom.size(); i++){
                if ( referee.getOccupierValue(context, listMyMovesFrom.get(i)) == Piece.Value.KING   ){
                    if ( referee.isSquareGreen(context, listMyMovesTo.get(i))){
                        mark = i;
                    }
                }
            }
        }

        return mark;
    }

    @Override
      protected ArrayList<Integer> decideMove(){

          ArrayList<Integer> move = new ArrayList<Integer>();
          int iFrom=0;
          int iTo=0;
          result = referee.getResult(context);
         // Clear any invalid pre-programmed sequence.

          if (sequence.size()>0){
              clearInvalidSequence();
          }

          // if this is the first move, choose an opening move sequence
          if (movesMade < 2){
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

          // (c1) hit green square with king.
          // (c) hit red square or pawn take.

          // (c1) move piece threatened by new pawn try

          // (d) attack back row if promoted pawn



          // (e) pre-programmed sequence.
          // (f) random other move

          // Did the opponent's move result in a Red square? If so, list counter-moves.
                  //  [a remote player gets this from the UI: the robot just asks the board]

          ArrayList<Integer> listCounterMoves = new ArrayList<Integer>();
          ArrayList<Integer> listEscapeMoves = new ArrayList<Integer>();
          if (result.iRedSquare !=0){

              for (int j=0; j<listMyMovesTo.size(); j++){
                  if (listMyMovesTo.get(j)== result.iRedSquare){
                       listCounterMoves.add(j);
                  }
              }
              // if this was the result of a pawn try, is a piece threatened?
              // just have a look at piece rather than retrieve old pawn try announcement
              if (referee.getOccupierValue(context, result.iRedSquare)==Piece.Value.PAWN){
                  // identify threatened squares
                  int sq1=0;
                  int sq2=0;
                  if (myColor == Piece.Color.BLACK){
                      //opponent is WHITE
                      if (getX(result.iRedSquare)>1 && getY(result.iRedSquare)<8){
                          sq1  = getP(getX(result.iRedSquare)-1, getY(result.iRedSquare)+1);
                      }
                      if (getX(result.iRedSquare)<8 && getY(result.iRedSquare)<8){
                          sq2  = getP(getX(result.iRedSquare)+1, getY(result.iRedSquare)+1);
                      }

                      //look for escape moves
                      for (int i=0; i<listMyMovesFrom.size(); i++){
                          if (listMyMovesFrom.get(i)==sq1 || listMyMovesFrom.get(i)==sq2){
                              listEscapeMoves.add(i);
                          }
                      }
                  }
              }
          }

          // Can I Queen a Pawn? Better still take a piece as I queen it?
          ArrayList<Integer> listPawnQueenings = new ArrayList<Integer>();
          ArrayList<Integer> listPawnQueenTakes = new ArrayList<Integer>();
          for (int j=0; j<listMyMovesFrom.size(); j++){
              if (referee.getOccupierValue(context, listMyMovesFrom.get(j))== Piece.Value.PAWN) {
                  switch (myColor) {

                      case WHITE:
                          if (listMyMovesFrom.get(j)>48){
                              if (getX(listMyMovesFrom.get(j)) != getX(listMyMovesTo.get(j)))
                                        listPawnQueenTakes.add(j);
                              else
                                        listPawnQueenings.add(j);
                          }
                          break;

                      case BLACK:
                          if (listMyMovesFrom.get(j)<17){
                              if (getX(listMyMovesFrom.get(j)) != getX(listMyMovesTo.get(j)))
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
              if (referee.getOccupierValue(context, listMyMovesFrom.get(j)) == Piece.Value.PAWN) {
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
                      if (getX(listMyMovesFrom.get(j)) != getX(listMyMovesTo.get(j))){
                          listCounterMoves.add(j);
                      }
                  } // end if
              }  // end for

          // is there an announcement that an enemy pawn been promoted ?
          ArrayList<Integer> listBackNo = new ArrayList<Integer>(); // move number
          ArrayList<Integer> listBackTo = new ArrayList<Integer>(); // move to
          ArrayList<Integer> listBackFrom = new ArrayList<Integer>(); // move from
          int backRow = -1; // default selected move number
          if (result.bQueenedPawn){

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


              // Pick best move to back row: look for check-relieving king move then longest scan across row

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

                         // is a king in check and in list to make a move to/on the back row?
                         boolean king = false;
                         if (result.bBlackInCheck || result.bWhiteInCheck){
                             for (int j=0; j<listBackTo.size(); j++){
                                 if (referee.getOccupierValue(context, listBackFrom.get(j)) == Piece.Value.KING){
                                     backRow = j;
                                 }
                             }
                         }

                         if (!king) // no king move relieving check, so try for longest span move
                         {
                             int span = 0;
                             int dist = 0;
                             for (int j=1; j<listBackTo.size(); j++){
                                 dist = getX(listBackTo.get(j))
                                             - getX(listBackFrom.get(j));

                                 if (getY(listBackTo.get(j)) == getY(listBackFrom.get(j))){ // on same (back) row
                                     if ( Math.abs(dist) > span){
                                         span = Math.abs(dist);
                                         backRow = j;
                                     }
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

              int kingStrike = KingHitsGreen();
              if (kingStrike>-1){

                iFrom = listMyMovesFrom.get(kingStrike);
                iTo = listMyMovesTo.get(kingStrike);


              } else {

              if (listCounterMoves.size()>0){
                  // there is at least one countermove
                  // test if the king can do a countermove
                  boolean king = false;

                  for (int j=0; j<listCounterMoves.size(); j++){
                      if (referee.getOccupierValue(context, listMyMovesFrom.get(listCounterMoves.get(j))) == Piece.Value.KING){
                          king = true;
                          iFrom = listMyMovesFrom.get(listCounterMoves.get(j));
                          iTo = listMyMovesTo.get(listCounterMoves.get(j));
                      }
                  }

                  if (!king)
                  {
                      randomIndex = generator.nextInt(listCounterMoves.size());
                      iFrom = listMyMovesFrom.get(listCounterMoves.get(randomIndex));
                      iTo = listMyMovesTo.get(listCounterMoves.get(randomIndex));
                  }

              } else {

                  if (listEscapeMoves.size()>0){
                      randomIndex = generator.nextInt(listEscapeMoves.size());
                      iFrom = listMyMovesFrom.get(listEscapeMoves.get(randomIndex));
                      iTo = listMyMovesTo.get(listEscapeMoves.get(randomIndex));
                  } else


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

                   //V3.3  but let's try to avoid opponent's pawn try squares if possible
                   triesList = referee.getOldTries();


                   boolean scan = true;
                   int scancount = 0;
                   while (scan){
                       scancount++;
                       randomIndex= generator.nextInt(listMyMovesFrom.size());
                       iTo = listMyMovesTo.get(randomIndex);
                       if (!existsInList(triesList,iTo) || scancount>50){
                            iFrom = listMyMovesFrom.get(randomIndex);
                            scan =false;
                       }
                   }
                   // v3.3

               }
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
