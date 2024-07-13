package uk.co.kriegspiel;
import java.util.*;

/** Kriegspiel Board class - defines properties and methods of a chess board
 *
 * @author denysbennett
 */

public class Board {
    
  // External object references
  public KSpielApp ksApp; // reference back to the creating App

  private int callLevel; // distinguish master from invoked copies
  
    // Variables
    // Declare an array which will hold 64 squares numbered 1-64 (set by constructor)
  private Square[] arraySquare;// = new Square[65];
  private boolean bWhiteToPlay;
  
  public Result result;    // result of movePiece - piece taken, Mates, Check by Knight
    // UI Display toggles
  private int iSelectedSquare; // 
  private ArrayList<Integer> greenList; // list of squares on checking lines
 // private ArrayList<Integer> pawnTriesList; // list of squares which may now be taken by pawns
    // Castling state variables
  private boolean bWhiteKingMoved = false;
  private boolean bBlackKingMoved = false;
  private boolean bBlackWestRookMoved = false;
  private boolean bBlackEastRookMoved = false;
  private boolean bWhiteWestRookMoved = false;
  private boolean bWhiteEastRookMoved = false;
  
  private int iMovesMade;

  // Constructors

  public Board(KSpielApp appRef, int level){
      
      callLevel = level;
      
      // Set the reference to the creating Application
      
      ksApp = appRef;
      arraySquare = new Square[65];  // create a new board squares array container
      
      // Create a set of empty squares with dark & light grey (WASH) colours
 
      for (int i=1; i<65; i++){   
          // create each new square with its own position number and board and application references
          arraySquare[i] = new Square(i, this, ksApp); 
          // place an empty "piece" in the square
          arraySquare[i].setOccupier(new Empty(ksApp.archEmpty, Piece.Color.WASH));
          arraySquare[i].getOccupier().Initialise(i, this, ksApp);  // complete the construction of Piece objects

      }
      
      result = new Result();
      // Ensure no square selected
      iSelectedSquare = 0;
     
      // create a empty list of check direction squares
      greenList = new ArrayList<Integer>();
      
      // bWhiteToPlay is set true by setStartingPositions()
  
 } // END Constructor

  
  public Board copyBoardFrom(Board master){
      // sets up the board to replicate master
      
      //constructor has already set up arraySquare = new Square[65];  // new board squares array container
      
      bWhiteToPlay = master.bWhiteToPlay;
      result.iRedSquare = master.result.iRedSquare;
      iSelectedSquare = master.iSelectedSquare;
      bWhiteKingMoved = master.bWhiteKingMoved;
      bBlackKingMoved = master.bBlackKingMoved;
      bBlackWestRookMoved = master.bBlackWestRookMoved;
      bBlackEastRookMoved = master.bBlackEastRookMoved;
      bWhiteWestRookMoved = master.bWhiteWestRookMoved;
      bWhiteEastRookMoved = master.bWhiteEastRookMoved;
      
      result.iBlackRemain = master.result.iBlackRemain;
      result.iWhiteRemain = master.result.iWhiteRemain;
      
      iMovesMade = master.iMovesMade;
      
      // until I can get clone() to work!
      for (int i=1; i<65; i++){
          
          Piece masterPiece = master.getOccupier(i);
          // Piece copyPiece=null;
          switch(masterPiece.value()){
              case EMPTY:
                  Empty copyEmpty = new Empty();
                  copyEmpty = copyEmpty.copyPieceFrom(masterPiece);
                  copyEmpty.setBoardRef(this);
                  // place the copy on the Square
                  arraySquare[i].setOccupier(copyEmpty);
                  break;
              case PAWN:
                  Pawn copyPawn = new Pawn();
                  copyPawn = copyPawn.copyPieceFrom(masterPiece);
                  copyPawn.setBoardRef(this);
                  // place the copy on the Square
                  arraySquare[i].setOccupier(copyPawn);
                  break;
              case ROOK:
                  Rook copyRook = new Rook();
                  copyRook = copyRook.copyPieceFrom(masterPiece);
                  copyRook.setBoardRef(this);
                  arraySquare[i].setOccupier(copyRook);
                  break;
              case BISHOP:
                  Bishop copyBishop = new Bishop();
                  copyBishop = copyBishop.copyPieceFrom(masterPiece);
                  copyBishop.setBoardRef(this);
                  arraySquare[i].setOccupier(copyBishop);
                  break;
              case KNIGHT:
                  Knight copyKnight = new Knight();
                  copyKnight = copyKnight.copyPieceFrom(masterPiece);
                  copyKnight.setBoardRef(this);
                  arraySquare[i].setOccupier(copyKnight);
                  break;
              case QUEEN:
                  Queen copyQueen = new Queen();
                  copyQueen = copyQueen.copyPieceFrom(masterPiece);
                  copyQueen.setBoardRef(this);
                  arraySquare[i].setOccupier(copyQueen);
                  break;
              case KING:
                  King copyKing = new King();
                  copyKing = copyKing.copyPieceFrom(masterPiece);
                  copyKing.setBoardRef(this);
                  arraySquare[i].setOccupier(copyKing);
                  break;
          }
          
          arraySquare[i].setVisible(master.getSquare(i).isVisible());
          arraySquare[i].setLegal(master.getSquare(i).isLegal());
          arraySquare[i].setGreen(master.getSquare(i).isGreen());
          arraySquare[i].setAspect(master.getSquare(i).getAspect());
      
      }
      
      return this;
  }  // end copyBoardFrom()
 
  // EMERGENCY STOP
  public void ErrorStop(String msg){
      ksApp.ksUI.errorMsg(msg);
      
  }
  
  // getters
  //functions returning x,y coordinates from square index p 1-64 & vice-versa
  // x is column no, y is row no
  private int getY(int p) { rangeCheck(p); int y = 1+(p-1)/8; return y; }
  private int getX(int p) { rangeCheck(p); int x = p % 8; if (x == 0){ x=8;} return x; }
  private int getP(int x, int y){ return x+(y-1)*8; }
  
  
  private Piece.Color getOtherSide(Piece.Color Side){
    Piece.Color otherSide;
    if (Side == Piece.Color.WHITE) otherSide = Piece.Color.BLACK;
    else otherSide = Piece.Color.WHITE;
    return otherSide; 
  }

  public Square getSquare(int iSq){rangeCheck(iSq); return arraySquare[iSq]; }
  public Square.Aspect getAspect(int iSq){ rangeCheck(iSq); return arraySquare[iSq].getAspect(); } 
  public boolean isSquareLegal(int iSq){rangeCheck(iSq); return arraySquare[iSq].isLegal(); }
  public Result getResult(){return result;}
  public Piece getOccupier(int iSq){
      rangeCheck(iSq);
     // Piece.Value value = arraySquare[iSq].getOccupier().value();   // activate for debug
      return arraySquare[iSq].getOccupier(); 
  }
  public boolean isWhiteToPlay(){ return bWhiteToPlay; }
  public Piece.Color toPlay(){
      if (isWhiteToPlay()) return Piece.Color.WHITE; else return Piece.Color.BLACK;
  }
  
  public Piece.Color NotToPlay(){
      if (!isWhiteToPlay()) return Piece.Color.WHITE; else return Piece.Color.BLACK;
  }
  public int getSelectedSquare(){ return iSelectedSquare; }
  public int getMovesMade(){ return iMovesMade; }
  
  public boolean IsInCheck(){
      return result.bBlackInCheck || result.bWhiteInCheck;
  }
  
  // setters
  public void setRedSquare(int iSq){ 

      result.iRedSquare = iSq;
      if (iSq != 0){
        rangeCheck(iSq);
        if (bWhiteToPlay){
          result.iBlackRemain--;
        } else {
          result.iWhiteRemain--;
        }
      }
  }
  public void setTestPiece(int iSq, int pieceIndex, Piece.Color pieceColor){
      getSquare(iSq).setTestPiece(pieceIndex, pieceColor);
  }

  public void clearTestPiece(int iSq, Piece.Color color){
      // color is player color, not piece color
      getSquare(iSq).clearTestPiece(color);
  }
  public void clearAllTestPieces(Piece.Color color){
      for (int i=1; i<65; i++){
          clearTestPiece(i,color);
      }
  }
  public boolean isRedSquare(int iSq){ rangeCheck(iSq); return (iSq == result.iRedSquare); }
  public int getRedSquare(){ return result.iRedSquare; }
  
  public void setVisible(boolean bWhite, boolean bBlack){
      for (int i=1; i<65; i++){
          Piece.Color color = arraySquare[i].getOccupier().color();
          switch (color){
              case BLACK:
                  arraySquare[i].setVisible(bBlack);
                  break;
          
              case WHITE:
                  arraySquare[i].setVisible(bWhite);              
                  break;
          }
                      
      }
  }
  public boolean isTrySquare(int iSq){ rangeCheck(iSq); return arraySquare[iSq].isTry(); }
  public boolean isGreenSquare(int iSq){ rangeCheck(iSq); return arraySquare[iSq].isGreen(); }
  public ArrayList<Integer> getGreenList() { return greenList; }
  
  public boolean mayCastleEast(Piece.Color player){
      boolean mayCastle = true;
      switch (player){
              case WHITE:
                  if (bWhiteKingMoved || bWhiteEastRookMoved ) mayCastle = false;          
                  break;
              case BLACK:
                  if (bBlackKingMoved || bBlackEastRookMoved) mayCastle = false;
                  
      } // END switch
                      
      return mayCastle;
      
  } // END mayCastleEast()
  
  public boolean mayCastleWest(Piece.Color player){
      boolean mayCastle = true;
      switch (player){
              case WHITE:
                  if (bWhiteKingMoved || bWhiteWestRookMoved) mayCastle=false;
                  break;
              case BLACK:
                  if (bBlackKingMoved || bBlackWestRookMoved) mayCastle = false;
      } // END switch
                      
      return mayCastle;
      
  } // END mayCastleWest()
  
  
  // setters
  public void setAppRef(KSpielApp app){ksApp = app;}
  public void setGreen(int iSq, boolean bState){ arraySquare[iSq].setGreen(bState);}

  public void setBoardGreens(boolean bState){
      
      for (int i=1; i<65; i++){
          setGreen(i, bState);
      }
      if (!bState){
          // if clearing all greens
          setRedSquare(0);
          greenList.clear();
          // otherwise leave them alone, it's an all green knight check handover screen
      }
  }


  public ArrayList<Integer> greenKnight(int iSq){
      // returns a (green) list of knight moves from iSq
      rangeCheck(iSq);
      // Create a new phantom Knight
      Knight knight = new Knight(ksApp.archKnight, Piece.Color.GHOST);
      // tell it its location
      knight.Initialise(iSq, this, ksApp);
      
      ArrayList<ArrayList<Integer>> wrappedList = knight.getAllowableMoves();
      
      return wrappedList.get(0); // legal == illuminated for knight, so could use get(0) or (get(1)
  } // end greenKnight()
  
  public void setAspect(int iSq, Square.Aspect a){rangeCheck(iSq); arraySquare[iSq].setAspect(a);}
 
  public ArrayList<ArrayList<Integer>> findLegalMovesFrom(int iSq){
       
      rangeCheck(iSq);
      ArrayList<Integer> listLegalMoves= new ArrayList<Integer>(28);
      ArrayList<Integer> listLegalMovesRevised = new ArrayList<Integer>(28);
      ArrayList<Integer> listIlluminatedMoves = new ArrayList<Integer>(28);
      ArrayList<Integer> listIlluminatedMovesRevised = new ArrayList<Integer>(28); // if a pawn take is in the offing, we need to revise
      ArrayList<ArrayList<Integer>> wrappedList = new ArrayList<ArrayList<Integer>>(56); // container for Illuminated & Legal moves lists; // container for Illuminated & Legal moves lists
      Board testBoard = new Board(ksApp, callLevel+1);
      ArrayList<Integer> testCheckingPieces;
      
      // Find legal moves for piece on iSq
      
      // what side am I on?
          Piece.Color myKingColor = getOccupier(iSq).color();
          
          //get the illuminated and allowable moves
          wrappedList = getOccupier(iSq).getAllowableMoves(); 
          // this returns two lists: unwrap them.
          listIlluminatedMoves = wrappedList.get(0); 
          listLegalMoves = wrappedList.get(1);
          
          // Is the piece a pawn?  If so the illuminated moves may need revising
          boolean isPawn = false;
          if (getSquare(iSq).getOccupier().value == Piece.Value.PAWN)
              isPawn = true;
          
          // Now scan the Legal moves list to eliminate moves into Check or failure to relieve Check
          for (int i=0; i<listLegalMoves.size(); i++){
              
              // Make a test copy of the game board
              testBoard.copyBoardFrom(this);
              
              // try out the movePiece on the test board
              testBoard.makeMove(iSq, listLegalMoves.get(i));
              
              // test the result for checks on my king
              testCheckingPieces = testBoard.getCheckingPiecesOn(myKingColor);

              if (testCheckingPieces.size()<2){
                // position 0 is always King; so size == 1 indicates no pieces checking the king.
                // thus if size <2, we can mark this square as LEGAL
                  arraySquare[listLegalMoves.get(i)].setLegal(true);
                  listLegalMovesRevised.add(listLegalMoves.get(i)); // add it to the revised legal list
                        
                } else 
                
                {
                  // the move may be illegal because the king is in check.  Is the move a pawn try?
                  
                  int colFrom = getX(iSq);
                  int colTo = getX(listLegalMoves.get(i));
                  if (isPawn && colFrom != colTo){
                      // it's a pawn try, so don't illuminate it - remove it illuminated list.
                     int target = listLegalMoves.get(i);
                     int targetindex=-1;
                     for (int j=0; j<listIlluminatedMoves.size(); j++){
                         if (listIlluminatedMoves.get(j)==target) 
                             targetindex = j;
                     }
                     if (targetindex>-1){
                        listIlluminatedMoves.remove(targetindex);
                     }
                     else
                     { System.out.println("couldn't find the pawn try in illuminated list"); }// error!
                  } 
                  else
                  {
                     // it's not a pawn try, so don't remove it from list of Illuminated moves
                  }
               
                    
                }
            
          } // end for loop
          
          
          // rewrap the list including the revised Legal Moves
          wrappedList.clear();
          wrappedList.add(listIlluminatedMoves); 
          wrappedList.add(listLegalMovesRevised);  // if size of list==0, no legal moves avaiable
      
      return wrappedList;   
      
  } // end findLegalMovesFrom()
   
  public ArrayList<ArrayList<Integer>> setSelectedSquare(int iSq){
      
      // set the object's persistent variable
      iSelectedSquare = iSq;
      
      // Selecting the square returns a list of ILLUMINATED moves from the square.
     // ArrayList<Integer> listIlluminatedMoves = new ArrayList<Integer>(28);
      // The piece on the square returns illuminated AND legal moves
      // as two Integer arrays wrapped in single container, to be unpacked here
      ArrayList<ArrayList<Integer>> wrappedList = new ArrayList<ArrayList<Integer>>(); // a container for Illuminated & Legal moves lists; 
      
      if (iSq>0){ // SHOW THE MOVES
          rangeCheck(iSq);
          // Find legal moves for piece on iSq
          wrappedList = findLegalMovesFrom(iSq);
          
          //to unwrap the two move Piece lists
            //  listIlluminatedMoves = wrappedList.get(0);
            //  listLegalMoves = wrappedList.get(1); // which is unused here
          
      } 
      else { // iSq==0, UNSHOW THE MOVES, no square selected
          
          // make sure no squares are marked as legal
          for (int i=1; i<65; i++){
              arraySquare[i].setLegal(false);
          } // END for (i)
      } // END else
      
      return wrappedList;
      //return listIlluminatedMoves;
      
  } // END setSelectedSquare(int iSq)
 

  public void disablePlayers(){
      // just disable all the squaes
      for (int i=1; i<65; i++){
          setAspect(i, Square.Aspect.DISABLED);
      }
      
  }
  public void enablePlayer(Piece.Color colour){
      
      // enable only the squares with an occupier of the right color
      for (int i=1; i<65; i++){
          Piece occ = getOccupier(i);
          
          if (occ.color() == colour){
                setAspect(i,Square.Aspect.ENABLED);
            }  else {
                setAspect(i,Square.Aspect.DISABLED);
            }
          if (colour == Piece.Color.WHITE){
            //  clearGhosts(colour);
              bWhiteToPlay = true;
          }
          else{
            //  clearGhosts(Piece.Color.BLACK);
              bWhiteToPlay = false;
          }
      }
      
  }// END enablePlayer(Piece.Color colour)
  
  private void clearTries(){
      for (int i=1; i<65; i++){
          clearTry(i);
         // arraySquare[i].setPawnTry(false);
      }
  }
  public void setStartingPosition(){
      //Load remaining pieces into starting positions
      // whites
      result = new Result();    // omitting this this might be cause of castle blocking FS090404.0
      
    for (int i=9; i<17; i++){
        arraySquare[i].setOccupier(new Pawn(ksApp.archPawn, Piece.Color.WHITE));
    }
      arraySquare[1].setOccupier(new Rook(ksApp.archRook, Piece.Color.WHITE));
      arraySquare[2].setOccupier(new Knight(ksApp.archKnight, Piece.Color.WHITE));      
      arraySquare[3].setOccupier(new Bishop(ksApp.archBishop, Piece.Color.WHITE));
      arraySquare[4].setOccupier(new Queen(ksApp.archQueen, Piece.Color.WHITE));
      arraySquare[5].setOccupier(new King(ksApp.archKing, Piece.Color.WHITE));
      arraySquare[6].setOccupier(new Bishop(ksApp.archBishop, Piece.Color.WHITE));
      arraySquare[7].setOccupier(new Knight(ksApp.archKnight, Piece.Color.WHITE));
      arraySquare[8].setOccupier(new Rook(ksApp.archRook, Piece.Color.WHITE));
      
      result.iWhiteRemain = 16;
      
      // blacks
    for (int i=49; i<57; i++){
        arraySquare[i].setOccupier(new Pawn(ksApp.archPawn, Piece.Color.BLACK));
    }
      
      arraySquare[57].setOccupier(new Rook(ksApp.archRook, Piece.Color.BLACK));
      arraySquare[58].setOccupier(new Knight(ksApp.archKnight, Piece.Color.BLACK));      
      arraySquare[59].setOccupier(new Bishop(ksApp.archBishop, Piece.Color.BLACK));
      arraySquare[60].setOccupier(new Queen(ksApp.archQueen, Piece.Color.BLACK));
      arraySquare[61].setOccupier(new King(ksApp.archKing, Piece.Color.BLACK));
      arraySquare[62].setOccupier(new Bishop(ksApp.archBishop, Piece.Color.BLACK));
      arraySquare[63].setOccupier(new Knight(ksApp.archKnight, Piece.Color.BLACK));
      arraySquare[64].setOccupier(new Rook(ksApp.archRook, Piece.Color.BLACK));
      
      result.iBlackRemain = 16;
      
      iMovesMade = 0; // reset moves made counter
      
      // ESSENTIAL: pass location & reference to owner Board to complete construction of piece objects
      
      for (int i=1; i<17; i++){arraySquare[i].getOccupier().Initialise(i, this, ksApp);}
      for (int i=49; i<65; i++){arraySquare[i].getOccupier().Initialise(i, this, ksApp);}
      
      for (int i=17; i<49; i++){   
          
          // place an empty "piece" in the square
          arraySquare[i].setOccupier(new Empty(ksApp.archEmpty, Piece.Color.WASH));
          arraySquare[i].getOccupier().Initialise(i, this, ksApp);  // complete the construction of Piece objects

      }
      
      // reset castling flags
      bWhiteKingMoved = false;
      bBlackKingMoved = false;
      bBlackWestRookMoved = false;
      bBlackEastRookMoved = false;
      bWhiteWestRookMoved = false;
      bWhiteEastRookMoved = false;
      
      // reset any testboard pieces
      clearAllTestPieces(Piece.Color.WHITE);
      clearAllTestPieces(Piece.Color.BLACK);

      
      iSelectedSquare=0;
      setBoardGreens(false);
      clearTries();
      clearGhosts(); // modification FS090404.0 don't know if this is relevant, but...
      result.Reset();
      enablePlayer(Piece.Color.WHITE); // show white to play
      disablePlayers(); // wait for choice of game
      
  } // END setStartingPosition()
  
  private void clearGhosts(){
      // An en-passant Ghost is an Empty piece with color GHOST which can be detected by attacking Pawn.
      // Ownership of Ghosts is determined by row position.
      // Method removes the ghosts belonging to that player by changing color from GHOST to WASH
      

              for (int i=17; i<25; i++){
          
                  if (arraySquare[i].getOccupier().color() == Piece.Color.GHOST){
                      arraySquare[i].getOccupier().setColor(Piece.Color.WASH);
                  }

              }
             for (int i=41; i<49; i++){
          
                  if (arraySquare[i].getOccupier().color() == Piece.Color.GHOST){
                      arraySquare[i].getOccupier().setColor(Piece.Color.WASH);
                  }
             } 
      
      
  } // END clearGhosts()
      
  
  // THE FOLLOWING ARE USED IN SEQUENCE IN makeMove()
  
  private void pawnTakesPawnEP(int iFrom, int iTo){
      
      // implement a Pawn x Pawn EP take where attacker moves to iTo (iFrom is unused)
      // Find the pawn to kill - it's in the row behind you!
      
      int iPawnKillLoc;
      if (iTo <25)
         iPawnKillLoc = iTo+8;
      else
         iPawnKillLoc = iTo-8;
      // Put reference to the captured pawn into the result.capture
      result.capture = arraySquare[iPawnKillLoc].getOccupier();
      
      // Remove the pawn & put an empty piece in the square
      arraySquare[iPawnKillLoc].setOccupier(new Empty(ksApp.archEmpty,Piece.Color.WASH));
      // initialise the empty piece
      arraySquare[iPawnKillLoc].getOccupier().Initialise(iPawnKillLoc, this, ksApp);
                    
      setRedSquare(iPawnKillLoc);  // set the Pawn square to red, not the ghost square
      result.iRedisplayButton = iPawnKillLoc;
      
                    
  }// END pawnTakesPawnEP(int iFrom, int iTo)
  
  private void doCastling(int iFrom, int iTo){
            // CASTLING SECTION
      
      // Is this a possible Rook or King making a first movePiece? or a Rook being taken?
      // If so, mark to prohibit further castling
      if (iFrom == 1  || iTo == 1) bWhiteWestRookMoved = true;
      if (iFrom == 8  || iTo == 8) bWhiteEastRookMoved = true;
      if (iFrom == 57 || iTo == 57)bBlackWestRookMoved = true;
      if (iFrom == 64 || iTo == 64)bBlackEastRookMoved = true;
      
      
      if (iFrom == 5) {
          Piece moved = arraySquare[iFrom].getOccupier();     
          // If this is a castling King, we must also movePiece the Rook
          if (moved.value() == Piece.Value.KING && moved.color() == Piece.Color.WHITE) {
              if (iTo == 3){
                  // White King castling, so movePiece Rook
                  movePiece(1, 4);
                  result.iRedisplayButton = 1; //set semaphore for UI
              }
              if (iTo == 7){
                  // White King castling, so movePiece Rook
                  movePiece(8, 6);
                  result.iRedisplayButton = 8; //set semaphore for UI
              }
               
          }
          bWhiteKingMoved = true;   // mark this anyway, whether it is a king moving from king's square or not
      }
      if (iFrom == 61) {
          Piece moved = arraySquare[iFrom].getOccupier();     
          // If this is a castling King, we must also movePiece the Rook
          if (moved.value() == Piece.Value.KING && moved.color() == Piece.Color.BLACK) {
              if (iTo == 59){
                  // Black King castling, so movePiece Rook
                  movePiece(57, 60);
                  result.iRedisplayButton = 57; //set semaphore for UI
              }
              if (iTo == 63){
                  // Black King castling, so movePiece Rook
                  movePiece(64, 62);
                  result.iRedisplayButton = 64; //set semaphore for UI
              }
            
          }
              
          bBlackKingMoved = true;  // mark this anyway, whether it is a king moving from king's square or not
         
      }
  } // END doCastling(int iFrom, int iTo)
  
  
  private void movePiece(int iFrom, int iTo){
      rangeCheck(iFrom); rangeCheck(iTo);
      // movePiece the piece
      arraySquare[iTo].setOccupier(arraySquare[iFrom].getOccupier());
      // Tell it its new position
      arraySquare[iTo].getOccupier().setPosition(iTo);
      // Put an empty piece in the square
      arraySquare[iFrom].setOccupier(new Empty(ksApp.archEmpty,Piece.Color.WASH));
      // initialise the empty piece
      arraySquare[iFrom].getOccupier().Initialise(iFrom, this, ksApp);
      // v3.02 clear any testpiece the player may have set
      arraySquare[iTo].clearTestPiece(toPlay());
      
      
  } // END movePiece(int iFrom, int iTo)
 // private boolean queenPawn(int iFrom, int iTo){
  private void queenPawn(int iFrom, int iTo){    
      // (iFrom is unused)
 //Xv3.02     boolean queened = false;
      result.bQueenedPawn = false;
      // Is this a Pawn that needs Queening?
          if (iTo > 56 ){

            // it's a white pawn, replace it with a White Queen
            arraySquare[iTo].setOccupier(new Queen (ksApp.archQueen, Piece.Color.WHITE));
            arraySquare[iTo].getOccupier().Initialise(iTo, this, ksApp);

  //Xv3.02          queened = true;
            result.bQueenedPawn = true;
            
          }
      
          if (iTo < 9 ){
        
            // it's a black pawn, replace it with a Black Queen
              arraySquare[iTo].setOccupier(new Queen (ksApp.archQueen, Piece.Color.BLACK));
              arraySquare[iTo].getOccupier().Initialise(iTo, this, ksApp);
          
  //Xv3.02            queened = true;
              result.bQueenedPawn = true;
          }
      
 //     return queened;
            
  }// END queenPawn(int ifrom, int iTo)
  
  private void setEnPassantGhost(int iFrom, int iTo){
       // EN-PASSANT SECTION
      //  When a pawn makes a double step opening movePiece, it may be attacked
      //   by another pawn as if it had only attacked a single step opening.
      
      //  This method achieves this effect by placing a GHOST piece on the single step for 1 movePiece only.
      //   The GHOST is detectable by an enemy pawn and can be attacked, removing the real pawn.
          
          if (iFrom < 17 && iTo > 24){
              // It's a two step white opener
              // Place a Ghost on the one step square
            
              arraySquare[iFrom+8].setOccupier(new Empty(ksApp.archEmpty, Piece.Color.GHOST));
              arraySquare[iFrom+8].getOccupier().Initialise(iFrom+8, this, ksApp);
          }
          
          if (iFrom > 48 && iTo < 41){
              // It's a two step black opener
              // Place a Ghost on the one step square
           
              arraySquare[iFrom-8].setOccupier(new Empty(ksApp.archEmpty, Piece.Color.GHOST));
              arraySquare[iFrom-8].getOccupier().Initialise(iFrom-8, this, ksApp);
              
          }
      
  } //END setEnPassantGhost(int iFrom, int iTo)
  
  
  public void clearLegalMoves(){
  
      for (int i=1; i<65; i++){
          arraySquare[i].setLegal(false);
      }
  } // END clearLegalMoves()
  
  // CHECK-DETECTION UTILITY METHODS
  
  protected ArrayList<Integer> getCheckingPiecesOn(Piece.Color kingColor){
      
      // THIS METHOD IS USED BY findLegalMoves() to eliminate legality of
      //        moves into check and moves not relieving check PRIOR to a movePiece
      
      //     AND BY CreateGreenList() to detect a check RESULTING from a movePiece
      
    ArrayList<Integer> listCheckingPieces = new ArrayList<Integer>();
      
   
      Piece.Color attackingColor = getOtherSide(kingColor);

    // First list entry (0) is location of kingColor's King
      // subsequent list entries are locations of checking pieces.
      
      // Find kingColor's King
      int iKing = 1;
      while (iKing !=0
              && !(arraySquare[iKing].getOccupier().value() == Piece.Value.KING 
                   && arraySquare[iKing].getOccupier().color() == kingColor))
          {
              iKing++;
              if (iKing>64) {
                  result.error = Result.ErrorCode.KING_NOT_FOUND;
                  iKing = 0;    // set error signal and an exit condition for loop
              }
            
          }      
      
      listCheckingPieces.add(iKing);
      
      if (iKing>0){ // don't try this under error condition
      // Now test every piece of my color for a legal movePiece to this square
      // and add its location to listCheckingPieces
      listCheckingPieces = scanSquareForCheckBy(iKing, attackingColor, listCheckingPieces);
      }
      if (listCheckingPieces.size()>1){
          switch (kingColor){
              case WHITE:
                  result.bWhiteInCheck = true;
                  break;
              case BLACK:
                  result.bBlackInCheck = true;
          }
      }
      
      return listCheckingPieces;
      
  } // END getCheckingPiecesOn()
 
  
  private boolean isCheckShort(int iKingLoc, int iChecker){  //V3.02
      // method to determine if a diagonal check is on long or short from King's point of view
      boolean bDiag = true; // will return 2 = Long, 1 = short, set short initially
      int iNESW = 0;   // counts diagonal NE-SW
      int iNWSE = 0;   // counts diagonal NW-SE
      int x; int y;
      
      // DO NE-SW
      x = getX(iKingLoc)+1; y = getY(iKingLoc)+1;
      while (x<9 && y<9){ iNESW++; x++; y++; }
      x = getX(iKingLoc)-1; y = getY(iKingLoc)-1;
      while (x>0 && y>0){ iNESW++; x--; y--; }
      
      // DO NW-SE
      x = getX(iKingLoc)+1; y = getY(iKingLoc)-1;
      while (x<9 && y>0){ iNWSE++; x++; y--; }
      x = getX(iKingLoc)-1; y = getY(iKingLoc)+1;
      while (x>0 && y<9){ iNWSE++; x--; y++; }
      
      // determine check direction NESW or NWSE
      //  - flag is set as short; change it if long
      if (getX(iChecker) - getX(iKingLoc) == getY(iChecker) - getY(iKingLoc)){
          // it's NESW
          if (iNESW>iNWSE) bDiag = false;   // set flag as long
      } else {
          // it's NWSE
          if (iNWSE>iNESW) bDiag = false;   // set flag as long
      }
            
      return bDiag;
  }
  
  public ArrayList<Integer> scanSquareForCheckBy(int iKingSquare, Piece.Color attackingColor, 
          ArrayList<Integer> listCheckingPieces){
      // ths method looks for a possible movePiece to iKingSquare by a
      // piece of attackingColor, and adds the location of the attacking piece
      // into the listCheckingPieces passed to it by the call
      
      // THIS METHOD USED BY King() to eliminate legality of castling across check
      //            AND BY GetCheckingPiecesOn() for check testing before and after moves
      
      // container for Illuminated & Legal moves lists
      ArrayList<ArrayList<Integer>> wrappedList1; // = new ArrayList<ArrayList<Integer>>(56);
      ArrayList<Integer> listLegalMoves1; //= new ArrayList<Integer>(28); 
      
      // Now test every piece of attacking color for a legal movePiece to this square
      for(int i=1; i<65; i++){
          if (getOccupier(i).color() == attackingColor){
              // it may be an attacking piece
              // ask the piece its allowable moves, then unwrap the legal movePiece lists
              
              // if the opposing piece is a King, evaluate threat directly
              // or we will invite an infinite loop - king tests king tests king etc etc
              if (getOccupier(i).value() == Piece.Value.KING){
                  if (ksApp.isAdjacent(i,iKingSquare)){
                      listCheckingPieces.add(i);
                  }
              } else {
                  wrappedList1 = getOccupier(i).getAllowableMoves(); 
                  // listIlluminatedMoves = wrappedList.get(0);
                  listLegalMoves1 = wrappedList1.get(1);
                  for (int p=0; p<listLegalMoves1.size(); p++){
                      // Scan the list for a legal movePiece to the King
                      if (listLegalMoves1.get(p) == iKingSquare){
                          // got one, so add the piece's location to the checking-pieces list
                          listCheckingPieces.add(i);
                      } // END if(listLegalMoves...           
                  } //END for p
              } //END else
          } // END if(getOccupier(i).color...         
      } // END for i
      return listCheckingPieces;
  } // end scanSquareForCheckBy()
  
  private void createGreenList (ArrayList<Integer> checkingList) {
  
                // Make greenList
          int iKingLoc = checkingList.get(0);
          int iChecker;
          
          //V3.02
          result.bCheckLong=false; // initialise
          result.bCheckShort=false;
          result.bCheckCol=false;
          result.bCheckRow=false;
          
          for (int p=1; p<checkingList.size(); p++){
          // Identify the direction of check 
              iChecker = checkingList.get(p);

              if (getX(iChecker) == getX(iKingLoc)){ 
                  // NS - add column to greenList
                  result.bCheckCol=true;   // signify File - V3.02
                  for (int y = 1; y<9; y++){ greenList.add(getP(getX(iKingLoc),y));}
                  
              } else if (getY(iChecker) == getY(iKingLoc)){
                  // EW - add row to greenList
                  result.bCheckRow=true;   //signify Rank  - V3.02
                  for (int x = 1; x<9; x++){ greenList.add(getP(x,getY(iKingLoc)));}
                  
              } else if (getX(iChecker) - getX(iKingLoc) == getY(iChecker) - getY(iKingLoc)){
                  // NE-SW - add diagonal to greenList
                  greenList.add(iKingLoc); 
                  
                  // v3.02 is this long or short diagonal
                  if (isCheckShort(iKingLoc,iChecker)) 
                      result.bCheckShort = true;   
                  else
                      result.bCheckLong = true;
                  
                  int x = getX(iKingLoc)+1; int y = getY(iKingLoc)+1;
                  while (x<9 && y<9){ greenList.add(getP(x,y)); x++; y++; }
                  x = getX(iKingLoc)-1; y = getY(iKingLoc)-1;
                  while (x>0 && y>0){ greenList.add(getP(x,y)); x--; y--; }
                                   
              } else if (getX(iChecker) - getX(iKingLoc) == getY(iKingLoc) - getY(iChecker)){
                  // NW-SE
                  greenList.add(iKingLoc);
                  
                  // v3.02 is this long or short diagonal
                  if (isCheckShort(iKingLoc,iChecker)) 
                      result.bCheckShort = true;   
                  else
                      result.bCheckLong = true;
                  
                  int x = getX(iKingLoc)+1; int y = getY(iKingLoc)-1;
                  while (x<9 && y>0){ greenList.add(getP(x,y)); x++; y--; }
                  x = getX(iKingLoc)-1; y = getY(iKingLoc)+1;
                  while (x>0 && y<9){ greenList.add(getP(x,y)); x--; y++; }
                  
                  
                  
              } else {
                  // Check by Knight 
                  // - in Kriegspiel the checked player sees knight moves pivoted on king
                  // but checking player sees knight moves pivoted on knight
                  // while a handover screen just shows all green.
                  // Tell the UI to request the knight check green squares from Board
                  result.iCheckByKnight = iChecker;
                  result.iCheckedKing = iKingLoc;
                  
              }
          } 
          
          // turn on the green display signals in squares
      for (int i=0; i<greenList.size(); i++){
          arraySquare[greenList.get(i)].setGreen(true);
      }
          
  } // END createGreenList()
  
   
  // THE makeMove() METHOD, final result of which is returned by createCheckingList()
  //  createCheckingList() is invoked by UI after invoking makeMove()
  
  public void makeMove(int iFrom, int iTo){
//  public boolean makeMove(int iFrom, int iTo){    
    
 //     boolean queened = false; // modification FS090404.6  Xv3.02
      
      // Make a move - (movePiece will move the piece itself):::::::>>>
      
      result.Reset();  // resets iRedSquare etc
 
      // result values are built up by makeMove()
      // final values of result will be returned by createCheckingList()
      
      rangeCheck(iFrom); rangeCheck(iTo);
      
      // has a piece been attacked? Or just an empty square?
      Piece attacked = arraySquare[iTo].getOccupier();
      // ensure that king is not being taken
      if (attacked.value() == Piece.Value.KING){
          // error!
          result.error = Result.ErrorCode.KING_TAKEN;
      }
      
      Piece moved = arraySquare[iFrom].getOccupier();
      //////
      if (attacked.color() == Piece.Color.GHOST && moved.value() == Piece.Value.PAWN){
         // It's a pawn attacking a ghost:
          //  only a pawn of the right color can react to it
             if ( (moved.color() == Piece.Color.WHITE && iTo > 40) 
                || moved.color() == Piece.Color.BLACK && iTo < 25) {
                   
                    pawnTakesPawnEP(iFrom, iTo);
             } 

      } else {
          // It's not a PAWN attacking a GHOST
          if (attacked.value != Piece.Value.EMPTY){
              // set a reference to captured piece in result
              result.capture = attacked;
              setRedSquare(iTo); // a piece has been taken
              // send attacked to lostpieces
          }
      }
      
    
      doCastling(iFrom, iTo);
 
      Piece movingPiece = arraySquare[iFrom].getOccupier();
      
      // movePiece the piece itself
      movePiece(iFrom, iTo);
      iMovesMade++; // increment the moves counter
      
      //  PAWN SPECIALS
      
      // clear the old en-passant ghosts
      clearGhosts();
      
      if (movingPiece.value() == Piece.Value.PAWN){
         
      // QUEENING SECTION
      //    queened = queenPawn(iFrom, iTo);
          queenPawn(iFrom, iTo);    //V3.02
      // EN-PASSANT SECTION
          setEnPassantGhost(iFrom, iTo);  
      }
      
      // END OF PAWN SPECIALS
      
      // clear the LEGAL MOVES list
      clearLegalMoves();
      
      // change player to play
      bWhiteToPlay = !bWhiteToPlay;
      iSelectedSquare = 0;
      
   //   return queened;   // modification FS090404.6 // V3.02
      
  } // END makeMove(int iFrom, int iTo)
  // The createCheckingList() method - invoked by UI after movePiece has been made
  // and returns the result of the movePiece
//xv3.02  public Result createCheckingList(int iMovingPieceSq) {
  public Result createCheckingList(Piece.Color checkedColor) {    
    //xv3.02  rangeCheck(iMovingPieceSq);
      // Test if opponent is now in check
      ArrayList<Integer> checkingList = new ArrayList<Integer>();
  
 //Xv3.02     Piece movingPiece = arraySquare[iMovingPieceSq].getOccupier(); 
 //xV3.02     Piece.Color checkedColor = getOtherSide(movingPiece.color());
      
      checkingList = getCheckingPiecesOn(checkedColor);
      
      boolean bInCheck = false;
      
      if (checkingList.size()>1) {
          createGreenList(checkingList); // uses checkingList as parameter
          bInCheck = true;
          
          /* Modification FS090404.0
          // disable castling for the checked king
          if (checkedColor == Piece.Color.WHITE)
              bWhiteKingMoved = true;
          else
              bBlackKingMoved = true;
           */ 
      }

      // the position must now be tested for Checkmate and Stalemate
      
      // Does checkedColor player have ANY legal movePiece available?
      //        if not, Checkmate if in check, Stalemate if not in check.
      boolean bAnyLegal = false;
      int i=1; 
      ArrayList<ArrayList<Integer>> wrappedList = new ArrayList<ArrayList<Integer>>(56);
      while (!bAnyLegal && i<65){
          rangeCheck(i);
          // test every piece of checkedColor for a legal movePiece
          if (arraySquare[i].getOccupier().color() == checkedColor){
                wrappedList = findLegalMovesFrom(i);
                // wrapped list contains at (0) illuminated moves list, at (1) legal moves list
                if (wrappedList.get(1).size() > 0){
                    // there's at least one legal movePiece
                    bAnyLegal = true;
                }
          }
            
          i++;
      }
      
      if (!bAnyLegal) {
          if (bInCheck){
              result.bCheckmate = true;
          } else {
              result.bStalemate = true;
          }
      }
      // Check for a draw through neither side having enough pieces to checkmate opponent
      //  Checkmate is impossible if there are only 3 pieces and no Queen, Rook or Pawn
      if (result.iBlackRemain + result.iWhiteRemain < 4) {
          boolean bDraw = true;
          for (int k=1; k<65; k++){
              switch(arraySquare[k].getOccupier().value()){
                  case QUEEN:
                  case PAWN:
                  case ROOK:
                      bDraw = false;
              }
          }
          if (bDraw){
              result.bStalemate = true;
          }
      }
      
      return result;
      
  } // end createCheckingList()

  public ArrayList<Integer> createPawnTriesList(){
      // makes list of squares now vulnerable to pawn attack on next play.
      
      
      ArrayList<ArrayList<Integer>> moveList;
      ArrayList<Integer> legalList;
      ArrayList<Integer> tryFrom = new ArrayList<Integer>();
      ArrayList<Integer> tryTo = new ArrayList<Integer>();
      
      Board tryBoard = new Board(ksApp, callLevel+1); // make a test board
      ArrayList<Integer> testCheckingPieces;
    
      
      for (int i=1; i<65; i++){
            
          // find all the player's pawns
          if (getOccupier(i).value() == Piece.Value.PAWN ){
                  
              if (getOccupier(i).color() == toPlay()){
                //    ksApp.ksUI.sysout(getOccupier(i).color().toString()+" Pawn at "+String.valueOf(i));
                  // Ask the pawn where it can move
                  moveList = getOccupier(i).getAllowableMoves();

                  legalList = moveList.get(1);  // unwrap the legal moves

                  for (int j=0; j<legalList.size(); j++){

                      if (getX(i) != getX(legalList.get(j))){

                          // Screen the move for a pinned pawn
                    //      ksApp.ksUI.sysout("A-"+String.valueOf(i)+"-"
                     //                 + String.valueOf(legalList.get(j))+","
                     //                 +  ksApp.ksUI.boardView.getCoords(legalList.get(j)));
                          
                          // Make a test copy of the game board
                          tryBoard.copyBoardFrom(this);
                          // try out the move on the test board
                          tryBoard.makeMove(i, legalList.get(j));
                          // test the result for checks on my king
                          testCheckingPieces = tryBoard.getCheckingPiecesOn(toPlay());
                          
                          if (testCheckingPieces.size()<2){
                            // position 0 is always King; so size == 1 indicates no pieces checking the king.
                            // thus if size <2, we can mark this square as LEGAL
                              // avoid duplicates: another pawn may already have marked this
                              boolean already=false;
                              int thissq = legalList.get(j);
                              //int lastsq =-1;
                              int size = tryTo.size();
                              if (size>0) {
                                  if (tryTo.contains(thissq))
                                      already = true;
                              }
                              
                              if (!already){
                                  tryFrom.add(i);
                                  tryTo.add(legalList.get(j));
                              //    ksApp.ksUI.sysout("B-"+String.valueOf(i)+"-"
                              //            + String.valueOf(legalList.get(j))+","
                              //            +  ksApp.ksUI.boardView.getCoords(legalList.get(j)));
                              }
                          }

                      }

                  }
                 // ksApp.ksUI.sysout("--");

              }
          }
       
      }
        
      
      // set the try signals in each try square
      for (int i=0; i<tryTo.size(); i++){
          arraySquare[tryTo.get(i)].setPawnTry(true);
      }
              
      
      return tryTo;

      
  }
  
  public void clearTry(int i){
      arraySquare[i].setPawnTry(false);
  }
  
//  ERROR HANDLING ROUTINES
  private void rangeCheck(int iSq){
      // traps out of range square references
      if (iSq<1 || iSq>64){
          result.error = Result.ErrorCode.OUT_OF_RANGE;
          KSpielUI ksUI = ksApp.ksUI;
          ksUI.errorMsg("Square "+String.valueOf(iSq) +" out of range");
      }
  }
 

} // END CBoard Class
