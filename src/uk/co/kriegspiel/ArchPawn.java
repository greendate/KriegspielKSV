/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/**
 *
 * @author Denys
 */
public class ArchPawn extends Arch{
    
       // Constructor
  public ArchPawn(){
      
    pieceValue = Piece.Value.PAWN;
    pieceName = pieceValue.toString().toLowerCase();
    
  }

}
