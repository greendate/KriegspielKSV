/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.JTextField;

// import com.skype.Skype;
// import com.skype.SkypeException;

/** Dialog for connecting to game server
 *  - invoked by remote client
 * @author denysbennett
 */
public class ConnectDialog extends JDialog implements ActionListener{

    private KSpielApp ksApp;
    private boolean isConnect;
    
    private JPanel jpnlProtocol;
    private JLabel jlbProtocol;
    private String strProtocol;
    
    private JPanel jpnlServer;
    private JPanel jpnlButtons;
    private JPanel jpnlContent;

    private JLabel jlbPlayerName;
    private JTextField jtfPlayerName;

    private JLabel jlbServerName;
    private JLabel jlbServerPort;
    private JTextField jtfServerName;
    private JTextField jtfServerPort;
    
    private ButtonGroup btnGroup;
    private JRadioButton jrbSkype;
  //  private JRadioButton jrbKS3;
    private JRadioButton jrbJeeva;
    
    private JButton jbtnConnect;
    private JButton jbtnCancel;
    
    
    // Constructor
    public ConnectDialog(JFrame mainFrame, KSpielApp theApp){
        
        
        
      super(mainFrame, true);
      
      ksApp = theApp;
      setTitle("Find Network Game Server");
      setSize(new Dimension(800,250));
      setModal(true);
      
        isConnect =false;
        
         //Create the text and components to be displayed.
        String strName = "Enter the computer name or IP address of the Game Server";
        String strPort = "Enter the logical port number";
        String strPlayerName ="Enter your name";
       
        jlbPlayerName = new JLabel(strPlayerName); 
        jtfPlayerName = new JTextField(strPlayerName);
        jlbServerName = new JLabel(strName);
        jlbServerPort = new JLabel(strPort);
        jtfPlayerName = new JTextField(ksApp.myCfg.PlayerName);
        jtfServerName = new JTextField(ksApp.myCfg.JeevaServerName);
        jtfServerPort = new JTextField("1436");
        jbtnCancel = new JButton("Cancel");
        jbtnConnect = new JButton("Connect");

        jlbProtocol = new JLabel("Choose the Game Server protocol");
        btnGroup = new ButtonGroup();
        jrbSkype = new JRadioButton("Skype");

        jrbJeeva = new JRadioButton("Jeeva Chatserver");
        btnGroup.add(jrbSkype);

        btnGroup.add(jrbJeeva);
        
        jpnlProtocol = new JPanel();
        jpnlProtocol.setLayout(new GridLayout(3,1));
        jpnlProtocol.add(jrbJeeva);
        jpnlProtocol.add(jrbSkype);

        
        
        jpnlServer = new JPanel();
        jpnlServer.setLayout(new GridLayout(3,2));
        jpnlServer.add(jlbPlayerName);
        jpnlServer.add(jtfPlayerName);
        jpnlServer.add(jlbServerName);
        jpnlServer.add(jtfServerName);
        jpnlServer.add(jlbServerPort);
        jpnlServer.add(jtfServerPort);
        
        
        
        jpnlButtons = new JPanel();
        
        jpnlButtons.add(jbtnCancel);
        jpnlButtons.add(jbtnConnect);
        
        getRootPane().setDefaultButton(jbtnConnect);
        
        jrbSkype.setSelected(true);

        jrbJeeva.setSelected(false);
        setSkype();
        
        
        

        jpnlContent = new JPanel();
        jpnlContent.add(jlbProtocol);
        jpnlContent.add(jpnlProtocol);
        jpnlContent.add(jpnlServer);
        jpnlContent.add(jpnlButtons);

        jrbSkype.addActionListener(this);

        jrbJeeva.addActionListener(this);
        
        jbtnConnect.addActionListener(this);
        jbtnCancel.addActionListener(this);

        setContentPane(jpnlContent);
             
        setVisible(true);
        
       
    }
    private String getSkypePrompt(){
        String str = "Make sure Skype is running before you press Connect..!";

        return str;
    }
    
    public void actionPerformed(ActionEvent event) {
        
        
        if (event.getSource() == jbtnConnect){
            isConnect = true;
            setVisible(false);
            
        } else if (event.getSource() == jbtnCancel) {
            isConnect = false;
            setVisible(false);
            
        } else if (event.getSource() == jrbSkype) {
            setSkype();

            
        } else if (event.getSource() == jrbJeeva){
            setJeeva();
            
        }

    }
    
    private void setJeeva(){
        
        
            strProtocol = "Jeeva";
            jtfPlayerName.setEnabled(true);
            jlbPlayerName.setEnabled(true);
            jtfServerName.setText(ksApp.myCfg.JeevaServerName);
            jtfServerName.setEnabled(true);
            jlbServerName.setEnabled(true);
            jtfServerPort.setText("1436");
            jtfServerPort.setEnabled(true);
            jlbServerPort.setEnabled(true);
            jrbSkype.setSelected(false);

        
    }
 
    private void setSkype(){
            strProtocol = "Skype";    
            jtfServerName.setText(getSkypePrompt());
            jtfServerName.setEnabled(false);
            jlbServerName.setEnabled(false);
            
            jtfPlayerName.setEnabled(false);
            jlbPlayerName.setEnabled(false);
            jtfServerPort.setText("0"); // needs a value or will generate exception in ksUI.loginToChat
            jtfServerPort.setEnabled(false);
            jlbServerPort.setEnabled(false);
            jrbJeeva.setSelected(false);

    }
    public String getPlayerName(){
        return jtfPlayerName.getText();
    }

    public String getServerName(){
      return jtfServerName.getText();
    }
    
    public String getServerPort(){
      return jtfServerPort.getText();
    }
    
    public boolean IsConnected(){
      return isConnect;
    }
    
    public String getProtocol(){
        return strProtocol;
    }
 }
    
   