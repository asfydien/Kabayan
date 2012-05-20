/* 
 * Copyright (C) 2012 A. Sofyan Wahyudin
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package bin.visual;

import bin.logic.Kabayan;
import javax.microedition.lcdui.*;

public class Golodog implements CommandListener{
    Kabayan midlet;
    
    Command cmdBack = new Command("Back", Command.BACK, 0);
    
    String NAME, VERSION, DESCRIP;
    
    String[] menu = {"Search", "Configuration", "Help", "About", "Exit"};
    public List lsMenu;
    
    public Golodog(Kabayan midlet){
        this.midlet = midlet;

        try {
            NAME    = midlet.cfg.getMetaManifest("MIDlet-Name");
            VERSION = midlet.cfg.getMetaManifest("MIDlet-Version");
            DESCRIP = midlet.cfg.getMetaManifest("MIDlet-Description");
        } catch (Exception ex){}
        
        setLanguage();
        
        Display.getDisplay(midlet).setCurrent(lsMenu);
    }

    public void setLanguage(){
        menu[0] = midlet.cfg.getWord("SEARCH");
        menu[1] = midlet.cfg.getWord("KONFIGURASI");
        menu[2] = midlet.cfg.getWord("HELP");
        menu[3] = midlet.cfg.getWord("ABOUT");
        
        lsMenu = new List(NAME, List.IMPLICIT, menu, null);
        lsMenu.setCommandListener(this);
        
        cmdBack = new Command(midlet.cfg.getWord("BACK"), Command.BACK, 1);
    }
    
    public void setTema(int index){
        
    }
    
    private void showForm(String j, String s, Command c1, Command c2){
        Form f = new Form(j);
        
        f.append(new StringItem("", s));
        f.addCommand(c1);
        if (c2!=null) f.addCommand(c2);
        f.setCommandListener(this);
        
        Display.getDisplay(midlet).setCurrent(f);
        
    }
    
    public static String replaceString(String s, String find, String replace) {

        StringBuffer sb = new StringBuffer();

        int idx = s.indexOf(find);
        int startPos = 0;
        int l = find.length();

        // ganti kabeh
        while (idx != -1) {
            sb.append(s.substring(startPos, idx)).append(replace);
            startPos = idx + l;
            idx = s.indexOf(find, startPos);
        }

        // tambahkeun sesana
        sb.append(s.substring(startPos, s.length()));

        return sb.toString();
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c.getCommandType() == Command.SCREEN) {
            switch (lsMenu.getSelectedIndex()){
                case 0:
                    midlet.pPencarian.init();
                    Display.getDisplay(midlet).setCurrent(midlet.pPencarian);
                    break;
                case 1:
                    Display.getDisplay(midlet).setCurrent(new KanvasKonfig(midlet));
                    break;
                case 2:
                    String hlp = midlet.cfg.getWord("TEXT_HELP");
                
                    hlp =  replaceString(hlp, "\\n", "\n");
                    hlp =  replaceString(hlp, "\\u0023", "\u0023"); //  #

                    showForm(midlet.cfg.getWord("HELP"), hlp, cmdBack, null);
                    break;
                case 3:
                    showForm(midlet.cfg.getWord("ABOUT"), 
                        "Kabayan " + VERSION +
                        "\n© 2012 Sofyan" +
                        "\n\n" + DESCRIP +
                        "\n\nUrl:\nhttp://code.google.com/p/kabayan/" +
                        "\n\nIcon set\nFugue and Tango Icons", cmdBack, null);
                    break;
                case 4:
                    midlet.exitMIDlet();
                    break;
            }
        }
        
        if (c == cmdBack) 
            Display.getDisplay(midlet).setCurrent(lsMenu);
        
    }
    
    
    
}
