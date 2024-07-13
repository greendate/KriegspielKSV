/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import uk.co.kriegspiel.KSpielUI.ViewMode;

/**
 *
 * @author denysbennett
 */
public class BoardView {

    private KSpielUI ksUI;
    private Board mainBoard;
    private JPanel jpanelCentre;
    private JPanel jpanelBoard;
    private JPanel jpanelTop;
//    private JPanel jpanelBottom;
//    private String nameWhite;
//    private String nameBlack;
    private JPanel jpanelHLabels;
    private JPanel jpanelVLabels;
    private JLabel[] jlabVLabels;
    private JLabel[] jlabHLabels;
    private int iOrientation;
    
    private JButton[] jbtnSquare;
    private ArrayList<Integer> greenList;
    private ArrayList<Integer> triesList;
    private ArrayList<Integer> oldTriesList; // for RobotC
    
//    private JLabel jpad;
    
    private JButton jbtnRotate;
    
    
    // constructor
    public BoardView(KSpielUI ui, Board board){
        ksUI = ui;
        mainBoard = board;

//        jpad = new JLabel();
//        jpad.setPreferredSize(new Dimension(5,5));
        
        jpanelCentre = new JPanel();
        jpanelCentre.setLayout(new FlowLayout());
        jpanelCentre.setPreferredSize(ksUI.scrnSize.dimCentre);
        
        jpanelBoard = makeEmptyBoard();
        jpanelTop = makeTopDisplay();
    //    jpanelBottom = makeBottomDisplay();
        jbtnRotate = makeRotateButton();
        jpanelHLabels = makeHLabels();
        jpanelVLabels = makeVLabels();
        
        jpanelCentre.add(jpanelTop);  //////
        jpanelCentre.add(jpanelBoard);
        jpanelCentre.add(jpanelHLabels);
  //      jpanelCentre.add(jpanelBottom);
        
       
        
    }
    
    // getters
    //public JPanel getTop(){ return jpanelTop;}
   // public JPanel getBottom(){ return jpanelBottom;}
    public JButton getRotate(){ return jbtnRotate;}
    public JPanel getBoard(){ return jpanelBoard;}
    public JPanel getHLabels(){ return jpanelHLabels;}
    public JPanel getVLabels(){ return jpanelVLabels;}
    public JPanel getCentre(){ return jpanelCentre;}
    
    // setters
    public void setBoard(Board newBoard){
        mainBoard = newBoard;
    }
    
    public void setControls(JPanel controls){
        jpanelTop = controls;
        jpanelTop.validate();
        jpanelTop.repaint();
    }

    // actions
    
    private JPanel makeEmptyBoard(){
    
        JPanel jpanelB = new JPanel();
        
        
        
        // Initialise Array Variables
        iOrientation = 0;
        greenList = new ArrayList<Integer>(); 
        triesList = new ArrayList<Integer>();
        oldTriesList = new ArrayList<Integer>();  //v3.3 for RobotC

         // Create a panel that will contain square icons 
        jpanelB = new JPanel();

        jpanelB.setPreferredSize(ksUI.scrnSize.dimBoard);


        jpanelB.setBorder(new javax.swing.border.EtchedBorder());
        jpanelB.setLayout(new GridLayout(8,8));

        // add the buttons to make icons
        jbtnSquare = new JButton[65];   // define 64 buttons
        // prepare light and dark empty square images
        final ImageIcon edIcon = ksUI.loadImageIcon("/images/pieces/dwemptyx.png");
        final ImageIcon elIcon = ksUI.loadImageIcon("/images/pieces/lwemptyx.png");
        // use nested for loop to make GridLayout put 1,1 at bottom left and 8,8 top right

        for (int j=7; j>-1; j--) {
                    for (int i=1; i<9; i++){      
                        //jpanelBoard.add(jbtnSquare[p]);
                        jpanelB.add(doSquare(i,j,elIcon,edIcon));
                    }
                }
        
        return jpanelB;
  
  }  
    
    private JPanel makeHLabels(){
        JPanel jpanelH = new JPanel();
        
        //  add the a - h labels
    
        jlabHLabels = new JLabel[9];
        //jpanelHLabels = new JPanel();
        jpanelH.setPreferredSize(ksUI.scrnSize.dimHLabels);
       
    
        for (int i=1; i<9; i++){
            jlabHLabels[i] = new JLabel();      

            String a = toLetter(i);

            jlabHLabels[i].setText(a);
            jlabHLabels[i].setPreferredSize(ksUI.scrnSize.dimHLab);
            jlabHLabels[i].setHorizontalTextPosition(SwingConstants.RIGHT);

            jpanelH.add(jlabHLabels[i]);
        }
        
        return jpanelH;
    }
    
    private JPanel makeVLabels(){
        JPanel jpanelVLab = new JPanel();
        
        jlabVLabels = new JLabel[9];
    
        jpanelVLab.setPreferredSize(ksUI.scrnSize.dimVLabels);
    
    
        //  add the 1-8 labels
        for (int i=1; i<9; i++){
            jlabVLabels[i] = new JLabel();
            jlabVLabels[i].setText(String.valueOf(9-i));
            jlabVLabels[i].setPreferredSize(ksUI.scrnSize.dimVLab);

            jpanelVLab.add(jlabVLabels[i]);
        }

            return jpanelVLab;
    }
    
    private JPanel makeTopDisplay(){
     
      JPanel jpanelT;
      jpanelT = new JPanel();

 //     jpanelT.setBorder(ksUI.borderTitle("Black"));
      jpanelT.setPreferredSize(ksUI.scrnSize.dimTop);
      
//      jlabBlackToPlay = new JLabel();

//      jlabBlackToPlay.setIcon(ksUI.loadImageIcon("/images/Effects/BlackToPlay.png"));
      
//      jpanelT.add(jlabBlackToPlay);
      
      return jpanelT;
    }
    /*  
    private JPanel makeBottomDisplay(){
 
      JPanel jpanelB;
      
      jpanelB = new JPanel();
      
      jpanelB.setBorder(ksUI.borderTitle("White"));
      
      jpanelB.setPreferredSize(ksUI.dimBottom);
      
      jlabWhiteToPlay = new JLabel();

      jlabWhiteToPlay.setIcon(ksUI.loadImageIcon("/images/Effects/WhiteToPlay.png"));
      
      jpanelB.add(jlabWhiteToPlay);
      
     
      return jpanelB;
    } 
    /*
    public void setWhiteToPlay(boolean white){
      jlabWhiteToPlay.setVisible(white);
      jlabBlackToPlay.setVisible(!white);
    }
    */
    private JButton makeRotateButton(){
        
        JButton jbtnRot = new JButton(ksUI.loadImageIcon("/images/Effects/Rotate.png"));
        jbtnRot.setToolTipText("Rotate board");
        jbtnRot.addActionListener(new ActionListener(){
          // open support website in default browser
          public void actionPerformed(ActionEvent ae){               
              rotate();
              }
          });
        
        return jbtnRot;
    }
    
    public String toLetter(int i){
      String a="";
      switch (i){
                case 1: a="a"; break;
                case 2: a="b"; break;
                case 3: a="c"; break;
                case 4: a="d"; break;
                case 5: a="e"; break;
                case 6: a="f"; break;
                case 7: a="g"; break;
                case 8: a="h"; break;             
            }           
      
      return a;
  }
    
    public void rotate(){
        iOrientation++;
              if (iOrientation>3) iOrientation = 0;
             
              
              for (int p=1; p<65; p++){
                  jpanelBoard.remove(jbtnSquare[p]);
              }
                                                        
              
              jpanelVLabels.removeAll();
              jpanelHLabels.removeAll();
                   
              
         
              switch (iOrientation){
                case 0:
                    for (int j=7; j>-1; j--) {
             
                        for (int i=1; i<9; i++){  
                          int p = i+8*j;
                          jpanelBoard.add(jbtnSquare[p]);                     
                        }
                    }
                    
                    for (int i=1; i<9; i++){         
                        String a = toLetter(i);   
                        jlabHLabels[i].setText(a);      
                        jpanelHLabels.add(jlabHLabels[i]);
                        
                        jlabVLabels[i].setText(String.valueOf(9-i));
                        jpanelVLabels.add(jlabVLabels[i]);
                    }
 
                    
                    break;
                    
                case 1:
               
                    
                    for (int i=1; i<9; i++){
               
                        for (int j=0; j<8; j++) {
                          int p = i+8*j;
                          jpanelBoard.add(jbtnSquare[p]);
                          
                        }
                        
                        String a = toLetter(i);   
                        jlabVLabels[i].setText(a);      
                        jpanelVLabels.add(jlabVLabels[i]);
                        
                        jlabHLabels[i].setText(String.valueOf(i));
                        jpanelHLabels.add(jlabHLabels[i]);
                    } 
                    
                    
                    break;
                case 2:
                
                    
                    for (int j=0; j<8; j++) {
               
                        for (int i=8; i>0; i--){      
                          int p = i+8*j;
                          jpanelBoard.add(jbtnSquare[p]);
                          
                        }
                    }
                    
                    for (int i=1; i<9; i++){         
                        String a = toLetter(9-i);   
                        jlabHLabels[i].setText(a);      
                        jpanelHLabels.add(jlabHLabels[i]);
                        
                        jlabVLabels[i].setText(String.valueOf(i));
                        jpanelVLabels.add(jlabVLabels[i]);
                    }
                    
                    break;
                case 3:
                 
                    for (int i=8; i>0; i--){
                  
                        for (int j=7; j>-1; j--) {
                          int p = i+8*j;
                          jpanelBoard.add(jbtnSquare[p]);
                          
                        }
                        
                        String a = toLetter(i);   
                        jlabVLabels[i].setText(a);      
                        jpanelVLabels.add(jlabVLabels[i]);
                        
                        jlabHLabels[i].setText(String.valueOf(i));
                        jpanelHLabels.add(jlabHLabels[i]);
                    }
                    
                    

                }
              jpanelHLabels.validate();
              jpanelVLabels.validate();
              jpanelBoard.validate();
              jpanelBoard.repaint();
              jpanelHLabels.repaint();
              jpanelVLabels.repaint();
    }

    public void setCursor(int p){  //v4.2

      if (ksUI.recorder.IsRecording()){  // if true, game is in play
        Piece piece = mainBoard.getOccupier(p);
        Square square = mainBoard.getSquare(p);

        jpanelCentre.setCursor(piece.getCursor());
        // temporarily set the square piece as invisible, as the cursor will show the piece
        square.setVisible(false);
        setButtonIcon(p);
      }

 }


     public void resetCursor(){ //v4.2
    if (  ksUI.recorder.IsRecording() ){ // if recording,game is in play
      // reset visibility which was suppressed by piece image cursor
      int i = mainBoard.getSelectedSquare();
      if (i>0){
          Square square = mainBoard.getSquare(i);
          square.setVisible(true);
          setButtonIcon(i);
          jpanelCentre.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    }
 }

    public void OnSquareButtonPressed(int p){
        
              // Visual check to identify button number which triggered event
//      jlabErrorMsg.setText(String.valueOf(p)); // for debug only
                    
      /* Discover status of button to determine action
       * DISABLED  -  do nothing
       * SELECTED - make it simply ENABLED
       * ENABLED - make it SELECTED
       * ILLUMINATED - if Legal offer move, else flash ILLEGAL and do nothing  
      */ 
      // turn off ILLEGAL MOVE flash
      ksUI.statusDisplay.setMoveIllegal(false);
      //jlabMoveIllegal.setVisible(false);
      int iSel = mainBoard.getSelectedSquare();
      
      Square.Aspect aspect = mainBoard.getAspect(p);
      switch (aspect){
          
          case DISABLED: // click on unavailable square
              if (ksUI.testBoardControl.getTestPieceIndex() > -1){ // This may be positioning a test piece on a "blank" square
                      ksUI.OnPlaceTestPieceAt(p);
                  }
                  else
              
              if (mainBoard.getSelectedSquare() !=0){
                  
                  {
                  // Flash Illegal if a piece was selected
                      ksUI.statusDisplay.flashIllegal(iSel,iSel);
                  }
              }
              break;   // otherwise just don't respond to zero square!
              
          case SELECTED: // click on piece which was already selected
               // Downgrade from SELECTED to simply ENABLED

              //v4.02  reset cursor from icon
                if(ksUI.recorder.IsRecording()) {
                    resetCursor();
                    }

                ksUI.deselect(p); 
              
                break;       
                  
          case ENABLED: // click on own unselected piece - so select it!
                  
                  // Check if another square is currently selected 
                  
                  if (iSel !=0){ // only do this if another square was previously SELECTED   
                  // Downgrade previously SELECTED square to simply ENABLED.
                      ksUI.deselect(iSel);
                  } 
                  
                  // deal with newly SELECTED square
                  
                  ksUI.boardView.setButtonAspect(p, Square.Aspect.SELECTED);

                  // KS V4.2  new animation -
                  //  change mouse icon to piece image and
                  //  square to blank image
                  if (ksUI.recorder.IsRecording() && !ksUI.isDemoMode() ){  //if game is in play and not Demo mode
                      setCursor(p);  // set cursor according to piece in square p
                  }


                  // Request the board to return a list of squares to which piece may move
                  //  then illuminate the squares
                  ksUI.showTries(false);
                 
                  
                  mainBoard.clearLegalMoves();
                  
                  ArrayList<ArrayList<Integer>> wrappedList = mainBoard.setSelectedSquare(p);
                   //a default setting to satisfy compiler
                  ArrayList<Integer>list = wrappedList.get(0); 
                 
                  switch (ksUI.gameMode){
                      case REFEREE:
                      case KSPIEL:
                          list = wrappedList.get(0);    // illuminated List
                          break;
                      case CHESS:
                          list = wrappedList.get(1);    // legal list
                          break;
                        
                  }
                  for (int i=0; i<list.size(); i++){  
                        setButtonAspect(list.get(i), Square.Aspect.ILLUMINATED);
                  }
                  
                  break;
                  
          case ILLUMINATED:
              
                  if (mainBoard.isSquareLegal(p)){
                        // KS v4.3 new animation - reset mouse icon to normal
                      resetCursor();  //v4.2
                      ksUI.playSound("/sounds/thin.bell.au"); // make a move sfx
                      
                     // animate (mainBoard.getSelectedSquare(),p);  // is in v3.03 release. What does i do???
                      
                      ksUI.InitiateMove(mainBoard.getSelectedSquare(),p);
                     
                  }
                  else {
                       // Flash Illegal
                      ksUI.statusDisplay.flashIllegal(iSel, p);
                       
                  }
                  
                      
      }
              
    }

/*
       //this method from V3.04 was omitted from V4.1
      public void setSqInvisible(int p){
      Square sq = mainBoard.getSquare(p);
      sq.setVisible(false);
      setButtonIcon(p);

  }

     //this method from V3.04 was omitted from V4.1
    public void illuminateList( ArrayList<Integer> list){
         for (int i=0; i<list.size(); i++){
             setButtonAspect(list.get(i), Square.Aspect.ILLUMINATED);
         }
    }

    //this method from V3.04 was omitted from V4.1
      public ArrayList<Integer> getListToIlluminate(int flag, int squareNo){
        // flag = 0 - theoretically possible moves
        // flag = 1 - legal moves only
        ArrayList<ArrayList<Integer>> wrappedList = mainBoard.setSelectedSquare(squareNo);
        ArrayList<Integer> list = wrappedList.get(flag);
        return list;
    }

 // this method omitted from V4.1
    private void animate(int iFrom, int iTo){ //v3.03
        
        ArrayList<Integer> squaresList; // the list of intermediate squares in order )
        squaresList = new ArrayList<Integer>();
       
        
       if (getX(iFrom) == getX(iTo)){ 
           if (getY(iFrom)>getY(iTo)) {     
           // NS - add column to squaresList
                for (int y = getY(iFrom); y>getY(iTo)-1; y--){ 
                    squaresList.add(ksUI.ksApp.getP(getX(iTo),y));
                } 
           }else {
                 // NS - add column to squaresList
                for (int y = getY(iFrom); y<getY(iTo)+1; y++){ 
                    squaresList.add(ksUI.ksApp.getP(getX(iTo),y));
                }
           }
                  
          } else if (getY(iFrom) == getY(iTo)){
                  // EW - add row to squaresList
               if (getX(iFrom)>getX(iTo)){ 
                  for (int x = getX(iFrom); x>getX(iTo)-1; x--){ squaresList.add(ksUI.ksApp.getP(x,getY(iTo)));}
               } else {
                  for (int x = getX(iFrom); x<getX(iTo)+1; x++){ squaresList.add(ksUI.ksApp.getP(x,getY(iTo)));}
               }
               
          } else if (getX(iFrom) - getX(iTo) == getY(iFrom) - getY(iTo)){
                  // NE-SW - add diagonal to squaresList
                  squaresList.add(iFrom); 
                  
                  int x = getX(iFrom)+1; int y = getY(iFrom)+1;
                //  while (x<9 && y<9){ squaresList.add(ksUI.ksApp.getP(x,y)); x++; y++; }
                  while (x<getX(iTo)+1 && y<getY(iTo)+1){ squaresList.add(ksUI.ksApp.getP(x,y)); x++; y++; }
                  x = getX(iFrom)-1; y = getY(iFrom)-1;
                  while (x>getX(iTo)-1 && y>getY(iTo)-1){ squaresList.add(ksUI.ksApp.getP(x,y)); x--; y--; }
                 // while (x>0 && y>0){ squaresList.add(ksUI.ksApp.getP(x,y)); x--; y--; }
                  
                                   
           } else if (getX(iFrom) - getX(iTo) == getY(iTo) - getY(iFrom)){
                  // NW-SE
                  squaresList.add(iFrom);
                  
                  int x = getX(iFrom)+1; int y = getY(iFrom)-1;
                  while (x<getX(iTo)+1 && y>getY(iTo)-1){ squaresList.add(ksUI.ksApp.getP(x,y)); x++; y--; }
                  x = getX(iFrom)-1; y = getY(iFrom)+1;
                  while (x>getX(iTo)-1 && y<getY(iTo)+1){ squaresList.add(ksUI.ksApp.getP(x,y)); x--; y++; }                 
                  
           }
              
      //  ksUI.sysout(squaresList.toString()); // test squares list
        
        // start of animation sequence
        // only do this if the list size is >2
        
        // identify the image of the piece which is moving
        // show it briefly at each square in the list except the final, leaving an empty square behind
        
        //  first transition: empty square in [0] and icon in [1] , pause
        //  next transition: empty square in [1] and icon in [2] , pause
        // etc
        
        
        
    }

    */
    
    private JButton doSquare(int i, int j, ImageIcon elIcon, ImageIcon edIcon){
       // Create the button with an associated ActionListener
            final int p = i+8*j; // each ActionListener needs a final unique value

         //   String pos = String.valueOf(getX(p))+String.valueOf(getY(p)); 

            jbtnSquare[p] = new JButton(elIcon); // icon placeholder
 
            jbtnSquare[p].addActionListener(new ActionListener(){
                
                public void actionPerformed(ActionEvent ae) {
                    // The button pressed was p
                    OnSquareButtonPressed(p);                

                }           
            });
            // determine if light or dark square (x,y both odd or even)(1,1 is dark)            
            if ( isDark(p)){
                jbtnSquare[p].setIcon(edIcon); 
               }
            else {
                jbtnSquare[p].setIcon(elIcon);
               }
            
            /* This code adds a locator tooltip to the squares
            String a="";
            switch (i){
                case 1: a="a"; break;
                case 2: a="b"; break;
                case 3: a="c"; break;
                case 4: a="d"; break;
                case 5: a="e"; break;
                case 6: a="f"; break;
                case 7: a="g"; break;
                case 8: a="g"; break;             
            }           
            jbtnSquare[p].setToolTipText(a+String.valueOf(j+1));
            */
            
            //jpanelBoard.add(jbtnSquare[p]);
            return jbtnSquare[p];
            //The jbtnSquares are always enabled: the actions therefrom are determined by the Square object's aspect property
    }
      
 public void setButtonIcon(int p){
      
      /* This method places the appropriate image on every square's button.
       * The images are held in .png files whose names follow the convention:
       * <square shade><piece colour><piece name><button aspect>
       * where:
       *   <Square shade> is l|d|g|p|r = light|dark|green|pine-green|red
       *   <piece colour> is w|b = nameWhite|nameBlack
       *   <button aspect> is <disabled>|<enabled>|<illuminated>|<selected>
       *   image filenames are postfixed x|u|d|f using old C++ loadImageIcon convention
      */
      
      Piece piece = mainBoard.getOccupier(p);
      Square square = mainBoard.getSquare(p);
    
      ImageIcon imgIcon;
    
      // start of v3.02 section
          // v3.02 adds: if invisible square has a test piece of the
          // opponents color && testview is set, show the piece icon
          // else show an empty square
      // find the occupier and test if it my color
      boolean mycolor = false;
      if (piece.color() == Piece.Color.BLACK && ksUI.viewMode == ViewMode.BLACK 
        ||piece.color() == Piece.Color.WHITE && ksUI.viewMode == ViewMode.WHITE)
          mycolor = true;
      
      //if (ksUI.IsTestview() && !mycolor){
        if (!mycolor){
          Piece testpiece;
          if (ksUI.viewMode == ViewMode.BLACK)
            testpiece = square.getTestPiece(Piece.Color.BLACK); /////////!!!
          else
            testpiece = square.getTestPiece(Piece.Color.WHITE); /////////!!!
          
          if (testpiece != null && ksUI.viewMode != ViewMode.NEITHER){
              // it needs its own icon
              imgIcon = testpiece.getIcon(square.getAspect());
          } else {
              // original action needed
               if (square.isVisible()){
              imgIcon = piece.getIcon(square.getAspect());
                }
                else {
            //Square is invisible, so show it as empty
                imgIcon = square.getEmpty().getIcon(square.getAspect());
                }
          }
      }

      else
        // end of v3.02 section : 
          //  this is my color piece and my color view ; I don't want pawn tries against me illuminating !!

      { // original V3.01 code
         if (square.isVisible()){       
              imgIcon = piece.getIcon(square.getAspect());   
          }
          else { 
            //Square is invisible, so show it as empty
                imgIcon = square.getEmpty().getIcon(square.getAspect());
          }
      }
          jbtnSquare[p].setIcon(imgIcon);
       
    
 }
      
  public void displayButtons(){
          // display the buttons
          for (int p = 1; p<65; p++){

              setButtonIcon(p);

          }
  }
    
  public void refreshBoardDisplay(ViewMode view){
      
      // set the view
      switch(view){
          case NEITHER:
              mainBoard.setVisible(false, false);
              break;
              
          case BOTH:
              mainBoard.setVisible(true, true);
              break;
              
          case WHITE:
              mainBoard.setVisible(true, false);
              break;
              
          case BLACK:
              mainBoard.setVisible(false, true);
              break;
              
      }
              
      // refresh the display
      for (int i=1; i<65; i++){
          if (mainBoard.getAspect(i) == Square.Aspect.ILLUMINATED){
              // What does this do????
              // this appears to suppress pawn tries in Chess mode
              // but they still don't show the reds in KS mode
              setButtonAspect(i, Square.Aspect.DISABLED);  //////xxxxxxxxx
                // ?????
          }
          setButtonIcon(i);
      }
  }

  
  public void setButtonAspect(int iSq, Square.Aspect aspect){
    // Ensure that visible aspect matches master record held 
    // by Square object within the main Board object 
    // by updating the Square object here.
    mainBoard.setAspect(iSq, aspect);
    setButtonIcon(iSq);
    
  }
  
  public void clearTries(){
      if (!triesList.isEmpty()){
           for (int i=0; i<triesList.size(); i++){
                        mainBoard.clearTry(triesList.get(i));
                        setButtonIcon(triesList.get(i));
           }
      }
  } 
  
  public void refreshTries(){

      oldTriesList = triesList; // save for Robot
      triesList = mainBoard.createPawnTriesList();
      if (!triesList.isEmpty()){
           for (int i=0; i<triesList.size(); i++){
               setButtonIcon(triesList.get(i));
           }
       }
  }

  public ArrayList<Integer> getOldTries(){ // for Robot v3.3
      return oldTriesList;
  }
  
  public String getTries(){
      String tries="";
      if (!triesList.isEmpty()){
          for (int i=0; i<triesList.size(); i++){
              tries = tries + getCoords(triesList.get(i)) + " ";
          }
      }
      return tries;
  }
  
  public void displayGreens(ArrayList<Integer> greens, ViewMode viewMode){
      
      greenList = greens;
      // Sound effect for piece taken
      if (mainBoard.getRedSquare() != 0)
                          ksUI.playSound("/sounds/that.hurts.au"); // taken sfx
      
      // Display the checked squares
      
      
      if (!greenList.isEmpty()){
          ksUI.playSound("/sounds/danger.au");  // check sfx
               for (int i=0; i<greenList.size(); i++){
                   setButtonIcon(greenList.get(i));
               }              
           }   
           
      if (mainBoard.result.iCheckByKnight !=0){
            ksUI.playSound("/sounds/danger.au");    // modification FS090404.2   
               showKnightCheck(mainBoard.result,viewMode);
               
           }
      
  }
  
  public void showKnightCheck(Result result, ViewMode viewMode){
      // Further green squares must be set according to view mode
      ArrayList<Integer> greenKnightList = new ArrayList<Integer>();
      
               switch(viewMode){
                   case BLACK:
                       // pivot on king (checked) or knight (checking)
                       if (mainBoard.isWhiteToPlay()){
                           greenKnightList = mainBoard.greenKnight(result.iCheckByKnight);
                           
                       } else {
                           greenKnightList = mainBoard.greenKnight(result.iCheckedKing);
                           greenKnightList.add(result.iCheckedKing);
                       }
                       
                       break;
                       
                   case WHITE:
                       // pivot on king (checked) or knight (checking)
                       if (!mainBoard.isWhiteToPlay()){
                           greenKnightList = mainBoard.greenKnight(result.iCheckByKnight);
                           
                       } else {
                           greenKnightList = mainBoard.greenKnight(result.iCheckedKing);
                           greenKnightList.add(result.iCheckedKing);
                       }
                       
                       break;
                       
                   case BOTH:
                       // just show the king in green
                       mainBoard.setGreen(result.iCheckedKing, true);
                       setButtonIcon(result.iCheckedKing);
                       break;
                       
                   case NEITHER:
                       // show the entire empty board in green
                       // this is done directly by the Handover button action
                       break;
               }
               
               for (int i=0; i<greenKnightList.size(); i++){
                   mainBoard.setGreen(greenKnightList.get(i), true);
                   setButtonIcon(greenKnightList.get(i));
               }
      
  }
      
        //functions returning x,y coordinates from index r = 1-64
    // x is column no, y is row no
  
    private int getY(int r){
      int y = 1+(r-1)/8;
      return y;
    }
    private int getX(int r){
      int x = r % 8;
      if (x == 0){ x=8;}
      return x;
    }
    
    public String getCoords(int p){
        return toLetter(getX(p))+String.valueOf(getY(p));
    }
      
    private boolean isDark(int p){
      // determine if dark square (x,y both odd or both even : 1,1 is dark, so is 64,64)
      return (getY(p)%2 == getX(p)%2);
    } 
}
