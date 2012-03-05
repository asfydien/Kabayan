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

public class KanvasNote extends Canvas  implements CommandListener {
    
    private int firstLine, jmlBaris, maxBaris;
    private String teksJudul, teksIsi;
    private Kabayan midlet;
    private Command cmdKembali, cmdZoom;
    private Splitter tn = new Splitter();
    private String oldJudul="";
    private Displayable dKembali;
    
    private int teksAlign = 1;   // 1-left; 2-center; 3-right
    
    private int fontSize = Font.SIZE_SMALL;
    private Font huruf;
    
    private boolean bZoom = false;
    private int minWidth = 0; //, iZoom = 0; // layar ideal 240
    
    public static final int ALIGN_HEAD_LEFT = 1, ALIGN_HEAD_CENTER = 2, ALIGN_HEAD_RIGHT = 3;
    
    // warna tema
    private int clHeadTeks, clHead, clShadow1, clShadow2, clShadow3, clTeks, clFill, clLine;
    
    //private FontBox fontbox;
    private FontBox fontbox;

    private Splitter.Cacagan[] hasil;
    
    public void setWarna(int clHeadTeks, int clHead, int clShadow1, int clShadow2, int clShadow3, int clTeks, int clFill, int clLine){
        this.clHeadTeks = clHeadTeks;
        this.clHead = clHead;
        this.clShadow1 = clShadow1;
        this.clShadow2 = clShadow2;
        this.clShadow3 = clShadow3;
        this.clTeks = clTeks;
        this.clFill = clFill;
        this.clLine = clLine;
    }
    
    public KanvasNote(Kabayan dict, Displayable dKembali, String sJudul, String sIsi) {
        midlet = dict;
        cmdKembali = new Command(midlet.cfg.getWord("BACK"), Command.BACK, 2);
        cmdZoom = new Command("Zoom", Command.ITEM, 1);
        addCommand(cmdKembali);
        
        if (getWidth() >= minWidth) 
            addCommand(cmdZoom);
        
        setCommandListener(this);
      
        firstLine = 0;
        
        teksIsi = sIsi;
        teksJudul = sJudul;
        this.dKembali = dKembali;

        int resMode = midlet.cfg.getResMode(getWidth());
        fontbox = new FontBox(midlet.cfg.getDataInt("notefont"),  resMode>1);
        
        fontSize = fontbox.getFontSize();
        //setFontSize();
        
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize);     
        
        setTema(midlet.cfg.getSavedTemaInt());
        
        hasil = tn.cacag(huruf, teksIsi, getWidth()-4);
        
    }
    
    private void setTema(int index){
        
        switch (index){
            case 0:
                setWarna(0xFFFFFF, 0x0C602F, 0x43512F, 0xDDDDDD, 0xEFEFEF, 0x000000, 0xFFFFFF, 0xEFEFEF);
                fontbox.setWarna(clHead, 0x02190A, clHeadTeks);
                break;
            case 1:
                setWarna(0xFFFFFF, 0x0026D7, 0x0026AF, 0xDDDDDD, 0xEFEFEF, 0x000000, 0xFFFFFF, 0xEFEFEF);
                fontbox.setWarna(0x2342B2, 0x112359, 0xFFFFFF);
                break;
            case 2:
                setWarna(0xFFFFFF, 0x333333, 0x000000, 0xDDDDDD, 0xEFEFEF, 0x000000, 0xFFFFFF, 0xEFEFEF);
                fontbox.setWarna(clHead, 0x000000, clHeadTeks);
                break;
        }
        
        repaint();
        
    }
    
    public void setTeksHead(String s){
        teksJudul = upFirstChar(s);
        
        if (!oldJudul.equals(s)){
            firstLine =0;
            oldJudul=s;
        }
    }
    
    
    public void setTeksAlignHead(int align){
        this.teksAlign = align;
    }
    
    public void setTeksIsi(String r){
        teksIsi = upFirstChar(r);
    }
    
    public void setdKembali(Displayable dKembali){
        this.dKembali = dKembali;
    }
    
    private String upFirstChar(String s) {
        String ret = "";
        
        if (s !=null || s.length() > 0)
            ret = s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
        else
            ret = s;
 
        return ret;
    }
    
    protected void paint(Graphics g) {
        String judul="";
        
        
        g.setFont(huruf);
        
        int tinggi = huruf.getHeight()+3;
        
        // bersihkeun layar
        g.setColor(clFill);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // dina sababaraha hp lamun ku Line teu sempurna jadi ganti ku fillrect
        g.setColor(clShadow3);
        g.fillRect(0, 0, getWidth(), tinggi+3); // garis bayangan 2
        g.setColor(clShadow2);
        g.fillRect(0, 0, getWidth(), tinggi+2); // garis bayangan 1
        g.setColor(clShadow1);
        g.fillRect(0, 0, getWidth(), tinggi+1); // garis paneges efek saacan bayangan
        g.setColor(clHead);
        g.fillRect(0, 0, getWidth(), tinggi); // head
        
        // ngadamel garis baris
        int startY = huruf.getHeight()+8;
        int hiFont = huruf.getHeight();
        g.setColor(clLine);
        for (int i=startY+hiFont; i<getHeight(); i+=hiFont)
            g.drawLine(0, i, getWidth(), i);
        
        // lamun aya karakter "|" potong jadi dua, terus pake teks anu bagian saatos karakter "|"
        if (teksJudul.indexOf("|") != -1)
            judul=teksJudul.substring(teksJudul.indexOf("|")+1);
        else
            judul=teksJudul;
        
        // ameh rapih huruf pertama kudu kapital
        judul = upFirstChar(judul);
        
        // tulis judul sirah sesuai align anu di pake
        g.setColor(clHeadTeks);
        g.setFont(huruf);
        
        switch (teksAlign){
            case ALIGN_HEAD_LEFT: // left
                g.drawString(judul, 2, 2, g.TOP | g.LEFT);
                break;
            case ALIGN_HEAD_CENTER: // center
                g.drawString(judul, getWidth()/2, 2, g.TOP | g.HCENTER);
                break;
            case ALIGN_HEAD_RIGHT: // right
                g.drawString(judul, getWidth() - huruf.stringWidth(judul) - 2, 2, g.TOP | g.LEFT);
                break;
        }
   
        // tulis isi note
        if (teksIsi!=null && teksIsi.length()>0 )
        tulisIsi(g, huruf, teksIsi, startY );
        
        // ngadamel kotak zoom
        if (bZoom & getWidth() >= minWidth)
            fontbox.render(g);
    }
    
    private void setFontSize(){
        int fSize = fontSize;
        
        fontSize = fontbox.getFontSize();
        
        midlet.cfg.setData("notefont", fontbox.getZoom());
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize);
        
        if (fSize!=fontSize)    // reload lamun ganti huruf
            hasil = tn.cacag(huruf, teksIsi, getWidth()-4);
        
        repaint();
    }
    
    private void tulisIsi(Graphics g, Font font, String s, int y){
        
        jmlBaris=hasil[hasil.length-1].baris;
        
        maxBaris = (getHeight()-y)/font.getHeight();
        
        tn.tulis(g, font, hasil, 2, y, firstLine, maxBaris, clTeks);
     }
    
    protected void keyPressed(int keyCode) {
        
        if (bZoom){
            fontbox.inputKey(getGameAction(keyCode), keyCode);
            
            setFontSize();
            
        } else {
            
            if (getGameAction(keyCode)==FIRE || keyCode == KEY_NUM5)
                Display.getDisplay(midlet).setCurrent(midlet.pPencarian);
        
            if (getGameAction(keyCode)==UP || keyCode==KEY_NUM2 || keyCode == KEY_NUM0){ // perintah ka luhur
                firstLine--;
                if (firstLine < 0) firstLine = 0;
            }

            if (getGameAction(keyCode)==DOWN || keyCode==KEY_NUM8 || keyCode == KEY_POUND){ // perintah turun
                firstLine++;
                
                if (jmlBaris < maxBaris) firstLine = 0; // lamun barisna saeutik tetep
                
                if (jmlBaris>maxBaris && (jmlBaris-firstLine)<maxBaris ) firstLine = jmlBaris - (maxBaris - 1);   // eureunkeun pas baris terakhir
                
                if ( firstLine>jmlBaris) firstLine = jmlBaris+1;  // antisipasi hungkul
            }
            
        }
            
        repaint();
    }

    public void commandAction(Command c, Displayable d) {
        if ( !bZoom && c == cmdKembali){
            Display.getDisplay(midlet).setCurrent(dKembali);
        } 
        
        if ( bZoom && c == cmdKembali){
            bZoom = false;
        } 
        
        if (c == cmdZoom){
            bZoom = !bZoom;
        }
        
        repaint();
    }
    
    public void pointerPressed(int x, int y){
        fontbox.inputPointer(x, y);
        if (bZoom) setFontSize();
    }
    
}
