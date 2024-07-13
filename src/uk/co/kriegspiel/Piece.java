package uk.co.kriegspiel;
import java.awt.Cursor;
import java.util.*;
import javax.swing.*;

/** kriegspiel Piece class - defines properties and methods of a chess piece
 *
 * @author denysbennett
 */

public class Piece {
    public enum Value { EMPTY, PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING, GHOST }

    public enum Color { WHITE, BLACK, WASH, GHOST }    
    // Note that EMPTY squares have piece color WASH. 
    //GHOST is the ghost of a pawn of either color taken en-passant
    protected int iPosition;
    protected Value value;
    protected Color color;
    protected String name;
    protected Board boardRef;
    protected KSpielApp appRef;
    protected Arch myArch;
   
  //  protected ImageIcon imgError;
    
    /*********************************
     * NOTE:  SetBoardRef() MUST be called on the piece 
     * by the creating object to complete construction of a piece object!
     ******************************* */
    
    // Constructors
  
    public Piece(){ // dummy constructor for piece objects inheriting Piece properties
   
    }
    
    public Piece(Arch arch, Color color) {
        myArch = arch;
        this.value = myArch.pieceValue;
        this.color = color;
       
        name = value.toString().toLowerCase();
    }

      public Cursor getCursor(){
        Cursor cursor=myArch.getCursor(color);
        return cursor;
    }

    public Piece copyPieceFrom(Piece master){
        Piece copy = new Piece();
        copy.myArch = master.myArch;
        copy.value = master.value;
        copy.iPosition = master.iPosition;
        copy.color = master.color; 
        copy.boardRef= master.boardRef;
        copy.appRef = master.appRef;
        copy.name = master.name;
        
        return copy;
    }
    
// setters
    public void setPosition(int iPos){iPosition = iPos;}
    public void setColor(Color c){color = c; }
    public void setValue(Value v){value = v;}
    public void setBoardRef(Board ref){ boardRef = ref; }
    public void Initialise(int iPos, Board ref, KSpielApp ksappRef){
        setPosition(iPos);
        setBoardRef(ref); // essential for pieces which refer back
        appRef = ksappRef;
       
    }
   
     // Override needed in Arch<piecetype>s of kind
    public ImageIcon getIcon(Square.Aspect aspect){
      
      ImageIcon img = myArch.getIcon(boardRef, iPosition, color, aspect);
      
      return img;
    }
  
    
    public int getPosition(){return iPosition;}
    
    public int getY(int p){
      int y = 1+(p-1)/8;
      return y;
    }
    public int getX(int p){
      int x = p % 8;
      if (x == 0){ x=8;}
      return x;
    }
    // return the position as an Integer object for containing in an ArrayList
    public Integer getP(int x, int y){
        return x+(y-1)*8;
    }
    
    public Value value() { return value; }
    public Color color() { return color; }
    public String getColor() { return color.toString().toLowerCase();}
    public String getC() {return getColor().substring(0, 1);}
    public String getName() {
        return name;
    }
    
    public ArrayList<ArrayList<Integer>> getAllowableMoves(){ //method overridden by specific pieces
        //  Method to identify squares to which the piece may move:-
       // purpose: to illuminate squares to which this piece might apparently move, and
       //         : to identify the subset of those moves which are legal.
       //   Two lists are created, then wrapped in an Array to return to
       //   the calling Board object which unwraps and stores the result
       //   for interrogation by the players e.g. via the UI.
        
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        
        return list;
    }
    
    protected ArrayList<ArrayList<Integer>> wrapLists(ArrayList<Integer> moves, ArrayList<Integer> legal){
        ArrayList<ArrayList<Integer>> wrappedList = new ArrayList<ArrayList<Integer>>();
        wrappedList.add(moves);
        wrappedList.add(legal);
        return wrappedList;
    }
  
    public ArrayList<Integer> getSupported(int iFrom){ // method overriden by specific piece
      // Method used exclusively by Robot to evaluate positional strength
      // Returns a list of all squares which the piece believes it might 
      // attack on its next move, were it from square iFrom, 
      // or from its current square if iFrom == 0;
      
      ArrayList<Integer> list = new ArrayList<Integer>();
      
      return list;
    }
}
