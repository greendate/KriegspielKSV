/**
 * Kriegspiel Package
 */

package uk.co.kriegspiel;

/**
 *
 * @author Denys Bennett
 */
public class ArchKnight extends Arch{
    
       // Constructor
  public ArchKnight(){
      
    pieceValue = Piece.Value.KNIGHT;
    pieceName = pieceValue.toString().toLowerCase();
   
  }

}
