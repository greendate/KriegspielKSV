/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package uk.co.kriegspiel;


import uk.co.kriegspiel.KSpielUI.OnlineStatus;

/** The Remote parent class
 * 
 *   Interfaces the board with a remote or robotic player
 *   
 *   Subclasses implement different communiation protocols
 * 
 *   Subclasses include
 *      SkypeCom    - a class for usingSkype app-app messaging
 *      KSCom       - a class implemeting the original LAN chat-server comms
 *      Robot       - a class simulating a remote opponent
 * 
 *
 * @author denysbennett
 */

public class Remote {
    protected enum SysMessage{$CHLNG,$ACCPT,$REJCT,$OFDRW,$ACCDR,$RJCDR,$RESGN,$ACKNG,$MOVES,$MOVEB,$MOVEW,$MVACK};

    protected KSpielApp ksApp;
    protected KSpielUI ksUI;
    protected Piece.Color myColor;
    
//    protected String UserName;
 //   protected String OpponentName; // current opponent
    protected boolean isConnected;
    
    // Serial numbers of sent and received messages
    static protected int messageSerialOut;  // serial of last message sent
    static protected int messageSerialIn;   // serial of last message received
    static protected String lastMessageSent;
    
//    private int iMoveFrom;
//    private int iMoveTo;
    
    // Constructors
    public Remote(){
      
    }
    public Remote(KSpielApp theApp, KSpielUI theUI, Piece.Color color){
        ksApp = theApp;
        ksUI = theUI;
        myColor = color;
        isConnected = false;
    }

    // The UI sends messages to a remote player via the following method calls.
    // The methods have an ActionListener counterpart for receiving messages.
    
    // The public Send methods are over-ridden by the inheriting Remote subclass particular to the comms protocol
    // The protected Receive methods call on the UI methods as if they were via user clicks.
    
    
    // Connection and Disconnection
    public boolean ConnectToServer(String PlayerName, String ServerName, int ServerPort){
        // logs in to game server
        // a messenger client overrides this
        ksUI.setPlayerName(PlayerName);
        // UserName = PlayerName; // and MUST contain this statement
        return false;   
    }
    
    public void DisconnectServer(){
        // logs off game server
        // overridden by active messenger object
    }
    
    public boolean IsConnected(){
        return isConnected;   // overridden by active messenger object
    }
    
    public void AdviseOpponent(String strVarlet){
        // called when a new opponent is selected
        // this normally does nothing, but can be overridden by messenger
        // to revise a p2p connection
    }
    
    // SENDING
    public void SendMove(int iFrom, int iTo, int iMovesMade){
      // transmits a move across the network to the ActionListener counterpart
        // overridden by active messenger object
    }
    
    public void SendChallenge(String strVarlet, KSpielUI.GameMode game, Piece.Color color,
            RuleSet rules){
      // transmits a message inviting game playing color
      // overridden by active messenger object
    }
    
    public void SendAcceptChallenge(boolean accept){
      // overridden by active messenger object
    }
    
    public void SendOfferDraw(){
      // overridden by active messenger object
    }
    
    public void SendAcceptDraw(boolean accept){
      // overridden by active messenger object
    }
    
    public void SendResign(){
        // overridden by active messenger object
    }
    public void SendMessage(SysMessage sys){
        // sends user -> user predefined system message
        // overridden by active messenger object
    }
    public void SendError(){
        // advise that a terminal error has occurred
        // overridden by active messenger object
    }
    
    
    public void SendMessage(String msg){
        // sends user -> user message
        // overridden by active messenger object
    }
    
    
  
    // RECEIVING
    // The following counterpart methods are invoked by the (remote) ActionListener
    // in the Remote subclass on receipt of a message.
    
    // App - App Messaging
    
        // System Message format:

        // MESS <UserRoom>~<fromUserName>: $<5char-commandcode><toUserName>:<optional command data>

        // User Message format:

        // MESS <UserRoom>~<fromUserName>: <Any text string not commencing with $>
    
    // receive system message as unparsed string
    protected void ReceiveSystemMessage(String fromUser, String sysMessage){
        
        // is the message addressed to me?
    //    UserName = ksUI.getNamePlayer();
        if (sysMessage.startsWith(ksUI.getNamePlayer(), 6)){
        
           // Message is addressed to me, so decode it. 
             
            String cmd = sysMessage.substring(0, 6);
            // strip out my name
            int s = sysMessage.indexOf(":");
            String params = sysMessage.substring(s+1);
          //  ClientMessage("From user: " + fromUser +"-"+cmd+"-"+ params +"."); // show for test
        
           if        (cmd.startsWith("$CHLNG")){
               KSpielUI.GameMode game; Piece.Color color;
               if (params.startsWith("C"))
                   game = KSpielUI.GameMode.CHESS;
               else 
                   game = KSpielUI.GameMode.KSPIEL;
               if (params.substring(2).startsWith("B"))
                   color = Piece.Color.BLACK;
               else
                   color = Piece.Color.WHITE;
               
               //v3.02  will need rule conformance check/set here
               String strLastChar = params.substring(params.length()-1);
               RuleSet rules = new RuleSet(strLastChar);
               
               ReceiveChallenge(fromUser,game,color,rules);

           } else if (cmd.startsWith("$ACCPT")){
               ReceiveAcceptChallenge(true);
           } else if (cmd.startsWith("$REJCT")){
               ReceiveAcceptChallenge(false);
           } else if (cmd.startsWith("$OFDRW")){
               ReceiveOfferDraw();
           } else if (cmd.startsWith("$ACCDR")){
               ReceiveAcceptDraw(true);
           } else if (cmd.startsWith("$RJCDR")){
               ReceiveAcceptDraw(false);
           } else if (cmd.startsWith("$RESGN")){
               ReceiveResign();
           } else if (cmd.startsWith("$ACKNG")){

           } else if (cmd.startsWith("$MOVES")){
               
               ReceiveMove(params);       // this may be messenger specific
               
           } else if (cmd.startsWith("$MOVEB")){

           } else if (cmd.startsWith("$MOVEW")){

           } else if (cmd.startsWith("$MVACK")){

           }
           
        } else {
            ClientMessage("NOT ME: "+ fromUser + sysMessage);
        }
    }
    
    protected void ReceiveMove (String params){
        
        // may be overridden by specific messenger according to protocol
        /*
        int s = params.indexOf(","); // find separator
        //String strFrom = params.substring(0, s);
        int From = Integer.valueOf(params.substring(0, s));
        String strTo = params.substring(s+1);
        s = strTo.indexOf(","); // find separator
        int To = Integer.valueOf(strTo.substring(0,s));
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
         * */
    }
    protected void ReceiveMove (int iFrom, int iTo){
      
        if (iFrom>0) 
            ksApp.mainBoard.setSelectedSquare(iFrom); // from 0 initiates a game
        ksUI.beep();

           //V4.1 omitted this feature which was in earlier versions and v3.05
             // new V4.2 step sequence

           if (ksUI.isDemoMode() ){ //only  if demo
                ksUI.StepDisplay(iFrom,iTo);

           } else
           //---- end animation v4.02

        ksUI.InitiateMove(iFrom, iTo);
        
    }
        
    protected void ReceiveChallenge (String strVarlet, KSpielUI.GameMode game, 
            Piece.Color color, RuleSet rules ){
      
        ChallengedMessage(strVarlet,game,color, rules );
        //OpponentName = strVarlet;
        ksUI.OnReceiveChallenge(strVarlet, game, color, rules);
    }
    protected void ChallengedMessage(String strVarlet, KSpielUI.GameMode game,
            Piece.Color color, RuleSet rules){
        ClientMessage(strVarlet +" is challenging "+ksUI.getNamePlayer()+" to "+game.toString()+ 
                " and to play "+color.toString() + ", announcing "+
                rules.getAnnouncement());
    }
    protected void ChallengingMessage(String strVarlet, 
            KSpielUI.GameMode game, Piece.Color color, RuleSet rules){
        ClientMessage(ksUI.getNamePlayer() +" is challenging "+strVarlet+" to "+game.toString()+ 
                " and to play "+color.toString() + ", announcing "+
                rules.getAnnouncement());
        
    }
    protected void ReceiveAcceptChallenge(boolean accept){
      String ans;
      if (accept) ans = "accepted."; else ans = "rejected.";
      ClientMessage("Your challenge to "+ksUI.getNameOpponent()+" has been "+ans);
        ksUI.OnReceiveChallengeResponse(accept);
    }
    
    protected void ReceiveOfferDraw(){
        ClientMessage(ksUI.getNameOpponent()+" is offering a draw.");
        ksUI.OnReceiveOfferDraw();  
    }
    protected void OfferDrawMessage(){
        ClientMessage(ksUI.getNamePlayer()+" is offering a draw.");
    }
    
    protected void ReceiveAcceptDraw(boolean accept){
      
      String ans;
      if (accept) ans = "accepted"; else ans = "rejected";
      ClientMessage(ksUI.getNameOpponent()+" has "+ans+" your offer of a draw.");
      ksUI.OnReceiveOfferDrawReply(accept);
    }
    
    protected void ReceiveResign(){
        ClientMessage(ksUI.getNameOpponent()+" has resigned.");
        ksUI.OnReceiveResign(); // true from remote, false from button
    }
    
    protected void ReceiveMessage(String msg){
        ksUI.OnReceiveMessage(msg);
    }
    
    public void ClientMessage(String msg){
        ksUI.OnReceiveMessage("**"+msg);
    }
    public void ClearMessageboard(){
        ksUI.clearMessageboard();
    }
    public void ClearOpponentList(){
        ksUI.removeOpponents();
    }
    public void AddOpponent(String Opponent, OnlineStatus status){
        ksUI.addOpponent(Opponent, status);
    }
    public void RemoveOpponent(String Opponent){
        ksUI.removeOpponent(Opponent);
    }
    
    public void GameEnd(){
        // advisory, can be overridden if needed
    }
}
