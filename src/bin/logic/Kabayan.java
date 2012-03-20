/* 
 * Copyright (C) 2010-2012 A. Sofyan Wahyudin
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

package bin.logic;

import bin.visual.Golodog;
import bin.visual.KanvasCari;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class Kabayan extends MIDlet {
    public KanvasCari pPencarian;
    public Golodog pMenu;
    public Config cfg;
    private Display layar;
    
    public Kabayan(){
        
        cfg = new Config();
        layar = Display.getDisplay(this);
        pPencarian = new KanvasCari(this);
        pMenu = new Golodog(this);

    }
    public void startApp() {
        layar.setCurrent(pMenu.lsMenu);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void exitMIDlet(){
        destroyApp(true);
        notifyDestroyed();
    }
}
