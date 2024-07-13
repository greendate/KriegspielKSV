/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.awt.*;

import javax.swing.*;

/**
 *
 * @author Denys
 */
public class MessageBox {

  /* 
    These are a list of STATIC MODAL dialogs

    int return codes of button pressed:

        -1 - WINDOW CLOSED - the X PRESSED
         0 - YES and OK
         1 - NO
         2 - CANCEL

    (thanks to flipside for the idea)
     */

    public static int yesno(String theMessage){
        int result = JOptionPane.showConfirmDialog((Component)
                null, theMessage, "alert", JOptionPane.YES_NO_OPTION);
        return result;
    }

    public static int yesnocancel(String theMessage){
        int result = JOptionPane.showConfirmDialog((Component)
                null, theMessage, "alert", JOptionPane.YES_NO_CANCEL_OPTION);
        return result;
    }

    public static int okcancel(String theMessage){
        int result = JOptionPane.showConfirmDialog((Component)
                null, theMessage, "alert", JOptionPane.OK_CANCEL_OPTION);
        return result;
    }

    public static int ok(String theMessage){
        int result = JOptionPane.showConfirmDialog((Component)
                null, theMessage, "alert", JOptionPane.DEFAULT_OPTION);
        return result;
    }
    /*
    public static int inputokcancel(String theMessage){
        int result = JOptionPane.showInputDialog((Component)
                null, theMessage, "alert", JOptionPane.OK_CANCEL_OPTION);
        return result;
    }


    public static void main(String args[]){
        int i = MessageBox.yesno("Are your sure ?");
        System.out.println("ret : " + i );
        i = MessageBox.yesnocancel("Are your sure ?");
        System.out.println("ret : " + i );
        i = MessageBox.okcancel("Are your sure ?");
        System.out.println("ret : " + i );
        i = MessageBox.ok("Done.");
        System.out.println("ret : " + i );
    }
 */
}
