/* 
 * Copyright (C) 2010-2011 A. Sofyan Wahyudin
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

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import bin.visual.*;

public class Kabayan extends MIDlet {
    public KanvasCari pPencarian;
    public KanvasMenu pMenu;
    public Config cfg;
    //public KanvasTranslate pTranslate;
    private Display layar;
    
    public Kabayan(){
        
        cfg = new Config();
        layar = Display.getDisplay(this);
        pPencarian = new KanvasCari(this);
        pMenu = new KanvasMenu(this);
       // pTranslate = new KanvasTranslate(this);

    }
    public void startApp() {
        layar.setCurrent(pMenu);
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
