/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;


import java.awt.Dimension;
//import java.util.ArrayList;
import java.awt.event.*;
import java.io.File;
//import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Implements the replay recorder
 * @author denysbennett
 */
public class Recorder {
    
    private Recording recording;
 
    private KSpielUI ksUI;
    private boolean bRecording;
  //  private KSpielUI.GameEnd gameEnd;
    
    private Timer playTimer;
    private Timer stepTimer;
    private int delay; // millisec
    private int delayStep;
       
    private int iFrom;
    private int iTo;

    
    // view object
    
    private JPanel jpanelRecorder;
    //public Recorder recorder;
    private JButton jbtnRewind;
    private JButton jbtnPause;
    private JButton jbtnStep;
    private JButton jbtnReplay;
  //  private JButton jbtnBack;
    private JSlider jslSlider;
    private JLabel jlabSlider;
    
    private JButton jbtnSave;
    private JButton jbtnLoad;
 //   private String lastPath;
    
 //   private Dimension dimRecorder;
    
    // constructor
    public Recorder(KSpielUI ui){
        
        ksUI = ui;
        recording = new Recording();

        setDelay(50);
  //      delay = 1000;   // initialise delay between moves
  //      delayStep = 500; // initialise illumniation delay

  //      lastPath = "";
        
    }
    
    public int getDelay(){
        return delay;       // Slider control available to Demo mode
    }
    
    private void setDelay(int t){
               // t has range 0-100

        // set delay range up to  3000
        // but with a minimum allowing for 32 frame move aimations of 3ms apiece
        // with minimum total step 4 x minimumt frame movement (>96 ms)
        // animation delay 25% starts coincidentally
        // thinking delay  50%

        delay = t*25 +500;
        delayStep = delay/2;


   //     delay = t;          // slider control access

    }

    public void endsWith(KSpielUI.GameEnd ending){
        recording.endsWith(ending);
        
        setRecordingMode(false);
        if (recording.getSize()>0){
            enableSave(true);  
        }
    }
    
    public boolean IsRecording(){
        return bRecording;
    }
    
    public void setRecordingMode(boolean bMode){
        bRecording = bMode; // true if recording
        // v4.2 onward - when recording, bRecording if true allows cursor to show moving piece & source square to be blank.
        if (bRecording){
            // disable all buttons
            disableRecorder();
            
            // Start a new recording
            recording = new Recording();
      
            recording.setBlack(ksUI.getNameBlack());   
            recording.setWhite(ksUI.getNameWhite());
            
            enableLoad(false);

            
        } else {
            Pause(); // stop the clock
            // enable Rewind button
            if (recording.getSize()>0){
                enableRewind(true);               
                enableSave(true);
                
            }
            enableLoad(true);
        }
    }
    
    public void RecordMove(int iFrom, int iTo){
        if (bRecording){
            recording.RecordMove(iFrom, iTo);
        }
    }
    
    private void Rewind(){
        recording.Rewind();

        ksUI.gameStart(KSpielUI.GameMode.CHESS);
        ksUI.setGamesButtonsVisible(true);  //v3.03
        bRecording = false;
        enableReplay(true);
        ksUI.setNames(recording.getWhite(), recording.getBlack());

        jbtnPause.setEnabled(false);
        
    }
    
    public void setNames(String white, String black){
        recording.setWhite(white);
        recording.setBlack(black);
    }
    
    private void Step(){              
        
        // Play a single move
        //if (!bRecording && nextMove<listMovesFrom.size()){
        if (!bRecording && !recording.isAtEnd()){
            enableLoad(false);
            enableSave(false);
            
            iFrom = recording.getFrom();
            iTo = recording.getTo();

         //   ksUI.StepDisplay(iFrom, iTo); // would be nice to use a single ksUI function

           ksUI.setAspect(iFrom, Square.Aspect.SELECTED);
           ksUI.setAspect(iTo, Square.Aspect.ILLUMINATED);
            
            
            
            // put a pause in here to show illuminated squares
            // viz. delayStep = 500;  // now set by constructor
            
            // using Thread.sleep() here just seems to cause the whole program to sleep before squareButtonPressed()
            // has had time to illuminate the display.
            // So use a timer and actionlistener instead.
            
            ActionListener alStep = new ActionListener(){
                public void actionPerformed(ActionEvent ae){
            
            // make the move after the delay
                  
                    if (! recording.isAtEnd()){
                       
                        ksUI.OnSquareButtonPressed(iFrom);
                        
                        iTo = recording.getTo();
                        ksUI.InitiateMove(iFrom, iTo);
                        recording.NextMove();
                        

                    } 
            
                     if (recording.isAtEnd()){
                        enableReplay(false);
                        ksUI.gameEnd(recording.getEnding());
                    }
                }
               };
               
               // set the delay in a non-repeating timer
             stepTimer = new Timer(delayStep, alStep);
             stepTimer.setRepeats(false);
             stepTimer.start();
             
        }
         
        
    }
    
    private void Replay(){
        
      // delay = 1000;  // now set by constructor
      ksUI.setGamesButtonsVisible(false);  //v3.03
      jbtnStep.setEnabled(false);
      jbtnReplay.setEnabled(false);
      jbtnPause.setEnabled(true);
   //   enableStep(false);

      ActionListener alReplay = new ActionListener(){
        
        public void actionPerformed(ActionEvent ae){
          
          // In response to the clock-timer,
            if (! recording.isAtEnd()) {
                Step();
                playTimer.setDelay(delay); // resensitize to slider setting
            } else {
            
                playTimer.setRepeats(false);
                playTimer.stop();
            }
          }
        
        };
        
      playTimer = new Timer(delay, alReplay);
      playTimer.setRepeats(true);
      playTimer.start();
    } // end Replay()
    
    public void Pause(){
        if (playTimer != null){ 
            playTimer.setRepeats(false);
            playTimer.stop();
        }

        enableRewind(true); //v3.02
        enableReplay(true); //v4.2
        enableStep(true);
            //    jbtnStep.setEnabled(true); //v3.03
        ksUI.setGamesButtonsVisible(true);  //v3.02
    } // end Pause()
    


// View object controls
  public JPanel makeRecorderDisplay(Dimension dimRecorder){
      
      jpanelRecorder = new JPanel();
 
      jpanelRecorder.setBorder(ksUI.borderTitle("Replay Recorder"));
      
      jpanelRecorder.setPreferredSize(dimRecorder);
      
      jbtnRewind = new JButton();
      jbtnPause = new JButton();
      jbtnStep = new JButton();
      jbtnReplay = new JButton();
  //    jbtnBack = new JButton();
      jslSlider = new JSlider();
      jlabSlider = new JLabel();

      jbtnSave = new JButton("Save");
      jbtnSave.setIcon((ksUI.loadImageIcon("/images/Recorder/FileSave.png")));
      jbtnSave.setPreferredSize(new Dimension(80,40));
      enableSave(false);
      jbtnLoad = new JButton("Open");
      jbtnLoad.setIcon((ksUI.loadImageIcon("/images/Recorder/FileOpen.png")));
      jbtnLoad.setPreferredSize(new Dimension(80,40));
      
      jbtnRewind.setIcon((ksUI.loadImageIcon("/images/Recorder/rewindu.png")));
      jbtnPause.setIcon((ksUI.loadImageIcon("/images/Recorder/pauseu.png")));
      jbtnStep.setIcon((ksUI.loadImageIcon("/images/Recorder/stepu.png")));
      jbtnReplay.setIcon((ksUI.loadImageIcon("/images/Recorder/playu.png")));
 //     jbtnBack.setIcon(ksUI.loadImageIcon("/images/Recorder/backu.png"));
      //enableBackstep(false);
      
      jbtnRewind.setToolTipText("Rewind to start of game");
      jbtnStep.setToolTipText("Replay single move");
      jbtnReplay.setToolTipText("Replay game");
      jbtnPause.setToolTipText("Pause replay");
      jbtnSave.setToolTipText("Save game to disk");
      jbtnLoad.setToolTipText("Load previously saved game");
      jslSlider.setToolTipText("Adjust speed of replay or robot");
      jslSlider.setPreferredSize(new Dimension(180,40));
      
      jpanelRecorder.add(jbtnRewind);
      jpanelRecorder.add(jbtnPause);
      jpanelRecorder.add(jbtnStep);
      //jpanelRecorder.add(jbtnBack);  // added 3.00.5
      
      jpanelRecorder.add(jbtnReplay);
      
      
      jpanelRecorder.add(jslSlider);
      jpanelRecorder.add(jlabSlider);
      
      jpanelRecorder.add(jbtnSave);
      jpanelRecorder.add(jbtnLoad);
          
      jlabSlider.setText(Integer.toString(getDelay()));
      jlabSlider.setText("Fast         ......         Slow");
      jslSlider.addChangeListener((new ChangeListener(){
          public void stateChanged(ChangeEvent ce) {
              
            setDelay(jslSlider.getValue())  ;
            //  int v = jslSlider.getValue();

            //  int delay = v*25 +100;
            //  setDelay(delay);
              
              
          }
      }));
      
      jbtnRewind.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
            
              ksUI.resetOpponentIndex();  // needed to turn off robot!
          
              Rewind();
          }
      }));
      
      jbtnPause.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              Pause();
          }
      }));
  
      jbtnStep.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              Step();
          }
      }));
      
      jbtnReplay.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              enableRewind(false);
              Replay();
              
          }
      }));
      /*
      jbtnBack.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              Backstep();
          }
      }));
      */
      jbtnSave.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              Save();
          }
      }));
      
      jbtnLoad.addActionListener((new ActionListener(){
          // 
          public void actionPerformed(ActionEvent ae){
              Load();
          }
      }));
      disableRecorder();
      
      return jpanelRecorder;
  }

  private void Load(){
      // offer file selector to user
      JFileChooser fc;
      
      if (ksUI.getPath() == null){
        fc = new JFileChooser();
      }
      else {
        fc = new JFileChooser(ksUI.getPath());
      }
      
      fc.setFileFilter(new MykspFileFilter());

      
      int returnval = fc.showOpenDialog(fc);
      if (returnval == JFileChooser.APPROVE_OPTION){
          File file = fc.getSelectedFile();
          //ksUI.sysout(file.toString());
          // retrieve the serialised recording
          
          // Create an input stream
          ObjectInputStream fin = null;
          try{
              fin = new ObjectInputStream(new FileInputStream(file));
          } catch (IOException exc){
              ksUI.sysout("Error opening file "+file.toString());             
          }
          
          // Retrieve the recording
          try{
              recording = (Recording) fin.readObject();
              
              ksUI.setPath(file.getParent());
              ksUI.setNames(recording.getWhite(), recording.getBlack());
              
              Rewind();
              enableSave(false);
              enableRewind(false);
              
          }catch (IOException exc){
              ksUI.sysout("Error reading file "+file.toString());             
          }
          catch (ClassNotFoundException exc){
              ksUI.sysout("recording Class not found in "+file.toString());             
          }
          
          // Close the file
          try{
              fin.close();
          } catch(IOException exc){
              ksUI.sysout("Error closing file "+file.toString());
          }
          
          Rewind();
      }  
          
  }
  
  private void enableSave(boolean enable){
      jbtnSave.setEnabled(enable);
  }
  private void enableLoad(boolean enable){
      jbtnLoad.setEnabled(enable);
  }
  
  private void Save(){
      // offer file selector to user
      JFileChooser fc;
      
      if (ksUI.getPath() == null){
        fc = new JFileChooser();
      }
      else {
        fc = new JFileChooser(ksUI.getPath());
      }
       
      fc.setFileFilter(new MykspFileFilter());
     // FileFilter filter = new FileFilter();
      
    //  fc.addChoosableFileFilter(new FileFilter());
      
      int returnval = fc.showSaveDialog(fc);
      if (returnval == JFileChooser.APPROVE_OPTION){
          File file = fc.getSelectedFile();
          
          boolean go = true;
          if (file.exists()){
              
              // check if overwrite, else
              
                JOptionPane pane = new JOptionPane(
                    "File already exists\nOverwrite?");
                Object[] options = new String[] { "Yes", "No" };
                pane.setOptions(options);
                JDialog dialog = pane.createDialog(new JFrame(), "File Exists");
                dialog.setVisible(true);
                
                Object obj = pane.getValue(); 
                int result = -1;
                for (int k = 0; k < options.length; k++)
                  if (options[k].equals(obj))
                    result = k;
                // System.out.println("User's choice: " + result);
              
                if (result == 0) go = true; else go = false;
                
              
          }
          
          if (go){
          // save the serialised recording
          
              // Create the output stream
              ObjectOutputStream fout = null;
              try{
                  fout = new ObjectOutputStream( new FileOutputStream(file));
              } catch (IOException exc){
                  ksUI.sysout("Error opening file "+file.toString());
              }

              // Write the recording to the file
              try{
                  ksUI.sysout("Writing recording to file "+file.toString());
                  fout.writeObject(recording);
                  ksUI.setPath(file.getParent() );
                  
              } catch (IOException exc){
                  ksUI.sysout("Error writing recording to file "+file.toString());      
              }

              // Close the file
              try{
                  fout.close();
              } catch(IOException exc){
                  ksUI.sysout("Error closing file "+file.toString());
              }
              
          // test here to see if it has a .ksp extension
          // fix up if not
          boolean success;
          String fname = file.getAbsolutePath();
          if (! fname.endsWith(".ksp")){
              File renamed = new File(fname+".ksp");
              
              success = file.renameTo(renamed);
              if (!success){
                  System.out.println("Rename failed "+fname +" to "+renamed.getAbsolutePath());
              }
          }
              
              
          }
          else
              ksUI.sysout("Save cancelled by user");
      }
      
      
  }
  
  public void undoLast(){
      recording.undoLast();
  }
  private void disableRecorder(){
      enableRewind(false);
     // jbtnRewind.setEnabled(false);
      jbtnPause.setEnabled(false);
      jbtnStep.setEnabled(false);
      jbtnReplay.setEnabled(false);
   //   jbtnBack.setEnabled(false);
      
  }
  
  private void enableRewind(boolean enable){
      jbtnRewind.setEnabled(enable);
  }
  /*
  public void enableBackstep(boolean enable){
      jbtnBack.setEnabled(enable);
      
  }
  
  public void setLastBoardState(Board last){
      lastState = last;
  }
  */
  /*
  public void enableSlider(boolean bState){
      jslSlider.setEnabled(bState);
  }
  */
  private  void enableStep(boolean bState){
      jbtnStep.setEnabled(bState);
  }

  private void enableReplay(boolean bState){
      jbtnPause.setEnabled(bState);
      jbtnStep.setEnabled(bState);
      jbtnReplay.setEnabled(bState);
    //  jslSlider.setEnabled(true);  // always on
      if (bState){
          ksUI.setResignButtonVisible(false);
         
       
      }
      
  }
}
