
package uk.co.kriegspiel;

/**  The Archetype Bishop Class
 * - defines the generalised behaviour (moves) of a Bishop
 * - see Arch Class
 * 
 * @author Denys Bennett
 */
public class ArchBishop extends Arch{

       // Constructor
  public ArchBishop(){
      
    pieceValue = Piece.Value.BISHOP;
    pieceName = pieceValue.toString().toLowerCase();
    
  }
    
}
