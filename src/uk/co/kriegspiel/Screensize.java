/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.awt.Dimension;

/** Screensize
 * A structure for holding the display sizes
 * Provides metrics for a netbook layout for 1024x600 pixel screen
 * or for a larger screen version for screens more than 700 pixels high
 * 
 * Constructor requires max screen size Dimension parameter
 * 
 * @author denysbennett
 */
public class Screensize {

    // component variables
    
     private int dMainframew; 
     private int dMainframeh; 
     
     private int dDisplayw; 
     private int dBoardw; 
     private int dRecorderw; 
     private int dHandoverw; 
     private int dListw; 
     private int dMessageboardw; 
     private int dMessagesw; 
     
     private int dLabelw; 
     private int dLabelh; 
     private int dCentreth; 
     private int dCentretw; 
     
             
     private int dBoardh; 
     private int dToph;        
 
     private int dRecorderh; 
     private int dStatush; 
     private int dHandoverh; 
                           
     private int dControlsh;    
     private int dOpponenth;       
                               
     private int dListh; 
     
     private int dMessageboardh; 
     private int dMessagesh; 
     private int dInputw; 
     private int dInputh; 
     
     private int dLostRowh;
     private int dLostRoww;
    
     private int dLostIconh;
     private int dLostIconw;

     private int dLabw; 
     private int dLabh; 
     
     private int dLabhw; 
     private int dLabhh;

     private int dSqw;
     private int dSqh;
     
     // Public Dimension variables
      public Dimension dimMainframe;
      public Dimension dimMain;
      public Dimension dimCentre;
      public Dimension dimBoard;
      public Dimension dimTop;
      public Dimension dimBottom;
      public Dimension dimVLabels;
      public Dimension dimHLabels;
      public Dimension dimVLab; 
      public Dimension dimHLab;
      public Dimension dimRight;
      public Dimension dimLeft;
      public Dimension dimRecorder;
      public Dimension dimStatus;
      public Dimension dimGameControls;
      public Dimension dimOpponents;
      public Dimension dimList;
      public Dimension dimMessageboard;
      public Dimension dimMessages;
      public Dimension dimLostRow;
      public Dimension dimLostIcon;
      public Dimension dimPiece;

      public Dimension dimInput;
      public Dimension dimHandover;
     
      public boolean bNetbook;
     
     // constructor
     public Screensize(Dimension maxscrn){
         
         setNetbook(); // default setting to fit a 1024 x 600 screen
         
         if (maxscrn.height > 700){ // modify for a larger screen

             bNetbook = false;
             dMainframeh = 700;
             dCentreth = 470;
             dMessageboardh = 150;
             dMessagesh = 79; 
             dInputw = 700; 
             dMessageboardw = 810;
             dMessagesw = 800; 
             dLostRowh = 50;
             dLostRoww = 800;
             dLostIconw = 45;
             dLostIconh = 45;
             dSqh = 50;
          }
         
         setDimensions();
         
         
        
     }
     
     private void setDimensions(){
         // set the public dimensions for the display constructors
      dimMainframe = new Dimension(dMainframew, dMainframeh);      
      dimMain = new Dimension(dDisplayw, dCentreth);
      dimCentre = new Dimension (dCentretw,dCentreth);
      dimBoard = new Dimension(dBoardw, dBoardh);
      dimVLabels = new Dimension(dLabelw, dLabelh);
      dimHLabels = new Dimension(dBoardw, dLabelw);
      dimVLab = new Dimension(dLabw,dLabh);
      dimHLab = new Dimension(dLabhw,dLabhh);
      dimTop = new Dimension(dBoardw,dToph);

      dimRight = new Dimension(dRecorderw,dCentreth);
      dimLeft = dimRight;
      dimRecorder = new Dimension(dRecorderw,dRecorderh);
      dimStatus = new Dimension(dRecorderw,dStatush);
      dimGameControls = new Dimension(dRecorderw,dControlsh);
      dimOpponents = new Dimension(dRecorderw, dOpponenth);
      dimList = new Dimension(dListw, dListh);
      dimMessageboard = new Dimension (dMessageboardw,dMessageboardh);

      dimHandover = new Dimension(dHandoverw,dHandoverh);
      dimMessages = new Dimension(dMessagesw,dMessagesh);
      dimInput = new Dimension(dInputw,dInputh);
      dimLostRow = new Dimension(dLostRoww,dLostRowh);
      dimLostIcon = new Dimension(dLostIconw,dLostIconh);
      dimPiece = new Dimension(dSqw,dSqh);
         
     }
     
     private void setNetbook(){
         // defaults for netbook sized screen (1024 x 600)
         bNetbook=true;
         dMainframew = 1000; // v3.3 was in v3.2 = 920;
         dMainframeh = 580; 
     
         dDisplayw = 900;
         dBoardw = 400;
         dRecorderw = 200;
         dHandoverw = dRecorderw - 20;
         dListw = 160; 
         dMessageboardw = 1000; 
         dMessagesw = 500;  
        
         dLabelw = 20;
         dLabelh = 445;
         dCentreth = 470; 
         dCentretw = dBoardw;
     
             
         dBoardh = 420;
         dToph = 5;       
         
         dRecorderh = 220; 
         dStatush = 220;    
         dHandoverh = 80;  

         dControlsh = 150;     
         dOpponenth = 290;      

         dListh = 220;  
     
         dMessageboardh = 75; 
         dMessagesh = 40;  
         dInputw = 300; 
         dInputh = 22;
         
         dLostRoww = 485;
         dLostRowh = 35;

         dLostIconh = 30;
         dLostIconw = 25;
     
         dSqw = 50;
         dSqh =40;

         dLabw = 8;
         dLabh = 47;  
     
         dLabhw = 45;
         dLabhh = 15;
     }
}
