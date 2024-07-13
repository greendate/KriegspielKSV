/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author denysbennett
 */
public class StatusDisplay {
    // References to UI and Board
    private KSpielUI ksUI;     
  //  private Board mainBoard;
    
    // internal variables
    private JLabel jlabWhiteToPlay;
    private JLabel jlabBlackToPlay;
    
    private JLabel jlabWhiteRemain;
    private JLabel jlabBlackRemain;
    private JLabel jlabMoveCount;
  
    private JLabel jlabMoveIllegal;
    private JLabel jlabWhiteInCheck; 
    private JLabel jlabBlackInCheck;
    private JLabel jlabCheckmate;
    private JLabel jlabStalemate;

    private JLabel jlabWhiteResigns;
    private JLabel jlabBlackResigns;
    private JLabel jlabAgreedDraw;
    private JLabel jlabMoveOK; 
  
    private JLabel jlabTaken;
    private JLabel jlabTries;
  
    private JLabel jlabQueened; //V3.02
    
    private JLabel jlabErrorMsg;
    private JLabel jlabInfoMsg;
    
    private String strMovesSoFar;
    
    // constructor
    StatusDisplay(KSpielUI ui){
        ksUI = ui;  //reference back to creator object
    //    mainBoard = board;
        
    }
    // getters
    public String getMovesSoFar(){
            return strMovesSoFar;
    }
    
    //setters
    public void setMoveCount(Board mainBoard){
        int movecount = mainBoard.getMovesMade();  // modification FS090404.7
        int odd = movecount%2;
        movecount = movecount/2;
        String text = "Moves so far: "+movecount;
        if (odd == 1) {text = text+"+";} else {text = text + "   ";}
        jlabMoveCount.setText(text);
        strMovesSoFar = text;
       // jlabMoveCount.setText("Moves so far: " + mainBoard.getMovesMade());
    }
    public void setMoveIllegal(boolean b){
        jlabMoveIllegal.setVisible(b);
    }

    public void setTries(String t){
        jlabTries.setText(t);
    }
    
    public void flashIllegal(int iFrom, int iTo){
      jlabMoveIllegal.setVisible(true);
    }
    
    public void setWhiteResigns(boolean b){
        jlabWhiteResigns.setVisible(b);
    }
    
    public void setBlackResigns(boolean b){
        jlabBlackResigns.setVisible(b);
    }
    
    public void setAgreedDraw(boolean b){
        jlabAgreedDraw.setVisible(b);
    }
    
    public void setNames(String nameWhite, String nameBlack){

    //  jpanelTop.setBorder(ksUI.borderTitle("Black "+nameBlack));
    //  jpanelBottom.setBorder(ksUI.borderTitle("White "+nameWhite));
  
    }
    
    public JPanel makeStatusDisplay(Board mainBoard){
        
      JPanel jpanelStatus = new JPanel();
        
 
      jpanelStatus.setLayout(new BoxLayout(jpanelStatus,BoxLayout.Y_AXIS));
      
      jpanelStatus.setBorder(ksUI.borderTitle("Playing Kriegspiel"));
      jpanelStatus.setPreferredSize(ksUI.scrnSize.dimStatus);
      
      JLabel jlabSpacer = new JLabel(" ");
      jlabSpacer.setAlignmentX(Component.CENTER_ALIGNMENT);
     // jpanelStatus.add(jlabSpacer);
      
      jlabBlackToPlay = new JLabel();
      jlabBlackToPlay.setIcon(ksUI.loadImageIcon("/images/Effects/BlackToPlay.png"));
      jlabBlackToPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabBlackToPlay);
      
      jlabWhiteToPlay = new JLabel();
      jlabWhiteToPlay.setIcon(ksUI.loadImageIcon("/images/Effects/WhiteToPlay.png"));
      jlabWhiteToPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabWhiteToPlay);
      
      jpanelStatus.add(jlabSpacer);
      
      //setWhiteToPlay(true);
      
      jlabWhiteRemain = new JLabel("White remaining: 16");
      jlabWhiteRemain.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabWhiteRemain);
      jlabBlackRemain = new JLabel(" Black remaining: 16");
      jlabBlackRemain.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabBlackRemain);
      
      jlabMoveCount = new JLabel("Moves so far: " + mainBoard.getMovesMade());
      jlabMoveCount.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabMoveCount);
      
      jlabMoveIllegal = new JLabel();
      jlabMoveIllegal.setIcon(ksUI.loadImageIcon("/images/Effects/MoveIllegal.png"));
      jlabMoveIllegal.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabMoveIllegal);
      
      jlabMoveOK = new JLabel();
      jlabMoveOK.setIcon(ksUI.loadImageIcon("/images/Effects/MoveOK.png"));
      jlabMoveOK.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabMoveOK);
      
      jlabWhiteInCheck = new JLabel();
      jlabWhiteInCheck.setIcon(ksUI.loadImageIcon("/images/Effects/WhiteInCheck.png"));
      jlabWhiteInCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabWhiteInCheck);
      
      jlabBlackInCheck = new JLabel();
      jlabBlackInCheck.setIcon(ksUI.loadImageIcon("/images/Effects/BlackInCheck.png"));
      jlabBlackInCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabBlackInCheck);
      
      jlabCheckmate = new JLabel();
      jlabCheckmate.setIcon(ksUI.loadImageIcon("/images/Effects/Checkmate.png"));
      jlabCheckmate.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabCheckmate);
      
      jlabStalemate = new JLabel();
      jlabStalemate.setIcon(ksUI.loadImageIcon("/images/Effects/Stalemate.png"));
      jlabStalemate.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabStalemate);
      
      jlabWhiteResigns = new JLabel();
      jlabWhiteResigns.setIcon(ksUI.loadImageIcon("/images/Effects/WhiteResigns.png"));
      jlabWhiteResigns.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabWhiteResigns);
      
      jlabBlackResigns = new JLabel();
      jlabBlackResigns.setIcon(ksUI.loadImageIcon("/images/Effects/BlackResigns.png"));
      jlabBlackResigns.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabBlackResigns);
      
      jlabAgreedDraw = new JLabel();
      jlabAgreedDraw.setIcon(ksUI.loadImageIcon("/images/Effects/AgreedDraw.png"));
      jlabAgreedDraw.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabAgreedDraw);
      
      jpanelStatus.add(jlabSpacer);  //v3.03
      
      jlabErrorMsg = new JLabel();
      jlabErrorMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabErrorMsg);
      
       
      jlabInfoMsg = new JLabel();
      jlabInfoMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabInfoMsg);
      
      jlabTaken = new JLabel();
      jlabTaken.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabTaken);
      
      //V3.02
      jlabQueened = new JLabel();
      jlabQueened.setText("Pawn Promoted");
      jlabQueened.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabQueened);
      
      jlabTries = new JLabel();
      jlabTries.setAlignmentX(Component.CENTER_ALIGNMENT);
      jpanelStatus.add(jlabTries);
      
      
      
      resetStatusDisplay();
        
        return jpanelStatus;
    }
    
    public void resetStatusDisplay(){
      jlabMoveIllegal.setVisible(false);
      jlabMoveOK.setVisible(false);
      jlabWhiteInCheck.setVisible(false);
      jlabBlackInCheck.setVisible(false);
      jlabCheckmate.setVisible(false);
      jlabStalemate.setVisible(false);
      jlabWhiteResigns.setVisible(false);
      jlabBlackResigns.setVisible(false);
      jlabAgreedDraw.setVisible(false);
      jlabQueened.setVisible(false);

      ksUI.setOpponentNames();
      
      setWhiteToPlay(true);

  }
  
  public void setWhiteToPlay(boolean white){
      jlabWhiteToPlay.setVisible(white);
      jlabBlackToPlay.setVisible(!white);
    }
  
  private String getCheckMessage(Result result){
      String checkMessage = "";
      
      if (result.iCheckByKnight !=0)
          checkMessage += " by Knight;";
      if (result.bCheckCol)
          checkMessage += " on File;";
      if (result.bCheckRow)
          checkMessage += " on Rank;";
      if (result.bCheckLong)
          checkMessage +=" on Long Diagonal;";
      if (result.bCheckShort)
          checkMessage += " on Short Diagonal;";
            
      return checkMessage;
  }
  
  public void flashResults(Result result){
      // refresh the status display
      // and send referee messages to messageboard (v3.02)
       String refereeMessage = "";
       jlabBlackInCheck.setVisible(result.bBlackInCheck);
       if (result.bBlackInCheck){           //V3.02        
        refereeMessage = "Black in check:"+getCheckMessage(result);
        ksUI.OnReceiveMessage(refereeMessage);
       }
       
       jlabWhiteInCheck.setVisible(result.bWhiteInCheck);
       if (result.bWhiteInCheck){               //V3.02
         refereeMessage = "White in check:"+getCheckMessage(result);            
         ksUI.OnReceiveMessage(refereeMessage);   
       }
       
       jlabCheckmate.setVisible(result.bCheckmate);
       jlabStalemate.setVisible(result.bStalemate);
       jlabWhiteRemain.setText("White remaining: " + result.iWhiteRemain);
       jlabBlackRemain.setText(" Black remaining:" + result.iBlackRemain);

       if (result.iRedSquare != 0){
           if (result.capture.color == Piece.Color.WHITE){
               refereeMessage = "White ";
           } else{
               refereeMessage = "Black ";
           }
           if (result.capture.value == Piece.Value.PAWN && ksUI.getApp().myCfg.rules.bPawnsAndPieces){
                refereeMessage += "Pawn Taken at "+ksUI.getCoords(result.iRedSquare);
           } else {
                refereeMessage += "Piece Taken at "+ksUI.getCoords(result.iRedSquare);
           }
           jlabTaken.setText(refereeMessage);
           ksUI.OnReceiveMessage(refereeMessage); //V3.02
       }   
       else {
           jlabTaken.setText("");
       }
       
       if (result.bQueenedPawn && ksUI.getApp().myCfg.rules.bAnnounceQueens){  //V3.02
        jlabQueened.setVisible(true);
        refereeMessage = "Pawn promoted"; 
        ksUI.OnReceiveMessage(refereeMessage);
       }
       else{
           jlabQueened.setVisible(false);
       }
       
  }
  
}
