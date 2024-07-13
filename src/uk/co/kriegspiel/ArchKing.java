/** Kriegspiel package
 * 
 * 
 */

package uk.co.kriegspiel;
import javax.swing.*;

/** The Archetype King Class
 * 
 * see Arch and Piece Classes
 *
 * @author Denys Bennett
 */


public class ArchKing extends Arch{

    // Constructor
  public ArchKing(){
      
    pieceValue = Piece.Value.KING;
    pieceName = pieceValue.toString().toLowerCase();
   
  }
  
  @Override
        protected void loadImageIcons(){
      // Load correct icons
      java.net.URL imageURL;
      imageURL = KSpielApp.class.getResource("/images/icon_warning.gif");
      imgError = new ImageIcon (imageURL); // do this before calling getImageIcon() !!
      
      imgLBX = getImageIcon("lb","x");
      imgDBX = getImageIcon("db","x");
      imgLWX = getImageIcon("lw","x");
      imgDWX = getImageIcon("dw","x");
      imgLBU = getImageIcon("lb","u");
      imgDBU = getImageIcon("db","u");
      imgLWU = getImageIcon("lw","u");
      imgDWU = getImageIcon("dw","u");
      imgLBF = getImageIcon("lb","f");
      imgDBF = getImageIcon("db","f");
      imgLWF = getImageIcon("lw","f");
      imgDWF = getImageIcon("dw","f");
      imgLBD = getImageIcon("lb","d");
      imgDBD = getImageIcon("db","d");
      imgLWD = getImageIcon("lw","d");
      imgDWD = getImageIcon("dw","d");
      
      // REDS
      imgRBX = getImageIcon("rb","x");
      imgRWX = getImageIcon("rw","x");
      imgRBU = getImageIcon("rb","u");
      imgRWU = getImageIcon("rw","u");
      imgRBF = getImageIcon("rb","f");
      imgRWF = getImageIcon("rw","f");
      imgRBD = getImageIcon("rb","d");
      imgRWD = getImageIcon("rw","d");       

      // GREENS
      imgGBX = getImageIcon("gb","x");
      imgGWX = getImageIcon("gw","x");
      imgGBU = getImageIcon("gb","u");
      imgGWU = getImageIcon("gw","u");
      imgGBF = getImageIcon("gb","f");
      imgGWF = getImageIcon("gw","f");
      imgGBD = getImageIcon("gb","d");
      imgGWD = getImageIcon("gw","d"); 
      // PINE
      imgPBX = getImageIcon("pb","x");
      imgPWX = getImageIcon("pw","x");
      imgPBU = getImageIcon("pb","u");
      imgPWU = getImageIcon("pw","u");
      imgPBF = getImageIcon("pb","f");
      imgPWF = getImageIcon("pw","f");
      imgPBD = getImageIcon("pb","d");
      imgPWD = getImageIcon("pw","d"); 
      
    }
    
}
