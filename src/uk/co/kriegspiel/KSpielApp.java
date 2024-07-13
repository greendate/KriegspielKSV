package uk.co.kriegspiel;

import javax.swing.*;
import java.util.*;
import java.awt.Toolkit.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;




/** Kriegspiel Blind Chess Game
 * written in Java for portability
 * Kriegspiel - the main application
 * @author Denys Bennett
 */


// the Application

public class KSpielApp {

  public String version = "KS 4.2";
    // References
  private static KSpielApp myRef; // the App
  public static KSpielApp getApplication(){ return myRef;}
  public KSpielUI ksUI; // the UI
  public Config myCfg; // the serializable config object
  
  // The Chess Board Objects
  public Board mainBoard; // the playing board
  public Board testBoard; // the board for testing potential moves for check

  //Piece Archetypes
  public Arch archPiece;    // dummy is overridden by Arch<piece> classes
  public ArchBishop archBishop;
  public ArchKing archKing;
  public ArchQueen archQueen;
  public ArchRook archRook;
  public ArchPawn archPawn;
  public ArchKnight archKnight;
  public ArchEmpty archEmpty;  // a virtual piece denoting an Empty Square
 
  // Communicator objects
  public Robot robotBlack;
  public Robot robotWhite;
 
//  public KS3Client ks3Client;
  // public SkypeClient skypeClient;
  public JeevaClient jeevaClient;
  
  // Some utilities
  
  public void sysout(String str){
      System.out.println(str);
  }
  
   // co-ordinate converters
    public int getX(int p){
      int x = p % 8;
      if (x == 0){ x=8;}
      return x;
    }
    public int getY(int p){
      int y = 1+(p-1)/8;
      return y;
    }
    // return the position as an Integer object for containing in an ArrayList
    public Integer getP(int x, int y){
        return x+(y-1)*8;
    }
    // tests for adjacency of squares
    public boolean isAdjacent(int p, int q){
        boolean bAdjacent = false;
        
        if (distance(p,q) == 1) {
        bAdjacent = true;
        }
        
       //     if ((getX(p)==getX(q)+1 || getX(p)==getX(q) || getX(p) == getX(q)-1)
       //       && getY(p)==getY(q)+1 || getY(p)==getX(q) || getY(p) == getY(q)-1){
       //         bAdjacent = true;  
       //     }
        return bAdjacent;
    }
    // measure no of squares (==King's moves) from p to q 
    public int distance(int p, int q){
        int iDistance;
        int x = Math.abs(getX(p) - getX(q));     
        int y = Math.abs(getY(p) - getY(q));
        iDistance = Math.max(x,y);
        
        return iDistance;
    }
  
    ArrayList<Integer> copyArray (ArrayList<Integer> array){
        ArrayList<Integer> copy = new ArrayList<Integer>();
        for (int i=0; i<array.size(); i++){
            copy.add(array.get(i));
        }
        return copy;
    }
  
    private void readConfig(){
        ObjectInputStream cfgIn = null;
        boolean error = false;
        
            // open the input file
            try{
                 cfgIn = new ObjectInputStream(new FileInputStream("Kspiel.cfg"));
            } catch(IOException exc) {
                ksUI.errorMsg("Failed to open Kspiel.cfg");
                error = true;
            }
            
        try{
                myCfg = (Config) cfgIn.readObject();
            }catch(IOException exc){
                ksUI.errorMsg("Error reading Kspiel.cfg");
                error = true;
            }
            catch(ClassNotFoundException exc){
                ksUI.errorMsg("Config class not found");
                error = true;
            }
        
            try{
                cfgIn.close();
            } catch(IOException exc) {
                ksUI.errorMsg("Error closing kspiel.cfg");
                error = true;
            }
        
        if (error){
            // write a new default config file
            ksUI.errorMsg("Repairing by writing new .cfg..");
            writeConfig();
            ksUI.errorMsg("done");
        }
    }
    
    public void writeConfig(){
        
    // Create a default config file
        ObjectOutputStream cfgOut = null;
        try{
                cfgOut = new ObjectOutputStream(new FileOutputStream("Kspiel.cfg"));               
        } catch(IOException exc) {
                ksUI.errorMsg("Failed to open output.cfg");
        }
            
        try{
           // String p = myCfg.lastPath;
            boolean s = myCfg.soundOn;
                cfgOut.writeObject(myCfg);
        } catch(IOException exc) {
                ksUI.errorMsg("Error writing .cfg");
        }
        try{
                cfgOut.close();
        } catch(IOException exc) {
                ksUI.errorMsg("Error closing output.cfg");
        }
    }
    
    
    private void config(){
        
        
        if (new File("Kspiel.cfg").exists()){
    
            readConfig();
            
        } else {
            
            writeConfig();
        }
    }
    
  // Constructor
  KSpielApp(){
    
    // Start of main app
      
    // record its reference for passing to other objects
    myRef = this;
    
    // NOTE THAT CONSTRUCTION ORDER IS IMPORTANT
    
    // Board needs piece archetypes constructed first!
    
    // create piece archetypes
    archPiece = new Arch();
    archPiece.loadImageIcons();
    
    archKing = new ArchKing();
    archKing.loadImageIcons();
    archKing.makeCursorIcons();  //v4.2 et seq.
    
    archQueen = new ArchQueen();
    archQueen.loadImageIcons();
    archQueen.makeCursorIcons();
    
    archRook = new ArchRook();
    archRook.loadImageIcons();
    archRook.makeCursorIcons();
    
    archKnight = new ArchKnight();
    archKnight.loadImageIcons();
    archKnight.makeCursorIcons();

    archBishop = new ArchBishop();
    archBishop.loadImageIcons();
    archBishop.makeCursorIcons();
    
    archPawn = new ArchPawn();
    archPawn.loadImageIcons();
    archPawn.makeCursorIcons();
    
    archEmpty = new ArchEmpty();
    archEmpty.loadImageIcons();
    
    
    // Create the main Board object
    mainBoard = new Board(myRef,0);  // the board needs to find other objects in the App
     
    
    // place the pieces 
    mainBoard.setStartingPosition();
    
    // Create a test board to use for detecting illegal moves into check
    testBoard = new Board(myRef,1);
    
 
    
    // Create the User Interface object
    
    ksUI = new KSpielUI();
    
    // Create / load the configuration data object
    myCfg = new Config(version, this);
    config();
    
    ksUI.setConnectToolTip();
//    String p = myCfg.lastPath;
    
    // Create some Remote communicators and robots
    
    robotWhite  = new RobotC(myRef, ksUI, Piece.Color.WHITE);
    robotBlack = new RobotC(myRef, ksUI, Piece.Color.BLACK);
    // skypeClient = new SkypeClient(myRef, ksUI);
//    ks3Client = new KS3Client(myRef, ksUI); 
    jeevaClient = new JeevaClient(myRef, ksUI);
    
    // show the board in the User Interface, with BOTH sides visible and the screen SHARED
    ksUI.Initialise(KSpielUI.ViewMode.BOTH, KSpielUI.UIMode.SHARED, KSpielUI.GameMode.CHESS);
    
   
}
  
  // the main program entry point
  
  public static void main(String args[]){
      // Create the frame on the event dispatching thread
      SwingUtilities.invokeLater(new Runnable(){
          public void run(){
           //   Run the entire app from the event dispatching thread
              new KSpielApp();
          }
      });
  }
}

