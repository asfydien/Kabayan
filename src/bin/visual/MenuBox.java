/* 
 * Copyright (C) 2011-2012 A. Sofyan Wahyudin
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
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
//import javax.microedition.lcdui.game.GameCanvas;

public class MenuBox {
          
    private int wScreen, hScreen, wMenu, hMenu, hList, xMenu, yMenu, yList, lokasi, wExtra;   // wExtra=space kanggo image
    private int fontSize;// = Font.SIZE_SMALL;
    private int clMenu1 = 0x000000, clTeks = 0xFFFFFF;
    private String[] menu;
    private Font huruf;
    private DList lstMenu;
    private Image imFade=null, imPanel=null;
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
        
        imFade  = ib.createFillRect(16, 16, 0x000000, 85);
        
    }
    
    public void setWarna(int clMenu1){
        this.clMenu1   = clMenu1;

        imPanel = createPanel(wScreen, hMenu, clMenu1, 245);
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
        
        int hItem = huruf.getHeight() + 2; 
        
        switch (getResMode()){
            case 2: hItem += 2; break;
            case 3: hItem += 4; break;    
        }
        
        hMenu = (hItem * menu.length) + 15;
        yMenu = hScreen - hMenu;
        
        
        yList = yMenu + 10;
        hList = (hItem * menu.length)+8;
                
        lstMenu = new DList(xMenu+3, yList, wScreen -7, hList, fontSize);
        lstMenu.setItems(menu);
        lstMenu.setWarna(clTeks, 0x000000,  clTeks, clTeks, clTeks, clMenu1);
        lstMenu.showStrip(false);
        lstMenu.setSelectedByValue(menu[0]);
        
        //imPanel = ib.createFillRect(wScreen, hMenu, 0x41418C, 240);
        imPanel = createPanel(wScreen, hMenu, clMenu1, 247);
        
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
    
    private Image createPanel(int w, int h, int cl, int opacity){
        Image ret = Image.createImage(w, h);
        
        Graphics g = ret.getGraphics();
        
        g.setColor(cl);
        g.fillRect(0, 0, w, h);
        
        // gradasi atas
        if (getResMode()>=2)
            for (int i=0; i<20; i++){
                g.setColor(grad.brightColor(cl, 0-i));
                g.fillRect(0, 15-i, w, 1);
            }
        
        // dark
        g.setColor(grad.brightColor(cl, -35));
        g.fillRect(0, 0, w, 1);
        
        for (int i=0; i<3; i++)
            g.drawRect((wScreen/2 - 6)+(6*i), 3, 1, 1);
        
        // line dark
        g.setColor(grad.brightColor(cl, -25));
        g.fillRect(1, 6, w-3, 1);
        
        // light
        g.setColor(grad.brightColor(cl, 30));
        g.fillRect(0, 1, w, 1);

        for (int i=0; i<3; i++)
            g.drawRect((wScreen/2 - 5)+(6*i), 4, 0, 0);
        
        // gradasi bawah
        if (getResMode()>=2)
            for (int i=0; i<35; i++){
                g.setColor(grad.brightColor(cl, 0-i));
                g.fillRect(0, (h-35)+i, w, 1);
            }
        
        // 
        ret = Image.createImage(ret);
        
        if (getResMode()>=2)
            ret = ib.blend(ret, opacity);
        
        return ret;
    }
    
    public void render(Graphics g){
        g.setFont(huruf);
        
        g.drawImage(imPanel, xMenu, yMenu, g.LEFT | g.TOP);

        lstMenu.render(g);
    }
            
}
