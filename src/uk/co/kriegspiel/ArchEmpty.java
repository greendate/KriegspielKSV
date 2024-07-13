

package uk.co.kriegspiel;
import javax.swing.*;

/** The Archetype Empty Piece class
 * 
 * see Arch Class and Piece Class
 *
 * @author Denys Bennett
 */

public class ArchEmpty extends Arch {
    
       // Constructor
  public ArchEmpty(){
      
    pieceValue = Piece.Value.EMPTY;
    pieceName = pieceValue.toString().toLowerCase();
    
  }

  @Override
  protected void loadImageIcons(){
    // Load correct icons
      java.net.URL imageURL;
      imageURL = KSpielApp.class.getResource("/images/icon_warning.gif");
      imgError = new ImageIcon (imageURL); // do this before calling getImageIcon() !!
      
     // WASH or GHOST color
      
      imgLWX = getImageIcon("lw","x");
      imgDWX = getImageIcon("dw","x");
      imgLWU = getImageIcon("lw","u");
      imgDWU = getImageIcon("dw","u");
      imgLWF = getImageIcon("lw","f");
      imgDWF = getImageIcon("dw","f");
      imgLWD = getImageIcon("lw","d");
      imgDWD = getImageIcon("dw","d");
      
      //REDS
      
      imgRWX = getImageIcon("rw","x");
      imgRWU = getImageIcon("rw","u");
      imgRWF = getImageIcon("rw","f");
      imgRWD = getImageIcon("rw","d");     
      

      // GREENS

      imgGWX = getImageIcon("gw","x");
      imgGWU = getImageIcon("gw","u");
      imgGWF = getImageIcon("gw","f");
      imgGWD = getImageIcon("gw","d"); 
    
      // PINE

      imgPWX = getImageIcon("pw","x");
      imgPWU = getImageIcon("pw","u");
      imgPWF = getImageIcon("pw","f");
      imgPWD = getImageIcon("pw","d"); 
    }
}
