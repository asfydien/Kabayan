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
import bin.logic.Keyboard;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;
//import javax.microedition.lcdui.game.GameCanvas;

public class MenuBox {
          
    private int wScreen, hScreen, wMenu, hMenu, xMenu, yMenu, lokasi, wExtra;   // wExtra=space kanggo image
    private int fontSize;// = Font.SIZE_SMALL;
    private int clMenu1 = 0x000000, clMenu2 = 0x000000, clTeks = 0xFFFFFF, clBolder1 = 0x000000, clBolder2 = 0x000000;
    private String[] menu;
    private Font huruf;
    private DList lstMenu;
    private Image imFade=null, imBlend=null;
    private ImageBlend ib = new ImageBlend();
    private Gradient grad = new Gradient();
    public static final int MENU_LEFT = 1, MENU_CENTER = 2, MENU_RIGHT = 3;
    
    boolean styler=false;
    
    public MenuBox(String[] lsMenu, int wScreen, int hScreen, int fontSize, int lokasi, boolean styler, int wExtra){
        menu = lsMenu;
        this.wScreen = wScreen;
        this.hScreen = hScreen;
        this.fontSize = fontSize;
        this.lokasi = lokasi;
        this.wExtra = wExtra;
        
        //bExtra = wScreen >= 176;
        this.styler = styler;
        
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize);
        
        initMenu();
        
        imFade  = ib.createFillRect(16, 16, 0x000000, 75);
        
        if (styler){
            imBlend = buatBlendRect(wMenu, hMenu, clMenu1, 0, clBolder1, clBolder2, 240);
        }
    }
    
    private Image buatBlendRect(int w, int h, int cl1, int cl1x, int cl2, int cl3, int opacity){
        Image tmp = null;
        
        tmp = Image.createImage(w, h);
        Graphics g = tmp.getGraphics();
        
        if (cl1x!=cl1)
            grad.drawGradientRect(g, cl1, cl1x, 0, 0, w, h, grad.VERTICAL);
        else {
            g.setColor(cl1);
            g.fillRect(0, 0, wMenu, hMenu);
        }
        
        g.setColor(cl2);
        g.drawRect(0, 0, wMenu-1, hMenu-1);
        g.setColor(cl3);
        g.drawRect(1, 1, wMenu-3, hMenu-3);
        
        imBlend = Image.createImage(tmp);
        
        return ib.blend(imBlend, opacity);
    }

    public void setWarna(int clMenu, int clTeks, int clBorder1, int clBolder2){
        setWarna(clMenu, clMenu, clTeks, clBorder1, clBolder2);
    }
    
    public void setWarna(int clMenu1, int clMenu2, int clTeks, int clBorder1, int clBolder2){
        this.clMenu1   = clMenu1;
        this.clMenu2   = clMenu2;
        this.clTeks    = clTeks;
        this.clBolder1 = clBorder1;
        this.clBolder2 = clBolder2;
        lstMenu.setWarna(clTeks, 0x000000,  clTeks, clTeks, clTeks, clMenu1, clTeks);

        if (styler)
            imBlend = buatBlendRect(wMenu, hMenu, clMenu1, clMenu2, clBorder1, clBolder2, 240);
    }
    
    public void setImage(String fname) {
        lstMenu.setImage(fname);
    }
    
    public void setMenuText(int index, String s){
        if (menu!=null && menu.length>=index)
            menu[index]=s;
    }
    
    public void setMenuItems(String[] items){
        menu = items;
        initMenu();
    }
    
    public void inputKey(int keyGameAction, int keyCode){
        if (keyGameAction==Canvas.UP & lstMenu.getHoveredItem() == 0)   // lamun aya pangluhurna, pindah ka handap
            lstMenu.setSelectedByValue(menu[menu.length-1]);
        else if (keyGameAction==Canvas.DOWN & lstMenu.getHoveredItem() == menu.length-1) // pindah ka luhur
            lstMenu.setSelectedByValue(menu[0]);
        else
            lstMenu.inputCodeKey(Keyboard.getValidKeyCode(keyGameAction, keyCode));
    }
    
    public void inputPointer(int x, int y){
        lstMenu.inputPointer(x, y);
    }
    
    public int getSelected(){
        return lstMenu.getHoveredItem();
    }
    
    public String getSelectedText(){
        return lstMenu.getSelectedText();
    }
    
    public void setSelctedByValue(String s){
        lstMenu.setSelectedByValue(s);
    }
    
    public void setActiveItem(int i){
        lstMenu.setActiveItem(i);
    }
    
    private int getResMode(){
        int ret=0;
        
        if (wScreen<128)
            ret = 1;
        else if (wScreen>=176 & wScreen<240)
            ret = 2;
        else if (wScreen>=240)
            ret = 3;
        
        return ret;
    }
    
    private void initMenu(){
        
        wMenu = 0;
        
        for (int i=0; i<menu.length; i++){
            int w = huruf.stringWidth(menu[i]);
            if (w > wMenu) wMenu = w;
        }
        
        wMenu += 25 + wExtra;
        
        if (wMenu>wScreen) wMenu = wScreen;
        
        int hItem = huruf.getHeight() + 2; 
        
        switch (getResMode()){
            case 2: hItem += 2; break;
            case 3: hItem += 4; break;    
        }
        
        hMenu = (hItem * menu.length) + 6;
        
        if (hItem>hScreen*0.666) hItem =  (int)(hScreen*0.666);
        
        switch (lokasi){
            case MENU_CENTER:
                if (wMenu<wScreen*0.666) wMenu = (int)(wScreen*0.666);  // seper3
                xMenu = (wScreen/2)-(wMenu/2);
                yMenu = (hScreen/2)-(hMenu/2);
                break;
            case MENU_LEFT:
                if (wMenu<wScreen*0.8) wMenu = (int)(wScreen*0.8);  // seper5
                xMenu = 1;
                yMenu = hScreen-hMenu;
                break;
            case MENU_RIGHT:
                if (wMenu<wScreen*0.8) wMenu = (int)(wScreen*0.8);  // seper5
                xMenu = wScreen-wMenu;
                yMenu = hScreen-hMenu;
        }
        
        lstMenu = new DList(xMenu+3, yMenu+3, wMenu-7, hMenu, fontSize);
        lstMenu.setItems(menu);
        lstMenu.setWarna(clTeks, 0x000000,  clTeks, clTeks, clTeks, clMenu1, clTeks);
        lstMenu.showStrip(false);
        lstMenu.setSelectedByValue(menu[0]);
        
        switch (getResMode()){
            case 2: lstMenu.setExtraItemHeight(2); break;
            case 3: lstMenu.setExtraItemHeight(4); break;    
        }
        
    }   

    public void drawFade(Graphics g){
        
        int wImg = imFade.getWidth();
        int hImg = imFade.getHeight();
        int h = (hScreen/hImg)+1;
        int w = (wScreen/wImg)+1;
        
        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++)
                g.drawImage(imFade, wImg*i, hImg*j, g.TOP | g.LEFT);
   
    }
    
    public void render(Graphics g){
        g.setFont(huruf);

        if (styler)
            g.drawImage(imBlend, xMenu, yMenu, g.TOP | g.LEFT);
        else {
            if (clMenu1!=clMenu2)
                grad.drawGradientRect(g, clMenu1, clMenu2, xMenu, yMenu, wMenu, hMenu, grad.VERTICAL);
            else {
                g.setColor(clMenu1);
                g.fillRect(xMenu, yMenu, wMenu, hMenu);
            }

            g.setColor(clBolder1);
            g.drawRect(xMenu, yMenu, wMenu-1, hMenu-1);
            g.setColor(clBolder2);
            g.drawRect(xMenu+1, yMenu+1, wMenu-3, hMenu-3);
        }
        
        lstMenu.render(g);
    }
            
}
