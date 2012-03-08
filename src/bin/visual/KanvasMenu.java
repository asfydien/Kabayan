/* Menu Utama Kabayan
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
 */

package bin.visual;

import bin.logic.Kabayan;
import javax.microedition.lcdui.*;

public class KanvasMenu extends Canvas implements CommandListener { 
    private Kabayan midlet;
    
    public static final int IMAGE_TOP_LEFT = 0, IMAGE_TOP_RIGHT = 1, IMAGE_BUTTON_LEFT = 2, IMAGE_BUTTON_RIGHT = 3;
    
    private Command cmdKeluar, cmdPilih, cmdBack;
    
    private String[]  menuItems = {"Cari Kata", "Konfigurasi", "Bantuan", "Tentang"};
    
    private String fileImgTL, fileImgTR, fileImgBL, fileImgBR;
    private int selectedItem = 0;
    private int xCenter, yCenter;
    private int clLatar, clMenuSel, clMenu, clPanah, clTagAtas, clTagBawah;
    
    private Image imTL = null, imTR = null, imBL = null, imBR = null, imCT = null; // <# gambar pamaes
    private Image imStrip1 = null, imStrip2 = null; // <# gambar pamaes
    
    private boolean bImg = true;
    
    private Font huruf, hurufSel;
    
    private ImageBlend ib = new ImageBlend();
    
    private void setWarna(int clLatar, int clMenuSel, int clMenu, int clPanah, int clTagAtas, int clTagBawah){
      this.clLatar = clLatar;
      this.clMenuSel = clMenuSel;
      this.clMenu = clMenu;
      this.clPanah = clPanah;
      this.clTagAtas = clTagAtas;
      this.clTagBawah = clTagBawah;
    }
    
    public KanvasMenu(Kabayan midlet) {
      this.midlet = midlet;

      setCommandListener(this);
      setLanguage();
      
      huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
      hurufSel = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
      
      xCenter = (getWidth() / 2);
      yCenter = (getHeight() / 2) - (hurufSel.getHeight() / 2);
      
      setTema(midlet.cfg.getSavedTemaInt());
      
      if (getWidth() >= 240) buatStrip();
    }  
    
    public void setLanguage(){
        removeCommand(cmdKeluar);
        removeCommand(cmdPilih);
        
        cmdKeluar = new Command(midlet.cfg.getWord("EXIT"), Command.EXIT, 2);
        cmdPilih = new Command(midlet.cfg.getWord("OK"), Command.OK, 1);
        cmdBack = new Command(midlet.cfg.getWord("BACK"), Command.BACK, 2);
        
        addCommand(cmdKeluar);
        addCommand(cmdPilih);

        menuItems[0] = midlet.cfg.getWord("SEARCH");
        menuItems[1] = midlet.cfg.getWord("KONFIGURASI");
        menuItems[2] = midlet.cfg.getWord("HELP");
        menuItems[3] = midlet.cfg.getWord("ABOUT");
        //menuItems[4] = midlet.cfg.getWord("TRANSLATE");
   
    }

    public void setTema(int index){
        switch (index){
            case 0:
                setWarna(0xCDE8CE, 0x306249, 0x4F7964, 0x32634A, 0x217B26, 0x217B26);
                fileImgBR = "/img/default.png";
                fileImgTL = ""; fileImgTR = ""; fileImgBL = "";
                bImg = true;
                break;
            case 1:
                setWarna(0x828CFF, 0x0A3566, 0x0474E8C, 0x3A4272, 0x3A4272, 0x3A4272);
                fileImgBL = "/img/injected.png";
                fileImgTL = ""; fileImgTR = ""; fileImgBR = "";
                bImg = true;
                break;
            case 2:
                setWarna(0x000000, 0xD2D2D2, 0x8F8F8F, 0xF7F7F7, 0xF2F2F2, 0xF2F2F2);
                fileImgBR = "/img/lesstrenk.png";
                fileImgTL = ""; fileImgTR = ""; fileImgBL = "";
                
                bImg = (getWidth() >= 240) ? true : false;
                
                break;
        }
        
        imTL = loadImage(fileImgTL);
        imTR = loadImage(fileImgTR);
        imBL = loadImage(fileImgBL);
        imBR = loadImage(fileImgBR);
        
    }
    
    private Image loadImage(String file){
        Image img = null;
        
        if (!file.equals("")){
            try {
                img = Image.createImage(file);
            }  catch (java.io.IOException e) { }
        }
        
        return img;
    }
    
    private void drawImage(Graphics g, Image img, int posisi){
        if (bImg && img != null) {
            try {
                switch (posisi){
                    case IMAGE_TOP_LEFT:
                        g.drawImage(img, 0, 0, Graphics.LEFT | Graphics.TOP);  
                        break;
                    case IMAGE_TOP_RIGHT:
                        g.drawImage(img, getWidth()-img.getWidth(), 0, Graphics.LEFT | Graphics.TOP);  
                        break;
                    case IMAGE_BUTTON_LEFT:
                        g.drawImage(img, 0, getHeight() - img.getHeight(), Graphics.LEFT | Graphics.TOP);  
                        break;
                    case IMAGE_BUTTON_RIGHT:
                        g.drawImage(img, getWidth()-img.getWidth(), getHeight() - img.getHeight(), Graphics.LEFT | Graphics.TOP);  
                        break;
                }
                
            }
            catch (Exception ex) { }  
        }
    }
    
    private void buatStrip(){
        imStrip1 = createStrip(8, 20 + huruf.getHeight() , 0x333333, 0x000000, 10);
        imStrip2 = createStrip(8, 15 + huruf.getHeight() , 0x333333, 0x000000, 10);
    }
    
    public Image createStrip(int lebar, int tinggi, int warna, int warnaStrip, int opacity){
        Image tmp = null;

        tmp =  Image.createImage(lebar, tinggi);

        Graphics g = tmp.getGraphics();

        g.setColor(warna);
        g.fillRect(0, 0, lebar, tinggi);
        g.setColor(warnaStrip);
        g.drawLine(0, 1, lebar, 1);
        g.drawLine(0, tinggi-1, lebar, tinggi-1);
            
        tmp = Image.createImage(tmp);
        
        return ib.blend(tmp, opacity);
    }
    
    protected void paint(Graphics g) {
        
        g.setColor(clLatar);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // <# tambahan gambar pamaes lamun aya, ameh rada seger
        drawImage(g, imTL, IMAGE_TOP_LEFT);
        drawImage(g, imTR, IMAGE_TOP_RIGHT);
        drawImage(g, imBL, IMAGE_BUTTON_LEFT);
        drawImage(g, imBR, IMAGE_BUTTON_RIGHT);
        
        g.setFont(huruf);
        g.setColor(clTagAtas);
        g.drawString(midlet.NAMEVERSION, xCenter, 12, g.TOP | g.HCENTER);
        g.setColor(clTagBawah);
        g.drawString(midlet.LABELBOTTOM, xCenter, getHeight() - huruf.getHeight() - 7 , g.TOP | g.HCENTER);
        
        g.setFont(hurufSel);

        g.setColor(clMenuSel); // <# warna teks menu nu dipilih
        g.drawString(menuItems[selectedItem], xCenter, yCenter, g.TOP | g.HCENTER);

        int yTri;

        if(selectedItem != 0){
            // <# tampilkeun teks menu di luhurna
            if (selectedItem > 0) {
                g.setColor(clMenu);
                g.setFont(huruf);
                g.drawString(menuItems[selectedItem-1], xCenter , yCenter - huruf.getHeight(), g.TOP | g.HCENTER);
            }

            g.setColor(clPanah); // <# warna panah ka luhur
            yTri = yCenter - huruf.getHeight() - 7;
            g.fillTriangle(xCenter-4, yTri+4, xCenter, yTri, xCenter+4, yTri+4);
        }

        if (selectedItem != menuItems.length-1){
            // <# tampilkeun teks menu dihandapna
            if (selectedItem < menuItems.length) {
                g.setColor(clMenu);
                g.setFont(huruf);
                g.drawString(menuItems[selectedItem+1], xCenter, yCenter + hurufSel.getHeight(), g.TOP | g.HCENTER);
            }

            g.setColor(clPanah); // <# warna panah kahandap
            yTri = yCenter + hurufSel.getHeight() + huruf.getHeight() + 7;
            g.fillTriangle(xCenter-4, yTri-4, xCenter, yTri, xCenter+4, yTri-4);
        }    
        
        if (getWidth() >= 240) {
            int l = getWidth()/8;   // 8 = lebar gambar
            for (int i=0; i<l; i++){
                try {
                    g.drawImage(imStrip1, i*8 , 0, Graphics.LEFT | Graphics.TOP);     
                } catch (Exception ex) { } 
                try {
                    g.drawImage(imStrip2, i*8 , getHeight()-imStrip2.getHeight(), Graphics.LEFT | Graphics.TOP);     
                } catch (Exception ex) { } 
            }
        }
        
    }
    
    protected void keyPressed(int keyCode) {

        if (getGameAction(keyCode)==UP || keyCode==KEY_NUM2 || keyCode == KEY_NUM0) // naek
            if (selectedItem>0) selectedItem--;
        
        if (getGameAction(keyCode)==DOWN || keyCode==KEY_NUM8 || keyCode == KEY_POUND) // turun
            if (selectedItem<menuItems.length-1) selectedItem++;
        
        if (getGameAction(keyCode)==FIRE || keyCode == KEY_NUM5) // menu dipilih
            pilihMenu();
        
        repaint();
    }

    public void pilihMenu()
    {
        switch(selectedItem){
            case 0: // cari kata
                midlet.pPencarian.init();
                Display.getDisplay(midlet).setCurrent(midlet.pPencarian);
                break;
            case 1: // konfigurasi
                Display.getDisplay(midlet).setCurrent(new KanvasKonfig(midlet));
                break;
            case 2: 
                String hlp = midlet.cfg.getWord("TEXT_HELP");
                
                hlp =  replaceString(hlp, "\\n", "\n");
                hlp =  replaceString(hlp, "\\u0023", "\u0023");
                
                showForm(midlet.cfg.getWord("HELP"), hlp, cmdBack, null);
                break; 
            case 3: 
                showForm(midlet.cfg.getWord("ABOUT"), 
                        midlet.NAMEVERSION +
                        "\n© 2012 Sofyan" +
                        "\n\nUrl:\nhttp://code.google.com/p/kabayan/" +
                        "\n\nIcon set\nFugue and Tango Icons", cmdBack, null);
                break;
            case 4: 
                //midlet.pTranslate.init();
                //Display.getDisplay(midlet).setCurrent(midlet.pTranslate);
                break;
        }
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == cmdKeluar)
            midlet.exitMIDlet();        

        if (c == cmdPilih){
            pilihMenu();
            repaint();
        }
        
        if (c == cmdBack) Display.getDisplay(midlet).setCurrent(this);
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
    
    // pointerPressed() pointerDragged() pointerReleased() 
    public void pointerPressed(int x, int y){
        if (y<yCenter)
            if (selectedItem>0) selectedItem--;
        
        if (y>yCenter)
            if (selectedItem<menuItems.length-1) selectedItem++;
        
        repaint();
    }
    
}
