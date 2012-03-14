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

import bin.logic.Keyboard;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class DTextField extends DControl {
    
    
    private StringBuffer contenido;  //Contenido del textBox
    private Font huruf; //fuente del textbox
    private int cursorPos; //Posicion del cursor en el texto
    private int cpos; //Posicion fisica del cursor
    private String[] keys = {"-_@", "abc���ABC", "def���DEF", "ghi���GHI", "jklJKL", "mno����MNO", "pqrsPQRS", "tuv���TUV", "wxyzWXYZ"}; 
    private int lastKey = 0, keyIndex=0; //variables para trabajar con las repeticiones de las teclas
    private long tick;
    private int topText;
    private Image imgTeks = null;   // image anu dipasang dina textfield
    
    private boolean bCursor = true;
    
    private int clTeks, clCursor, clFill, clBorder;
    
    public void setWarna(int clTeks, int clCursor, int clFill, int clBorder){
        this.clTeks = clTeks;
        this.clCursor = clCursor;
        this.clFill = clFill;
        this.clBorder = clBorder;
    }
    
    public DTextField(String isi, String[] keyboard, int x, int y, int lebar, int tinggi, int fontSize ) {
        super(x, y, lebar, tinggi);
        
        this.contenido = new StringBuffer(isi);
        
        keys = keyboard;
        
        setFontSize(fontSize);
        
        cursorPos = contenido.length();
        cpos = huruf.substringWidth(isi, 0, cursorPos);
        
        // muat gambar anu bakal di tempel di text entry
        try {   
            imgTeks = Image.createImage("/img/paluruh.png"); 
        } catch (java.io.IOException e) { }
        
        setWarna(0x800000, 0x000000, 0xDFDFDF, 0x8C8C8C);
    }
    
    public void setFontSize(int fontSize){
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize);
        initSize();
        
        cpos = huruf.substringWidth(contenido.toString(), 0, cursorPos);    // atur dei posisi kursor
    }
    
    private void initSize(){
        topText = y;    // posisi teks
        if (height>huruf.getHeight())
            topText = y + ((height-huruf.getHeight())/2);
    }
    
    public void setSize(int x, int y, int lebar, int tinggi){
        this.x = x;
        this.y = y;
        this.width = lebar;
        this.height = tinggi;
        initSize();
    }
    
    public void setKeyboard(String [] teclado){
        keys = teclado;
    }
    public void render(Graphics g){
//-- dibujo el fondo del textfield
        g.setColor(clFill);  // <# warna dasar
        g.fillRect(x, y, width, height);
        g.setColor(0xCCCCCC); // <# warna bayangan1 di jero boorder text entry
        g.drawLine(x, y+1, width+1, y+1);
        g.setColor(0xD3D3D3); // <# warna bayangan2 di jero boorder text entry
        g.drawLine(x, y+2, width+1, y+2);
        g.setColor(0xF2F2F2); // <# warna bayangan1 di luar boorder text entry
        g.drawLine(x, y+height+1, width+2, y+height+1);
        g.setColor(clBorder); // <# warna boorder text entry
        g.drawRect(x, y, width, height);
        
        
        // tambah gambar di textfield, lamun aya
        try {
            int yImg = (height - imgTeks.getHeight()) / 2;
            g.drawImage(imgTeks, (x+width) - imgTeks.getWidth() - yImg, y + yImg + 1 , Graphics.LEFT | Graphics.TOP); 
        } catch (Exception ex) { }
        
        if (hasFocus()){
//-- Dibujo el cursor
            g.setColor(clCursor);
            g.drawLine(x+4+cpos, y + 2, x+4+cpos, y+height-2);
        }
        
//-- Dibujo el texo en el textbox
        g.setColor(clTeks);
        g.setFont(huruf);
        
        g.drawString(contenido.toString(), x+4, topText, g.TOP|g.LEFT);
        
    }
    
    public void inputKey(int keyGameAction, int keyCode){
        if ( Keyboard.isTextButtonPushed(keyCode)){
                gestionarEvento(keyCode);
        } else {
            if (keyGameAction==Canvas.LEFT || (keyCode==-2 && keyGameAction==0)) //-2 and -5 are Motorola phones
                gestionarEvento(-3);
            if (keyGameAction==Canvas.RIGHT || (keyCode==-5 && keyGameAction==0))
                gestionarEvento(-4);
        }
    }
    
    public void gestionarEvento(int cod){
        
        if (Keyboard.isASCII(cod) && width - huruf.stringWidth(contenido.toString())>7){
            if ('\b' == (char) cod){
                //backspace
                contenido.delete(cursorPos-1,cursorPos);
                cursorPos--;
            }else{
                cursorPos++;
                contenido.insert(cursorPos-1, (char) cod);
            }
        }
         
         if (cod >= Canvas.KEY_NUM1 && cod <= Canvas.KEY_NUM9 && width - huruf.stringWidth(contenido.toString())>7){  //Gestion de entrada de letras
            if (cod == lastKey){
                if (System.currentTimeMillis() - tick < 900){
                    keyIndex ++;
                    contenido.delete(cursorPos-1,cursorPos);
                }else{
                    keyIndex = 0;
                    cursorPos++;
                }
            }else{
                keyIndex = 0;
                cursorPos++;
            }
            
            int pushedKey=0;
            
            switch(cod){
                case Canvas.KEY_NUM1: pushedKey=0; break;
                case Canvas.KEY_NUM2: pushedKey=1; break;
                case Canvas.KEY_NUM3: pushedKey=2; break;
                case Canvas.KEY_NUM4: pushedKey=3; break;
                case Canvas.KEY_NUM5: pushedKey=4; break;
                case Canvas.KEY_NUM6: pushedKey=5; break;
                case Canvas.KEY_NUM7: pushedKey=6; break;
                case Canvas.KEY_NUM8: pushedKey=7; break;
                case Canvas.KEY_NUM9: pushedKey=8; break;
            }

            if (keys[pushedKey].length() == keyIndex)
                keyIndex = 0;

            contenido.insert(cursorPos-1, keys[pushedKey].charAt(keyIndex));
         }else{
             if (cod == Canvas.KEY_STAR || cod == -8){  //Borrar letrass
                 if (cursorPos != 0){
                     contenido.delete(cursorPos-1,cursorPos);
                     cursorPos--;
                 }
             }else{ // -- MOvimiento del cursor
                 switch (cod){
                     case -3:
                         if (cursorPos !=0)
                             cursorPos--;
                         break;
                     case -4:
                         if (cursorPos < contenido.length())
                             cursorPos++;
                 }
             }
        }
        cpos = huruf.substringWidth(contenido.toString(), 0, cursorPos);
        lastKey = cod;
        tick = System.currentTimeMillis();
    }

    
    public String getText(){
        return contenido.toString();
    }
    
    public void setText(String text){
        this.contenido.delete(0, contenido.length());
        if (text !=null)
            contenido.append(text);
        cursorPos = contenido.length();
        cpos = huruf.substringWidth(contenido.toString(), 0, cursorPos);
    }
  
}
