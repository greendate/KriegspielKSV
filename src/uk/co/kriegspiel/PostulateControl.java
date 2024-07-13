/* PostulateControl panel
 * This method presents a palette of pieces
 * which can be placed on "empty" squares postulating
 * the opponents position
 */

package uk.co.kriegspiel;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import uk.co.kriegspiel.KSpielUI.ViewMode;

/** Postulate control panel
 * This object creates a palette of pieces
 * which can be placed on "empty" squares postulating
 * the opponents position
 * 
 * @author denysbennett
 */
public class PostulateControl {

     
    private KSpielUI ksUI;
    
    private JPanel jpnlPostulateControl;
    private JPanel jpnlPieces;

    private JButton [] jbtnPieces;

    private int index;
    private ViewMode myColor;
    private boolean blackView;  // can black see imagined enemy pieces
    private boolean whiteView;  // can white see imagined enemy pieces

    
    // constructor
    public PostulateControl(KSpielUI ui, ViewMode color){
        ksUI = ui;

        myColor = color;
        
        jpnlPostulateControl = new JPanel();
        jpnlPieces = new JPanel();

        
        jpnlPostulateControl.setPreferredSize(ksUI.scrnSize.dimMessageboard);
        // same size as Messageboard

        if (!ksUI.scrnSize.bNetbook) // v3.3
            jpnlPostulateControl.setBorder(ksUI.borderTitle("Select opponent's imagined piece to place on board"));
        
        jpnlPieces.setPreferredSize(ksUI.scrnSize.dimLostRow);


        jpnlPostulateControl.add(jpnlPieces);

        jbtnPieces = new JButton[8];   // define 7 buttons (6 pieces + empty+Clear all)

        index = -1; //set selected button index to nothing selected
        blackView = false;
        whiteView = false;

       // set up an empty set of buttons
        final ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/dwemptyx.png");
                
        for (int i=0; i<8; i++){
            final int p = i;
            jbtnPieces[i] = new JButton(elIcon);
            jbtnPieces[i].setPreferredSize(ksUI.scrnSize.dimPiece);

            jpnlPieces.add(jbtnPieces[i]);
            jbtnPieces[i].setIcon(elIcon);
            
             jbtnPieces[i].addActionListener(new ActionListener(){
                
                public void actionPerformed(ActionEvent ae) {
                    // The button pressed was p
                    
                 OnPieceButtonPressed(p);
                 //   ksUI.sysout("button "+String.valueOf(p));
                    

                }           
            });
        }

             loadPieces(color);
    }
    
    private ImageIcon loadImageIcon(String imgfile){
        ImageIcon imgIcon = new ImageIcon();
        imgIcon = ksUI.loadImageIcon(imgfile);
        
        // reduce the image size to fit
        Image img;
        img = imgIcon.getImage();
        img = img.getScaledInstance(ksUI.scrnSize.dimLostIcon.width, ksUI.scrnSize.dimLostIcon.height, 0);
        ImageIcon icon = new ImageIcon(img);
        
        imgIcon = icon;
        return imgIcon;
    }
    /**
     * Put the specified color images on the menu buttons
     * @param color
     */
    public void loadPieces(ViewMode color){
        
        myColor = color;
    
        ImageIcon icon;
        if (color == ViewMode.WHITE){
            icon = loadImageIcon("/images/pieces/dwemptyx.png");
            jbtnPieces[0].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbpawnx.png");
            jbtnPieces[1].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbkingx.png");
            jbtnPieces[2].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbqueenx.png");
            jbtnPieces[3].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbbishopx.png");
            jbtnPieces[4].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbknightx.png");
            jbtnPieces[5].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dbrookx.png");
            jbtnPieces[6].setIcon(icon);
            icon = loadImageIcon("/images/pieces/gwemptyd.png");
            jbtnPieces[7].setIcon(icon);

        } else {
            icon = loadImageIcon("/images/pieces/dwemptyx.png");
            jbtnPieces[0].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwpawnx.png");
            jbtnPieces[1].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwkingx.png");
            jbtnPieces[2].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwqueenx.png");
            jbtnPieces[3].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwbishopx.png");
            jbtnPieces[4].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwknightx.png");
            jbtnPieces[5].setIcon(icon);
            icon = loadImageIcon("/images/pieces/dwrookx.png");
            jbtnPieces[6].setIcon(icon);
            icon = loadImageIcon("/images/pieces/gwemptyd.png");
            jbtnPieces[7].setIcon(icon);
        }
      jbtnPieces[0].setToolTipText("Empty square");
      jbtnPieces[1].setToolTipText("Pawn");
      jbtnPieces[2].setToolTipText("King");
      jbtnPieces[3].setToolTipText("Queen");
      jbtnPieces[4].setToolTipText("Bishop");
      jbtnPieces[5].setToolTipText("Knight");
      jbtnPieces[6].setToolTipText("Rook");
      jbtnPieces[7].setToolTipText("Empty all squares");

    }
    
     public JPanel getPostulateControl(){
        return jpnlPostulateControl;
    }

     public void OnPieceButtonPressed(int button){

         index = button;

         ImageIcon icon = null;
         loadPieces(ksUI.viewMode); // reset all buttons to unselected
         
         if (ksUI.viewMode == ViewMode.WHITE){
             switch (index){
                 case 0:    // an empty square
                     icon = ksUI.loadImageIcon("/images/pieces/dwemptyd.png");
                     break;
                 case 1:    //Pawn
                     icon = ksUI.loadImageIcon("/images/pieces/dbpawnd.png");
                     break;
                 case 2:    //King
                     icon = ksUI.loadImageIcon("/images/pieces/dbkingd.png");
                     break;
                 case 3:    //Queen
                     icon = ksUI.loadImageIcon("/images/pieces/dbqueend.png");
                     break;
                 case 4:    //Bishop
                     icon = ksUI.loadImageIcon("/images/pieces/dbbishopd.png");
                     break;
                 case 5:    //Knight
                     icon = ksUI.loadImageIcon("/images/pieces/dbknightd.png");
                     break;
                 case 6:    //Rook
                     icon = ksUI.loadImageIcon("/images/pieces/dbrookd.png");
                     break;

                 case 7:
                     icon = ksUI.loadImageIcon("/images/pieces/gwemptyd.png");
                     //empty all squares
                     for (int i=1; i<65; i++ ){
                         if (ksUI.viewMode == ViewMode.BLACK)
                            ksUI.ksApp.mainBoard.clearTestPiece(i,Piece.Color.BLACK);
                         else
                            ksUI.ksApp.mainBoard.clearTestPiece(i,Piece.Color.WHITE);

                         ksUI.boardView.refreshBoardDisplay(ksUI.getViewMode());
                     }
                     index = -1; //reset index to nothing selected
                     break;
             }
             
         } else {  // WHITE
             
             switch (index){
                 case 0:    // an empty square
                     icon = ksUI.loadImageIcon("/images/pieces/dwemptyd.png");
                     break;
                 case 1:    //Pawn
                     icon = ksUI.loadImageIcon("/images/pieces/dwpawnd.png");
                     break;
                 case 2:    //King
                     icon = ksUI.loadImageIcon("/images/pieces/dwkingd.png");
                     break;
                 case 3:    //Queen
                     icon = ksUI.loadImageIcon("/images/pieces/dwqueend.png");
                     break;
                 case 4:    //Bishop
                     icon = ksUI.loadImageIcon("/images/pieces/dwbishopd.png");
                     break;
                 case 5:    //Knight
                     icon = ksUI.loadImageIcon("/images/pieces/dwknightd.png");
                     break;
                 case 6:    //Rook
                     icon = ksUI.loadImageIcon("/images/pieces/dwrookd.png");
                     break;

                 case 7:
                     icon = ksUI.loadImageIcon("/images/pieces/gwemptyd.png");
                     //empty all squares
                     for (int i=1; i<65; i++ ){
                         if (ksUI.viewMode == ViewMode.BLACK)
                            ksUI.ksApp.mainBoard.clearTestPiece(i,Piece.Color.BLACK);
                         else
                            ksUI.ksApp.mainBoard.clearTestPiece(i,Piece.Color.WHITE);

                         ksUI.boardView.refreshBoardDisplay(ksUI.getViewMode());
                     }
                     index = -1; //reset index to nothing selected
                     break;
             }
             
         }
         if (index>-1)
            jbtnPieces[index].setIcon(icon);
     }

     public int getTestPieceIndex(){
         return index;
     }

     public void reset(){
         index = -1;
         loadPieces(ksUI.viewMode); // reset all buttons to unselected
     }
     
     public boolean IsTestView(){
         if (ksUI.viewMode == ViewMode.BLACK) return blackView;
         if (ksUI.viewMode == ViewMode.WHITE) return whiteView;
         return false;  // default
     }
   
   
}
