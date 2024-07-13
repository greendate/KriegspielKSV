/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.io.Serializable;

/**
 * This object defines the Kriegspiel variant announcement rules
 * Pawn promotions
 * Pawn takes distinct from piece takes
 * @author denysbennett
 */
public class RuleSet implements Serializable {  // V3.02
    
    public boolean bAnnounceQueens;
    public boolean bPawnsAndPieces;
       

    // constructor for ruleset from alphacode
    /**
     * this constructor translates the transmissible String code into a RuleSet object
     * used to instruct the referee to announce pawn events as follows:
     * Code     Promotions  Takes
     *  P       yes         yes
     *  Q       yes         no
     *  R       no          yes
     *  S       no          no
     * @param strCode (P,Q,R,or S)
     */
    public RuleSet(String strCode){
        bAnnounceQueens = false; // default S
        bPawnsAndPieces = false;
        
        if (strCode.contains("P")) {
            bAnnounceQueens = true;
            bPawnsAndPieces = true;
        } else if (strCode.contains("Q")){
            bAnnounceQueens = true;
            bPawnsAndPieces = false;
        } else if (strCode.contains("R")){
            bAnnounceQueens = false;
            bPawnsAndPieces = true;
        }
    }

    // constructor for Serialization
    public RuleSet(){
        
    }
     
    /**
     * this method provides a transmissible translation code for the ruleset
     * to announce pawn events as follows
     * Code     Promotions  Takes
     *  P       yes         yes
     *  Q       yes         no
     *  R       no          yes
     *  S       no          no
     * @return String (P,Q,R,S)
     */
    public String getAlphaCode(){
        
          String strRules = "S"; // default code // V3.02

            if (bAnnounceQueens){
                if (bPawnsAndPieces) strRules = "P";  //promos & takes
                else strRules = "Q"; //promos & not takes
            } else {
                if (bPawnsAndPieces) strRules = "R";  //takes & not promos
                else strRules = "S"; //not promos & not takes
            }
        
        
        return strRules;
    }
    
    /**
     * this method translates the RuleSet into plain text for alerts
     *
     * @return String
     */

    public String getAnnouncement(){
        String strRules="Announce ";

        if (bAnnounceQueens){
                if (bPawnsAndPieces) strRules = "pawn Promotions AND Takes";  
                else strRules = "pawn Promotions but NOT Takes"; 
            } else {
                if (bPawnsAndPieces) strRules = "pawn Takes but NOT Promotions";  
                else strRules = "neither pawn Promotions NOR Takes";
            }
        return strRules;
    }

    public boolean isSameAs(RuleSet rules){

        return (bPawnsAndPieces == rules.bPawnsAndPieces &&
                bAnnounceQueens == rules.bAnnounceQueens);
    }
    
}
