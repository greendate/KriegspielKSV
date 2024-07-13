/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/**
 *
 * @author Denys
 */
public class ArchRook extends Arch {
    
       // Constructor
  public ArchRook(){
      
    pieceValue = Piece.Value.ROOK;
    pieceName = pieceValue.toString().toLowerCase();
    
  }

}
