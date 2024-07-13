package uk.co.kriegspiel;

/**  Kriegspiel Square class - defines properties and methods of a chess square
 *
 * @author denysbennett
 */
public class Square {
  
   private Piece Occupier; 
   private Integer iPosition;
   private Piece Empty;   // allows square to return underlying EMPTY for invisibility
   private Board boardRef;
   private KSpielApp ksApp;
   //v3.02 - these data items for test board views
   private Piece blackPlayersTest; // imagined white occupier as seen by black
   private Piece whitePlayersTest; // imagined black occupier as seen by white
   
  // Button Aspect settings
   public enum Aspect {DISABLED, ENABLED, ILLUMINATED, SELECTED}; 
   private Aspect aspect;
   
   private boolean bVisible;
   private boolean bLegal;
   private boolean bGreen; // display check direction marker
   private boolean bPawnTry; // display pawn try target - mark by attacker's color! xxx
   
   // setters
   public void setBoardRef(Board ref){ boardRef = ref;}
   public void setOccupier(Piece squareOcc){
     Occupier = squareOcc;
     Occupier.setPosition(iPosition);
   }
   
   public void setTestPiece(int pieceIndex, Piece.Color pieceColor){ // v3.02
       // this allows player to postulate opponents test pieces which
       // show on his board view but
       // may not in fact exist
       Piece test = null;
       if (pieceIndex>0){
           switch (pieceIndex){
               case 0: // empty - this statement not reachable
                   break;
               case 1: // Pawn
                   test = new Pawn(ksApp.archPawn, pieceColor);
                   break;
               case 2: // King
                   test = new King(ksApp.archKing, pieceColor);
                   break;
               case 3: // Queen
                   test = new Queen(ksApp.archQueen, pieceColor);
                   break;
               case 4: // Bishop
                   test = new Bishop(ksApp.archBishop, pieceColor);
                   break;
               case 5: // Knight
                   test = new Knight(ksApp.archKnight, pieceColor);
                   break;
               case 6: // Rook
                   test = new Rook(ksApp.archRook, pieceColor);
                   break;
               case 7: // clear all - this statement not reachable
                   break;
           }

           test.Initialise(iPosition, boardRef, ksApp);

           if (pieceColor == Piece.Color.WHITE){
               blackPlayersTest = test;    // black refers to player's view, i.e White opponent
           } else {
               whitePlayersTest = test;
           }
       }
   }

   public Piece getTestPiece(Piece.Color playersColor){
       if (playersColor == Piece.Color.BLACK)
           return blackPlayersTest;
       else
           return whitePlayersTest;
   }
   public void clearTestPiece(Piece.Color playersColor){
         // reset test square to empty by calling method with test = null;
       if (playersColor == Piece.Color.BLACK){
           blackPlayersTest = null;    // black refers to player's view, i.e. a white piece
       } else {
           whitePlayersTest = null;
       }
   }
   
   public void setAspect(Aspect a){aspect = a;}
//   public void setSelected(boolean sel){bSelected = sel;}
   public void setVisible(boolean visible){bVisible = visible;}

   public void setLegal(boolean legal){bLegal = legal;}
   public void setGreen(boolean green){bGreen = green;}
   public void setPawnTry(boolean pawnTry){bPawnTry = pawnTry;}
   
   // getters
   public Piece getOccupier(){
  //     int x=1;
       return Occupier;
   }
   public Aspect getAspect(){return aspect;}
   public boolean isVisible(){return bVisible;}
   public Piece getEmpty(){
       return Empty; 
   }
   public boolean isLegal(){return bLegal;}
   public Integer getPosition(){return iPosition;}
   public boolean isGreen(){return bGreen;}
   public boolean isTry(){return bPawnTry;}
   // Constructor
   Square(Integer iPos, Board ref, KSpielApp app){
       iPosition = iPos;
       boardRef = ref;
       ksApp = app;
       
       bVisible = true;
       bLegal = false;
       bGreen = false;
       aspect = Aspect.DISABLED;
   
       // make an invisible piece to return if bVisible is false
       Empty = new Empty(ksApp.archEmpty, Piece.Color.WASH);
       Empty.Initialise(iPosition, boardRef, ksApp);
      
   }
}
