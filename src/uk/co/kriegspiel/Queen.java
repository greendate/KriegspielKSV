/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author denysbennett
 */

public class Queen extends Piece {
 
      // Constructors
    public Queen (ArchQueen archRef, Color color) {
        myArch = archRef;
      super.setValue(myArch.pieceValue);    
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
    }
    
    public Queen(){
        
    }
    
    
    
    @Override
    public Queen copyPieceFrom(Piece master){
        Queen copy = new Queen();
        copy.myArch = master.myArch;
        copy.value = master.value;
        copy.iPosition = master.iPosition;
        copy.color = master.color; 
      //  copy.boardRef= master.boardRef;
        copy.appRef = master.appRef;
        copy.name = master.name;
        
        return copy;
    }
    @Override
    public ImageIcon getIcon(Square.Aspect aspect){
      
      ImageIcon img = appRef.archQueen.getIcon(boardRef, iPosition, color, aspect);
      
      return img;
    }
    
    @Override
    public ArrayList<ArrayList<Integer>> getAllowableMoves(){
      // method to illuminate squares to which this piece might move.
        ArrayList<Integer> listIlluminate = new ArrayList<Integer>();
        ArrayList<Integer>listLegal = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>>wrappedList;
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
        scanx = myx-1; scany = myy; stillLegal = true;
        while (scanx>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx--;
        }
        // NW
        scanx = myx-1;
        scany = myy+1;
        stillLegal = true;
        while (scanx>0 && scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx--; scany++;
        }
        //NE
        scanx = myx+1;
        scany = myy+1;
        stillLegal = true;
        while (scanx<9 && scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx++; scany++;
        }
        //SE
        scanx = myx+1;
        scany = myy-1;
        stillLegal = true;
        while (scanx<9 && scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx++; scany--;
        }
        //SW
        scanx = myx-1;
        scany = myy-1;
        stillLegal = true;
        while (scanx>0 && scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx,scany));
            if (stillLegal) listLegal.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { stillLegal = false;}
            scanx--; scany--;
        }
        wrappedList = wrapLists(listIlluminate, listLegal);
        return wrappedList;
    }
     
  
    @Override
    public ArrayList<Integer> getSupported(int iFrom){
      ArrayList<Integer> supported = new ArrayList<Integer>();

      // The Queen "believes" it may support any square to which it may move, 
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
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scany++;
        }
        //S
        scanx = myx; scany = myy-1; stillLegal = true;
        while (scany>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scany--;
        }
        //E
        scanx = myx+1; scany = myy; stillLegal = true;
        while (scanx<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx++;
        }
        //W
        scanx = myx-1; scany = myy; stillLegal = true;
        while (scanx>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx--;
        }
        // NW
        scanx = myx-1;
        scany = myy+1;
        stillLegal = true;
        while (scanx>0 && scany<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx--; scany++;
        }
        //NE
        scanx = myx+1;
        scany = myy+1;
        stillLegal = true;
        while (scanx<9 && scany<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx++; scany++;
        }
        //SE
        scanx = myx+1;
        scany = myy-1;
        stillLegal = true;
        while (scanx<9 && scany>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx++; scany--;
        }
        //SW
        scanx = myx-1;
        scany = myy-1;
        stillLegal = true;
        while (scanx>0 && scany>0 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) { 
              stillLegal = false; // turn off scan once piece own own color encountered
            }
            scanx--; scany--;
        }
      
      return supported;
    }

}
