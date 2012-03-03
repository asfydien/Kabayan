/* 
 * Copyright (C) 2007 Aram Julhakyan (ProcesoBuscador.java)
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

package bin.logic;

import bin.visual.DList;
import bin.visual.KanvasCari;
import java.io.IOException;
//import java.io.UnsupportedEncodingException;

public class ThrdPencari extends Thread {
    
    /** Creates a new instance of ProcesoBuscador */
    DList lista;
    Pencari buscador;
    String text, txt;
    boolean quit, stop, bQuick=false;
    KanvasCari searchCanvas;
    
    String txtQuick="", oldtxtQuick="", txtQ;
    
    public ThrdPencari(KanvasCari sc, String indexfile) {
        searchCanvas = sc;
        stop = false;
        quit = false;
        text = "";
        lista = sc.getList();
        buscador = new Pencari();
    }
    
    public void setIndexFile(String file){
        try {
            buscador.setIndexFile(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public String searchWord(String palabra){
        if (palabra != null){
            try {
                return buscador.searchExactWord(palabra);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    
    public synchronized void setTeksQuick(String t){
        if (t != null){
            txtQuick = t;
            stop = true;
        }
    }
    
    public  synchronized void searchText(String t){
        text = t;
        stop = false;
    }
    
    public synchronized void stop(boolean b){
        stop = b;
    }
    
    public  void searchOff(){
        quit = true;
    }
    
    public void run(){
        while (!quit){
            txt = new String(text);
            txtQ = new String(txtQuick);
            
            if (text.compareTo("")!=0 && stop==false){
                lista.setItems(buscador.startSearch(text));
                searchCanvas.repaint();
            } else if (txtQuick.compareTo("")!=0) { 
                String ret = searchWord(txtQuick);
                //System.out.println("Thrd: "+searchWord(txtQuick));
                searchCanvas.setTeksQuickView(ret);
                searchCanvas.repaint();
            }
            
            try {
                //Si despues de hacer la b�suqueda el text es el mismo entonces esperamos sino no
                if (txt.compareTo(text)==0 & txtQ.compareTo(txtQuick)==0){
                    synchronized(searchCanvas){
                        //System.out.println("Thrd: wait");
                        searchCanvas.wait();
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
}
