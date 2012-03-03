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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import java.util.Vector;

public class TukangNyacag {
    
    public class Cacagan // <# ieu teh struct di c# mah
    {
        public int baris;
        public byte warna;
        public String kata;
       
        public Cacagan(int baris, byte warna, String kata) {
            this.baris = baris;
            this.warna = warna;
            this.kata = kata;
        }
    }

    public Cacagan[] cacag(Font font, String s, int width) {
        return cacag(font, s, width, true, 0, 0);
    }
    
    public Cacagan[] cacag(Font font, String s, int width, boolean replaceChar, int maxTeks, int maxLine) {
        
        Cacagan[] ret = new Cacagan[0]; 
        
        if (s.length()>0)
        {
            int jmlKata;
            String[] arKata;
            boolean autoBaru = false;
        
            if (replaceChar) {
                s = replaceString(s, "\\u0023", "#");
                s = replaceString(s, "\\n", " \\n ");
            }
            
            int panjang=0;
            int pjgKata=0;
            
            int baris=0;
            byte warna=0; // hideung
            String kata;
            
            int spasi = font.stringWidth(" ");  // ukuran spasi
            
            // potong2 kalimat jadi array kata
            arKata = split(s, " ", maxTeks);
            jmlKata = arKata.length;
            
            ret = new Cacagan[jmlKata];
            
            for (int i=0; i<jmlKata; i++){
                
                // lamun maxline di isi, eureun sampe dieu
                if (maxLine>0 && baris>maxLine) break;
                
                warna=0;
                
                // periksa tiap kata, jika ada tanda, warna apakah itu!
                if (arKata[i].length()>0)
                {
                    if (arKata[i].substring(0, 1).equals("*")){
                        kata = arKata[i].substring(1, arKata[i].length());
                        warna=1; // merah
                    }
                    else if (arKata[i].substring(0, 1).equals("^")){
                        kata = arKata[i].substring(1, arKata[i].length());
                        warna=2; // hijau
                    }
                    else if (arKata[i].substring(0, 1).equals("!")){
                        kata = arKata[i].substring(1, arKata[i].length());
                        warna=3; // biru
                    } else 
                        kata = arKata[i];
                    
                }
                else
                    kata = arKata[i];
                        
                
                if (kata.equals("\\n")) {
                    kata="";
                    panjang=0;
                    if (autoBaru==false) baris++;   // lamun tos baris anyar secara otomatis, tong di tambahan
                    autoBaru=false;
                } else { 
                    
                    pjgKata = font.stringWidth(kata);
                    
                    if((panjang + pjgKata) > width){
                        panjang = pjgKata + spasi;
                        baris++;
                        autoBaru = true;
                    } else {
                        panjang += pjgKata + spasi;     
                        autoBaru = false;
                    }
                    
                }
                
                ret[i] = new Cacagan(baris, warna, kata);
            } // end for
            
            arKata=null;
        }
        
        return ret;
    }
    
    public void tulis(Graphics g, Font font, Cacagan[] hasil, int x, int y, int firstLine, int jmlBaris, int clTeks){
        
        int hFont = font.getHeight();
        int xStart = x;
        int oldBaris=0;
        int spasi = font.stringWidth(" ");
        
        for (int i=0;i<hasil.length;i++){
           if (hasil[i].kata.length()>0 && hasil[i].baris>=firstLine & hasil[i].baris<(firstLine+jmlBaris)){
                // tentukeun baris
                if (oldBaris!=hasil[i].baris){
                    oldBaris=hasil[i].baris;
                    xStart=x;
                }

                // sesuaikeun warna
                switch (hasil[i].warna){
                    case 1: g.setColor(0x800000); break;  // beureum
                    case 2: g.setColor(0x008000); break;  // hejo
                    case 3: g.setColor(0x0000A0); break;  // bulao
                    default:
                        g.setColor(0);

                        if (hasil[i].warna==0 & clTeks!=0x000000)
                            g.setColor(clTeks);
                }

                // tulis isi sesuai baris
                g.drawString(hasil[i].kata, xStart, y+(hFont*((hasil[i].baris)-firstLine)), g.TOP | g.LEFT);
                xStart += font.stringWidth(hasil[i].kata) + spasi; // <# tendeun posisi x terakhir
           }
        }
    }
    
    public static String[] split(String s, String pemisah) {
        return split(s, pemisah, 0);
    }
    
     // <# paranti motong string
    public static String[] split(String s, String pemisah, int max) {
        Vector nodes = new Vector();
        boolean bMax = false;
        
        if (max>0) bMax = true;
        
        max -= 1;
        
        // Parse node ke vektor
        int idx = s.indexOf(pemisah);
        int jml = 0;
        int l = pemisah.length();
        
        while(idx>=0)
        {
            nodes.addElement(s.substring(0, idx));
            s = s.substring(idx+l);
            idx = s.indexOf(pemisah);

            // eureun lamun aya permintaan jumlah max teks
            if (bMax && idx>=0 & jml>=max){
                s = s.substring(0, idx);    // sesa
                idx = -1;
                jml++;
            }
        }
        
        // sesa node terakhir
        nodes.addElement(s);

        // Buat split string array
        int size = nodes.size();
        String[] ret = new String[size];
        if (size>0) {
            for(int i=0; i<size; i++)
                ret[i] = (String)nodes.elementAt(i);
        }
        
        nodes = null;
        
        return ret;
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
