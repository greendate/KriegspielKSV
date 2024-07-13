/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import java.util.*;
import javax.swing.*;


/**
 * Instantiate this object to create a Bishop piece
 * @author denysbennett
 */

public class Bishop extends Piece {
 
     
    // Constructors
    public Bishop(ArchBishop archRef, Color color) {
        myArch = archRef;
        
      super.setValue(myArch.pieceValue);   
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
    }
    
    public Bishop(){
        
    }
    
    @Override
    public Bishop copyPieceFrom(Piece master){
        Bishop copy = new Bishop();
        copy.myArch = master.myArch;
        copy.value = master.value;
        copy.iPosition = master.iPosition;
        copy.color = master.color; 
        //copy.boardRef= master.boardRef;
        copy.appRef = master.appRef;
        copy.name = master.name;
        
        return copy;
    }
    
    @Override
    public ImageIcon getIcon(Square.Aspect aspect){
      
      
      ImageIcon img = myArch.getIcon(boardRef, iPosition, color, aspect);
      
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
      
      // The Bishop "believes" it may support any square to which it may move, 
      // always provided there is not an intervening piece of its own colour.
      // (In Kriegspiel it cannot "see" the opposing pieces).
      
      ArrayList<Integer> supported = new ArrayList<Integer>();

       // Locate the piece
        int myp;
        if (iFrom ==0) myp = getPosition(); else myp = iFrom;
        int myx = getX(myp);
        int myy = getY(myp);
        int scanx;
        int scany;
        boolean stillLegal;
        
        // Scan in each direction until I hit my own piece or the edge of board
        // NW
        scanx = myx-1;
        scany = myy+1;
        stillLegal = true;
        while (scanx>0 && scany<9 && stillLegal){
            supported.add(getP(scanx,scany));
            if (boardRef.getOccupier(getP(scanx,scany)).color() == color) {
              stillLegal = false; // turn off scan once piece of own color encountered
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
              stillLegal = false; // turn off scan once piece of own color encountered
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
              stillLegal = false; // turn off scan once piece of own color encountered
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
              stillLegal = false; // turn off scan once piece of own color encountered
            }
            scanx--; scany--;
        }
        
      return supported;
    }
   

}
