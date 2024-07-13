package uk.co.kriegspiel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javax.swing.border.*;
import java.awt.Toolkit.*;
import java.lang.System;

import javax.swing.event.*;
import javax.swing.ImageIcon;

/**  the Kriegspiel User Interface Class
 *
 * @author Denys Bennett
 */


public class KSpielUI {
  // External object references
  public KSpielApp ksApp;
  public Board mainBoard;
  public JFrame mainFrame;
  
  // Variables

  private Timer stepTimer;
  private boolean hangup;   // hang-up prevention on exit override
  // active panels & controls
  private JPanel jpanelMain;
  
  // The board object
  private Board oldBoard;   // copy to Undo for Referee mode
  
  private Stack<Board> pastBoards; // contains game hostory for referee mode
  
  public BoardView boardView;
  
  public StatusDisplay statusDisplay;
  
  private MessageBoard messageBoard;
  private LostPieces lostPieces;
  public PostulateControl testBoardControl;
  
  private JPanel jpanelControls;
  
  private JPanel jpanelCentre;  // a blank container holding the board object components

  private JPanel jpanelVLabels;

  private ArrayList<Integer> greenList; // list of squares displaying direction of check

  // Board controls  
  private JButton jbtnRotate;
  
  //The status panel and recorder objects
  private JPanel jpanelLeft;    // a blank container to hold jpanelRecorder & jpanelStatus
  
  private JPanel jpanelRecorder;
  public Recorder recorder;
  
  // public SoundEffects soundEffects;
  
  private JPanel jpanelStatus;
   
  // The opponents control object

  private JPanel jpanelRight;
  private JPanel jpanelOpponents;
  private JList jlistOpponents;
  private JPanel jpanelGameControls;
  private int iSelectedOpponentIndex;
  
  DefaultListModel opponentList;
  DefaultListModel opponentStatus;
  
  private JLabel jlabPlayer;
  private JLabel jlabOpponent;
  
  private JButton jbtnSound;

  private JButton jbtnHelp;
  
  private JButton jbtnSettings;
  
//  private JButton jbtnTest;
//  private boolean testview;
  
  private JButton jbtnConnect;
  private JButton jbtnDisconnect;
  public boolean isConnected;
  private Color lampcolor;
  private JPanel jpnlLamp;

  
  private String namePlayer;    // this is the master record for network name
                                // and is assigned to nameBlack/nameWhite on networked games
  
  private String nameBlack;     // this are the master records to be
  private String nameWhite;     // copied into recording (via recorder) and boardView
                                // these names can be locally entered for shared screen play.
  
  // The Game Control buttons
  
  private JButton jbtnPlayChess;
  private JButton jbtnPlayKS;
  private JButton jbtnResign;
  private JButton jbtnOfferDraw;
  
  private JButton jbtnDemo; // for debug to demo the robots
  private JButton jbtnReferee; // referee mode (KSPIEL gamemode, view both sides)
  private JButton jbtnUndo; // for referee mode
  
  // The Handover panel object
  private JPanel jpanelHandover;
  private JButton jbtnHandover;
  private JButton jbtnTakeover;
  
  // The messageboard object 
  private JTabbedPane jTab;

  private JPanel jpanelMessageboard;
  private JPanel jpanelLostPieces;
  private JPanel jpanelTestBoard;
    
  // Display Dimensions
  public Screensize scrnSize;
  
  // References to the remote player/messenger objects 
  private Remote activeMessenger; // the currently active messenger object

  private Remote blackPlayer;   // who may also be this
  private Remote whitePlayer;   // or alternatively this
  private String nameOpponent;  // this is the master record
  
  // Player modes
  private boolean bBlackIsRemote;
  private boolean bWhiteIsRemote;
  public boolean isWhiteRemote(){ return bWhiteIsRemote;}
  public boolean isBlackRemote(){ return bBlackIsRemote;}
  private Piece.Color playingColor; // resident player
  private Piece.Color opponentColor; // remote player
  private int delayStep;
  // Viewing Modes


  public enum ViewMode{BOTH, WHITE, BLACK, NEITHER} // Which side's pieces can be seen on this screen?
  public ViewMode viewMode;
  // UI Modes
  public enum UIMode{SHARED, SOLEWHITE, SOLEBLACK} // Is the screen input shared by two players, or solely used by one?
  private UIMode uiMode;
  // Game Modes
  public enum GameMode{CHESS, KSPIEL, REFEREE} // Is this Chess or Kriegspiel?
  public GameMode gameMode;
  
  private enum Phase{SHOW, HANDOVER, TAKEOVER}
  
  public enum GameEnd{CHECKMATE, STALEMATE, WHITE_RESIGNS, BLACK_RESIGNS, AGREED_DRAW, ERROR}
  
  public enum OnlineStatus{PERMANENT,ONLINE,CONNECTED,OFFLINE}
  
  
  // Methods
  public KSpielApp getApp(){ return ksApp;}
  
  //system print to console
  public void sysout(String str){
      ksApp.sysout(str);
  }

  public boolean isDemoMode(){
      return (iSelectedOpponentIndex==-1);
  }


    public void OnPlaceTestPieceAt(int p) {
       // throw new UnsupportedOperationException("Not yet implemented");
      //V3.02 Attempts to place test piece in the test view
      // Which piece has been selected?
      int index = testBoardControl.getTestPieceIndex();
      switch(index){
          case -1:
                // nothing selected
              break;
          case 0:
              if (viewMode == ViewMode.BLACK)
                mainBoard.clearTestPiece(p, Piece.Color.BLACK);
              else
                mainBoard.clearTestPiece(p, Piece.Color.WHITE);  
              
              testBoardControl.reset(); // reset it so only one "piece" placed at a time
              
              break;
          case 7:
  //            mainBoard.clearAllTestPieces(playingColor); // shouldn't reach here
              break;
          default:
              // A piece (or empty) has been selected:
              // Can only be placed if not occupied by player's own piece
              if (mainBoard.getOccupier(p).color() != mainBoard.toPlay()){ 

                  if (viewMode == ViewMode.BLACK)
                    mainBoard.setTestPiece(p, index, Piece.Color.WHITE); // needs to !match viewmode
                  else
                    mainBoard.setTestPiece(p, index, Piece.Color.BLACK); // needs to !match viewmode
                  
                  testBoardControl.reset(); // reset it so only one "piece" placed at a time
                  
              } else {
                  testBoardControl.reset();  // has clicked a piece of his own colour

              }

      }
        boardView.refreshBoardDisplay(viewMode);
    }
  public Board getBoardCopy(){  // used in Undo
      Board position = new Board(ksApp,0); // note this always tags board as a master.
      position.copyBoardFrom(mainBoard);
      return position;
  }

  public String getCoords( int iRedSquare){
      return boardView.getCoords(iRedSquare);
  }
  private ViewMode computeView(Phase phase){
      // Phase is relevant for SHARED uiMode only
      switch (gameMode){
          
          case REFEREE:
          case CHESS:
              viewMode = ViewMode.BOTH;
              break;
              
          case KSPIEL:
              
              switch (uiMode){
                  
                  case SHARED:
                      switch (phase){
                          case SHOW:
                              if (mainBoard.isWhiteToPlay()) 
                                  viewMode = ViewMode.BLACK; 
                              else 
                                  viewMode = ViewMode.WHITE;
                              break;
                          case HANDOVER:
                              viewMode = ViewMode.NEITHER;
                              break;
                              
                          case TAKEOVER:
                              if (!mainBoard.isWhiteToPlay()) 
                                  viewMode = ViewMode.BLACK; 
                              else 
                                  viewMode = ViewMode.WHITE;
                              break;
                      }
                      break;
                      
                  case SOLEWHITE:
                      viewMode = ViewMode.WHITE;
                      break;
                      
                  case SOLEBLACK:
                      viewMode = ViewMode.BLACK;
                      break;
              }
              
              break;
              
      }
      
      return viewMode;
  }

  public void InitiateMove(int iFrom, int iTo) {
  
    // offer the move to the Board
      
       // Ask board to reset Greens & Reds
       mainBoard.setBoardGreens(false);
       // redraw the buttons
       for (int i=1; i<65; i++){
           boardView.setButtonIcon(i);
       }
       boolean startRemoteGame = false;
       if (iFrom == 0 && iTo == 0){ // signal for me to start as white, from remote player
           
           startRemoteGame = true;
       }
       
       //////  THE MOVE TO BE MADE  //////////////////
          

       if (!startRemoteGame){
           

           
           oldBoard = getBoardCopy();   // save position before move
           
           pushBoard(oldBoard);

           // the following statement makes the move!
           mainBoard.makeMove(iFrom, iTo);
          
           if (mainBoard.result.bQueenedPawn && ksApp.myCfg.rules.bAnnounceQueens){   //V3.02
              playSound("/sounds/techno_machine.au"); // queened pawn sfx FS090404.6 
              
           }
           
           // get potentially checked color  v3.02
           Piece.Color checkedColor;
           if (mainBoard.isWhiteToPlay())
               checkedColor = Piece.Color.WHITE;
           else
               checkedColor = Piece.Color.BLACK;
           //
           //xv3.02 mainBoard.result = mainBoard.createCheckingList(iTo);
           mainBoard.result = mainBoard.createCheckingList(checkedColor);
           if (mainBoard.result.iRedSquare !=0){
               // a piece has been taken, so put its image in the referee's capture panel
               lostPieces.addLost(mainBoard.result.capture, mainBoard.getMovesMade());
           }
           
           
           
           
           recorder.RecordMove(iFrom, iTo); // swapped with line above in 3.00.5
           
           jbtnUndo.setEnabled(true);
           
       }
       
       
       /////// IT IS NOW THE OTHER SIDE'S TURN //////////

       // Flash Results

       statusDisplay.flashResults(mainBoard.result);

       // Redisplay squares and change aspects.
       boardView.clearTries();   // is this the tries problem fix??
       
       mainBoard.disablePlayers();

       // update the board display

       viewMode = computeView(Phase.SHOW);
       refreshBoardDisplay(viewMode); // turn off move illumination

           // Redraw buttons which have Check displays (will show up in Green / Pine)

           // Special arrangements are needed in the case of Check by a Knight
           //  If playing Chess, just green the king
           //  If playing KSpiel, the checked King's viewMode is green squares a Knights move from king
           //                 but the checking Knight's viewMode is green squares a Knight's move from Knight
           //                 and in shared Handover mode, viewMode is whole board green & pine.

           // Ask the board to add to the greenlist accordingly...


           // redisplay the squares which are now green

       greenList = mainBoard.getGreenList(); // also causes Board to mark squares green
           // redisplay the buttons signalled green
       boardView.displayGreens(greenList, viewMode);
       
       OnReceiveMessage(statusDisplay.getMovesSoFar());

           // HANDOVER PHASE
           
           // In the case of a SHARED UI, a blank screen is interposed between
           // alternating views of the piece colours.
           
           // In the case of a Remote user, the view remains the same, but the input is disabled
           // until the remote move has been completed (and shown if playing CHESS).
          
       // toggle the To Play indicators
       statusDisplay.setWhiteToPlay(mainBoard.isWhiteToPlay());
       //boardView.setWhiteToPlay(mainBoard.isWhiteToPlay());

       
       
       if (gameMode == GameMode.KSPIEL && uiMode == UIMode.SHARED) {
           // A screen Handover is required
           jbtnHandover.setEnabled(true);
           jbtnHandover.setVisible(true);
           jbtnResign.setEnabled(false);
           //jbtnResign.setVisible(false);
       } else {
           
           // A screen handover is not required
           
           // either or both players may be remote - if so, send the move
           if ( mainBoard.isWhiteToPlay() && isWhiteRemote()){
               // send move to White player at remote terminal
               whitePlayer.SendMove(iFrom, iTo, mainBoard.getMovesMade());
           }
           if ((!mainBoard.isWhiteToPlay()) && isBlackRemote()){
               //send move to Black player at remote terminal
               blackPlayer.SendMove(iFrom, iTo, mainBoard.getMovesMade()); 
           }

       }
           
           
       
       // Test for end of Game
       if (mainBoard.result.bCheckmate || mainBoard.result.bStalemate
               || mainBoard.result.error != Result.ErrorCode.NO_ERROR){
           // GAME OVER
           if (mainBoard.result.bCheckmate){
                   
                   // redisplay the squares which are now green
               greenList = mainBoard.getGreenList();
                   // redisplay the buttons signalled green
               if (!greenList.isEmpty()){
                   for (int i=0; i<greenList.size(); i++){
                       boardView.setButtonIcon(greenList.get(i));
                   }              
               }
                   // don't bother with knight check display here, king is probably greened anyway

               gameEnd(GameEnd.CHECKMATE);
           } else {
               if (mainBoard.result.bStalemate){
               gameEnd(GameEnd.STALEMATE);
               } else {
                   gameEnd(GameEnd.ERROR);
               }
           }
           
           
           //  stop all the clocks and disable the players' pieces
           

       } // end of test for end of game
           
       // Enable player's pieces as necessary
                    
       if (gameMode == GameMode.KSPIEL && uiMode == UIMode.SHARED){
           // Kriegspiel with shared screen. Do nothing:
           // - pieces will be enabled when the jbtnTakeover action phase is activated
       } else {
           // Chess, or Kriegspiel with a remote player
           // Enable the player's pieces if he is to play AND is local to this machine
           if (mainBoard.isWhiteToPlay()){
                // white is to play            
                if (!isWhiteRemote()){
                    // if white is local:
                    mainBoard.enablePlayer(Piece.Color.WHITE);
                 
                 //   if (gameMode == GameMode.KSPIEL || gameMode == GameMode.REFEREE) Xv3.02 
                 //       showTries(true);
                        //boardView.refreshTries();
                }

            }else {
            // black is to play                
                if (!isBlackRemote()){
                    // if black is local
                    mainBoard.enablePlayer(Piece.Color.BLACK);
                 
                   // if (gameMode == GameMode.KSPIEL || gameMode == GameMode.REFEREE) Xv3.02
                   //     showTries(true);
                        //boardView.refreshTries();
                }
            }
           
           if (gameMode == GameMode.KSPIEL || gameMode == GameMode.REFEREE) //V3.02
               showTries(true);
        
        } // 
       
        
      }
 
  public void showTries(boolean show){
      if (show){
          String refereeMessage = "";
          boardView.refreshTries();
          
          if (boardView.getTries().length()>0){
         //   jlabTries.setText("Tries:"+boardView.getTries());                
            
              refereeMessage  = "Tries:"+boardView.getTries();
              statusDisplay.setTries(refereeMessage);
            
              if (mainBoard.isWhiteToPlay()){
                 refereeMessage = "White has "+ refereeMessage; 
              }
              else {
                  refereeMessage = "Black has "+ refereeMessage;
              }
            
            OnReceiveMessage(refereeMessage);
          }
          
      }
      else{
          boardView.clearTries();
        //  statusDisplay.showTries(0);
        //  jlabTries.setText("");
          statusDisplay.setTries("");
      }
  }
 
  public void errorMsg(String msg){
      sysout(msg);
      OnReceiveMessage(msg);

      if (recorder != null){
          recorder.Pause();
          recorder.endsWith(GameEnd.ERROR);
      }
  }

  public ImageIcon loadImageIcon(String imageF) {
      java.net.URL imageURL = KSpielApp.class.getResource(imageF);
      if (imageURL == null){
          imageURL = KSpielApp.class.getResource("/images/icon_warning.gif");
          errorMsg("<html>Missing icon file<br>"+imageF);
      }
      ImageIcon i = new ImageIcon(imageURL);
      return i;
  }
  
  private void refreshBoardDisplay(ViewMode view){
      
  //     boardView.setBoard(mainBoard);    // added 3.00.5
      boardView.refreshBoardDisplay(view);
    //  int check = mainBoard.getMovesMade();
      statusDisplay.setMoveCount(mainBoard);
      statusDisplay.setWhiteToPlay(mainBoard.isWhiteToPlay()); //V3.02
    //  check = mainBoard.getMovesMade();
   //   jlabMoveCount.setText("Moves so far: " + mainBoard.getMovesMade());
 
      
  }

  // Board Event Action Responders
  public void deselect(int p){
      // Downgrade square from SELECTED to simply ENABLED
        // In this order... 
        // Remove record of this square as being SELECTED.
        mainBoard.setSelectedSquare(0);
        // Change aspect of this square.
        boardView.setButtonAspect(p, Square.Aspect.ENABLED);
               
        refreshBoardDisplay(viewMode);
  }
  
  /**
   * This method is used by the replay recorder to toggle a square's illumination
   * @param p - the square number
   * @param a - the Aspect: ILLUMINATED, SELECTED, DISABLED, ENABLED
   */
  public void setAspect(int p, Square.Aspect a){
      // 
      boardView.setButtonAspect(p, a);
      
  }
  
  public void OnSquareButtonPressed(int p) {
           
      // All this now in BoardView
      boardView.OnSquareButtonPressed(p);

 }

  public void StepDisplay(final int iFrom, final int iTo){
      // for Demo mode -makes the display do one step, with illumination of choice followed by move
           setAspect(iFrom, Square.Aspect.SELECTED);
           setAspect(iTo, Square.Aspect.ILLUMINATED);

           // put a pause in here to show illuminated squares

            ActionListener alStep = new ActionListener(){
                public void actionPerformed(ActionEvent ae){

            // make the move after the delay

                        InitiateMove(iFrom,iTo);

                }
               };

               // set the delay in a non-repeating timer
             stepTimer = new Timer(delayStep, alStep);
             stepTimer.setRepeats(false);
             stepTimer.start();


  }

  public void resetTestView(){
      testBoardControl.reset();
  }
  
  public int getTestPieceIndex(){
      return testBoardControl.getTestPieceIndex();
  }
  private void OnHandover(){
      jbtnHandover.setEnabled(false);
              jbtnTakeover.setEnabled(true);
              jbtnTakeover.setVisible(true);
              // update the board display

              viewMode = computeView(Phase.HANDOVER);
              if (mainBoard.result.iCheckByKnight !=0){
                  // we need to green the whole board
                  mainBoard.setBoardGreens(true);
              }
              refreshBoardDisplay(viewMode);

              resetTestView();

              //jbtnTest.setEnabled(false);
  }
  
  private void OnTakeover(){
              jbtnResign.setVisible(true);
              jbtnResign.setEnabled(true);
              jbtnTakeover.setEnabled(false);
              // update the board display
              if (mainBoard.isWhiteToPlay()){
                  mainBoard.enablePlayer(Piece.Color.WHITE);

              }else {
                  mainBoard.enablePlayer(Piece.Color.BLACK);

              } 
              viewMode = computeView(Phase.TAKEOVER);
              // we may need to recompute the greens
              if (mainBoard.result.iCheckByKnight !=0){
                  mainBoard.setBoardGreens(false);
                  boardView.displayGreens(greenList, viewMode);
              }
           
              showTries(true);
              refreshBoardDisplay(viewMode);
              testBoardControl.loadPieces(viewMode);

  }
  
  private void makeHandoverDisplay(){
    
    jpanelHandover = new JPanel();
    jpanelHandover.setPreferredSize(scrnSize.dimHandover);
 
    jbtnHandover = new JButton("Handover");
      jbtnHandover.setVisible(false);
      jbtnHandover.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){
              OnHandover();
          }
          
      });
      
      jbtnTakeover = new JButton("Takeover");
      jbtnTakeover.setVisible(false);
      jbtnTakeover.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){
              OnTakeover();
          }
          
      });
      
      jpanelHandover.add(jbtnHandover);
      jpanelHandover.add(jbtnTakeover);
    
  }
  
  public Border borderTitle(String title){
      Border etched = BorderFactory.createEtchedBorder();
      Border titled = BorderFactory.createTitledBorder(etched, title);
      return titled;
  }
 
  
  //Main entry point for App to initialise UI
  public void Initialise(ViewMode viewMode, UIMode uimode, GameMode game){
      
      this.viewMode = viewMode;
      uiMode = uimode;
      gameMode = game;

      // initialise two robot players by assigning remote references

      blackPlayer = ksApp.robotBlack;  // the actual black robot
      whitePlayer = ksApp.robotWhite;  // the actual white

      
      // display the buttons
      boardView.displayButtons();
    
      statusDisplay.setWhiteToPlay(mainBoard.isWhiteToPlay());

      // soundEffects.setSound(ksApp.myCfg.soundOn);
      playSound("/sounds/roar.au");       // start the game sfx
     
  }
   
  public void resetOpponentIndex(){
      iSelectedOpponentIndex = 0;   // needed to turn off robot!
      jlistOpponents.setSelectedIndex(iSelectedOpponentIndex);
      setOpponentName("");
  }
  
  public void setOpponentNames(){
      
      switch (iSelectedOpponentIndex){
          case -1:
              nameBlack = "Demo robot";
              nameWhite = nameBlack;
              break;
          case 0:   // shared local screen
              if (ksApp.myCfg != null){
                  nameBlack = ksApp.myCfg.nameBlack; // might leave this alone for manual setting
                  nameWhite = ksApp.myCfg.nameWhite; // and not losing them after
              }
              break;
         
          default:  // vs robot or remote player
              if (isBlackRemote()){
                  nameBlack = opponentList.get(iSelectedOpponentIndex).toString();
                  nameWhite = namePlayer;
              }
              else {
                  nameWhite = opponentList.get(iSelectedOpponentIndex).toString();
                  nameBlack = namePlayer;
              }
              
      }
      setNames(nameWhite, nameBlack);   // copy the names into the slave records
      

  }
  public void gameEnd(GameEnd ending){
      
      recorder.endsWith(ending); // turn off the recorder.
      
      //V3.03 tidy away test pieces
      mainBoard.clearAllTestPieces(Piece.Color.BLACK);
      mainBoard.clearAllTestPieces(Piece.Color.WHITE);

      //v4/02
      boardView.resetCursor();  //v4.02

      viewMode = ViewMode.BOTH;
      refreshBoardDisplay(viewMode);
      jbtnResign.setVisible(false);
      jbtnOfferDraw.setVisible(false);
      jbtnPlayKS.setVisible(true);
      jbtnPlayChess.setVisible(true);
      jbtnDemo.setVisible(true);
      jbtnReferee.setVisible(true);
      jbtnUndo.setVisible(false);
     // jpanelMessageboard.setVisible(true);
      jpanelHandover.setVisible(false);
      if (ending == null){
          // may be a recording of an unfinished game
      } else {
      
          OnReceiveMessage("Game ended with "+ ending.toString()); //V3.02
          switch (ending){

              case CHECKMATE:
                  break;
              case STALEMATE:
                  break;
              case WHITE_RESIGNS:
                  statusDisplay.setWhiteResigns(true);
                  mainBoard.result.bWhiteResigns = true;
                  mainBoard.result.bWhiteResigns = true;
                  break;
              case BLACK_RESIGNS:
                  statusDisplay.setBlackResigns(true);
                  mainBoard.result.bBlackResigns = true;
                  mainBoard.result.bBlackResigns = true;
                  break;
              case AGREED_DRAW:
                  statusDisplay.setAgreedDraw(true);
                  mainBoard.result.bAgreedDraw = true;
                  mainBoard.result.bAgreedDraw = true;
                  break;
              case ERROR:
                // This could do with some work-up
                //  jlabErrorMsg.setVisible(true);
                  break;
          }
          if (activeMessenger != null){
              activeMessenger.GameEnd();
          }
      }
      resetOpponentIndex();
      showTries(false);
      
      playSound("/sounds/gong.au"); // end of game sfx
      
   
  }
  
  // Player controls methods
  public void setConnectToolTip(){
      if (ksApp.myCfg.protocol.equals("Skype")){
        jbtnConnect.setToolTipText("Attach to Skype application - Skype must be running!");  
      } else {
        jbtnConnect.setToolTipText("Connect to LAN server - make sure settings are correct!");
      }
  }
  private void makeOpponentsDisplay(){
 
      jpanelOpponents = new JPanel();
      jpanelOpponents.setBorder(borderTitle("Opponents"));
   
      jpanelOpponents.setPreferredSize(scrnSize.dimOpponents);
      
      
      
      jbtnConnect = new JButton("Connect");
      
      jbtnDisconnect = new JButton("Disconnect");
      isConnected = false;
      jbtnDisconnect.setVisible(isConnected);
      jpanelOpponents.add(jbtnConnect);
      jpanelOpponents.add(jbtnDisconnect);
      
      // set up the "Connected" lamp
      jpnlLamp = new JPanel();
      jpnlLamp.setPreferredSize(new Dimension(10,10));
      
      jpanelOpponents.add(jpnlLamp);   
      offLamp();  // set it to off
      
      jbtnConnect.addActionListener(new ActionListener(){
          // Logs in to game server
          public void actionPerformed(ActionEvent ae){ 
              
             OnConnect();
          }
          
      });
      
      jbtnDisconnect.addActionListener(new ActionListener(){
          // Disconnects from game server
          public void actionPerformed(ActionEvent ae){ 
              
              OnDisconnect();
            
          }
          
      });
      
      // create the opponents list
      opponentList = new DefaultListModel();
      opponentStatus = new DefaultListModel();
      
      
      namePlayer = "";  // initialise this - if it's null, addOpponent will crash
      addOpponent("Local Opponent", OnlineStatus.PERMANENT);
      addOpponent("Robot", OnlineStatus.PERMANENT);

      
      jlistOpponents = new JList(opponentList);
      
      // attach cell rendering
      final MyListCellRenderer renderer = new MyListCellRenderer(this);
      jlistOpponents.setCellRenderer(renderer);
      
      
      iSelectedOpponentIndex = 0;   //default to Local Opponent
      jlistOpponents.setSelectedIndex(iSelectedOpponentIndex);// default setting to local opponent
     
      // iSelectedOpponentIndex = ...
      // Local == 0
      // Robot == 1
      // On-line opponents == 2,3, etc
      // Demo mode == -1, set by Demo button
      
      jlistOpponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      jlistOpponents.setLayoutOrientation(JList.VERTICAL);
      
      jlistOpponents.addListSelectionListener(new ListSelectionListener() {
          public void valueChanged(ListSelectionEvent le) {
              // Get index of selected item
          //    int index = iSelectedOpponentIndex; // save old value in case I need it
              iSelectedOpponentIndex = jlistOpponents.getSelectedIndex();
              if (iSelectedOpponentIndex>1 ){
                      if (opponentStatus.get(iSelectedOpponentIndex) != OnlineStatus.OFFLINE){

                        nameOpponent = opponentList.get(iSelectedOpponentIndex).toString();
                        activeMessenger.AdviseOpponent(nameOpponent); 

                        } else {
                          OnReceiveMessage("The other player needs to be on-line");
                        }
                }
              
          }
      });
  
      JScrollPane listScroller = new JScrollPane(jlistOpponents);
 
      listScroller.setPreferredSize(scrnSize.dimList);
      
      jpanelOpponents.add(listScroller);
      
      jlabPlayer = new JLabel(namePlayer);
      jlabOpponent = new JLabel(nameOpponent);
      jpanelOpponents.add(jlabPlayer);
      jpanelOpponents.add(jlabOpponent);
      
  }
  public void setConnectButton(boolean connect){
      // a true value gives Connect
      // a false value gives Disconnect
      jbtnConnect.setVisible(connect);  // set visible button
      jbtnDisconnect.setVisible(!connect);
  }
  public void enableConnectButton(boolean enable){
      // enables/disables both Connect and Disconnect buttons
      jbtnConnect.setEnabled(enable);
      jbtnDisconnect.setEnabled(enable);
  }
  
  public void OnConnect(){
              
               // open dialog to identify game server location
              // and make connection to game server
              isConnected = loginToServer();
              
           //   if (isConnected) onLamp(); // set lamp to "on"  // do this in remote client
            
              jbtnConnect.setVisible(!isConnected);  // set visible button
              jbtnDisconnect.setVisible(isConnected);
  }
  
  public void OnDisconnect(){
        
      if (activeMessenger != null && isConnected){
              activeMessenger.DisconnectServer();
              jbtnConnect.setVisible(true);
              jbtnDisconnect.setVisible(false);
              resetOpponentIndex();
              //removeOpponents();  //to be initiated by activeMessenger
              //offLamp();          //to be initiated by activeMessenger!
      }
  }
  
  public void addOpponent(String Opponent, OnlineStatus status){
      
      
      if (Opponent.contentEquals(namePlayer)){
          // not used with Skype, used with Jeeva
          // may be better to exclude this rather than add.
          // just set playername in display instead??
          Opponent = Opponent+"**";
          status = OnlineStatus.OFFLINE;
      }
      
      
      opponentList.addElement(Opponent);
      opponentStatus.addElement(status);

  }
  public OnlineStatus getOnlineStatus(int index){
      OnlineStatus status = (OnlineStatus) opponentStatus.get(index);
      return status;
  }
  
  public void updateOpponent(String Opponent, OnlineStatus status){
      int index =opponentList.indexOf(Opponent);
      OnlineStatus old = (OnlineStatus) opponentStatus.get(index);
      if (status != old){
          sysout("New status for "+Opponent);
          opponentStatus.set(index, status);
          // refresh opponent display
          jpanelOpponents.repaint();
         
      }
  }
  
  public void setOnlineStatus(String opponent, OnlineStatus status){
      int index = opponentList.indexOf(opponent);
      opponentStatus.set(index,status);
  }
  public void removeOpponent(String Opponent){
      opponentStatus.remove(opponentList.indexOf(Opponent));
      opponentList.removeElement(Opponent);
  }
  
  public void removeOpponents(){
      int size = opponentList.size();
      if (opponentList.size()>2){
          opponentStatus.clear();
          opponentList.clear();
          addOpponent("Local Opponent", OnlineStatus.PERMANENT);
          addOpponent("Robot", OnlineStatus.PERMANENT);
          resetOpponentIndex();
      }
   
  }
  
  public boolean loginToServer(){
       
       boolean bLogin = false;
       
       if (ksApp.myCfg.protocol.equals("Skype")){
           // activeMessenger = ksApp.skypeClient;
       } else if (ksApp.myCfg.protocol.equals("Jeeva")){
           activeMessenger = ksApp.jeevaClient;
       } else {
           
           JOptionPane.showMessageDialog(mainFrame,"Unknown protocol!", "Protocol Error", JOptionPane.WARNING_MESSAGE);
           return false; 
       }
        
       
       // nb in following call, SkypeMessenger ignores all but playerName 
       bLogin = activeMessenger.ConnectToServer(ksApp.myCfg.PlayerName, 
                        ksApp.myCfg.JeevaServerName, Integer.valueOf(ksApp.myCfg.JeevaPort));

       return bLogin;
    }
  // The connection lamp is DARK_GRAY when off, or lampcolor when on.
  // - the lampcolor can be set to another color, default is GREEEN
  
  public void onLamp(Color color){
      lampcolor = color;
      jpnlLamp.setBackground(lampcolor);
      
      if (lampcolor == Color.RED){
          enableConnectButton(false); // Connect/Disconnect disabled
          messageBoard.enableInput(false);

      }
      if (lampcolor == Color.GREEN){
          setConnectButton(false);  // Disconnect enabled
          enableConnectButton(true);
          messageBoard.enableInput(true);
          
      }
  }
  
  public void offLamp(){
      lampcolor = Color.DARK_GRAY;
      jpnlLamp.setBackground(Color.DARK_GRAY);
      enableConnectButton(true); // Connect enabled
      setConnectButton(true);
      messageBoard.enableInput(false);

  }
  
  private void setLocalMode(){
    
      bWhiteIsRemote = false;
      bBlackIsRemote = false;
      uiMode = UIMode.SHARED;
      if (gameMode == GameMode.CHESS){
          viewMode = ViewMode.BOTH;
          jpanelHandover.setVisible(false); // handover panel not required
          // the messageboard panel may be visible if active (e.g. recorder replaying a remote game) 
      }
      else
      {
          // Handover / Takeover mode
           
          viewMode = ViewMode.WHITE; // initial setting
          jpanelHandover.setVisible(true);      // handover panel specifically required
        
      }
  }

  private void setRemoteMode(Piece.Color color){
      // color is the color of THIS UI player
      jpanelHandover.setVisible(false);
      jbtnOfferDraw.setVisible(true);   // only offer draws to robots
    // set the view and assign the remote player object
      switch (color){
          case BLACK:
              
              bWhiteIsRemote = true;
              bBlackIsRemote = false;
              uiMode = UIMode.SOLEBLACK;
 
              if (gameMode == GameMode.CHESS){
                  viewMode = ViewMode.BOTH;
              } else {
                  viewMode = ViewMode.BLACK;
              }
              if (iSelectedOpponentIndex>1)
                  whitePlayer = activeMessenger; //the remote player object in the list
              else
                  whitePlayer = ksApp.robotWhite;
              break;
          case WHITE:
              
              bWhiteIsRemote = false;
              bBlackIsRemote = true;
              uiMode = UIMode.SOLEWHITE;
              if (gameMode == GameMode.CHESS){
                  viewMode = ViewMode.BOTH;
              } else {
                  viewMode = ViewMode.WHITE;
              }
              if (iSelectedOpponentIndex>1)
                  blackPlayer = activeMessenger; //the remote player in the list
              else
                  blackPlayer = ksApp.robotBlack;
              break;
      }
     
  }
  
  private void makeGameControlsDisplay(){
      
      jpanelGameControls = new JPanel();

      jpanelGameControls.setBorder(borderTitle("Game Controls"));
      
      jpanelGameControls.setPreferredSize(scrnSize.dimGameControls);
      
      jbtnResign = new JButton("Resign");
      jbtnResign.setVisible(false);
      jbtnResign.addActionListener(new ActionListener(){
          // Ends Game with resignation
          public void actionPerformed(ActionEvent ae){ OnResign(); // false from button, true from remote 
          }
          });
      
      jbtnOfferDraw = new JButton("Offer Draw");
      jbtnOfferDraw.setVisible(false);
      jbtnOfferDraw.addActionListener(new ActionListener(){
          // Sends remote player offer of a draw
          public void actionPerformed(ActionEvent ae){ OnOfferDraw();  
          }
          });
          
      jbtnPlayKS = new JButton("Kriegspiel");
      jbtnPlayKS.setToolTipText("Challenge highlighted opponent to Kriegspiel");
      jbtnPlayKS.addActionListener(new ActionListener(){ 
          // Starts new Kriegspiel Game
          public void actionPerformed(ActionEvent ae){
              OnKriegspiel();
          }
          
      });
      
      jbtnPlayChess = new JButton("Chess");
      jbtnPlayChess.setToolTipText("Challenge highlighted opponent to Chess");
      jbtnPlayChess.addActionListener(new ActionListener(){
          // Starts new Chess Game
          public void actionPerformed(ActionEvent ae){
              OnChess();

          }
      });
      
      jbtnDemo = new JButton("Demo");
      jbtnDemo.setToolTipText("Exercise blind robots");
      jbtnDemo.addActionListener(new ActionListener(){
          // Starts new demo game robot vs robot
          public void actionPerformed(ActionEvent ae){ 
              OnDemo();
              
          }         
      });
      
      jbtnReferee = new JButton("Referee");
      jbtnReferee.setToolTipText("Referee Kriegspiel");
      jbtnReferee.addActionListener(new ActionListener(){
          // Starts new demo game robot vs robot
          public void actionPerformed(ActionEvent ae){  
              OnReferee();
              
          }         
      });
      
      jbtnUndo = new JButton("Undo");
      jbtnUndo.setToolTipText("Undo last move");
      jbtnUndo.setVisible(false);
      jbtnUndo.addActionListener(new ActionListener(){
          // Starts new demo game robot vs robot
          public void actionPerformed(ActionEvent ae){              
          
              OnUndo();
              
               
          }         
      });
      
      jpanelGameControls.add(jbtnResign);
      jpanelGameControls.add(jbtnPlayKS);
      jpanelGameControls.add(jbtnPlayChess);
      jpanelGameControls.add(jbtnDemo);
      jpanelGameControls.add(jbtnReferee);
      jpanelGameControls.add(jbtnUndo);
      jpanelGameControls.add(jbtnOfferDraw);
      
  }
  
  private void OnKriegspiel(){
      jTab.removeAll();
      jTab.addTab("Messages", messageBoard.getMessageboard());
      jTab.addTab("Piece menu", testBoardControl.getPostulateControl());
      SendChallenge(iSelectedOpponentIndex,GameMode.KSPIEL );
  }
  private void OnChess(){
      jTab.removeAll();
      jTab.addTab("Messages", messageBoard.getMessageboard());
      SendChallenge(iSelectedOpponentIndex,GameMode.CHESS ); 
  }
  private void OnReferee(){
      iSelectedOpponentIndex = -1; // signals a demo run
      jTab.removeAll();
      jTab.addTab("Messages", messageBoard.getMessageboard());
      jTab.addTab("Lost Pieces", lostPieces.getLostPieces());
      recorder.setRecordingMode(true);
              //recorder.enableBackstep(true); // added3.00.5
      jbtnUndo.setEnabled(false);
      jbtnUndo.setVisible(true);
      resetOpponentIndex();   // local mode only
      SendChallenge(iSelectedOpponentIndex,GameMode.REFEREE );  
  }
  private void OnDemo(){
      jTab.removeAll();
      jTab.addTab("Messages", messageBoard.getMessageboard());
      jTab.addTab("Lost Pieces", lostPieces.getLostPieces());
      iSelectedOpponentIndex = -1; // signals a demo run
      recorder.setRecordingMode(true);
      gameStart(GameMode.CHESS);    
  }
  private void pushBoard(Board save){
            pastBoards.push(save);
  }
  private Board popBoard(){
     
      return pastBoards.pop();
  }
  private void OnUndo(){
      
      mainBoard = popBoard();

      boardView.setBoard(mainBoard);

      int sel = mainBoard.getSelectedSquare();
      mainBoard.setSelectedSquare(0);
      mainBoard.setAspect(sel, Square.Aspect.ENABLED);
    
      //V3.02
      // get potentially checked color  v3.02
      Piece.Color checkedColor;
      if (mainBoard.isWhiteToPlay())
          checkedColor = Piece.Color.WHITE;
      else
          checkedColor = Piece.Color.BLACK;           
      mainBoard.result = mainBoard.createCheckingList(checkedColor);
      ////

      refreshBoardDisplay(viewMode);
      
      recorder.undoLast();
      lostPieces.undoMove(mainBoard.getMovesMade()+1);
      if (pastBoards.isEmpty()){
        jbtnUndo.setEnabled(false);

      } 
      
      OnReceiveMessage("Undo to "+statusDisplay.getMovesSoFar()); // post message on messageboard
      
      jbtnUndo.setVisible(true);
  }
  
  public void setGamesButtonsVisible(boolean visible){ //V3.02
      if (jbtnPlayKS != null){ //V3.02 safety check
          jbtnPlayKS.setVisible(visible);
          jbtnPlayChess.setVisible(visible);
          jbtnDemo.setVisible(visible);
          jbtnReferee.setVisible(visible);
      }
  }
  
  public void setResignButtonVisible(boolean visible){
      jbtnResign.setVisible(visible);
 
  }
  
  public void OnOfferDraw(){
      if (iSelectedOpponentIndex>1)  // don't send this to the robot
            activeMessenger.SendOfferDraw();
      else {
          
              OnReceiveMessage("The Robot accepts your offer of a draw");
              OnReceiveOfferDrawReply(true);
          
      }
  }
  public void OnReceiveOfferDraw(){
      if (MessageBox.yesno(nameOpponent+" is offering you a Draw. Accept?") == 0){
          activeMessenger.SendAcceptDraw(true);
          gameEnd(GameEnd.AGREED_DRAW);
      } else {
          activeMessenger.SendAcceptDraw(false);
      }
      
  }
  public void OnReceiveOfferDrawReply(boolean accept){
      if (accept){
          // game ends with agreed draw
          gameEnd(GameEnd.AGREED_DRAW);
      } else
          playSound("/sounds/drip.au"); // rejection sfx
  }
  
  public void SendChallenge(int iOpponentIndex, GameMode game){
      
      gameMode = game;  // set the game mode
      nameOpponent = opponentList.get(iSelectedOpponentIndex).toString();
      
      // check that he is not challenging himself
      if ( nameOpponent.endsWith(namePlayer+"**")){
          iSelectedOpponentIndex= 0; //default to Local Opponent
          jlistOpponents.setSelectedIndex(iSelectedOpponentIndex);// default setting to local opponent

          OnReceiveMessage("**You can't challenge yourself!!"); 
          
      } else {

      // Prepare a Black/White decision for remote/robotic game
          Random generator = new Random();
          int randomIndex= generator.nextInt(2); // generates random integer between 0 and 1 (1 less than 2)
        
          if (randomIndex == 1)  {
              playingColor = Piece.Color.BLACK;
              opponentColor = Piece.Color.WHITE;
              nameWhite = nameOpponent;
              nameBlack = namePlayer;
              bWhiteIsRemote = true;
              bBlackIsRemote = false;

          }
          else {
              playingColor = Piece.Color.WHITE;
              opponentColor = Piece.Color.BLACK;
              nameWhite = namePlayer;
              nameBlack = nameOpponent;
              bWhiteIsRemote = false;
              bBlackIsRemote = true;

          }
          
          if (iOpponentIndex<2){

              // local player, robot or demo mode, just start directly

              recorder.setRecordingMode(true);
              
              gameStart(game);

              } else {

                  // Challenge the remote player

                  activeMessenger.SendChallenge(nameOpponent, gameMode, opponentColor, ksApp.myCfg.rules);

              }
          }
  }
  // Await response as follows  
  public void OnReceiveChallengeResponse(boolean accept){
      
      if(accept){
          // Start the game
          recorder.setRecordingMode(true);
          playSound("/sounds/danger.au");   // accept challenge sfx
          gameStart(gameMode);
      } else
          playSound("/sounds/drip.au"); // rejection sfx
  }
  
  public void OnReceiveChallenge(String strOpponent, GameMode game, 
          Piece.Color color, RuleSet rules){
      if (MessageBox.yesno(strOpponent+" is challenging you to a game of "+game.toString() + 
              " and you to play " +color.toString()+ 
              ", announcing  " + rules.getAnnouncement()+". Accept?") == 0){
          
         // SendAcceptChallenge(true);
          playingColor = color;
          gameMode = game;
          nameOpponent = strOpponent;
          iSelectedOpponentIndex = opponentList.indexOf(nameOpponent);
          SendAcceptChallenge(true);
         // if (iSelectedOpponentIndex < 2) iSelectedOpponentIndex = 99; // fix in case opponent not found!
          // test if rules are same as mine
          if (!ksApp.myCfg.rules.isSameAs(rules)){
              // change my rules
              ksApp.myCfg.rules = rules;
              OnReceiveMessage("Rules changed to "+ksApp.myCfg.rules.getAnnouncement());
          }

          setRemoteMode(playingColor);
          recorder.setRecordingMode(true);
          jTab.removeAll();
          jTab.addTab("Messages", messageBoard.getMessageboard());
          jTab.addTab("Piece menu", testBoardControl.getPostulateControl());
          
          gameStart(gameMode);
          
          
          
      } else {
          
          SendAcceptChallenge(false);
      }
  }
  
  private void SendAcceptChallenge(boolean accept){
      activeMessenger.SendAcceptChallenge(accept);
  }
  public void OnReceiveResign(){
      if (isBlackRemote()){
          gameEnd(GameEnd.BLACK_RESIGNS);
      } else {
          gameEnd(GameEnd.WHITE_RESIGNS);
      }
  }
  public void OnResign(){
      // Action Response to Resign button pressed
          // tell remote player
          if (isBlackRemote()){ 
                      blackPlayer.SendResign();
               
          }
          if (isWhiteRemote()) {
                      whitePlayer.SendResign();
                
          }  
      
      switch (uiMode){
           case SHARED:
                    
                    if (mainBoard.isWhiteToPlay()){
                        gameEnd(GameEnd.WHITE_RESIGNS);
                    }
                    else {
                        gameEnd(GameEnd.BLACK_RESIGNS);
                    }
                 
                    break;
                  
            case SOLEBLACK:
                      gameEnd(GameEnd.BLACK_RESIGNS);
                      break;
                      
            case SOLEWHITE:
                      gameEnd(GameEnd.WHITE_RESIGNS);
                      break;

        }
      
  }

  public void OnReceiveMessage(String msg){

      messageBoard.ReceiveMessage(msg);

  }
  
  public void clearMessageboard(){
      messageBoard.clear();
      
  }
  
  public boolean IsConnected(){
      return isConnected;
  }
  
  public void SendToActiveMessenger(String str){
      activeMessenger.SendMessage(str);
  }
  public void gameStart(GameMode mode){
      
      setResignButtonVisible(true); //V3.02
      setGamesButtonsVisible(false); //V3.02
      
      gameMode = mode;
              
      mainBoard.setStartingPosition();
      lostPieces.reset();
      pastBoards = new Stack<Board>();
                    
      statusDisplay.flashResults(mainBoard.getResult());
      statusDisplay.resetStatusDisplay();
      
      
      // set the opponent color
      opponentColor = Piece.Color.BLACK;
      if (playingColor == Piece.Color.BLACK) opponentColor = Piece.Color.WHITE;
     
      
      String refereeMessage = "Starting game of " + gameMode.name()+". "; //v3.02       
      
      switch(iSelectedOpponentIndex){
          case -1: // test demo mode
              bWhiteIsRemote = true;
              bBlackIsRemote = true;
              viewMode = computeView(Phase.SHOW); // phase is irrelevant for chess game view
              whitePlayer.SendMove(0, 0, 0);
              break; 
          
          case 0: // A local opponent sharing this UI
                setLocalMode();
                viewMode = computeView(Phase.TAKEOVER);
                refreshBoardDisplay(viewMode);

                refereeMessage += " Opponent is Local.";  //v3.02
                
                mainBoard.enablePlayer(Piece.Color.WHITE);
                break;
             
          default:  // A remote player or the robot
                
              refereeMessage += opponentColor.toString()+" Opponent is "+ nameOpponent+"."; //v3.02
              
              
                setRemoteMode(playingColor);
                viewMode = computeView(Phase.TAKEOVER);
                refreshBoardDisplay(viewMode);
                
                
                // if playing white, kickoff here
                if (playingColor == Piece.Color.WHITE) {
                    mainBoard.enablePlayer(Piece.Color.WHITE);
                    
                    bWhiteIsRemote = false;
                    bBlackIsRemote = true;
                }
                else {
                    // Kick off the opponent
                    bWhiteIsRemote = true;
                    bBlackIsRemote = false;
                    whitePlayer.SendMove(0, 0, 0);
                }
                
                break;
          }
     
      OnReceiveMessage(" ");
      OnReceiveMessage("=============");
      OnReceiveMessage(refereeMessage); // v3.02
      sysout(refereeMessage); //v3.02
 
  }
  public void setPlayerName(String str){
      namePlayer = str;
      jlabPlayer.setText("Player: "+str);
      ksApp.myCfg.PlayerName = namePlayer;
      ksApp.writeConfig();  // save to file!
  }

  public Piece.Color getPlayingColor(){
      return playingColor;
  }
  
  public String getNamePlayer(){
      return namePlayer;
  }
  public String getNameOpponent(){
      return nameOpponent;
  }
  
  public void setOpponentName(String str){
      nameOpponent = str;
      jlabOpponent.setText(str);
      
      // set display list highlight
      // but what happens if someone not in buddies list connects?
      // search jlistOpponents for match, 
      if (str.length()>0){
          jlabOpponent.setText("Opponent: "+str);
          iSelectedOpponentIndex = findOpponentIndex(str);
          jlistOpponents.setSelectedIndex(iSelectedOpponentIndex);
      }
  }
  
  private int findOpponentIndex(String str){
      int index = -1;
      for (int i=0; i<opponentList.getSize(); i++){
          if (opponentList.get(i).toString().contentEquals(str)){
              index = i;
          }
              
      }
      return index;
  }
  
  public String getNameBlack(){
      return nameBlack;
  }
  public String getNameWhite(){
      return nameWhite;
  }
  
  public void setNames(String white, String black){
      // copy the names to the slave records

      statusDisplay.setNames(white, black);

      recorder.setNames(white, black);
      if (ksApp.myCfg != null){
          ksApp.myCfg.nameBlack = black;    // synchronise cfg records
          ksApp.myCfg.nameWhite = white;
      }

  }
  
  public void openBrowser(String url){
      
        BareBonesBrowserLaunch.openURL(url);
  }
          
  private boolean checkLatestVersion(){
      boolean latest = true;
      // yet to be coded
      return latest;
  }
 private boolean offerUpdateDownload(){
     boolean download = false;
     // yet to be coded
     return download;
 }
 public ViewMode getViewMode(){
     return viewMode;
 }


 public String getPath(){
     return ksApp.myCfg.lastPath;
 }
 
 public void setPath(String path){
     ksApp.myCfg.lastPath = path;
 }


 public boolean IsTestview(){ return testBoardControl.IsTestView();}
  // The Class Constructor
  
  KSpielUI(){
      
      
      
    // Find the App which holds the Board Object.
      // nb assumes KSUI object constructed last!
    ksApp = KSpielApp.getApplication();
    mainBoard = ksApp.mainBoard;
      
    //Create a new JFrame container.  /////////////////////////////////
    mainFrame = new JFrame(ksApp.version);
    
    // Set up the listener and the exit conditions
    hangup = false;   // allows one attempt at tidy exit on JFrame close
                        // when true, exit is immediate
    mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (hangup) System.exit(0);//if we had a hang-up last time make immediate exit now.
                // otherwise try for graceful exit
                hangup = true;  // leave a marker on the trail

                ksApp.writeConfig();
                OnDisconnect(); // this might hang up.
                System.exit(0);
            }
    });

    //Terminate the program when the user closes the application.
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //Set the layout manager to FlowLayout.
    mainFrame.setLayout(new FlowLayout() );
    
    // Set the dimensions for the panels in the display
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension scrnsize = toolkit.getScreenSize();
    
    System.out.println ("Screen size : " + scrnsize);
    
    scrnSize = new Screensize(scrnsize); // create scrnSize object

    //Give the frame an initial size
    mainFrame.setSize(scrnSize.dimMainframe); //(dDisplayw, dDisplayh);
    
    ////////////////////////////////////////////////////////////////////
    
    nameBlack = "";
    nameWhite = "";
    
    
    boardView = new BoardView(this, mainBoard);
    statusDisplay = new StatusDisplay(this);
    
    recorder = new Recorder(this);       
    jpanelRecorder = recorder.makeRecorderDisplay(scrnSize.dimRecorder);
    
    jpanelStatus = statusDisplay.makeStatusDisplay(mainBoard);

    jTab = new JTabbedPane();
    messageBoard = new MessageBoard(this);    // must precede makeOpponentsDisplay??
    lostPieces = new LostPieces(this);
    testBoardControl = new PostulateControl(this, ViewMode.WHITE);

    jTab.addTab("Messages", messageBoard.getMessageboard()); // other tabs will be added later according to game mode selected

    makeOpponentsDisplay(); // define jpanelOpponents
    
    makeGameControlsDisplay();
    makeHandoverDisplay();
    
    // make the control buttons
     
    /// Rotate
    jbtnRotate = boardView.getRotate();
    jbtnRotate.setPreferredSize(new Dimension(35,35));

    /// Sound Effects
    // soundEffects = new SoundEffects(this);
    // jbtnSound = soundEffects.getOnOffButton();
    jbtnSound.setPreferredSize(new Dimension(35,35));
    
    /// Settings
    jbtnSettings = new JButton();
    jbtnSettings.setIcon(loadImageIcon("/images/Effects/preferences.png"));
    jbtnSettings.setToolTipText("Alter settings or player names");
    jbtnSettings.setPreferredSize(new Dimension(35,35));
    jbtnSettings.addActionListener(new ActionListener(){
          // open support website in default browser
          public void actionPerformed(ActionEvent ae){ 
             SettingsDialog dialog = new SettingsDialog(mainFrame, ksApp); 
            
    
          
          }
          });

    
    /// Help
    jbtnHelp = new JButton();
    jbtnHelp.setIcon((loadImageIcon("/images/Effects/CurHelp.png")));
    jbtnHelp.setToolTipText("Get Online Help");
    jbtnHelp.setPreferredSize(new Dimension(35,35));
    jbtnHelp.addActionListener(new ActionListener(){
          // open support website in default browser
          public void actionPerformed(ActionEvent ae){ 
              openBrowser("http://www.kriegspiel.co.uk/"); // false from button, true from remote 
          }
        });
          
    
    
    jpanelMain = new JPanel();
    jpanelMain.setLayout(new FlowLayout());
    jpanelMain.setPreferredSize(scrnSize.dimMain);
    
    jpanelLeft = new JPanel();
    jpanelLeft.setLayout(new FlowLayout());
    jpanelLeft.setPreferredSize(scrnSize.dimLeft);
    
    // The Recorder and Status panels
    jpanelLeft.add(jpanelRecorder);
    jpanelLeft.add(jpanelStatus);
    
    jpanelMain.add(jpanelLeft);
    
  
    /// Make the control buttons:
    jpanelControls = new JPanel();
    jpanelControls.setPreferredSize(new Dimension(50,400));
    // add the rotate button        
    jpanelControls.add(jbtnRotate);

    // add the sound button
    jpanelControls.add(jbtnSound);
    
    // add the settings button
    jpanelControls.add(jbtnSettings);
    
    /// add the help button
    jpanelControls.add(jbtnHelp);
    
    
    //The Board panels
    
    jpanelCentre = boardView.getCentre();  
    jpanelVLabels = boardView.getVLabels();
    //  add the 1-8 labels
    jpanelMain.add(jpanelVLabels);        
    
    jpanelMain.add(jpanelCentre);
    
    // The Player controls panels
    
    jpanelRight = new JPanel();
    jpanelRight.setPreferredSize(scrnSize.dimRight);
    jpanelRight.add(jpanelOpponents);
    
    jpanelGameControls.add(jpanelHandover);
    jpanelRight.add(jpanelGameControls);
    
    bBlackIsRemote = false; //false;
    bWhiteIsRemote = false; //false;

    jpanelMain.add(jpanelRight);
    
    jpanelMain.add(jpanelControls);
    
    mainFrame.add(jpanelMain);

    mainFrame.add(jTab);
 
    jpanelHandover.setVisible(false);
    
    // initialise the steptimer delay (used in Demo mode)
    delayStep = 500; // initialise illumniation delay

    // Display the entire frame
    mainFrame.setVisible(true);
  
    
    // OnReceiveMessage("Welcome to Kriegspiel! Display size is " + maxw + "x" + maxht);
    //Update the board display showing pieces
    
//    Initialise(ViewMode.BOTH, UIMode.SHARED, GameMode.CHESS);
    
//    playSound("/sounds/roar.au");       // start the game sfx
    
    // openBrowser("http://www.google.co.uk/"); // demo
    if (! checkLatestVersion())
        offerUpdateDownload();
  
}

  private void showLostPieces(boolean show){
      jpanelMessageboard.setVisible(!show);
      jpanelLostPieces.setVisible(show);
  }
  
  public void topTestBoard(boolean top){
      jpanelTestBoard.setVisible(top);
      jpanelMessageboard.setVisible(!top);  
  }
  
  private void topLostPieces(boolean top){
      jpanelLostPieces.setVisible(top);
      jpanelMessageboard.setVisible(!top);
  }

  public void beep(){
      
      // soundEffects.beep();

  }
  
  public void playSound(String auFile){
      
      // soundEffects.playSound(auFile);

  }
      
  
  ////////////////////////////////////////////////////////////////////
}
  

