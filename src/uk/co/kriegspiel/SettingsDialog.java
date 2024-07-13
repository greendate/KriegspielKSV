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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author denysbennett
 */
public class SettingsDialog extends JDialog implements ActionListener{

    private KSpielApp ksApp;
    private JPanel jpnlContent;
    
    private JPanel jpnlNames;
    private JLabel jlbWhite;
    private JLabel jlbBlack;
    private JLabel jlbPlayer;
    
    private JTextField jtfWhiteName;
    private JTextField jtfBlackName;
    private JTextField jtfPlayer;
     
    private JPanel jpnlProtocol;
    private JLabel jlbProtocol;
    private ButtonGroup btnGroup;
    private JRadioButton jrbSkype;
    private JRadioButton jrbJeeva;
    private String strProtocol;
    
    private JPanel jpnlServer;
    private JLabel jlbServerName;
    private JLabel jlbServerPort;
    private JTextField jtfServerName;
    private JTextField jtfServerPort;
    
    private JPanel jpnlPath;
    private JLabel jlbSavepath;
    private JTextField jtfSavepath;
    
    private JPanel jpnlButtons;
    private JButton jbtnOK;
    private JButton jbtnCancel;
 
    // V3.02 Rules settings
    private JPanel jpnlRules;
//    private ButtonGroup bgRules;
    private JCheckBox jrbShowPromotions;
    private JCheckBox jrbShowPawnTakes;
    
    
    // constructor
    
    public SettingsDialog(JFrame mainFrame, KSpielApp theApp){
        
        
        
      super(mainFrame, true);
      
      ksApp = theApp;
      setTitle("Settings");
      setSize(new Dimension(800,450));
      setModal(true);
      
      jpnlContent = new JPanel();
      
      // make the names panel
      jpnlNames = new JPanel();
      jpnlNames.setBorder(ksApp.ksUI.borderTitle("Player Names"));
      jlbWhite = new JLabel("White");
      jlbBlack = new JLabel("Black");
      jlbPlayer = new JLabel("Skype Name");
      jtfWhiteName = new JTextField(ksApp.myCfg.nameWhite);
      jtfBlackName = new JTextField(ksApp.myCfg.nameBlack);
      jtfPlayer = new JTextField(ksApp.myCfg.PlayerName);
      jtfWhiteName.setPreferredSize(new Dimension(125,20));
      jtfBlackName.setPreferredSize(new Dimension(125,20));
      jtfPlayer.setPreferredSize(new Dimension(125,20));
    
      jpnlNames.add(jlbWhite);
      jpnlNames.add(jtfWhiteName);
      jpnlNames.add(jlbBlack);
      jpnlNames.add(jtfBlackName);
      jpnlNames.add(jlbPlayer);
      jpnlNames.add(jtfPlayer);
      
      // make the server options panel
 
      jpnlProtocol = new JPanel();
      jpnlProtocol.setLayout(new GridLayout(3,1));
      jpnlProtocol.setBorder(ksApp.ksUI.borderTitle("Remote connection"));
      
      jlbProtocol = new JLabel("Choose the connection protocol");
      
      btnGroup = new ButtonGroup();
      jrbSkype = new JRadioButton("Skype");
      jrbJeeva = new JRadioButton("Jeeva Chatserver");
      btnGroup.add(jrbSkype);
      btnGroup.add(jrbJeeva);
      jrbSkype.addActionListener(this);
      jrbJeeva.addActionListener(this);
      
      jrbSkype.setSelected(true);
      jrbJeeva.setSelected(false);
      
      jpnlProtocol.add(jlbProtocol);
      jpnlProtocol.add(jrbJeeva);
      jpnlProtocol.add(jrbSkype);
      
      //V3.02 Make the rules panel
      
      jpnlRules = new JPanel();      
      jpnlRules.setPreferredSize(new Dimension(250,100));
      jpnlRules.setLayout(new GridLayout(2,1));
      jpnlRules.setBorder(ksApp.ksUI.borderTitle("Kriegspiel Rules"));
      jrbShowPromotions = new JCheckBox("Announce pawn promotions");
      jrbShowPawnTakes = new JCheckBox("Announce pawn gone");
      jrbShowPromotions.setSelected(ksApp.myCfg.rules.bAnnounceQueens);
      jrbShowPawnTakes.setSelected(ksApp.myCfg.rules.bPawnsAndPieces);
      jpnlRules.add(jrbShowPromotions);
      jpnlRules.add(jrbShowPawnTakes);
      
      // make the LAN server panel
      jpnlServer = new JPanel();
      jpnlServer.setBorder(ksApp.ksUI.borderTitle("LAN settings"));
      
      String strName = "Enter the computer name or IP address of the LAN Server";
      String strPort = "Enter the logical port number (default = 1436)";
      jlbServerName = new JLabel(strName);
      jlbServerPort = new JLabel(strPort);
      jtfServerName = new JTextField(ksApp.myCfg.JeevaServerName);
      jtfServerPort = new JTextField(ksApp.myCfg.JeevaPort);
      
      jpnlServer.setLayout(new GridLayout(3,2));
      jpnlServer.add(jlbServerName);
      jpnlServer.add(jtfServerName);
      jpnlServer.add(jlbServerPort);
      jpnlServer.add(jtfServerPort);
      
      // make the path panel
      jpnlPath = new JPanel();
      jpnlPath.setPreferredSize(new Dimension(300,60));
      jlbSavepath = new JLabel("Path to saved games");
      jtfSavepath = new JTextField(ksApp.myCfg.lastPath);
      jtfSavepath.setPreferredSize(new Dimension(280,20));
      jpnlPath.add(jlbSavepath);
      jpnlPath.add(jtfSavepath);
      
      // make the buttons panel
      jpnlButtons = new JPanel();
      
      jbtnCancel = new JButton("Cancel");
      jbtnOK = new JButton("Save");
      
      jpnlButtons.add(jbtnOK);
      jpnlButtons.add(jbtnCancel);
      
      jbtnOK.addActionListener(this);
      jbtnCancel.addActionListener(this);
      
      // Add all the sub panels to the content pane
                  
      jpnlContent.add(jpnlNames);
      jpnlContent.add(jpnlServer);
      jpnlContent.add(jpnlProtocol);
      jpnlContent.add(jpnlPath);
      jpnlContent.add(jpnlButtons);

      jpnlContent.add(jpnlRules); //v3.02
      
      getRootPane().setDefaultButton(jbtnOK);
      
      setContentPane(jpnlContent);
      setSkype();
      setVisible(true);
      
      
    }
    
    public void actionPerformed(ActionEvent event) {
        
        
        if (event.getSource() == jbtnOK){
            // set the configuration
            ksApp.myCfg.JeevaServerName = getServerName();
            ksApp.myCfg.PlayerName = getPlayerName();
            ksApp.myCfg.JeevaPort = getServerPort();
            ksApp.myCfg.lastPath = getPath();
            ksApp.myCfg.nameBlack = getBlack();
            ksApp.myCfg.nameWhite = getWhite();
            ksApp.ksUI.setNames(getWhite(), getBlack());
            ksApp.myCfg.protocol = strProtocol;
            
            //V3.02
            ksApp.myCfg.rules.bAnnounceQueens = jrbShowPromotions.isSelected();
            ksApp.myCfg.rules.bPawnsAndPieces = jrbShowPawnTakes.isSelected();
                        
           // save the configuration in the config file
            ksApp.writeConfig();
            ksApp.ksUI.setConnectToolTip();
            setVisible(false);
            
        } else if (event.getSource() == jbtnCancel) {
            
            setVisible(false);
            
        } else if (event.getSource() == jrbSkype) {
            setSkype();

            
        } else if (event.getSource() == jrbJeeva){
            setJeeva();
            
        }

    }
    
    private void setJeeva(){
        
        
            strProtocol = "Jeeva";
            jtfPlayer.setEnabled(true);
            jlbPlayer.setText("Player name");
            jlbPlayer.setEnabled(true);
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
            jtfServerName.setEnabled(false);
            jlbServerName.setEnabled(false);
            
            jtfPlayer.setEnabled(false);
            jlbPlayer.setText("Skype name");
            jlbPlayer.setEnabled(false);
            jtfServerPort.setText("0"); // needs a value or will generate exception in ksUI.loginToChat
            jtfServerPort.setEnabled(false);
            jlbServerPort.setEnabled(false);
            jrbJeeva.setSelected(false);

    }

     
    public String getPlayerName(){
        return jtfPlayer.getText();
    }

    public String getServerName(){
      return jtfServerName.getText();
    }
    
    public String getServerPort(){
      return jtfServerPort.getText();
    }    
    
    public String getProtocol(){
        return strProtocol;
    }
        
    public String getBlack(){
        return jtfBlackName.getText();
    }
    public String getWhite(){
        return jtfWhiteName.getText();
    }
    public String getPath(){
        return jtfSavepath.getText();
    }
}
