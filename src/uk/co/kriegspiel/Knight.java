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

public class Knight extends Piece {
   
     // Constructors
    public Knight(ArchKnight archRef, Color color) {
        myArch = archRef;
      super.setValue(myArch.pieceValue);   
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
    }
    
    public Knight(){
        
    }
    
    @Override
    public Knight copyPieceFrom(Piece master){
        Knight copy = new Knight();
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
      
      ImageIcon img  = appRef.archKnight.getIcon(boardRef, iPosition, color, aspect);
      
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
        
        //WNW
        scanx = myx-2; scany = myy+1;
        if (scanx>0 && scany<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //NNW
        scanx = myx-1; scany = myy+2;
        if (scanx>0 && scany<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //NNE
        scanx = myx+1; scany = myy+2;
        if (scanx<9 && scany<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //ENE
        scanx = myx+2; scany = myy+1;
        if (scanx<9 && scany<9 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //ESE
        scanx = myx+2; scany = myy-1;
        if (scanx<9 && scany>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //SSE
        scanx = myx+1; scany = myy-2;
        if (scanx<9 && scany>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //SSW
        scanx = myx-1; scany = myy-2;
        if (scanx>0 && scany>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        //WSW
        scanx = myx-2; scany = myy-1;
        if (scanx>0 && scany>0 && (boardRef.getOccupier(getP(scanx, scany)).color()  != color )){
            listIlluminate.add(getP(scanx, scany));
            listLegal.add(getP(scanx, scany));
        }
        
        
        wrappedList = wrapLists(listIlluminate, listLegal);
        return wrappedList;
    }
     
    @Override
    public ArrayList<Integer> getSupported(int iFrom){
      
      // The Knight may support any square to which it may move.
      
      ArrayList<Integer> listLegal = new ArrayList<Integer>();

        // Locate the piece
        int myp;
        if (iFrom ==0) myp = getPosition(); else myp = iFrom;
        int myx = getX(myp);
        int myy = getY(myp);
        int scanx;
        int scany;
        
        //WNW
        scanx = myx-2; scany = myy+1;
        if (scanx>0 && scany<9 ){
            listLegal.add(getP(scanx, scany));
        }
        //NNW
        scanx = myx-1; scany = myy+2;
        if (scanx>0 && scany<9){
            listLegal.add(getP(scanx, scany));
        }
        //NNE
        scanx = myx+1; scany = myy+2;
        if (scanx<9 && scany<9 ){
            listLegal.add(getP(scanx, scany));
        }
        //ENE
        scanx = myx+2; scany = myy+1;
        if (scanx<9 && scany<9){
            listLegal.add(getP(scanx, scany));
        }
        //ESE
        scanx = myx+2; scany = myy-1;
        if (scanx<9 && scany>0){
            listLegal.add(getP(scanx, scany));
        }
        //SSE
        scanx = myx+1; scany = myy-2;
        if (scanx<9 && scany>0){
            listLegal.add(getP(scanx, scany));
        }
        //SSW
        scanx = myx-1; scany = myy-2;
        if (scanx>0 && scany>0){
            listLegal.add(getP(scanx, scany));
        }
        //WSW
        scanx = myx-2; scany = myy-1;
        if (scanx>0 && scany>0){
            listLegal.add(getP(scanx, scany));
        }

      return listLegal;
    }

}
