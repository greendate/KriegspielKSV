/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import javax.swing.*;

/**
 *
 * @author Denys
 */


public class Empty extends Piece {

    // Constructors
   public Empty(){ // dummy constructor for piece objects inheriting Piece properties
 
    }
   
   public Empty(ArchEmpty archRef, Color color) {
       myArch = archRef;
      super.setValue(myArch.pieceValue); 
      super.setColor(color);  
      super.name = value.toString().toLowerCase();
    }
  
    @Override
   public Empty copyPieceFrom(Piece master){
        Empty copy = new Empty();
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
      
      ImageIcon img = appRef.archEmpty.getIcon(boardRef, iPosition, color, aspect);
      
      
      return img;
    }
  
  
}
