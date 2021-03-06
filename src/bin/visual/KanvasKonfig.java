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
 */

package bin.visual;

import bin.logic.Kabayan;
import bin.logic.Keyboard;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;

public class KanvasKonfig extends Canvas implements CommandListener{
    
    private DList list;
    private Kabayan midlet;
    private Command cmdPilih, cmdKembali;
    private Font huruf;
        
    private String[] items, itemdic, dictag, itemkey, itemlang, itemtema;
    private int tabIndex = 0;
    private int fontSize = Font.SIZE_SMALL;
            
    private Image imTab=null;
    
    private int clTeks, clSelFill, clSelBorder, clFill, clBorder, clEfek, clShadow1, clShadow2, clShadow3, clClient, clLatar;
    
    private int resMode = 1;
    
    public void setWarna(int clTeks, int clSelFill, int clSelBorder, int clFill, int clBorder, int clEfek, int clShadow1, int clShadow2, int clShadow3, int clClient, int clLatar){
        
        this.clTeks = clTeks;
        this.clSelFill = clSelFill;
        this.clSelBorder = clSelBorder;
        this.clFill = clFill;
        this.clBorder = clBorder;
        this.clEfek = clEfek;  
        this.clShadow1 = clShadow1; 
        this.clShadow2 = clShadow2;  
        this.clShadow3 = clShadow3;
        this.clClient = clClient; 
        this.clLatar = clLatar;  

    }
    
    public KanvasKonfig(Kabayan midlet) {
        this.midlet = midlet;
        
        // sesuiakan ukuran pada HP yang berlayar lebih 240
        resMode = midlet.cfg.getResMode(getWidth());
        
        if (resMode>1) {
            fontSize = Font.SIZE_MEDIUM;
            lt = 32;
            tt = 28;
        }
        
        huruf = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, fontSize);
        
        int yList = huruf.getHeight() +tt +10;
        list = new DList(5, yList, getWidth()-11, getHeight() - (yList + 6), fontSize );
        //list.setSeleccionable(true);
        
        switch (resMode){
            case 2: list.setExtraItemHeight(2); break;
            case 3: list.setExtraItemHeight(4); break;    
        }
            
        String simgTab = "/img/config-small.png";
        
        if (fontSize == Font.SIZE_SMALL)
            list.setImage("/img/list-small.png");
        else {
            list.setImage("/img/list.png");
            simgTab = "/img/config.png";
        }
        
        itemdic  = midlet.cfg.getDictionaries();
        dictag   = midlet.cfg.getDictionariesTag();
        itemkey  = midlet.cfg.getKyeboards();
        itemlang = midlet.cfg.getLanguages();
        itemtema = midlet.cfg.getThemes();
         
        // set bahasa sakalian jeung tombol
        setBahasa();
        setCommandListener(this);
        
        try {
            imTab = Image.createImage(simgTab);
        }  catch (java.io.IOException e) { }
        
        setTema();
        
        initTab( 2, 2, getWidth()-4, getHeight()-4);
        
        gantiList();
    }
    
    private int getCl(int i){
        return midlet.cfg.getColor(i);
    }
    
    private void setTema(){

        setWarna(getCl(17), getCl(18), getCl(19), getCl(20), getCl(21), getCl(22), getCl(23), getCl(24), getCl(25), getCl(26), getCl(27));
        list.setWarna(getCl(28), getCl(29), getCl(30), getCl(31), getCl(32), getCl(33));

        repaint();
    }
    
    private void setBahasa(){
        items = new String[4]; 
        items[0] = midlet.cfg.getWord("DICTIONARY");
        items[1] = midlet.cfg.getWord("KEYBOARD");
        items[2] = midlet.cfg.getWord("LANGUAGE");
        items[3] = midlet.cfg.getWord("TEMA");
        
        removeCommand(cmdPilih);
        removeCommand(cmdKembali);
        cmdPilih = new Command(midlet.cfg.getWord("PILIH"), Command.OK, 1);
        cmdKembali = new Command(midlet.cfg.getWord("BACK"), Command.BACK, 2);
        addCommand(cmdPilih);
        addCommand(cmdKembali);
    }
    
     private void gantiList(){

        switch (resMode){
            case 2: list.setExtraItemHeight(2); break;
            case 3: list.setExtraItemHeight(4); break;    
        }
        
        switch (tabIndex){
            case 0:
                if (resMode>1) {
                    list.showSecondItems(true);
                    //list.setExtraItemHeight(0);
                }
                
                list.setItems(itemdic);
                list.setSecondItems(dictag);
                list.setSelectedByValue(midlet.cfg.getData("dictionary"));
                break;
            case 1:
                list.setItems(itemkey);
                list.showSecondItems(false);
                list.setSelectedByValue(midlet.cfg.getData("keyboard"));
                break;
            case 2:
                list.setItems(itemlang);
                list.showSecondItems(false);
                list.setSelectedByValue(midlet.cfg.getData("language"));
                break;
            case 3:
                list.setItems(itemtema);
                list.showSecondItems(false);
                list.setSelectedByValue(midlet.cfg.getData("tema"));
                break;
        }
        
        list.setActiveItem(list.getHoveredItem());
        
    }
    
    protected void keyPressed(int keyCode) {
        // -2 motorola
        if (getGameAction(keyCode)==LEFT || (keyCode==-2 && getGameAction(keyCode)==0)){
            if (tabIndex > 0) tabIndex--;
            gantiList();
        }
        
        // -5 motorola
        if (getGameAction(keyCode)==RIGHT || (keyCode==-5 && getGameAction(keyCode)==0)){
            if (tabIndex < items.length-1) tabIndex++;
            gantiList();
        }
        
        list.inputCodeKey(Keyboard.getValidKeyCode(getGameAction(keyCode), keyCode));
        
        repaint();
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == cmdPilih){  
            
            list.setActiveItem(list.getHoveredItem());
            
            switch (tabIndex){
                case 0:
                    midlet.cfg.setDictionary(list.getSelectedText());
                    break;
                case 1:
                    midlet.cfg.setKeyboard(list.getSelectedText());
                    break;
                case 2:
                    midlet.cfg.setLanguage(list.getSelectedText());
                    midlet.pMenu.setLanguage();
                    setBahasa();
                    break;
                case 3:
                    midlet.cfg.setTheme(list.getSelectedText());
                    midlet.pMenu.setTema(midlet.cfg.getTemaInt());
                    setTema();
                    break;
            }
            
            repaint();
        }
        
        if (c == cmdKembali){ 
            Display.getDisplay(midlet).setCurrent(midlet.pMenu.lsMenu);
        }
    }
    
    protected void paint(Graphics g) {
        g.setColor(clLatar);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        drawTab(g, tabIndex);
        
        list.render(g);
    }
    
    private int lt = 24, tt = 20;   // lebar tab, tinggi tab
    private int th, x, y, xTab, yHead, lebar, tinggi;    // variabel tab
    private int yTri, xTri, wImg, xImg, yImg, yTeks;
    
    private void initTab( int x, int y, int lebar, int tinggi){
        th =huruf.getHeight()+5;    // tinggi head judul
        xTab = x + 3;   // posisi awal tab
        yHead = y+tt;
        this.x = x;
        this.y = y;
        this.lebar = lebar;
        this.tinggi = tinggi;
        
        yTri = y + (th/2) + tt;
        xTri = x + 8;
        wImg = imTab.getHeight();
        xImg = (lt/2) - (wImg/2);
        yImg = (tt/2) - (wImg/2);
        
        yTeks = yHead + ((th-huruf.getHeight())/2) + ((th-huruf.getHeight())%2);
    }
    
    private void drawTab(Graphics g, int index){
        
        g.setColor(clClient);
        g.fillRect(x, yHead, lebar-1, tinggi-tt-1); 
        
        // BUAT KEPALA JUDUL
        g.setColor(clShadow3);
        g.fillRect(x, yHead, lebar, th+3); // garis bayangan 2
        g.setColor(clShadow2);
        g.fillRect(x, yHead, lebar, th+2); // garis bayangan 1
        g.setColor(clShadow1);
        g.fillRect(x, yHead, lebar, th+1); // garis paneges efek saacan bayangan
        g.setColor(clSelFill);
        g.fillRect(x, yHead, lebar, th); // head
        
        // BUAT BINGKAI + BAYANGAN
        g.setColor(clSelBorder);
        g.drawRect(x, yHead, lebar-1, tinggi-tt-1);
        
        // BUAT TAB AKTIF + MATI
        for (byte i=0; i<items.length; i++){
            if (i == index){
                // tab aktif
                g.setColor(clSelFill);
                g.fillRect(xTab+(lt*i), y, lt-1, tt+2);
        
                g.setColor(clSelBorder);
                g.drawLine(xTab+(lt*i), y+1, xTab+(lt*i), yHead);    // garis kiri
                g.drawLine(xTab+(lt*i)+lt-2, y+1, xTab+(lt*i)+lt-2, yHead);  // garis kanan
                
                g.fillRect(xTab+(lt*i), y, lt-1, 2);    // tutup luhur tab aktif
                
                g.setColor(clEfek);
                g.drawLine(xTab+(lt*i)+1, y+2, xTab+(lt*i)+1, yHead);    // garis dalam kiri
                g.drawLine(xTab+(lt*i)+lt-3, y+2, xTab+(lt*i)+lt-3, yHead);  // garis dalam kanan
                g.drawLine(xTab+(lt*i)+1, y+2, xTab+(lt*i)+lt-3, y+2);  // tutup dalam luhur tab aktif
                
                g.drawLine(x+1, yHead+1, xTab+(lt*i)+1, yHead+1); // garis dalam
                g.drawLine(xTab+(lt*i)+lt-3, yHead+1, lebar, yHead+1);
                
            } else {
                g.setColor(clFill);
                g.fillRect(xTab+(lt*i), y+2, lt-1, tt-2);
        
                g.setColor(clBorder);
                g.drawLine(xTab+(lt*i), y+2, xTab+(lt*i), yHead-1);  // garis kiri
                g.drawLine(xTab+(lt*i)+lt-2, y+2, xTab+(lt*i)+lt-2, yHead-1);    // garis kanan
                g.drawLine(xTab+(lt*i), y+2, xTab+(lt*i)+lt-2, y+2);    // garis tutup luhur
            }
        }
        
        // TULIS JUDUL
        g.setFont(huruf);
        g.setColor(clTeks);
        g.drawString(items[index], getWidth()/2, yTeks, g.TOP | g.HCENTER);
        
        // GAMBAR PANAH
        g.setColor((index==0) ? clEfek : clTeks);
        g.fillTriangle(xTri, yTri, xTri+3, yTri-3, xTri+3, yTri+3); // kiri
        
        g.setColor((index==items.length-1) ? clEfek : clTeks);
        g.fillTriangle(getWidth()-xTri, yTri, getWidth()-xTri-3, yTri-3, getWidth()-xTri-3, yTri+3);    // kanan
        
        // GAMBAR ICON
        try {
            for (byte i=0; i<5; i++){
                g.drawRegion(imTab, wImg*i, 0, wImg, wImg, Sprite.TRANS_NONE, xTab+(lt*i)+xImg, y+yImg+1, g.LEFT | g.TOP);
            }
        } catch (Exception ex) { }

    }
    
    public void pointerPressed(int x, int y){
        
        if (y>this.y & y<this.y+tt & x>xTab & x<xTab+(lt*items.length)){
            int pilih = (x-xTab)/lt;
            tabIndex = pilih;
            
            gantiList();
        }
        
        list.inputPointer(x, y);
        repaint();
    }
    
}
