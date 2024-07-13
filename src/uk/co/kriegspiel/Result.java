/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/**
 *
 * @author denysbennett
 */
/** Result Class
 *
 * @author denysbennett
 */
public class Result {
    // Result of move - structured data packet
    public boolean bCheckmate = false;
    public boolean bStalemate = false;
    public boolean bWhiteInCheck = false;
    public boolean bBlackInCheck = false;
    public boolean bWhiteResigns = false;
    public boolean bBlackResigns = false;
    public boolean bAgreedDraw = false;
    
    public boolean bQueenedPawn = false; //V3.02
    
    public Piece capture = null;
    public int iRedSquare= 0;
    public int iCheckByKnight = 0; // semaphore to show UI square of a checking Knight
    public int iCheckedKing = 0;   // matching semaphore showing location of King
   
    //v.302
    public boolean bCheckLong = false; 
    public boolean bCheckShort = false;
    public boolean bCheckCol = false;
    public boolean bCheckRow = false;
    
    public int iRedisplayButton = 0;
    public int iWhiteRemain;
    public int iBlackRemain;
    public enum ErrorCode{NO_ERROR, KING_NOT_FOUND, OUT_OF_RANGE, KING_TAKEN}
    public ErrorCode error = ErrorCode.NO_ERROR;
    
    // Constructor
    public Result(){
        
    }

    public void Reset(){
        bCheckmate = false;
        bStalemate = false;
        bWhiteInCheck = false;
        bBlackInCheck = false;
        bWhiteResigns = false;
        bBlackResigns = false;
        bAgreedDraw = false;
        
        bQueenedPawn = false;   //V3.02
        
        error = ErrorCode.NO_ERROR;
        capture = null;
        iRedSquare = 0;
        iCheckByKnight = 0;
        iCheckedKing = 0;

        //v3.02
        bCheckLong = false;
        bCheckShort = false;
        bCheckCol = false;
        bCheckRow = false;
        
        iRedisplayButton = 0;
    }
 /*   
    public void setRedSquare(int iSquare){
        iRedSquare = iSquare;
      //  capture = taken;
    }
 */   
    public Result copy(){
        Result newcopy = new Result();
        newcopy.bCheckmate = bCheckmate;
        newcopy.bStalemate = bStalemate;
        newcopy.bWhiteInCheck = bWhiteInCheck;
        newcopy.bBlackInCheck = bBlackInCheck;
        newcopy.bWhiteResigns = bWhiteResigns;
        newcopy.bBlackResigns = bBlackResigns;
        newcopy.bAgreedDraw = bAgreedDraw;
        
        newcopy.bQueenedPawn = bQueenedPawn;    //V3.02
        
        newcopy.iRedSquare= iRedSquare;
        newcopy.iCheckByKnight = iCheckByKnight; // semaphore to show UI square of a checking Knight
        newcopy.iCheckedKing = iCheckedKing;   // matching semaphore showing location of King
        
        //V3.02
        newcopy.bCheckShort = bCheckShort;
        newcopy.bCheckLong = bCheckLong;
        newcopy.bCheckRow = bCheckRow;
        newcopy.bCheckCol = bCheckCol;
        
        newcopy.iRedisplayButton = iRedisplayButton;
        newcopy.iWhiteRemain = iWhiteRemain;
        newcopy.iBlackRemain = iBlackRemain;
        newcopy.error = error;
        newcopy.capture = capture;
        return newcopy;
    }
}

