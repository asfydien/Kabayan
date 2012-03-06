/* Memotong kalimat menjadi perkata, baris & memberi warna
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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import java.util.Vector;

public class Splitter {
    
    public class Cacagan // <# ieu teh struct di c# mah
    {
        public int baris;
        public int warna;
        public String kata;
       
        public Cacagan(int baris, int warna, String kata) {
            this.baris = baris;
            this.warna = warna;
            this.kata = kata;
        }
    }

    public Vector cacag(Font font, String s, int width) {
        return cacag(font, s, width, true, 0, 0);
    }
    
    public Vector cacag(Font font, String s, int width, boolean replaceChar, int maxWord, int maxLine) {
          
        Vector v = new Vector();
        String key = "*^!";
        
        boolean bBatas = (maxWord > 0 | maxLine > 0);
        
        if (s.length()>0)
        {
            boolean isNewLine = false;
        
            if (replaceChar) {
                s = replaceString(s, "\\u0023", "#");
                s = replaceString(s, "\\n", " \\n ");
            }
            
            int wTotal=0;
            int wKata=0;
            
            int line=0;
            int warna=0; // hideung
            String kata;
            
            int spasi = font.stringWidth(" ");  // ukuran spasi
            
            String pemisah = " ";
            String sKata;
            int idx;
            int l = pemisah.length();    // pemisah

            boolean on = true;

            while(on)
            {
                idx = s.indexOf(pemisah);

                if (idx >= 0){
                    sKata = s.substring(0, idx);
                    s = s.substring(idx+l);
                } else {
                    sKata = s;
                    on = false;
                }

                // #
                warna=0;
                
                // periksa tiap kata, jika ada tanda, warna apakah itu!
                if (sKata.length()>0) {
                    
                    int k = key.indexOf(sKata.charAt(0));
                    
                    if (k != -1){
                        kata = sKata.substring(1);
                        warna = k+1;
                    } else
                        kata = sKata;
                    
                }
                else
                    kata = sKata;
                        
                
                if (kata.equals("\\n")) {
                    kata="";
                    wTotal=0;
                    if (isNewLine == false) line++;   // lamun tos baris anyar secara otomatis, tong di tambahan
                    isNewLine=false;
                } else { 
                    
                    wKata = font.stringWidth(kata);
                    
                    if((wTotal + wKata) > width){
                        wTotal = wKata + spasi;
                        line++;
                        isNewLine = true;
                    } else {
                        wTotal += wKata + spasi;     
                        isNewLine = false;
                    }
                    
                }
                
                // # BATAS
                if (bBatas && (v.size() >= maxWord | line >= maxLine)) break;
                
                v.addElement(new Cacagan(line, warna, kata));
            }
            
        }
        
        return v;
    }
    
      public void tulis(Graphics g, Font font, Vector hasil, int x, int y, int firstLine, int jmlBaris, int clTeks){
        
        int hFont = font.getHeight();
        int xStart = x;
        int oldBaris=0;
        int spasi = font.stringWidth(" ");
        int warna[] = {0x000000, 0x800000, 0x008000, 0x0000A0};
        Cacagan cn;
        
        for (int i=0;i<hasil.size();i++){
           cn = (Cacagan)hasil.elementAt(i);
           if (cn.kata.length()>0 && cn.baris>=firstLine & cn.baris<(firstLine+jmlBaris)){
                // tentukeun baris
                if (oldBaris != cn.baris){
                    oldBaris = cn.baris;
                    xStart=x;
                }
                
                g.setColor(warna[cn.warna]);
                
                if (cn.warna==0 & clTeks!=0x000000)
                    g.setColor(clTeks);

                // tulis isi sesuai baris
                g.drawString(cn.kata, xStart, y+(hFont*((cn.baris)-firstLine)), g.TOP | g.LEFT);
                xStart += font.stringWidth(cn.kata) + spasi; // <# tendeun posisi x terakhir
           }
        }
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
    
}
