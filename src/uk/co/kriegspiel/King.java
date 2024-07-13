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

public class King extends Piece {
 
     // Constructors
    public King (ArchKing archRef, Color color) {
        myArch = archRef;
        super.setValue(myArch.pieceValue); 
        super.setColor(color);  
        super.name = value.toString().toLowerCase();
    }
    
    public King(){
        
    }
    
    @Override
    public King copyPieceFrom(Piece master){
        King copy = new King();
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


      ImageIcon img = appRef.archKing.getIcon(boardRef, iPosition, color, aspect);
      
      return img;
    }
    
    // For EMPTY and KING only
 
    

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
        int scanx; int scany; 
        boolean stillLegal; // used for castling scan;
       
        
        // North
        scanx = myx; scany = myy+1;
        if (scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }

        // East & Castling
        scanx = myx+1; scany = myy; stillLegal = true;
        if (scanx<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            // East sq is not occupied by one of my pieces, so we can illuminate it
            listIlluminate.add(getP(scanx,scany));
            listLegal.add(getP(scanx,scany)); // the king can move or take an opponent here
            // (unless this would put king in check, which will be tested by Board later)
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) { 
                stillLegal = false; // the square is occupied, so no East castling possible
            }
            
            // Castling East
            if (boardRef.mayCastleEast(color) && stillLegal){
                
                scanx++;
                if (boardRef.getOccupier(getP(scanx, scany)).color()  != color ){
                    listIlluminate.add(getP(scanx,scany));
                    
                    // Test here to see if this would castle across check
                    ArrayList<Integer> checkList = new ArrayList<Integer>();
                    switch (color){
                        case WHITE:
                            // is square no 6 in check by Black?
                            checkList = boardRef.scanSquareForCheckBy(6, Piece.Color.BLACK, checkList);
                            break;
                        case BLACK:
                            // is square no 62 in check by White?
                            checkList = boardRef.scanSquareForCheckBy(62, Piece.Color.WHITE, checkList);
                    }
                    if (checkList.size()>0) stillLegal = false;
                    
                    //Mark as square as Legal if ok
    
                    if(stillLegal 
                            && boardRef.getOccupier(getP(scanx,scany)).value()==Piece.Value.EMPTY
                            ) 
                        listLegal.add(getP(scanx,scany));
                }
            }
        }
        
        // South
        scanx = myx; scany = myy-1;
        if (scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }
     
        // West & Castling
        scanx = myx-1; scany = myy; stillLegal = true;
        if (scanx>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            // West sq is not occupied by one of my pieces, so we can illuminate it
            listIlluminate.add(getP(scanx,scany));
            listLegal.add(getP(scanx,scany));  // the king can move or take an opponent here
            // (unless this would put king in check, which will be tested by Board later)
            
            if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY) {
                stillLegal = false; // the square is occupied, so no West castling possible
            }
            
            // Castling West
            if (boardRef.mayCastleWest(color) && stillLegal){
                
                scanx--;
                if (boardRef.getOccupier(getP(scanx, scany)).color()  != color
                        && boardRef.getOccupier(getP(scanx-1,scany)).color() != color) {
                    listIlluminate.add(getP(scanx,scany));
                
                    // Test here to see if this would castle across check
                    ArrayList<Integer> checkList = new ArrayList<Integer>();
                    switch (color){
                        case WHITE:
                            // is square no 4 in check by Black?
                            checkList = boardRef.scanSquareForCheckBy(4, Piece.Color.BLACK, checkList);
                            break;
                        case BLACK:
                            // is square no 60 in check by White?
                            checkList = boardRef.scanSquareForCheckBy(60, Piece.Color.WHITE, checkList);
                    }
                    if (checkList.size()>0) stillLegal = false;
                    
                    //Mark as square as Legal if ok
                
                    if(stillLegal 
                            && boardRef.getOccupier(getP(scanx,scany)).value()==Piece.Value.EMPTY
                            && boardRef.getOccupier(getP(scanx-1,scany)).value()== Piece.Value.EMPTY) 
                        listLegal.add(getP(scanx,scany));
                }
            }
        }
       
        
        // North West
        scanx = myx-1; scany = myy+1;
        if (scanx>0 && scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }
        // North East
        scanx = myx+1; scany = myy+1;
        if (scanx<9 && scany<9 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }
        // South East
        scanx = myx+1; scany = myy-1;
        if (scanx<9 && scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }
        // South West
        scanx = myx-1; scany = myy-1;
        if (scanx>0 && scany>0 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx,scany));     
        }
        // Castle East
        
        // Castle West
                
        wrappedList = wrapLists(listIlluminate, listLegal);
        return wrappedList;
    }
     
    @Override
    public ArrayList<Integer> getSupported(int iFrom){
      ArrayList<Integer> supported = new ArrayList<Integer>();

      // Locate the piece
        int myp;
        if (iFrom ==0) myp = getPosition(); else myp = iFrom;
        int myx = getX(myp);
        int myy = getY(myp);
        int scanx; int scany;
      
      // The King may support any adjacent piece / square
      // subject to not putting himself in check.
      // However, in Kriegspiel the King does not "know" if the move
      // would put him in check, so must assume the move would be ok.
      // The King may not take whilst castling, so cannot support via a castling move.
      
      // North
      scanx = myx; scany = myy+1;
      if (scany<9)
        supported.add(getP(scanx,scany)); 
      
      // East
      scanx = myx+1; scany = myy;
      if (scanx<9)
        supported.add(getP(scanx,scany));
      
      // South
      scanx = myx; scany = myy-1;
      if (scany>0)
        supported.add(getP(scanx,scany));
      
      // West
      scanx = myx-1; scany = myy;
      if (scanx>0)
        supported.add(getP(scanx,scany));
      
      // North West
      scanx = myx-1; scany = myy+1;
      if (scanx>0 && scany<9)
        supported.add(getP(scanx,scany));
        
      // North East
      scanx = myx+1; scany = myy+1;
      if (scanx<9 && scany<9)
        supported.add(getP(scanx,scany));
        
      // South East
      scanx = myx+1; scany = myy-1;
      if (scanx<9 && scany>0)
        supported.add(getP(scanx,scany));
        
      // South West
      scanx = myx-1; scany = myy-1;
      if (scanx>0 && scany>0)
        supported.add(getP(scanx,scany));
      
      return supported;
    }
   

}
