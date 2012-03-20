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
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

public class FontBox {
          
        private int xZomm, yZoom, wZoom, hZoom;
        private int wLatar1 = 0x000000, wLatar2 = 0x000000, wTeks = 0xFFFFFF;
        private String[] sSize = {"Small", "Medium", "Large"};
        private Font fontZoom;
        private int iZoom=0;
        private ImageBlend ib = new ImageBlend();
        //private Gradient grad =  new Gradient();
        private Image imBlend=null;
        private boolean styler=false;
        
        public FontBox(int izoom, boolean styler){
            init();
            this.iZoom = izoom;
            this.styler= styler;
        }
        
        public FontBox(int wLatar, int wTeks, int izoom, boolean styler){
            init();
            setWarna(wLatar, wTeks);
            this.iZoom = izoom;
            this.styler= styler;
        }
        
        private void init(){
            fontZoom = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);

            wZoom = 120;
            hZoom = fontZoom.getHeight() + 4 + 20;
            
            if (styler)
                imBlend = ib.createFillRect(wZoom, hZoom, wLatar1, wLatar2, 245);
        }
        
        public void setWarna(int wLatar, int wTeks){
            setWarna(wLatar, wLatar, wTeks);
        }
        
        public void setWarna(int wLatar1, int wLatar2, int wTeks){
            this.wLatar1 = wLatar1;
            this.wLatar2 = wLatar2;
            this.wTeks = wTeks;
            
            if (styler)
                imBlend = ib.createFillRect(wZoom, hZoom, wLatar1, wLatar2, 240);
        }

        public void inputKey(int keyGameAction, int keyCode){
            if (keyGameAction==Canvas.LEFT || (keyCode==-2 && keyGameAction==0)) 
                if (iZoom > 0) iZoom--;
            if (keyGameAction==Canvas.RIGHT || (keyCode==-5 && keyGameAction==0))
                if (iZoom < 2) iZoom++;
        }
        
        public void setZoom(int i){
            this.iZoom = i;
        }
        
        public int getZoom(){
            return iZoom;
        }
        
        public int getFontSize(){
            switch (iZoom){
                case 0:
                    return Font.SIZE_SMALL;
                case 1:
                    return  Font.SIZE_MEDIUM;
                case 2:
                    return  Font.SIZE_LARGE;
                default:
                    return Font.SIZE_SMALL;
            }
        }
        
        public void setBahasa(String small, String medium, String large){
            sSize[0] = small;
            sSize[1] = medium;
            sSize[2] = large;
        }
        
        public void render(Graphics g){
            xZomm = (g.getClipWidth()/2) - (wZoom/2);
            yZoom = g.getClipHeight() - hZoom - 10; 
            
            //g.setColor(wLatar1);
            //g.fillRect(xZomm, yZoom, wZoom, hZoom);
            //if (styler)
                g.drawImage(imBlend, xZomm, yZoom, g.TOP | g.LEFT);
            //else{
            //    grad.drawGradientRect(g, wLatar1, wLatar2, xZomm, yZoom, wZoom, hZoom, grad.VERTICAL);
            //}
                
                
            g.setColor(wTeks);
            g.drawRect(xZomm+1, yZoom+1, wZoom-3, hZoom-3);

            g.setFont(fontZoom);
            g.drawString(sSize[iZoom], xZomm + (wZoom/2), yZoom + 4, g.TOP | g.HCENTER);

            int wTick = wZoom - 14 - 1; // - 1 panyaimbang
            g.fillRect(xZomm+7, yZoom+(hZoom-10), wTick, 1);

            for (byte i=0; i<3; i++){
                if (iZoom == i)
                    g.fillRect((xZomm+7-2) + ((wTick/2)*i), yZoom+(hZoom-12), 5, 5);
                else
                    g.fillRect((xZomm+7) + ((wTick/2)*i), yZoom+(hZoom-11), 2, 3);
            }

        }
        
        public void inputPointer(int x, int y){

            if (y>yZoom & y<yZoom+hZoom & x>xZomm & x<xZomm+wZoom){
                int pilih = (x-xZomm)/(wZoom/3);

                if (pilih<=2) iZoom = pilih;
            }
        }
        
}
