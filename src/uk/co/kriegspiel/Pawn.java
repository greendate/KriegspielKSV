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


public class Pawn extends Piece {
   
       
    // Constructors
    public Pawn(ArchPawn archRef, Color color) {
        myArch = archRef;
      super.setValue(myArch.pieceValue);    
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
    }
    
    public Pawn(){
        
    }

    
    @Override
    public Pawn copyPieceFrom(Piece master){
        Pawn copy = new Pawn();
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
      
      ImageIcon img  = appRef.archPawn.getIcon(boardRef, iPosition, color, aspect);
      
      return img;
    }
    
    
  @Override
    public ArrayList<ArrayList<Integer>> getAllowableMoves(){
      // method to illuminate squares to which this piece might move.
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
        // What colour pawn am I?
        switch(color){
            case WHITE:
                // regular forward move
                stillLegal = true; scanx = myx; scany = myy+1;
                if (myy < 8 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){ 
                    listIlluminate.add(getP(scanx, scany)); 
                    if (boardRef.getOccupier(getP(scanx,scany)).value() == Piece.Value.EMPTY){
                        listLegal.add(getP(scanx, scany));
                        
                       // listIlluminate.add(getP(scanx, scany)); //V3.02
                    }
                    else {
                        stillLegal = false;
                    }
                } 
                else {stillLegal = false;
                }
                
                // opening move
                scany++;
                if (myy == 2 && (boardRef.getOccupier(getP(scanx,scany))).color() != color){
                    listIlluminate.add(getP(scanx,scany));

                    if (stillLegal && boardRef.getOccupier(getP(scanx,scany)).value() == Piece.Value.EMPTY){  
                        listLegal.add(getP(scanx,scany));
                       // listIlluminate.add(getP(scanx,scany));  //V3.02
                    }
                    
                }
              
                // NW diagonal take
                scanx = myx-1; scany = myy+1;
                if (myy < 8 && myx > 1 && (boardRef.getOccupier(getP(scanx,scany)).color() != color))
                {
                    // there is no piece of mine on a diagonal square
                
                    // find the square reference and ask it if it is a try
                   // if (boardRef.getSquare(getP(scanx,scany)).isTry())
                        
                    
                     if(boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY
                             || (boardRef.getOccupier(getP(scanx,scany)).color() == Piece.Color.GHOST) && scany == 6 )
                     
                     {
                         listLegal.add(getP(scanx,scany));
                         listIlluminate.add(getP(scanx,scany)); // V3.02
                     }
                   
                }
              
                // NE diagonal take
                scanx = myx+1; scany = myy+1;
                if (myy < 8 && myx < 8 && (boardRef.getOccupier(getP(scanx,scany)).color() != color))
                {
                     // find the square reference and ask it if it is a try
                   // if (boardRef.getSquare(getP(scanx,scany)).isTry())
                        
                      
                
                      if(boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY
                              || (boardRef.getOccupier(getP(scanx,scany)).color() == Piece.Color.GHOST) && scany == 6 )
                      
                      {
                          listLegal.add(getP(scanx,scany));
                          listIlluminate.add(getP(scanx,scany));  //V3.02
                      }
                  
                }
     
                break;
                
            case BLACK:
                // regular forward move
                stillLegal = true; scanx = myx; scany = myy-1;
                if (myy > 1 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
                    listIlluminate.add(getP(scanx,scany));
                    
                    if ( boardRef.getOccupier(getP(scanx,scany)).value() == Piece.Value.EMPTY){
                        listLegal.add(getP(scanx,scany));
                        
                     //   listIlluminate.add(getP(scanx,scany));  // V3.02
                    }
                    else {
                        stillLegal = false;
                    }
                } else {
                    stillLegal = false;
                }
                
                // opening move
                scany--;
                if (myy == 7 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
                    
                     listIlluminate.add(getP(scanx,scany));  
                     if (stillLegal && boardRef.getOccupier(getP(scanx,scany)).value() == Piece.Value.EMPTY){
                        listLegal.add(getP(scanx,scany));
                     //   listIlluminate.add(getP(scanx,scany)); //V3.02
                     }
                }
                // SW diagonal take
                scanx = myx-1; scany = myy-1;
                if (myy > 1 && myx > 1 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
                    
                    // find the square reference and ask it if it is a try
                    //if (boardRef.getSquare(getP(scanx,scany)).isTry())
                        
                   
                
                     if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY
                             || (boardRef.getOccupier(getP(scanx,scany)).color() == Piece.Color.GHOST) && scany == 3  )
                     
                     {
                         listLegal.add(getP(scanx,scany));
                         listIlluminate.add(getP(scanx,scany));  //V3.02
                     }
                   
                }
                // SE diagonal take
                scanx = myx+1;  scany = myy-1;
                if (myy > 1 && myx < 8 && (boardRef.getOccupier(getP(scanx,scany)).color() != color)){
                    
                    // find the square refeence and ask it if it is a try
                   // if (boardRef.getSquare(getP(scanx,scany)).isTry())
                        
                    
 
                    if (boardRef.getOccupier(getP(scanx,scany)).value() != Piece.Value.EMPTY
                            || (boardRef.getOccupier(getP(scanx,scany)).color() == Piece.Color.GHOST)&& scany == 3  )
                    
                    {
                        listLegal.add(getP(scanx,scany));
                        listIlluminate.add(getP(scanx,scany)); //V3.02
                    }
                        
                }
                
        }
        
        wrappedList = wrapLists(listIlluminate, listLegal);
        return wrappedList;
    }  
  
  @Override
  public ArrayList<Integer> getSupported(int iFrom){
    ArrayList<Integer> supported = new ArrayList<Integer>();
    // The Pawn may support any square on which it could make a diagonal take.
    // Locate the piece
        int myp;
        if (iFrom ==0) myp = getPosition(); else myp = iFrom;
        int myx = getX(myp);
        int myy = getY(myp);
        
        int scanx; int scany;
        
        switch(color){
          case WHITE:
            
            // NW diagonal take
              scanx = myx-1; scany = myy+1;
              if (myy < 8 && myx > 1)
                supported.add(getP(scanx,scany));
              
            // NE diagonal take
              scanx = myx+1; scany = myy+1;
              if (myy < 8 && myx < 8)
                supported.add(getP(scanx,scany));
            
              break;
              
          case BLACK:
            
            // SW diagonal take
                scanx = myx-1; scany = myy-1;
                if (myy > 1 && myx > 1)
                  supported.add(getP(scanx,scany));
            // SE diagonal take
                scanx = myx+1;  scany = myy-1;
                if (myy > 1 && myx < 8)   
                  supported.add(getP(scanx,scany));
            break;
        }
        
    return supported;
    
  }
 
  

}
