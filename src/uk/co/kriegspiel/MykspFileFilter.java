/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.kriegspiel;

import java.io.File;

/**
 *
 * @author denysbennett
 */
public class MykspFileFilter extends javax.swing.filechooser.FileFilter {

    public boolean accept(File f){
        //if it is a directory -- we want to show it so return true.
        if (f.isDirectory()) 
            return true;
  
            //get the extension of the file
            String extension = getExtension(f);
            //check to see if the extension is equal to ""ksp
            if ((extension.equals("ksp"))) 
                return true; 
        //default -- fall through. False is return on all
        //occasions except:
        //a) the file is a directory
        //b) the file's extension is what we are looking for.
        return false;
    }
    
    /**
    Again, this is declared in the abstract class
    The description of this filter
     * @return 
     */
      public String getDescription() {
          return ".ksp files";
      }
        /**
        Method to get the extension of the file, in lowercase
       */
      private String getExtension(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) 
          return s.substring(i+1).toLowerCase();
        return "";
      }
}
