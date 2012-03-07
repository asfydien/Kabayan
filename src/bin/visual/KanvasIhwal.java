/* 
 * Copyright (C) 2011 A. Sofyan Wahyudin
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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import bin.logic.Kabayan;

public class KanvasIhwal extends Canvas implements CommandListener{
    
    Kabayan midlet;
    Command cmdKembali;
    
    public KanvasIhwal(Kabayan midlet) {
        this.midlet = midlet;
        cmdKembali = new Command(midlet.cfg.getWord("BACK"), Command.BACK, 2);
        addCommand(cmdKembali);
        setCommandListener(this);
        //repaint();
    }
    
    
    
    protected void keyPressed(int keyCode) {
        if (FIRE == getGameAction(keyCode) || keyCode == KEY_NUM5){
            Display.getDisplay(midlet).setCurrent(midlet.pMenu);
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == cmdKembali){ 
            Display.getDisplay(midlet).setCurrent(midlet.pMenu);
        }
    }

    protected void paint(Graphics g) {
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Font huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL);
        int th = huruf.getHeight();   // tinggi head
        
        // tulis judul
        g.setFont(huruf);
        g.setColor(0xF2F2F2);
        g.drawString(midlet.cfg.getWord("ABOUT"), getWidth()/2, 3, g.TOP|g.HCENTER);
        
        int ti = th + 6;    // posisi mulai isi
        
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        // tulis isi
        g.setFont(huruf);
        //g.setColor(0xD2D2D2);
        g.drawString(midlet.NAMEVERSION, getWidth()/2, ti + (th*1), g.BOTTOM|g.HCENTER);
        
        g.setColor(0xBFBFBF);
        g.drawString("Sofyan Wahyudin", getWidth()/2, ti + (th*2), g.BOTTOM|g.HCENTER);
        g.drawString("code.google.com/p/kabayan", getWidth()/2, ti + (th*3), g.BOTTOM|g.HCENTER);
        
        ti += 4;    // bedakeun jarak saeutik
        g.setColor(0xF2F2F2);
        g.drawString("Based on Omnidic 1.0", getWidth()/2, ti + (th*4), g.BOTTOM|g.HCENTER);
        
        g.setColor(0xBFBFBF);
        g.drawString("By Aram Julhakyan", getWidth()/2, ti + (th*5), g.BOTTOM|g.HCENTER);
        
        ti += 4;    // bedakeun jarak saeutik
        g.setColor(0xF2F2F2);
        g.drawString("Icon Set", getWidth()/2, ti + (th*6), g.BOTTOM|g.HCENTER);
        
        g.setColor(0xBFBFBF);
        g.drawString("Fugue and Tango", getWidth()/2, ti + (th*7), g.BOTTOM|g.HCENTER);
        
    }
    
}
