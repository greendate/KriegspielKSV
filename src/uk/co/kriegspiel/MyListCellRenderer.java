/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

/*
 * MyListCellRenderer.java
 *
 */

import javax.swing.*;
import java.util.*;
import java.awt.*;

import uk.co.kriegspiel.KSpielUI.OnlineStatus;

public class MyListCellRenderer extends JLabel implements ListCellRenderer {
   
    private KSpielUI ksUI;
    //Set visitedItems = new HashSet();

    public MyListCellRenderer(KSpielUI theUI) {
        ksUI = theUI;
        setOpaque(true);
    }
   
    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
       
        
        /*
        if ( visitedItems.contains(value) )
            setFont( getFont().deriveFont(Font.PLAIN) );
        else
            setFont( getFont().deriveFont(Font.BOLD) );
        
        */
       
        setBackground(isSelected ?
                      list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ?
                      list.getSelectionForeground() : list.getForeground());
        setEnabled(list.isEnabled());
       
        // Find out user's on-line status
        
        OnlineStatus status = ksUI.getOnlineStatus(index);
        switch(status){
            case CONNECTED:
                setForeground(Color.RED);
                    break;
            case ONLINE:
                setForeground(Color.BLUE);
                break;
                
            case PERMANENT:
                setForeground(Color.BLACK);
                break;
                
            case OFFLINE:
                setForeground(Color.GRAY);
                break;
                        
        }
        /*
        if (index<2)
            setForeground(Color.BLACK);
        else
            setForeground(Color.RED);
        */
        return this;        
    }
   /*
    public void markAsVisited(Object item) {
        visitedItems.add(item);
    }
    */
}
