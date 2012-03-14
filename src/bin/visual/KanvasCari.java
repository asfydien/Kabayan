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
import bin.logic.ThrdPencari;
import javax.microedition.lcdui.*;
import bin.logic.Kabayan;
import java.util.Vector;

public class KanvasCari extends Canvas implements CommandListener {
    
    private DTextField txtCari;
    private DList lstResults;
    
    private Command cmdLihat, cmdMenu, cmdPilih, cmdBatal, cmdSelesai;
    private String scmdLihat, scmdMenu, scmdPilih, scmdBatal, scmdSelesai, ssmall, smedium, slarge;
    
    private Kabayan midlet;
    private String index_file;
    
    private ThrdPencari pPencari;
    
    private int clLatar = 0xFFFFFF;
    private int clQLine1 = 0xFFFFFF, clQLine2 = 0x000000, clQFIll = 0xD2D2D2;
    
    private String[] menu = {"Ganti kamus", "Quick view", "Font Size", "Ke Menu utama"};
    private boolean bMenu = false, bDict = false, bFont = false, bQuick = false, bCmdLihat;
    
    private Font huruf;
    private int fontSize = Font.SIZE_SMALL, fontSizeMenu;
    
    public static final int CMD_CLEAR = 0, CMD_NORMAL = 1, CMD_FADE = 2, CMD_FINISH = 3;
    
    private FontBox fontbox;
    private MenuBox menubox, menuboxDict;
    
    private int yDList;
    
    private String teksQuick = "", stxtCari = "";
    
    private Splitter tn = new Splitter();
    
    private long tick;
    
    //private boolean bExtra=false;
    private int resMode = 1;
    
    public KanvasCari(Kabayan midlet) {
       this.midlet = midlet; 
       index_file = "";
       
       //bExtra = getWidth() >= 176;
       resMode = midlet.cfg.getResMode(getWidth()); 
       // sesuiakeun font jang ukuran layar HP anu leuwih 240x..
       fontSizeMenu = (resMode>2) ? Font.SIZE_MEDIUM : Font.SIZE_SMALL;
       
       bQuick = midlet.cfg.getDataBool("quickview");
       
       huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize); 
       
       txtCari = new DTextField("", midlet.cfg.getKeyboard(),2 , 2, getWidth()-5, huruf.getHeight()+2, fontSize);
       txtCari.setFocus(true);
       
       yDList = txtCari.y + txtCari.height + 4;   // +4 jarak antara top ka txtcari + jarak txtcari ka list
       lstResults = new DList(2, yDList, getWidth()-5, getHeight() - yDList - 2, fontSize);   // -2 ameh teu antel teuing ka handap
       //lstResults.setSeleccionable(true);
       if (resMode>2) lstResults.setExtraItemHeight(2);
       
       pPencari = new ThrdPencari(this, midlet.cfg.getIndexFile());
       pPencari.start();
       
      init();
      setFontSize();
      
      setCommandListener(this);
      
    }
    
    public void  init(){
        bCmdLihat=false;
        
        // label cmd
        scmdBatal   = midlet.cfg.getWord("BATAL");
        scmdLihat   = midlet.cfg.getWord("LIHAT");
        scmdMenu    = "Menu";
        scmdPilih   = midlet.cfg.getWord("PILIH");
        scmdSelesai = midlet.cfg.getWord("TUTUP");
        
        // label menu
        menu[0] = midlet.cfg.getWord("GANTI_KAMUS");
        menu[1] = bQuick ? "Hide QuickView" : "Show QuickView";
        menu[3] = midlet.cfg.getWord("KE_MENU_UTAMA");
        
        // bahasa zoom
        ssmall  = midlet.cfg.getWord("KECIL");
        smedium = midlet.cfg.getWord("SEDANG");
        slarge  = midlet.cfg.getWord("BESAR");
        
        fontbox = new FontBox(0x000000, 0xFFFFFF, midlet.cfg.getDataInt("listfont"), resMode>1);
        fontbox.setBahasa(ssmall, smedium, slarge);
       
        menubox = new MenuBox(menu, getWidth(), getHeight(), fontSizeMenu, MenuBox.MENU_RIGHT, resMode>1, 0);
        
        if (resMode>1){
            menuboxDict = new MenuBox(midlet.cfg.getDictionaries(), getWidth(), getHeight(), fontSizeMenu, MenuBox.MENU_CENTER, resMode>1, 18);
            menuboxDict.setImage("/img/list.png");
        }else{
            menuboxDict = new MenuBox(midlet.cfg.getDictionaries(), getWidth(), getHeight(), fontSizeMenu, MenuBox.MENU_CENTER, resMode>1, 0);
            
        }
        
        commandMode(CMD_NORMAL);
        
        txtCari.setText(null);
        txtCari.setKeyboard(midlet.cfg.getKeyboard());
        lstResults.setItems(null);
        
        stxtCari = "";
        
        if (midlet.cfg.getIndexFile().compareTo(index_file)!=0){
          index_file = midlet.cfg.getIndexFile();  
          pPencari.setIndexFile(index_file);
        }
        
        if (bQuick) modeQuickView(true);
        
        setTema(midlet.cfg.getSavedTemaInt());
        
    }
    
    private void commandMode(int cmd){
        // hapus sadayana
        removeCommand(cmdBatal);
        removeCommand(cmdLihat);
        removeCommand(cmdMenu);
        removeCommand(cmdPilih);
        removeCommand(cmdSelesai);
        
        switch (cmd){
            case CMD_CLEAR:
                break;
            case CMD_NORMAL:
                bCmdLihat=false;
                buatCmdLihat();

                cmdMenu = new Command(scmdMenu, Command.BACK, 2);
                addCommand(cmdMenu);
                break;
            case CMD_FADE:
                cmdPilih = new Command(scmdPilih, Command.OK, 1);
                cmdBatal = new Command(scmdBatal, Command.CANCEL, 2);
                addCommand(cmdPilih);
                addCommand(cmdBatal);
                break;
            case CMD_FINISH:
                cmdSelesai = new Command(scmdSelesai, Command.OK, 1);
                addCommand(cmdSelesai);
        }
    }
    
    private void buatCmdLihat(){
        // cmdLihat bakal muncul lamun list pencarian ayaan
        if (lstResults.content != null && ((!bMenu | !bDict) & lstResults.content.length > 0)) {
            if (!bCmdLihat){
                cmdLihat = new Command(scmdLihat, Command.OK, 1);
                addCommand(cmdLihat);
                bCmdLihat=true;
            }
        } else {
            removeCommand(cmdLihat);
            bCmdLihat=false;
        }
    }
    
    private void setTema(int index){
        int deg = 0;
        
        switch (index){
            case 0:
                //lstResults.setWarna(0x000000, 0xFFFFFF, 0x4D9336, 0x306249, 0x4E9935, 0xF5FFF5, 0x4D9336);
                lstResults.setWarna(0x000000, 0xFFFFFF, 0x4D9336, 0x306249, 0x4E9935, 0xE0E0E0);
                txtCari.setWarna(0x005000, 0x000000, 0xDFDFDF, 0x687256);
                clLatar = 0xFAFFFA; clQLine1=0x687256; clQLine2=0xEAF2EA; clQFIll = 0xDEE5DE;
                
                menubox.setWarna(0x4D9336);
                menuboxDict.setWarna(0x4D9336);
                fontbox.setWarna(0x0C602F, deg, 0xFFFFFF);
                break;
            case 1:
                //lstResults.setWarna(0x00003F, 0xFFFFFF, 0x2146DD, 0x1E41CC, 0x2146DD, 0xF5F5FF, 0x41418C);
                lstResults.setWarna(0x00003F, 0xFFFFFF, 0x2146DD, 0x1E41CC, 0x2146DD, 0xE0E0E0);
                txtCari.setWarna(0x000050, 0x000000, 0xDFDFDF, 0x4D4DA5);
                clLatar = 0xF8F8FF; clQLine1=0x7F7FFF; clQLine2=0xD8D8FF; clQFIll = 0xCCCCFF;
                
                menubox.setWarna(0x2146DD);
                menuboxDict.setWarna(0x2146DD);
                fontbox.setWarna(0x2342B2, deg, 0xFFFFFF);
                break;
            case 2:
                lstResults.setWarna(0xD8D8D8, 0xFFFFFF, 0x8C8C8C, 0xE5E5E5, 0x727272, 0x0C0C0C);
                txtCari.setWarna(0x000050, 0x000000, 0xDFDFDF, 0xFFFFFF);
                clLatar = 0x000000; clQLine1=0xE5E5E5; clQLine2=0xD8D8D8; clQFIll = 0xD2D2D2;
                
                menubox.setWarna(0x515151);
                menuboxDict.setWarna(0x515151);
                fontbox.setWarna(0x515151, deg, 0xFFFFFF);
                break;
        }

        
        repaint();
       
    }
    
    private void setFontSize(){

        fontSize = fontbox.getFontSize();
        huruf = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, fontSize); 

        txtCari.setSize(2, 2, getWidth()-5, huruf.getHeight()+2);
        txtCari.setFontSize(fontSize);
       
        yDList = txtCari.y + txtCari.height + 4;
        lstResults.y = yDList;
        
        if (bQuick)
            lstResults.setSize(2, yDList, getWidth()-5, ((getHeight()/3)*2) - yDList);
        else
            lstResults.setSize(2, yDList, getWidth()-5, getHeight() - yDList - 2); // -2 ameh teu antel teuing ka handap
        
        lstResults.setFontSize(fontSize);
        
        midlet.cfg.setData("listfont", fontbox.getZoom());
        
    }
    
    private void modeQuickView(boolean b){
        
        yDList = txtCari.y + txtCari.height + 4;
        
        String sbak = txtCari.getText();
        
        if (b) {
            lstResults.setSize(2, yDList, getWidth()-5, ((getHeight()/3)*2) - yDList);
            txtCari.setText(sbak);
        } else {
            lstResults.setSize(2, yDList, getWidth()-5, getHeight() - yDList - 2); // -2 ameh teu antel teuing ka handap
            txtCari.setText(sbak);
        }

        midlet.cfg.setData("quickview", b);
        txtCari.setText(sbak);
        refind(sbak);
    }
    
    public void paint(Graphics g) {
        
        g.setColor(clLatar);
        g.fillRect(0, 0, getWidth(), getHeight());
        txtCari.render(g);
        lstResults.render(g);
        
        g.setColor(0x000000);
        
        buatCmdLihat();
       
        if (bQuick) drawQuick(g);
        
        if (bMenu){
            menubox.drawFade(g);
            menubox.render(g);
        } 
        
        if (bDict){
            menuboxDict.drawFade(g);
            menuboxDict.render(g);
        }
        
        if (bFont) {
            menubox.drawFade(g);
            fontbox.render(g);
        }
       
        
    }
    
    private  void drawQuick(Graphics g){
        int seper3 = getHeight()/3;
        int yQuick = (seper3*2) + 3;
        int hQuick = getHeight() - yQuick; 
        
        g.setColor(clQFIll);
        g.fillRect (3, yQuick, getWidth()-6, hQuick);

        g.setColor(clQLine1);
        g.drawRect(2, yQuick, getWidth()-5, hQuick);
        
        
        for (int i=0; i<3; i++){
            g.setColor(clQLine1);
            g.drawRect((getWidth()/2 - 6)+(6*i), yQuick+2, 1, 1);
            g.setColor(clQLine2);
            g.drawRect((getWidth()/2 - 6)+(6*i)+1, yQuick+3, 0, 0);
        }
        
        g.setColor(clQLine2);
        g.fillRect(3, yQuick+1, getWidth()-6, 1);
        
        g.setColor(clQLine2);
        g.fillRect(3, yQuick+5, getWidth()-6, 1);
        
        tulisQuick(g);
    }
    
    public void setTeksQuickView(String s){
        teksQuick = (lstResults.content!=null) ? s : "";
    }
    
    public void tulisQuick(Graphics g) {
        int seper3 = getHeight()/3;
        int yQuick = (seper3*2) + 7;
        int hQuick = getHeight() - yQuick - 2; 
        int maxLine = (hQuick-4)/huruf.getHeight();

        if (lstResults.content!=null)
            try{
                Vector hasil = tn.cacag(huruf, teksQuick, getWidth()-12, false, 20, maxLine);
                tn.tulis(g, huruf, hasil, 6, yQuick+3, 0, maxLine, 0x000000);
            } catch (Exception ex) {}
    }

    protected void keyPressed(int keyCode) {
        
          if (bMenu) {
              menubox.inputKey(getGameAction(keyCode), keyCode);
          } else if (bDict) {
              menuboxDict.inputKey(getGameAction(keyCode), keyCode);
          } else if (bFont) {
              fontbox.inputKey(getGameAction(keyCode), keyCode);
              setFontSize();
          } else {
              // kirim sinyal
              txtCari.inputKey(getGameAction(keyCode), keyCode);
              lstResults.inputCodeKey(Keyboard.getValidKeyCode(getGameAction(keyCode), keyCode));
              
              String tCari = txtCari.getText();
              
              // lamun txtCari kosong, kosongkeun list oge
              if (tCari.equals("")) lstResults.setItems(null);
              
              // perbarui list atau quickview, ulah dua nana bisi trid stack soalna make .notify
              if (!stxtCari.equals(tCari)){   // not
                  refind(tCari);
                  teksQuick = "";
                  stxtCari  = tCari;
                  //if (lstResults.content!=null & lstResults.content>0) refindQuick(tCari);
                  
              } else if (getGameAction(keyCode)==DOWN | getGameAction(keyCode)==UP) {
                  if (lstResults.getSelectedText() != null & bQuick==true) {
                      
                      if (System.currentTimeMillis() - tick > 150)  // antisipasi si jari lincah, daripada ngaheng
                          refindQuick(lstResults.getSelectedText());
                      else
                          teksQuick = "";
                      
                  }
                  
                  tick = System.currentTimeMillis();    // waktu pas terakhir mencet
              }
              
          }
     
      repaint();
      
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdLihat)
            this.tampilkanArti();
        
        if (c == cmdBatal){
                bMenu = false;
                bDict = false;
                bFont = false;
                
                commandMode(CMD_NORMAL);
        }
        
        if (c == cmdMenu){
            menubox.setSelctedByValue(menu[0]);
            bMenu = !bMenu;
            
            if (bMenu) commandMode(CMD_FADE);
        }
        
        if (c == cmdPilih){
            if (bDict){
                commandMode(CMD_NORMAL);
                
                bDict = false;
                String selTeks = menuboxDict.getSelectedText();
                
                if (!midlet.cfg.getDictionary().equals(selTeks)) {     // not
                    midlet.cfg.setDictionary(selTeks);
                    pPencari.setIndexFile(midlet.cfg.getIndexFile());  

                    // perbarui pencarian di kamus baru
                    refind(txtCari.getText());
                    teksQuick = "";
                }
            }
            
            if (bMenu){
            
                switch (menubox.getSelected()){
                    case 0:
                        menuboxDict.setSelctedByValue(midlet.cfg.getData("dictionary"));
                        menuboxDict.setActiveItem(menuboxDict.getSelected());
                        bDict = true;
                        break;
                    case 1:
                        bQuick = !bQuick;
                        modeQuickView(bQuick);
                        commandMode(CMD_NORMAL);
                        
                        menubox.setMenuText(1, bQuick ? "Hide QuickView" : "Show QuickView");
                        
                        break;
                    case 2:
                        bFont = true;
                        commandMode(CMD_FINISH);
                        break;
                    case 3:
                        commandMode(CMD_CLEAR);
                        Display.getDisplay(midlet).setCurrent(midlet.pMenu.lsMenu);
                }
                
                bMenu = false;
            }
        }
        
        if (c == cmdSelesai){
            bDict = false;
            bMenu = false;
            bFont = false;
            commandMode(CMD_NORMAL);
            refind(txtCari.getText());
        }
        
        repaint();
    }
    
    // kirim teks ka trid ameh di teremahkeun
    private synchronized void refindQuick(String s){
        pPencari.setTeksQuick(s);
        this.notify();
    }
    
    // anyarkeun pencarian
    private synchronized void refind(String s){
        pPencari.searchText(s);
        this.notify();
    }
    
    private void tampilkanArti(){
        pPencari.stop(true);
        KanvasNote kNote = new KanvasNote(midlet, this, 
                                           lstResults.getSelectedText(),
                                           pPencari.searchWord(lstResults.getSelectedText()));
        Display.getDisplay(midlet).setCurrent(kNote);
    }
    
    public DList getList(){
        return lstResults;
    }
    
    public void pointerPressed(int x, int y){
        
        if (bMenu)
            menubox.inputPointer(x, y);
        else if (bDict)
            menuboxDict.inputPointer(x, y);
        else if (bFont) {
            fontbox.inputPointer(x, y);
            setFontSize();
        } else {
            
            if (lstResults.getSelectedText() != null & bQuick==true){
                String s="", ns="";
                s = lstResults.getSelectedText();
                
                lstResults.inputPointer(x, y);
                
                ns = lstResults.getSelectedText();
                if (!s.equals(ns)) refindQuick(ns);
            } else
                lstResults.inputPointer(x, y);
            
        }
            
        repaint();
    }
    
}
