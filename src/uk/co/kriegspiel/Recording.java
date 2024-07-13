/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author denysbennett
 */
public class Recording implements Serializable {
    
    private ArrayList<Integer> listMovesFrom;
    private ArrayList<Integer> listMovesTo;
    private int nextMove;
    
    private KSpielUI.GameEnd gameEnd;
    
    private String blackName;
    private String whiteName;
    
    public Recording(){
        listMovesFrom = new ArrayList<Integer>();
        listMovesTo = new ArrayList<Integer>();
        nextMove = 0;
        blackName = "";
        whiteName = "";
    }

    public void RecordMove(int iFrom, int iTo){
        listMovesFrom.add(iFrom);
        listMovesTo.add(iTo);
    }
    public int getSize(){
        return listMovesFrom.size();
    }
    public boolean isAtEnd(){
        return (nextMove >= listMovesFrom.size());
    }
    
    public void endsWith(KSpielUI.GameEnd ending){
        gameEnd = ending;
    }
    public KSpielUI.GameEnd getEnding(){
        return gameEnd;
    }
    
    public void Rewind(){
        nextMove = 0;
    }
    public void setBlack(String name){
        blackName = name;
    }
    public String getBlack(){
        return blackName;
    }
    public void setWhite(String name){
        whiteName = name;
    }
    public String getWhite(){
        return whiteName;
    }
    public int NextMove(){
        nextMove++;
        return nextMove;
    }
    public int getTo(){
        return listMovesTo.get(nextMove);
    }
    public int getFrom(){
        return listMovesFrom.get(nextMove);
    }
    public void undoLast(){
      int last = listMovesFrom.size() -1;
      listMovesFrom.remove(last);
      listMovesTo.remove(last);
  }
}
