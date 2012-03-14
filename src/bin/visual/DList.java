/* 
 * Copyright (C) 2007 Aram Julhakyan
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
 */


package bin.visual;

//import bin.logic.Kabayan;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
//import javax.microedition.midlet.MIDlet;
//import java.util.Vector;

public class DList extends DControl {
    
    public String[] content, secondContent;
    private int itemsVisibles;  // jumlah item terlihat dalam daftar
    private int selectedItem;  // item yang dipilih
    private int hoveredItem;  // Item que tiene la sombra
    private int firstVisible, lastVisible; //Con esto sabre la parte visible de la lista
    
    private int activeItem = -1;
    
    private Font huruf, hurufTag;
    
    private Image imList = null;
    private boolean bImage = false, bStrip=true, bTag=false;
    
    private int imgSize = 0, itemHeigh=0, tagHeigh=0, hExtra=0;

    // variabel scrollbar
    private int jmlItem, jmlTampil, yScroll=0, hScroll=0, hGeser=0, hSisa=0;
    
    // variabel render
    private int wItem, xTeks, yTeks, yTag;
    
    private int clTeks, clSelTeks, clList, clBorder1, clBorder2, clListStrip;
    
    
    public void setWarna(int clTeks, int clSelTeks, int clList, int clBorder1, int clBorder2, int clLisStrip){
        this.clTeks = clTeks;
        this.clSelTeks = clSelTeks;
        this.clList = clList;
        this.clBorder1 = clBorder1;
        this.clBorder2 = clBorder2;
        this.clListStrip = clLisStrip;
    }
    
    public DList(int x, int y, int lebar, int tinggi, int fontSize) {
        super(x, y, lebar, tinggi);
        
        hurufTag = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        
        setFontSize(fontSize);
        
        selectedItem = 0;
        hoveredItem = 0;
        firstVisible = 0;
        activeItem = -1;    // item anu aktif/kos radiobuton
        
        itemsVisibles = (height-1) / itemHeigh;
        
        content = null;

        setWarna(0x000000, 0xFFFFFF, 0x85A54D, 0x657F3B, 0x92B752, 0xF2F4FF);
    }
    
    public void setExtraItemHeight(int main){
        hExtra = main;
        initSize();
    }
    
    public void showStrip(boolean b){
        bStrip = b;
    }
    
    public void setFontSize(int fontSize){
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize);
        initSize();
    }
    
    private void initSize(){
        
        itemHeigh = huruf.getHeight() + 2 + hExtra;;
        
        tagHeigh = (bTag) ? hurufTag.getHeight() : 0;
        
        itemsVisibles = ((height-1)-tagHeigh) / itemHeigh;
        
        lastVisible = itemsVisibles-1;
        
        initScroll();
        initRender();
    }
    
    public void setImage(String fname) {
        try {
            imList = Image.createImage(fname);
            bImage = true;
            imgSize = imList.getHeight();
            initRender();
        }  catch (java.io.IOException e) {}
    }
    
    public void setImage(Image im) {
        this.imList = im;
        bImage = true;
        imgSize = im.getHeight();
    }
    
    public int getItemHeight(){
        return itemHeigh;
    }
    
    public void setSize(int x, int y, int lebar, int tinggi){
        this.x = x;
        this.y = y;
        this.width = lebar;
        this.height = tinggi;
        initSize();
    }
    
    private void initRender(){
            wItem = width;
            xTeks = x+4;    // indent
            yTeks = (((itemHeigh-huruf.getHeight())/2) + ((itemHeigh-huruf.getHeight())%2));
            yTag  = yTeks + huruf.getHeight();
            
            if (bImage) xTeks += imgSize + 2;   // lamun pake image, tambah indent
            
            if ( content!=null && content.length>itemsVisibles) wItem = width-4;    // jang sroll
    }
    
    public void render (Graphics g){
        
        if (isVisible()){
            
            g.setColor(255, 255, 255);
            
            int y1 = y;
            int itemtagHeigh = itemHeigh + tagHeigh;
            
            if (content !=null && content.length>0)
            for(int i = firstVisible; i <= lastVisible; i++){
                if (i < content.length){
                    
                    g.setFont(huruf);
                    
                    if (bStrip){
                        g.setColor(clListStrip);    // <= garis
                        g.fillRect(x, y1, wItem+1, 1);
                        g.fillRect(x, y1+itemtagHeigh, wItem+1, 1);
                    }
                    
                    if (i == hoveredItem){
                        drawHoverItem(g, x, y1, itemtagHeigh);
                        
                        
                        if ( !(activeItem != -1 & activeItem == i) & bImage) 
                            drawIcon(g, 0, x+2, y1);

                        g.setColor(clSelTeks);
                        g.drawString(teksAsli(content[i]), xTeks, y1+yTeks, g.TOP | g.LEFT);
                        
                        // lamun item dikasih tag, tampilkeun teks tag
                        if (bTag & secondContent!=null && i<secondContent.length){
                            g.setFont(hurufTag);
                            g.drawString(secondContent[i], xTeks, y1+yTag, g.TOP | g.LEFT);
                        }
                        
                    }else{
                         // <# ameh list katinggalina belang, warnaan daftar anu indekna genap
                        //if(bStrip && (i % 2 == 0)){
                        //   g.setColor(clListStrip);
                           //g.fillRect(x, y1, wItem+1, itemHeigh);  
                        //}
                        
                        if ( !(activeItem != -1 & activeItem == i) & bImage) 
                            drawIcon(g, 1, x+2, y1);
                        
                        g.setColor(clTeks);
                        g.drawString(teksAsli(content[i]), xTeks, y1+yTeks, g.TOP | g.LEFT);
                    } // else if
                    
                    if ((activeItem != -1 & activeItem == i) && bImage)
                        drawIcon(g, 2, x+2, y1);
                    
                    y1 += itemHeigh;
                    if (i == hoveredItem) y1 += tagHeigh;
                    
                } // if
                
            } // for
        
            drawScroll(g);
        
        }
    }
    
    private void drawHoverItem(Graphics g, int x, int y, int h){
        g.setColor(clList);  // <# warna latar teks anu dipilih
        g.fillRect(x, y, wItem, h);                     
        g.setColor(clBorder1);  // <# warna border latar teks anu dipilih
        g.drawRect(x, y, wItem, h-1); // <# gambar border
        g.setColor(clBorder2);  // <# warna border latar teks anu dipilih (di jerona)
        g.drawRect(x+1, y+1, wItem-2, h-3); // <# gambar border jero
    }
    
    private void drawIcon(Graphics g, int isplit, int x, int y){
        try {
            g.drawRegion(imList, imgSize*isplit, 0, imgSize, imgSize, Sprite.TRANS_NONE, x, y + ((itemHeigh-imgSize)/2), g.LEFT | g.TOP);
        } catch (Exception ex) { }
    }
    
    private String teksAsli(String s){
        if (s.indexOf("|") != -1)
            return s.substring(s.indexOf("|")+1);
        else
            return  s;
    }
    
    private void initScroll(){
        if (content!=null && content.length>itemsVisibles){
            yScroll=0;
            
            jmlItem = content.length-1; // jumlah item
            jmlTampil = itemsVisibles;  // jumlah item anu ditampilkeun
            
            hGeser = height / jmlItem; // nilai tiap kali geser
            hSisa = height % jmlItem; // sesa bagi
            
            hScroll = jmlTampil * hGeser;    // panjang scroll
            hScroll += hSisa;    // tambahkeun sesa bagi
            
        }
    }
    
    private void drawScroll(Graphics g){
        
        if (content!=null && content.length>itemsVisibles){
                
            int itemSell = (hoveredItem-firstVisible);    // posisi item anu dipilih
            
            g.setColor(0xFFFFFF);

            // turun
            if (itemSell==jmlTampil-1 && hoveredItem>jmlTampil-1)
                yScroll = (hoveredItem-(jmlTampil-1))*hGeser;

            // naek
            if (itemSell==0 && hoveredItem>-1){
                yScroll = (hoveredItem-(jmlTampil-1))*hGeser;
                yScroll += (hScroll-hGeser-hSisa); // + skip saitemen
            }
            
            g.setColor(clBorder1);
            g.fillRect(x + width-1, y+yScroll, 2, hScroll-hGeser);
        }
        
    }
    
     public void inputCodeKey(int cod){
         
        // naik 
        if (cod== Canvas.UP || cod==Canvas.KEY_NUM0){
        ///if (cod==-1 || cod==-59 || cod==35){
            hoveredItem--;
            if (hoveredItem < 0)
                hoveredItem = 0;
        }
        
        // turun
        if (cod== Canvas.DOWN || cod==Canvas.KEY_POUND){
        //if (cod == -2 || cod == -60 || cod==48){
            hoveredItem++;
            if (content != null && hoveredItem >= content.length)
                hoveredItem = content.length-1;
        }
        
        // pilih item di daftar
        if (cod == -5 || cod == -26)
            selectedItem = hoveredItem;
        
        if (hoveredItem > lastVisible){
            firstVisible++;
            lastVisible++;
        }
        if (hoveredItem < firstVisible){
            firstVisible--;
            lastVisible--;
        }
     
    }
   
    public void setSelected(int i){
            selectedItem = i;
    }
    
    public void setSelectedByValue(String s){
        if (content !=null){
            for(int i=0; i < content.length; i++){
                if(s.compareTo(content[i])==0){
                    selectedItem = i;
                    hoveredItem = i;
                    break;
                }
            }
        }
        
        // sesuaileun posisi start
        if (hoveredItem>lastVisible){
            firstVisible = hoveredItem-(itemsVisibles-1);
            lastVisible = hoveredItem;
        }  
    }
    
    public int getSelected(){
        return selectedItem;
    }
    
    public int getHoveredItem(){
        return hoveredItem;
    }
    
    public String getSelectedText(){
        if (content !=null && content.length>0)
            return content[hoveredItem];
        else
            return "";
    }

    public int getActiveItem(){
        return activeItem; 
    }
    
    public void setActiveItem(int index){
        if (index > content.length)
            activeItem = -1;
        else
            activeItem = index;
        
    }
    
    public void setItems(String [] items){
        content = items;
        hoveredItem = 0;
        selectedItem = 0;
        firstVisible = 0;
        lastVisible = itemsVisibles-1;

        initSize();
    }
    
    public void setSecondItems(String[] items){
        secondContent = items;
        initSize();
    }
    
    public void showSecondItems(boolean b){
        bTag = b;
        initSize();
    }
    
    public void inputPointer(int x, int y){

        int pilih, hTag= bTag ? tagHeigh : 0;
        
        if (content !=null && content.length>0){
            int jml = content.length - firstVisible;
            int h = (jml)*itemHeigh; 

            if (y>this.y & y<this.y+h+hTag & x>this.x & x<this.x+width){
                
                if (bTag & (y-this.y)>(hoveredItem - firstVisible)*itemHeigh)
                    pilih = (firstVisible + ((y-this.y+hTag) / itemHeigh))-1;
                else
                    pilih = firstVisible + ((y-this.y) / itemHeigh);
                    
                if (pilih<=lastVisible & pilih < jml) hoveredItem = pilih;
                
            }
        }
    }
    
}
