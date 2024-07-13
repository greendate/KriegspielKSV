/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import java.awt.Color;
import java.awt.Label;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.net.*;
import javax.swing.JDialog;
import uk.co.kriegspiel.KSpielUI.OnlineStatus;

/**
 *
 * @author denysbennett
 */


public class JeevaClient extends Remote implements Runnable{
    
    // Constructors
    public JeevaClient(){
        
    }
    
    public JeevaClient(KSpielApp theApp, KSpielUI theUI){
        ksApp = theApp;
        ksUI = theUI;

       // UserName = "KsApp"; // for testing
        UserRoom = "General";
    }
    

    Socket socket;
    
    String UserRoom, ServerData, SplitString;
    StringTokenizer Tokenizer;
    StringBuffer stringbuffer;
    Label InformationLabel;
    int TotalUserCount;
    DataOutputStream dataoutputstream;
    DataInputStream datainputstream;
   // boolean StartFlag;
    Thread thread;
    ConnectDialog dialog;
    JDialog dialog2;
    
    
    @Override
    public boolean ConnectToServer(String PlayerName, String ServerName, int ServerPort){
        
        ksUI.setPlayerName(PlayerName);
     //   UserName = PlayerName;
        isConnected = false;
        
        ClientMessage("Connecting To Server "+ServerName+" on port "+ServerPort+"... Please Wait...");
        
        try{
            socket = TimedSocket.getSocket(ServerName, ServerPort, 3000);
            dataoutputstream = new DataOutputStream(socket.getOutputStream());
            // ***********Send HELO To Server ********** //
            SendMessageToServer("HELO "+ ksUI.getNamePlayer());			
            datainputstream  = new DataInputStream(socket.getInputStream());
            
          //  StartFlag = true;		
                
            ClientMessage("Connected");
            isConnected = true;

            }catch(IOException IoExc) {// QuitConnection(QUIT_TYPE_NULL);
                
                ClientMessage("Connection failed");
                isConnected = false;
                ksUI.onLamp(Color.GREEN);
            }

        if (isConnected){
                        
          // Start the message monitoring thread                  
          thread = new Thread(this);
          thread.start();				
         // EnableAll();	
          
          // This connection succeeded so serialize the config file with these new defaults
            
          ksApp.myCfg.PlayerName = PlayerName;
          ksApp.myCfg.JeevaServerName = ServerName;
          
          ksApp.writeConfig();
          
        }
        return isConnected;
    }
    
    @Override
    public void DisconnectServer(){
        
        isConnected = false;
        RemoveOpponent(ksUI.getNamePlayer()); 
        if(socket != null)
            
            {
            
                ClientMessage("CONNECTION TO THE SERVER CLOSED");
                try {
				
			SendMessageToServer("QUIT "+ksUI.getNamePlayer()+"~"+UserRoom);				
				
			socket.close();	
			socket = null;
			ksUI.offLamp();
                        ksUI.removeOpponents();
                        
			}catch(IOException IoExc) { 
                            
                            ClientMessage("failed to send QUIT to server or to close socket");
                        
                        }				
            }	
    }
    // System Message format:
    
    // MESS <UserRoom>~<fromUserName>: $<5char-commandcode><toUserName>:<optional command data>
    
    // User Message format:
    
    // MESS <UserRoom>~<fromUserName>: <Any text string not commencing with $>
    
    @Override
    public void SendMessage(SysMessage sys){
        
        SendMessage(sys.toString()+ksUI.getNameOpponent());
        
    }
    
    @Override
    public void SendMessage(String msg){
        
        SendMessageToServer("MESS "+UserRoom+"~"+ksUI.getNamePlayer()+": "+msg);
      //  ClientMessage(UserName+ ": "+msg); // echo to app for test
    }
	
    private void SendMessageToServer(String Message){
		try {
			dataoutputstream.writeBytes(Message+"\r\n");	
		}catch(IOException IoExc) {
                    
                    ClientMessage("failed to send: "+Message);
                }			
    }
  
    
    @Override
    public void SendChallenge(String strVarlet, KSpielUI.GameMode game, Piece.Color color, RuleSet rules ){
      // transmits a message inviting game type (K/C) and playing color (B/W)
    //  OpponentName = strVarlet;
      SendMessage(SysMessage.$CHLNG.toString()+strVarlet+":"+game.toString().charAt(0)+" "+color.toString());
      ChallengedMessage(strVarlet,game,color, rules); // echo to local client
    }
    
    @Override
    public void SendAcceptChallenge(boolean accept){
        if (accept)
            SendMessage(SysMessage.$ACCPT);
        else
            SendMessage(SysMessage.$REJCT);
    }
    
    @Override
    public void SendResign(){
        SendMessage(SysMessage.$RESGN);
    }
    
    @Override
    public void SendAcceptDraw(boolean accept){
        if (accept)
            SendMessage(SysMessage.$ACCDR);
        else
            SendMessage(SysMessage.$RJCDR);
    } 
    
  @Override
    public void SendMove(int iFrom, int iTo, int iMovesMade){
      SendMessage(SysMessage.$MOVES.toString()+ksUI.getNameOpponent()+":"+String.valueOf(iFrom)+","+String.valueOf(iTo));
    }
  
    @Override
  protected void ReceiveMove (String params){
        
        
        // may be overridden by specific messenger according to protocol
        int s = params.indexOf(","); // find separator
        
        //String strFrom = params.substring(0, s);
        int From = Integer.valueOf(params.substring(0, s));
        String strTo = params.substring(s+1);
        
        //s = strTo.indexOf(","); // find separator
        int To = Integer.valueOf(strTo.substring(s+1));
        
        ReceiveMove(From,To);
        
        /*
        int MoveNo = Integer.valueOf(strTo.substring(s+1));
               
        int movesMade = ksApp.mainBoard.getMovesMade();
               
        int expected;
        if (movesMade == 0 && ksUI.getPlayingColor() == Piece.Color.WHITE){
            
            expected = 0;
        } else {
            expected = movesMade+1;
        }
        // check that this move number is expected
               
        ksUI.sysout("received move no "+String.valueOf(MoveNo)+" expecting "+String.valueOf(expected));
        if (MoveNo == expected){
            // it's the expected move, 
            ReceiveMove(From,To);
        } else {
            // it's not the expected move
            // it's repeat of last move so ignore it        
        }
        */
        
  }
  
    @Override
  public void SendOfferDraw(){
      SendMessage(SysMessage.$OFDRW);
      OfferDrawMessage(); // echo to local client
  }
    /*
    private void UpdateInformationLabel()
	{
		stringbuffer = new StringBuffer();
		stringbuffer.append("User Name: ");
		stringbuffer.append(UserName);
		stringbuffer.append("       ");
		stringbuffer.append("Room Name: ");
		stringbuffer.append(UserRoom);
		stringbuffer.append("       ");
		stringbuffer.append("No. Of Users: ");
		stringbuffer.append(TotalUserCount);
		stringbuffer.append("       ");	
		InformationLabel.setText(stringbuffer.toString());
		
	}
    */
    public void run(){
     // this method listens for incoming messages on a separate thread
        if(thread == null) ksUI.offLamp();
        
       while(thread != null && socket != null){
            try {
                    //ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                   
                    ServerData = datainputstream.readLine();
                   // ClientMessage(ServerData);
                    
                    // ******** MESS RFC Coding Starts ********** //
                    if( ServerData.startsWith("MESS")){
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        // **** Chk whether ignored user ********* //
                        String strText = ServerData.substring(5);
                        
                        // extract the fromUserName:
                        int s = strText.indexOf(":");
                        String fromUser = strText.substring(0, s);
                        String msg = strText.substring(s+2);
                        
                        if (msg.startsWith("$")){
                            // it's a KS system message
                          ReceiveSystemMessage(fromUser, msg);
                            
                        } else {
                            // it's a user-user message, so just pass it on intact (including fromUser).
                            ksUI.OnReceiveMessage(strText);
                        //  ksUI.receiveMesssage(ServerData.substring(5));
                    //    if(!(tappanel.UserCanvas.IsIgnoredUser(ServerData.substring(5,ServerData.indexOf(":")))))						
                    //        messagecanvas.AddMessageToMessageObject(ServerData.substring(5),MESSAGE_TYPE_DEFAULT);							
                        }
                    }

                    // ********* LIST UserName;UserName; RFC Coding*********** //
                    if(ServerData.startsWith("LIST")) {
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        Tokenizer = new StringTokenizer(ServerData.substring(5),";");						
			/********Update the Information Label *********/
			TotalUserCount = Tokenizer.countTokens();						
		//	UpdateInformationLabel();
			// **********Add User Item into User Canvas ********* //						
		//	tappanel.UserCanvas.ClearAll();
                        ClearOpponentList();
			while(Tokenizer.hasMoreTokens()){
                            AddOpponent(Tokenizer.nextToken(), OnlineStatus.ONLINE);
                //            tappanel.UserCanvas.AddListItemToMessageObject(Tokenizer.nextToken());							
			}	
						
		//	messagecanvas.ClearAll();
                    //    ClearMessageboard();
                        ClientMessage(ksUI.getNamePlayer()+ " now connected to Kriegspiel (Jeeva) GameServer!");
                    //    ClientMessage("Welcome to the "+UserRoom+" Room!");
		//	messagecanvas.AddMessageToMessageObject("Welcome To The "+UserRoom+" Room!",MESSAGE_TYPE_JOIN);		
                    }
			
                    
                    
                    // *********Room Rfc ******** //
                    if( ServerData.startsWith("ROOM")){
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        // ********** Loading Room List in to Room Canvas ************** //
                        Tokenizer = new StringTokenizer(ServerData.substring(5),";");
                        UserRoom = Tokenizer.nextToken();
                   //     UpdateInformationLabel();
                        // **********Add User Item into User Canvas ********* //						
                  //      tappanel.RoomCanvas.ClearAll();
                  //      tappanel.RoomCanvas.AddListItemToMessageObject(UserRoom);
                        while(Tokenizer.hasMoreTokens()){
                            Tokenizer.nextToken();
                  //          tappanel.RoomCanvas.AddListItemToMessageObject(Tokenizer.nextToken());							
                        }
                    }

                     
                    
                    // ********** ADD RFC ********* //
                    if(ServerData.startsWith("ADD")){
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        // ********Update the Information Label ********* //
                        TotalUserCount++;
                     //   UpdateInformationLabel();

                        // **********Add User Item into User Canvas ********* //
                        SplitString = ServerData.substring(5);
                       // EnablePrivateWindow(SplitString);
                        AddOpponent(SplitString, OnlineStatus.ONLINE);
                        
                   //     tappanel.UserCanvas.AddListItemToMessageObject(SplitString);
                        
                   //     messagecanvas.AddMessageToMessageObject(SplitString + " joins chat...",MESSAGE_TYPE_JOIN);						
                    }

                    
                    
                    
                    // *********If User Name Alread Exists ********** //
                    if (ServerData.startsWith("EXIS")){	
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        ClientMessage(" User Name Already Exists... Try Again With Some Other Name!");
                    //    messagecanvas.AddMessageToMessageObject(" User Name Already Exists... Try Again With Some Other Name!",MESSAGE_TYPE_ADMIN);								
                        thread = null;
                        DisconnectServer();
                    //    QuitConnection(QUIT_TYPE_NULL);
                    }					 

                    // ******** REMOVE User RFC Coding ********** //
                    if (ServerData.startsWith("REMO")){	
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        SplitString = ServerData.substring(5);	

                     //   tappanel.UserCanvas.RemoveListItem(SplitString);
                     //   RemoveUserFromPrivateChat(SplitString);
                        RemoveOpponent(SplitString);
                        ClientMessage(SplitString+" has been logged Out from Chat!");
                     //   messagecanvas.AddMessageToMessageObject(SplitString+" has been logged Out from Chat!",MESSAGE_TYPE_LEAVE );

                        // *****Update the Information Label ******** /
                        TotalUserCount--;
                     //   UpdateInformationLabel();

                    }

                    

                    // ***** KICK RFC Starts *********** //
                    if (ServerData.startsWith("KICK")){
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                    //    messagecanvas.AddMessageToMessageObject("You are Kicked Out From Chat for flooding the message!",MESSAGE_TYPE_ADMIN);
                        thread = null;
                    //    QuitConnection(QUIT_TYPE_KICK);	
                    }

                    // ***** INKI RFC (Information about kicked off User ********* /
                    if( ServerData.startsWith("INKI")){
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        SplitString = ServerData.substring(5);							
                    //    tappanel.UserCanvas.RemoveListItem(SplitString);
                    //    RemoveUserFromPrivateChat(SplitString);
                    //    messagecanvas.AddMessageToMessageObject(SplitString+" has been kicked Out from Chat by the Administrator!",MESSAGE_TYPE_ADMIN );

                        // *****Update the Information Label ******** //
                        TotalUserCount--;
                     //   UpdateInformationLabel();	
                    }

                    // ***** Change Room RFC ********** //
                    if( ServerData.startsWith("CHRO")){
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        UserRoom = ServerData.substring(5);	
                    }

                    // ********** Join Room RFC ************* /
                    if( ServerData.startsWith("JORO")){
                     //   ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        SplitString = ServerData.substring(5);
                     //   tappanel.UserCanvas.AddListItemToMessageObject(SplitString);
                        // *****Update the Information Label ******** /
                        TotalUserCount++;
                     //   UpdateInformationLabel();	

                    //    messagecanvas.AddMessageToMessageObject(SplitString + " joins chat...",MESSAGE_TYPE_JOIN);
                    }

                    // ***********Leave Room RFC ********** /
                    if( ServerData.startsWith("LERO")){
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        SplitString = ServerData.substring(5,ServerData.indexOf("~"));
                        
                        RemoveOpponent(SplitString);
                    //    tappanel.UserCanvas.RemoveListItem(SplitString);
                    //    messagecanvas.AddMessageToMessageObject(SplitString+" has leaves "+UserRoom+" Room and join into "+ServerData.substring(ServerData.indexOf("~")+1)+" Room",MESSAGE_TYPE_ADMIN);													

                        // *****Update the Information Label ******** //
                        TotalUserCount--;
                    //    UpdateInformationLabel();	
                    }

                    // ********** Room Count RFC ******** /					
                    if( ServerData.startsWith("ROCO")){
                    //    ksUI.tweakLamp("J");   // cause lamp to alternate red and green
                        SplitString = ServerData.substring(5,ServerData.indexOf("~"));
                    //    tappanel.TxtUserCount.setText("Total Users in "+SplitString+" : "+ServerData.substring(ServerData.indexOf("~")+1));
                    }


                    /*
                    // ******* Private Message RFC ********** /
                    if( ServerData.startsWith("PRIV")){												
                        SplitString = ServerData.substring(5,ServerData.indexOf(":"));
                        // **** Chk whether ignored user ********* //
                       /*
                        if(!(tappanel.UserCanvas.IsIgnoredUser(SplitString)))	{
                            boolean PrivateFlag = false;
                            for(G_ILoop = 0; G_ILoop < PrivateWindowCount;G_ILoop++){								
                                if(privatewindow[G_ILoop].UserName.equals(SplitString)){
                                    privatewindow[G_ILoop].AddMessageToMessageCanvas(ServerData.substring(5));
                                    privatewindow[G_ILoop].show();
                                    privatewindow[G_ILoop].requestFocus();
                                    PrivateFlag = true;
                                    break;										
                                }
                            }	

                            if(!(PrivateFlag)){	
                                if(PrivateWindowCount >= MAX_PRIVATE_WINDOW){
                                    messagecanvas.AddMessageToMessageObject("You are Exceeding private window limit! So you may lose some message from your friends!",MESSAGE_TYPE_ADMIN);	
                                }
                                else {														
                                    privatewindow[PrivateWindowCount++] = new PrivateChat(this,SplitString);
                                    privatewindow[PrivateWindowCount-1].AddMessageToMessageCanvas(ServerData.substring(5));
                                    privatewindow[PrivateWindowCount-1].show();
                                    privatewindow[PrivateWindowCount-1].requestFocus();																
                                }
                            }

                        }
                        
                        
                    }
                    
                //    ksUI.tweakLamp("J");
                    */
                    
            }catch(Exception Exc) { 
                ClientMessage(Exc.getMessage());
                thread.interrupt();

              //  messagecanvas.AddMessageToMessageObject(_Exc.getMessage(),MESSAGE_TYPE_ADMIN);QuitConnection(QUIT_TYPE_DEFAULT); 
            }	
	}
     //   ksUI.tweakLamp("J");
        if(thread == null) ksUI.offLamp();
   }
}


