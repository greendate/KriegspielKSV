/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/*
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
 */ 
import java.io.Serializable;

/** This class contains the configuration data for the program
 * which can be serialized and reloaded.
 *
 * @author denysbennett
 */
public class Config implements Serializable {
    
// constructor
   public Config(String str, KSpielApp ksApp){
       
        version = str;
        // invent default settings
        PlayerName = "KS Player";
        JeevaServerName = "192.168.0.1";
        JeevaPort = "1436";
        nameBlack = "";
        nameWhite = "";
        lastPath = null;
        protocol = "Skype";
        
        rules = new RuleSet();
        
        rules.bAnnounceQueens = false;  //new default v4.2
        rules.bPawnsAndPieces = true;

        soundOn = true;
   }
   public String version;
   public String protocol;
   public String PlayerName;
   public String JeevaServerName;
   public String JeevaPort;

   public String nameWhite;
   public String nameBlack;
   public String lastPath;
   
   public RuleSet rules;

   public boolean soundOn;
   
 
}

