
package uk.co.kriegspiel;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author denysbennett
 */


public class Rook extends Piece {
 
      
    // Constructors
    public Rook(ArchRook archRef, Color color) {
        myArch = archRef;
      super.setValue(myArch.pieceValue);    
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
      
    }

    public Rook(){
        
    }
    
    public Rook copyPieceFrom(Piece master){
        Rook copy = new Rook();
        copy.myArch = master.myArch;
        copy.value = master.value;
        copy.iPosition = master.iPosition;
        copy.color = master.color; 
       // copy.boardRef= master.boardRef;
        copy.appRef = master.appRef;
        copy.name = master.name;
        
        return copy;
    }
    
    @Override
    public ImageIcon getIcon(Square.Aspect aspect){
      
      ImageIcon img  = appRef.archRook.getIcon(boardRef, iPosition, color, aspect);
      
      return img;
    }
    
   @Override
    public ArrayList<ArrayList<Integer>> getAllowableMoves(){
      //  Method to identify squares to which the piece may move:-
       // purpose: to illuminate squares to which this piece might apparently move, and
       //         : to identify the subset of those moves which are legal.
       //   Two lists are created, then wrapped in an Array to return to
       //   the calling Board object which unwraps and stores the result
       //   for interrogation by the players e.g. via the UI.
       
        ArrayList<Integer> listIlluminate = new ArrayList<Integer>();
        ArrayList<Integer> listLegal = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> wrappedList;
        // Locate the piece
        int myp = getPosition();
        int myx = getX(myp);
        int myy = getY(myp);
        int scanx;
        int scany;
        boolean stillLegal;
        // Scan in each direction until I hit my own piece or the edge of board
        //N
        scanx = myx; scany = myy+1; stillLegal = true;
        while (scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scany++;
        }
        //S
        scanx = myx; scany = myy-1; stillLegal = true;
        while (scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color()  != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scany--;
        }
        //E
        scanx = myx+1; scany = myy; stillLegal = true;
        while (scanx<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx++;
        }
        //W
        scanx = myx-1; scany = myy; stillLegal=true;
        while (scanx>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx--;
        }
        wrappedList = wrapLists(listIlluminate, listLegal);
        return wrappedList;
    }
    
  
   @Override
    public ArrayList<Integer> getSupported(int iFrom){
      ArrayList<Integer> supported = new ArrayList<Integer>();
      // The Rook "believes" it may support any square to which it may move, 
      // always provided there is not an intervening piece of its own colour.
      // (In Kriegspiel it cannot "see" the opposing pieces).
       // Locate the piece
        int myp;
        if (iFrom ==0) myp = getPosition(); else myp = iFrom;
        int myx = getX(myp);
        int myy = getY(myp);
        int scanx;
        int scany;
        boolean stillLegal;
        
         // Scan in each direction until I hit my own piece or the edge of board
        //N
        scanx = myx; scany = myy+1; stillLegal = true;
        while (scany<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once one of my pieces found
            }
            scany++;
        }
        //S
        scanx = myx; scany = myy-1; stillLegal = true;
        while (scany>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false;
            }
            scany--;
        }
        //E
        scanx = myx+1; scany = myy; stillLegal = true;
        while (scanx<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false;
            }
            scanx++;
        }
        //W
        scanx = myx-1; scany = myy; stillLegal=true;
        while (scanx>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false;
            }
            scanx--;
        }

      return supported;
    }

}


