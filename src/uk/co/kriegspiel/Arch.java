
package uk.co.kriegspiel;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.*;
/** The Piece Archetype Class
 * 
 * This class defines the immutable behaviour of a generalised chess piece,
 * i.e. those characteristics that do not change once the piece has been
 * instatiated or created, specifically the various images used of a piece
 * occupying a chess square, and the piece's value.
 * 
 * A separate class Piece holds the mutable data, and uses calls to the Arch object
 * to perform methods which do not rely on the current state of the piece.
 * 
 * On initialisation, the App creates a unique set of archetype pieces, of subclasses
 * ArchKing, ArchQueen, ArchBishop, ... ArchEmpty, which define the immutable
 * properties and methods of the piece types themselves, specifically the moves
 * the piece can make.
 * 
 * The Board object will instantiate a set of Black and White Piece objects of 
 * subclasses, King, Queen, Bishop, ... Empty which contain just the state data
 * for that particular piece such as its position, plus a reference to the related Arch object in the App.
 *
 * Each inheriting piece class may have several images: 
 * L or D for occupying Light or Dark Squares;
 * B or W for Black or White, or R red where a piece has been taken, or G green a check vector;
 * X or U or F or D altering the aspect of the image as it is e.g enabled or selected.
 * @author Denys Bennett
 */
public class Arch {

  // Constructors
  
  public Arch(){
    // Dummy constructor for inheritance classes
  }
  
  public Arch(Piece.Value value){
    pieceValue = value;
    pieceName = value.toString().toLowerCase();

  }
  
  // Variables
  protected Piece.Value pieceValue;   // enum KING, QUEEN,... EMPTY
  protected String pieceName;
  protected KSpielApp theApp;
  
  // Button images
    
    protected ImageIcon imgLBX;
    protected ImageIcon imgDBX;
    protected ImageIcon imgLWX;
    protected ImageIcon imgDWX;
    protected ImageIcon imgLBU;
    protected ImageIcon imgDBU;
    protected ImageIcon imgLWU;
    protected ImageIcon imgDWU;
    protected ImageIcon imgLBF;
    protected ImageIcon imgDBF;
    protected ImageIcon imgLWF;
    protected ImageIcon imgDWF;
    protected ImageIcon imgLBD;
    protected ImageIcon imgDBD;
    protected ImageIcon imgLWD;
    protected ImageIcon imgDWD;
    
    protected ImageIcon imgRBX;
    protected ImageIcon imgRWX;
    protected ImageIcon imgRBU;
    protected ImageIcon imgRWU;
    protected ImageIcon imgRBF;
    protected ImageIcon imgRWF;
    protected ImageIcon imgRBD;
    protected ImageIcon imgRWD;
    
    // Needed For EMPTY and KING only
    // But generic getIcon refers to GREEN & PINE cases
    protected ImageIcon imgGBX;
    protected ImageIcon imgGWX;
    protected ImageIcon imgGBU;
    protected ImageIcon imgGWU;
    protected ImageIcon imgGBF;
    protected ImageIcon imgGWF;
    protected ImageIcon imgGBD;
    protected ImageIcon imgGWD;
    
    protected ImageIcon imgPBX;
    protected ImageIcon imgPWX;
    protected ImageIcon imgPBU;
    protected ImageIcon imgPWU;
    protected ImageIcon imgPBF;
    protected ImageIcon imgPWF;
    protected ImageIcon imgPBD;
    protected ImageIcon imgPWD;
    
    protected ImageIcon imgError;

       // Pieces as Cursors images  v4.2

    protected Cursor curCB;
    protected Cursor curCW;

    
    // UTILITIES
    protected int getY(int r){
      int y = 1+(r-1)/8;
      return y;
    }
    protected int getX(int r){
      int x = r % 8;
      if (x == 0){ x=8;}
      return x;
    }
    
    // IMAGE METHODS
    // getters

       public Cursor getCursor(Piece.Color color){ //v3.04
        if (color == Piece.Color.BLACK)
            return curCB;
        else return curCW;
    }

    public ImageIcon getIcon(Board boardRef, int iPosition, 
            Piece.Color color, Square.Aspect aspect){
      
      ImageIcon img = imgError; // set up default
      // From Square property determine if Red, Green or normal
      
      // From square position,  for normal give L or D,
      //                        for green give G or P, 
      //                        for red give R
      // From piece colour,  give W or B
      
      

      // Test for Red Square
      if (boardRef.isRedSquare(iPosition)){
          if (boardRef.isTrySquare(iPosition)){
              //img = reds(color, aspect); 
                img = reds(color, Square.Aspect.ILLUMINATED);
          } else {
                            
                img = reds(color, aspect);  
          }
      }
      else
         // Test for a pawn try 
      if (boardRef.isTrySquare(iPosition)) {  // && is also my view not his!
          // set these squares as if illuminated aspect
          
          // Tries are illuminated:
          //    if the view is BOTH , or
          //    if the piece with the try is the same colour as the View,
          //    not otherwise
          
          if ((getY(iPosition)%2 == getX(iPosition)%2)) {
                // DARK squares
                  if (boardRef.isGreenSquare(iPosition)){

                      img = pines(boardRef, iPosition, color, Square.Aspect.ILLUMINATED);

                  }else{

                      img = darks(color, Square.Aspect.ILLUMINATED);

                  }
            } else {
                // LIGHT squares
                  if (boardRef.isGreenSquare(iPosition)){

                      img = greens(boardRef, iPosition, color, Square.Aspect.ILLUMINATED);

                  }else{

                      img = lights(color, Square.Aspect.ILLUMINATED);

                  }
            }
      }
          
     
      else
         // 
      if ((getY(iPosition)%2 == getX(iPosition)%2)) {
        // DARK squares
          if (boardRef.isGreenSquare(iPosition)){
              
              img = pines(boardRef, iPosition, color, aspect);
             
          }else{
          
              img = darks(color, aspect);

          }
      } else {
        // LIGHT squares
          if (boardRef.isGreenSquare(iPosition)){
              
              img = greens(boardRef, iPosition, color, aspect);
           
          }else{
              
              img = lights(color, aspect);
          
        }               
      }           
      return img;
    }
    public  ImageIcon getImageIcon(String strPre, String strPost){
      java.net.URL imageURL;
    
      ImageIcon img;
    
      imageURL = KSpielApp.class.getResource("/images/pieces/" + strPre + pieceName + strPost+".png");
      if (imageURL == null)
        img = imgError;
      else
        img = new ImageIcon(imageURL);
      return img;
    }

    protected void makeCursorIcons(){  // v4.03
        //Get the default toolkit
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        java.net.URL imageURL; Image image; ImageIcon icon;
        //Create the hotspot for the cursors
        Point hotSpot = new Point(24,24);

        // Make the black cursor
        imageURL = KSpielApp.class.getResource("/images/pieces/cb"+pieceName+".png");
        //Load an image for the cursor
        image = toolkit.getImage(imageURL);
        //Create the custom cursor
        curCB = toolkit.createCustomCursor(image, hotSpot, "BlackPieceCursor");
        // make the sprite image icon
//        spriteB = new ImageIcon(imageURL); //v4.3//

        // Make the white cursor
        imageURL = KSpielApp.class.getResource("/images/pieces/cw"+pieceName+".png");
        //Load an image for the cursor
        image = toolkit.getImage(imageURL);
        //Create the custom cursor
        curCW = toolkit.createCustomCursor(image, hotSpot, "WhitePieceCursor");
        // make the sprite image icon
 //       spriteW = new ImageIcon(imageURL);  //v4.3?

    }



    protected void loadImageIcons(){
      // Load correct icons
      java.net.URL imageURL;
      imageURL = KSpielApp.class.getResource("/images/icon_warning.gif");
      imgError = new ImageIcon (imageURL); // do this before calling getImageIcon() !!
      
      // DARK SQUARES
      imgDBX = getImageIcon("db","x");      
      imgDWX = getImageIcon("dw","x");     
      imgDBU = getImageIcon("db","u");     
      imgDWU = getImageIcon("dw","u");     
      imgDBF = getImageIcon("db","f");      
      imgDWF = getImageIcon("dw","f");      
      imgDBD = getImageIcon("db","d");     
      imgDWD = getImageIcon("dw","d");
      // LIGHT SQUARES
      imgLBX = getImageIcon("lb","x");
      imgLWX = getImageIcon("lw","x");
      imgLBU = getImageIcon("lb","u");
      imgLWU = getImageIcon("lw","u");
      imgLBF = getImageIcon("lb","f");
      imgLWF = getImageIcon("lw","f");
      imgLBD = getImageIcon("lb","d");
      imgLWD = getImageIcon("lw","d");
      
      // RED SQUARES
      imgRBX = getImageIcon("rb","x");
      imgRWX = getImageIcon("rw","x");
      imgRBU = getImageIcon("rb","u");
      imgRWU = getImageIcon("rw","u");
      imgRBF = getImageIcon("rb","f");
      imgRWF = getImageIcon("rw","f");
      imgRBD = getImageIcon("rb","d");
      imgRWD = getImageIcon("rw","d");       
      
    }
    
    private ImageIcon reds(Piece.Color color, Square.Aspect aspect){
        ImageIcon img = imgError;
        switch (color){ // Piece color on RED square
             case WHITE:
             case GHOST:
             case WASH: // Empty square (virtual piece) color
                //if (color == Piece.Color.WHITE || color == Piece.Color.WASH){
                  // DARK square WHITE piece or Empty Square
                  switch (aspect){
                    case DISABLED:
                      img = imgRWX;
                      break;
                    case ENABLED:
                      img = imgRWU;
                      break;
                    case ILLUMINATED:
                      img = imgRWD;
                      break;
                    case SELECTED:
                      img = imgRWF;           
                  }
                  break;
                  
             case BLACK:  // Piece color on RED square
          
          // DARK square BLACK piece
                  switch (aspect){
                    case DISABLED:
                      img = imgRBX;
                      break;
                    case ENABLED:
                      img = imgRBU;
                      break;
                    case ILLUMINATED:
                      img = imgRBD;
                      break;
                    case SELECTED:
                      img = imgRBF;           
                  }
                  break;
            }
        return img;  
    }
    
    private ImageIcon pines(Board boardRef, int iPosition, 
            Piece.Color color, Square.Aspect aspect){
      
        ImageIcon img = imgError;
        
        switch (color){ // Piece color on DARK GREEN square
             case WHITE:
                 // KING occupied squares are shown GREEN, others as normal
                 if (boardRef.getOccupier(iPosition).value()!=Piece.Value.KING){
                     switch (aspect){
                    case DISABLED:
                      img = imgDWX;
                      break;
                    case ENABLED:
                      img = imgDWU;
                      break;
                    case ILLUMINATED:
                      img = imgDWD;
                      break;  
                    case SELECTED:
                      img = imgDWF;           
                  }
                  break;
                 }
                 //NOTE: NO <break;> here after close of if() lets KING fall through
                 // to GHOST & WASH cases; has same effect as else{ }
             case GHOST:
             case WASH: // Empty or Ghost color on DARK GREEN square
                //if (color == Piece.Color.WHITE || color == Piece.Color.WASH){
                  // DARK square WHITE piece or Empty Square
                  switch (aspect){
                    case DISABLED:
                      img = imgPWX;
                      break;
                    case ENABLED:
                      img = imgPWU;
                      break;
                    case ILLUMINATED:
                      img = imgPWD;
                      break;  
                    case SELECTED:
                      img = imgPWF;           
                  }
                  break;
             
             case BLACK:  // Piece color 
          
                 // KING occupied squares are shown GREEN, others as normal
                 if (boardRef.getOccupier(iPosition).value()==Piece.Value.KING){
              // DARK GREEN square BLACK piece
                      switch (aspect){
                        case DISABLED:
                          img = imgPBX;
                          break;
                        case ENABLED:
                          img = imgPBU;
                          break;
                        case ILLUMINATED:
                          img = imgPBD;
                          break;  
                        case SELECTED:
                          img = imgPBF;           
                      }
                 }
                 else{
                     // DARK square BLACK piece
                      switch (aspect){
                        case DISABLED:
                          img = imgDBX;
                          break;
                        case ENABLED:
                          img = imgDBU;
                          break;
                        case ILLUMINATED:
                          img = imgDBD;
                          break;  
                        case SELECTED:
                          img = imgDBF;           
                      }
                 }
            }
        
        return img;  
    }
    
    private ImageIcon greens(Board boardRef, int iPosition,
            Piece.Color color, Square.Aspect aspect){
      
        ImageIcon img = imgError;
        
        switch (color){
              case WHITE:
                  // KING occupied squares are shown GREEN, others as normal
                 if (boardRef.getOccupier(iPosition).value()!=Piece.Value.KING){
                     // LIGHT square WHITE piece or Empty Square
                  switch (aspect){
                    case DISABLED:
                      img = imgLWX;
                      break;
                    case ENABLED:
                      img = imgLWU;
                      break;
                    case ILLUMINATED:
                      img = imgLWD;
                      break;  
                    case SELECTED:
                      img = imgLWF;
                      break;
                  }
                  break;
                 }
               //NOTE: NO <break;> here after close of if() lets KING fall through
               // to GHOST & WASH cases; has same effect as else{ }   
              case GHOST:
              case WASH:
          
                  
                  // LIGHT square WHITE piece or Empty Square GREEN
                  switch (aspect){
                    case DISABLED:
                      img = imgGWX;
                      break;
                    case ENABLED:
                      img = imgGWU;
                      break;
                    case ILLUMINATED:
                      img = imgGWD;
                      break;  
                    case SELECTED:
                        img = imgGWF;
                  }
                  break;
      
              case BLACK:
                   // KING occupied squares are shown GREEN, others as normal
                 if (boardRef.getOccupier(iPosition).value()!=Piece.Value.KING){
                     // LIGHT square BLACK piece
                      switch (aspect){
                        case DISABLED:
                          img = imgLBX;
                          break;
                        case ENABLED:
                          img = imgLBU;
                          break;
                        case ILLUMINATED:
                          img = imgLBD;
                          break;  
                        case SELECTED:
                          img = imgLBF;
                      }
                 }else{
                     
              // LIGHT square BLACK piece
                      switch (aspect){
                        case DISABLED:
                          img = imgGBX;
                          break;
                        case ENABLED:
                          img = imgGBU;
                          break;
                        case ILLUMINATED:
                          img = imgGBD;
                          break;  
                        case SELECTED:
                          img = imgGBF;
                      }  
                 }
          }
        
        return img;  
    }
    
    private ImageIcon darks(Piece.Color color, Square.Aspect aspect){
        ImageIcon img = imgError;
        
        switch (color){ // Piece color
             case WHITE:
             case GHOST:
             case WASH: // Empty square (virtual piece) color
                //if (color == Piece.Color.WHITE || color == Piece.Color.WASH){
                  // DARK square WHITE piece or Empty Square
                  switch (aspect){
                    case DISABLED:
                      img = imgDWX;
                      break;
                    case ENABLED:
                      img = imgDWU;
                      break;
                    case ILLUMINATED:
                      img = imgDWD;
                      break;  
                    case SELECTED:
                      img = imgDWF;           
                  }
                  break;
             
             case BLACK:  // Piece color
          
          // DARK square BLACK piece
                  switch (aspect){
                    case DISABLED:
                      img = imgDBX;
                      break;
                    case ENABLED:
                      img = imgDBU;
                      break;
                    case ILLUMINATED:
                      img = imgDBD;
                      break;  
                    case SELECTED:
                      img = imgDBF;           
                  }
          
            }
        
        return img;  
    }
    
    private ImageIcon lights(Piece.Color color, Square.Aspect aspect){
        ImageIcon img = imgError;
        
        switch (color){
              case WHITE:
              case GHOST:
              case WASH:
          
                  
                  // LIGHT square WHITE piece or Empty Square
                  switch (aspect){
                    case DISABLED:
                      img = imgLWX;
                      break;
                    case ENABLED:
                      img = imgLWU;
                      break;
                    case ILLUMINATED:
                      img = imgLWD;
                      break;  
                    case SELECTED:
                        img = imgLWF;
                  }
                  break;
      
              case BLACK:
          // LIGHT square BLACK piece
                  switch (aspect){
                    case DISABLED:
                      img = imgLBX;
                      break;
                    case ENABLED:
                      img = imgLBU;
                      break;
                    case ILLUMINATED:
                      img = imgLBD;
                      break;  
                    case SELECTED:
                      img = imgLBF;
                  }        
          }  
        
        return img;  
    }
    /*
    public void resizeImage(){
        
    }
     * */
}
