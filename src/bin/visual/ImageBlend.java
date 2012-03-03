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
import javax.microedition.lcdui.Image;

public class ImageBlend {
         
    Gradient gr = new Gradient();
    
    public ImageBlend(){ }

    public Image blend(Image img, int opacity) {

        // jieun array
        int rgb[] = new int[img.getWidth()*img.getHeight()];  

        // isian array pixel gambar
        img.getRGB(rgb, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());  

        // sisipkeun nilai opacity ka satiap pixel
        int opa = (opacity << 24);
        for (int i=0; i<rgb.length; i++)
            rgb[i] = opa + rgb[i];  // isi opacity di posisi 24

        // 0 x FF FF FF FF
        //     24 16 8  0

        // jieun deui jadi gambar
        return Image.createRGBImage(rgb, img.getWidth(), img.getHeight(), true);

    }

    public Image blendEx(Image img, int opacity) {

        // jieun array
        int rgb[] = new int[img.getWidth()*img.getHeight()];  

        // isian array pixel gambar
        img.getRGB(rgb, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());  

        // sisipkeun nilai opacity ka setiap pixel
        int opa = (opacity << 24);
            
        for (int i=0; i<rgb.length; i++)
        if ((rgb[i]) == -1 )
            rgb[i] = 0x00000000;    // hapus warna bodas
        else
            rgb[i] = opa + rgb[i];   // blend
            
        // jieun deui jadi gambar
        return Image.createRGBImage(rgb, img.getWidth(), img.getHeight(), true);
    }

    public Image createFillRect(int lebar, int tinggi, int warna, int opacity){
        return createFillRect(lebar, tinggi, warna, warna, opacity);
    }

    public Image createFillRect(int lebar, int tinggi, int warna1, int warna2, int opacity){
        Image tmp = null;

        tmp =  Image.createImage(lebar, tinggi);

        Graphics g = tmp.getGraphics();

        if (warna1!=warna2)
            gr.drawGradientRect(g, warna1, warna2, 0, 0, lebar, tinggi, gr.VERTICAL);
        else{
            g.setColor(warna1);
            g.fillRect(0, 0, lebar, tinggi);
        }
            
        tmp = Image.createImage(tmp);
        
        return blend(tmp, opacity);
    }
 
}
