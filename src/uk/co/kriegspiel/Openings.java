/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;
import java.util.ArrayList;

/** This class lists some opening move sequences
 *  for use by the robots
 *
 * @author denysbennett
 */
public class Openings {
    
    private ArrayList<ArrayList<Integer>> OpeningMoves; // a list of lists
    // Each opening move sequence is a series of From To integers
    private int howMany;
    
    // constructor
    public Openings(Piece.Color color){
        
        Piece.Color myColor = color;
        OpeningMoves = new ArrayList<ArrayList<Integer>>();
        // preset a selection of opening moves
        howMany = 0;
        
        if (myColor == Piece.Color.WHITE){
            // White's openings here
        
            // Create 1st move sequence 
            ArrayList<Integer> sequence = new ArrayList<Integer>();       
            // load up the moves sequence
            sequence.add(13);    // King's pawn      first from
            sequence.add(29);    // to K4            first to
            sequence.add(12);    // Queen's pawn
            sequence.add(20);    // to Q3
            sequence.add(7);        // King's Knight to KR3
            sequence.add(24);       // (Fool's mate defence0
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;

       /*
            // Create 2nd move sequence - Queen's's Knight raider
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
 //           sequence.add(12);   //Queen's pawn
 //           sequence.add(20);   // to Q3

            sequence.add(2);    // Queen's Knight
            sequence.add(19);    // to QB3

//            sequence.add(3);    // Queen's Bishop
//            sequence.add(30);   // to KB(f)4


            sequence.add(19);   //Knight to QB5
            sequence.add(34);

        //    sequence.add(12);   //Bishop to QR5
        //    sequence.add(33);

            sequence.add(34);   // Knight to QB7
            sequence.add(51);

            sequence.add(51);   // Knight attempts Rook
            sequence.add(57);

            sequence.add(57);   // Knight withdraws
            sequence.add(51);

            sequence.add(51);  // Knight attempts Queen
            sequence.add(61);

            sequence.add(61);  // ditto
            sequence.add(55);
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
            
*/
            // Create 3rd move sequence - Queen's Knight raider with Queen trap
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(12);   //Queen's pawn
            sequence.add(20);   // to Q3
            
            sequence.add(2);    // Queen's Knight
            sequence.add(19);    // to QB3
            
            sequence.add(3);    // Queen's Bishop
            sequence.add(30);   // to KB(f)4

            
            sequence.add(19);   //Knight to QB5
            sequence.add(34);
            
        //    sequence.add(12);   //Bishop to QR5
        //    sequence.add(33);
            
            sequence.add(34);   // Knight to QB7
            sequence.add(51);
            
            sequence.add(51);   // Knight attempts Rook
            sequence.add(57);
            
            sequence.add(57);   // Knight withdraws
            sequence.add(51);
            
            sequence.add(51);  // Knight attempts Queen
            sequence.add(61);
            
            sequence.add(61);  // ditto
            sequence.add(55);
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
        
            // Create 4th move sequence - Fool's mate
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(13);    // King's Pawn
            sequence.add(21);    // to K3
            
            sequence.add(6);   // King's Bishop
            sequence.add(27);   // to 
            
            sequence.add(4);    // Queen
            sequence.add(22);   // to KB3
            
            sequence.add(22);   // Q to KB7
            sequence.add(54);   // Checkmate, hopefully
            
            sequence.add(54);   // retreat if necessary!
            sequence.add(22);
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
            
            // Create 5th move sequence - Castle fortress
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(15);    // King's Knight's Pawn
            sequence.add(23);    // to KN3
            
            sequence.add(7);   // King's Knight
            sequence.add(22);   // to KB3
            
            sequence.add(6);    // King's Bishop
            sequence.add(15);   // to KN2
            
            sequence.add(5);   // King Castles
            sequence.add(7);   
            
           
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
        }
        else
        {
            // Black's openings here
            
            // Create 1st move sequence 
            ArrayList<Integer> sequence = new ArrayList<Integer>();       
            // load up the moves sequence
            sequence.add(53);    // King's pawn      first from
            sequence.add(37);    // to K4            first to
            sequence.add(52);    // Queen's pawn
            sequence.add(44);    // to Q3
            sequence.add(63);        // King's Knight to KR3
            sequence.add(48);       // (Fool's mate defence0
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;

            /* v3.3 not very successful, drop this
            // Create 2nd move sequence - King's Knight raider
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(63);    // King's Knight
            sequence.add(46);    // to KB3
            
            sequence.add(46);
            sequence.add(31);
            
            sequence.add(31);
            sequence.add(14);
            
            sequence.add(14);
            sequence.add(8);
            
            sequence.add(8);
            sequence.add(14);
            
            sequence.add(14);
            sequence.add(4);
            
            sequence.add(4);
            sequence.add(10);
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
            */
            // Create 3rd move sequence - Queen's Knight raider
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(58);    // Queen's Knight
            sequence.add(43);    // to QB3
            
            sequence.add(43);
            sequence.add(26);
            
            sequence.add(26);
            sequence.add(11);
            
            sequence.add(11);
            sequence.add(1);
            
            sequence.add(1);
            sequence.add(11);
            
            sequence.add(11);
            sequence.add(13);
            
            sequence.add(13);
            sequence.add(4);
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
        
            // Create 4th move sequence - Fool's mate
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(53);    // King's Pawn
            sequence.add(45);    // to K3
            
            sequence.add(62);   // King's Bishop
            sequence.add(35);   // to QB4
            
            sequence.add(60);    // Queen
            sequence.add(46);   // to KB3
            
            sequence.add(46);   // Q to KB7
            sequence.add(14);   // Checkmate, hopefully
            
            sequence.add(14);   // retreat if necessary!
            sequence.add(46);
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
            
            // Create 5th move sequence - Castle fortress
            sequence = new ArrayList<Integer>();       
            // load up the moves sequence
           
            sequence.add(55);    // King's Knight's Pawn
            sequence.add(47);    // to KN3
            
            sequence.add(63);   // King's Knight
            sequence.add(46);   // to KB3
            
            sequence.add(62);    // King's Bishop
            sequence.add(55);   // to KN2
            
            sequence.add(61);   // King Castles
            sequence.add(63);   
            
           
            
            OpeningMoves.add(sequence);  // Add moves to list of lists 
            howMany++;
        }
    }
    
    public ArrayList<Integer> getOpening(int n){
        
        ArrayList<Integer> ChosenMoves = new ArrayList<Integer>(); // make a new list
        
        if (n<OpeningMoves.size()){
            
            for (int i=0; i<OpeningMoves.get(n).size(); i++){
                boolean add = ChosenMoves.add(OpeningMoves.get(n).get(i));
            }
            
        }
        return ChosenMoves;
    }
    
    public int getHowMany(){
        return howMany;
    }
}
