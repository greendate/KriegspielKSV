/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/**
 *
 * @author Denys
 */
public class ArchQueen extends Arch{
    
       // Constructor
  public ArchQueen(){
      
    pieceValue = Piece.Value.QUEEN;
    pieceName = pieceValue.toString().toLowerCase();
    
  }

}
