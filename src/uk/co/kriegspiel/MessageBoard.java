/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author denysbennett
 */
public class MessageBoard {

    KSpielUI ksUI;
    
    private JPanel jpanelMessageboard;
  
    private JScrollPane jscrlpMessages;
    private JTextArea jtxtMessages;
  

    private JTextField jtfInput;
    private JLabel jlabInput;
    
    // constructor
    public MessageBoard(KSpielUI ui){
        ksUI = ui;
        
        // create the container
      
          jpanelMessageboard = new JPanel();
          jpanelMessageboard.setPreferredSize(ksUI.scrnSize.dimMessageboard);

          if (!ksUI.scrnSize.bNetbook) //V3.3
              jpanelMessageboard.setBorder(ksUI.borderTitle("Messages"));
          // make the message display

          jtxtMessages = new JTextArea();
          jtxtMessages.setLineWrap(true);

          jtxtMessages.setText("");
          jtxtMessages.setEditable(false);
          jtxtMessages.setBackground(Color.LIGHT_GRAY);

          jscrlpMessages = new JScrollPane(jtxtMessages);

          jscrlpMessages.setPreferredSize(ksUI.scrnSize.dimMessages);
          jscrlpMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

          // make the message input

          jlabInput = new JLabel("type here:");
          jtfInput = new JTextField();
          jtfInput.setPreferredSize(ksUI.scrnSize.dimInput);

          ActionListener tf = new ActionListener(){
              public void actionPerformed(ActionEvent ae){
                  OnTextInput();
              }  
          };

          jtfInput.addActionListener(tf);

          // Add the components together

          jpanelMessageboard.add(jscrlpMessages);
          jpanelMessageboard.add(jlabInput);
          jpanelMessageboard.add(jtfInput);
   
        
    }
    
    public JPanel getMessageboard(){
        return jpanelMessageboard;
    }
    
    public void enableInput(boolean enable){
        jtfInput.setEnabled(enable);
        jlabInput.setEnabled(enable);
        
    }
    
    public void OnReceiveMessage(String msg){

      String message ="\n"+ msg;
      jtxtMessages.append(message);
      
      // add this to force autoscroll to show last character
      int len = jtxtMessages.getDocument().getLength();
      jtxtMessages.setCaretPosition(jtxtMessages.getDocument().getLength());
      jtxtMessages.select(len, len); 
      
      //add this to force output prior to network operation completing
      jtxtMessages.paintImmediately(0,0,ksUI.scrnSize.dimMessageboard.width,ksUI.scrnSize.dimMessageboard.height); // no delay!  0,0,800,170
      // add this to cause a tidy up in case autoscroll was overtaken by paintImmediately 
      jtxtMessages.repaint(); 
  }
    
  public void clearMessageboard(){
      jtxtMessages.setText("");


  }
    
    private void OnTextInput(){
      // Retrieve the input text
            String Input = jtfInput.getText();
            jtfInput.setText("");   // clear text as acknowledgement
           
            if (ksUI.IsConnected()){
                // send the message to the server via the messaging client
                
                
                //suppress any starting $ in messages, as this is a reserved command
                if (Input.startsWith("$")) Input = " "+Input; // so add a starting space
                
                ksUI.SendToActiveMessenger(Input);
                OnReceiveMessage(ksUI.getNamePlayer() + ": "+Input);  // echo to local message view
            } else {
                OnReceiveMessage("NOT CONNECTED TO NETWORK!");
            }            
          
    }
    
    public void ReceiveMessage(String msg){

          String message ="\n"+ msg;
          jtxtMessages.append(message);

          // add this to force autoscroll to show last character
          int len = jtxtMessages.getDocument().getLength();
          jtxtMessages.setCaretPosition(jtxtMessages.getDocument().getLength());
          jtxtMessages.select(len, len); 

          //add this to force output prior to network operation completing
          jtxtMessages.paintImmediately(0,0,ksUI.scrnSize.dimMessageboard.width,ksUI.scrnSize.dimMessageboard.height); // no delay!  0,0,800,170
          // add this to cause a tidy up in case autoscroll was overtaken by paintImmediately 
          jtxtMessages.repaint(); 
      }
      
    public void clear(){
        jtxtMessages.setText("");


    }
  
}
