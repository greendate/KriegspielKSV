/*Lost Pieces Panel 
 * This object is used in Referee mode to
 * show the lost pieces.
 * getLostPanel() returns the JPanel object
 */

package uk.co.kriegspiel;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *This object displays the pieces taken
 * and is used in Referee mode instead of the MessagePanel object
 * @author denysbennett
 */
public class LostPieces {

    private KSpielUI ksUI;
    
    private JPanel jpnlLostPieces;
    private JPanel jpnlWhites;
    private JPanel jpnlBlacks;
    private JButton [] jbtnWhites;
    private JButton [] jbtnBlacks;
    private Integer [] white;   // move no on which white piece captured
    private Integer [] black;   // move no on which black piece captured
    private int nextBlack;
    private int nextWhite;
    
    
    //constructor
    public LostPieces(KSpielUI ui){
        ksUI = ui;
        
        jpnlLostPieces = new JPanel();
        jpnlWhites = new JPanel();
        jpnlBlacks = new JPanel();
        
        jpnlLostPieces.setPreferredSize(ksUI.scrnSize.dimMessageboard); // same size as Messageboard

        if (!ksUI.scrnSize.bNetbook) // v3.3
            jpnlLostPieces.setBorder(ksUI.borderTitle("Pieces Taken"));
        
        jpnlWhites.setPreferredSize(ksUI.scrnSize.dimLostRow);
        jpnlBlacks.setPreferredSize(ksUI.scrnSize.dimLostRow);

        jpnlLostPieces.add(jpnlWhites);
        jpnlLostPieces.add(jpnlBlacks);

        
        jbtnWhites = new JButton[15];   // define 15 labels
        jbtnBlacks = new JButton[15];
        white = new Integer[15];
        black = new Integer[15];

        
        final ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/lwemptyx.png");
        for (int i=0; i<15; i++){
            jbtnWhites[i] = new JButton(elIcon);
            jbtnWhites[i].setPreferredSize(ksUI.scrnSize.dimLostIcon);
            jbtnWhites[i].setSize(ksUI.scrnSize.dimLostIcon);
            jpnlWhites.add(jbtnWhites[i]);
            white[i] = 0;
        }
        nextWhite = 0;
        
        for (int i=0; i<15; i++){
            jbtnBlacks[i] = new JButton(elIcon);
            jbtnBlacks[i].setSize(ksUI.scrnSize.dimLostIcon);
            jbtnBlacks[i].setPreferredSize(ksUI.scrnSize.dimLostIcon);
            jpnlBlacks.add(jbtnBlacks[i]);
            black[i] = 0;
        }
        nextBlack = 0;
        
    }
    public void reset(){
        
        final ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/lwemptyx.png");
        for (int i=0; i<15; i++){
            jbtnWhites[i].setIcon(elIcon);
            jbtnBlacks[i].setIcon(elIcon);
            white[i] = 0;
            black[i] = 0;
        }
        nextWhite = 0;
        nextBlack = 0;
    }
    
    public JPanel getLostPieces(){
        return jpnlLostPieces;
    }
    
    public void addLost(Piece piece, int move){
        // here we need to place the piece's image in the panel
        ImageIcon imgIcon = piece.getIcon(Square.Aspect.DISABLED);
        // reduce the image size to fit
        Image img;
        img = imgIcon.getImage();
        img = img.getScaledInstance(ksUI.scrnSize.dimLostIcon.width, ksUI.scrnSize.dimLostIcon.height, 0);
        ImageIcon icon = new ImageIcon(img);
        
        imgIcon = icon;
        
        if (piece.color == Piece.Color.WHITE){
            jbtnWhites[nextWhite].setIcon(imgIcon);
            white[nextWhite] = move;
            nextWhite++;
            
        } else {
            jbtnBlacks[nextBlack].setIcon(imgIcon);
            black[nextBlack] = move;
            nextBlack++;
               
        }
        
    }
    public void undoMove(int move){
        if (nextWhite !=0)
            if (white[nextWhite-1] == move){
                // remove image of the last white piece
                nextWhite--;
                white[nextWhite]=0;
                ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/lwemptyx.png");
                jbtnWhites[nextWhite].setIcon(elIcon);
            }
        if (nextBlack !=0)
            if (black[nextBlack-1] == move){
                // remove image of the last black piece
                nextBlack--;
                black[nextBlack]=0;
                ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/lwemptyx.png");
                jbtnBlacks[nextBlack].setIcon(elIcon);
        }
            
        
    }
}
