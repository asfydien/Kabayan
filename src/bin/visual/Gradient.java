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

/*
 * referensi:
 * http://www.developer.nokia.com/Community/Wiki/index.php/Draw_Gradient_in_Java_ME
 * 
 */

package bin.visual;

import javax.microedition.lcdui.Graphics;

public class Gradient {
    public final int VERTICAL = 0;
    public final int HORIZONTAL = 1;

    public void drawGradientRect(Graphics g, int cl1, int cl2, int x, int y, int width, int height, int orientation)
    {
        int max = orientation == VERTICAL ? height : width;

        int x2 = x + width - 1;
        int y2 = y + height - 1;

        int R, G, B;    // merah, hijau, biru
        int color, j;

        int R1 = ((cl1 >> 16) & 0xff);
        int G1 = ((cl1 >> 8)  & 0xff);
        int B1 = ((cl1 >> 0)  & 0xff);
        int R2 = ((cl2 >> 16) & 0xff);
        int G2 = ((cl2 >> 8)  & 0xff);
        int B2 = ((cl2 >> 0)  & 0xff);

        for(int i = 0; i < max; i++)
        {
//-- hitung midcolor
            j = max - i;

            R = (R1 * j + R2 * i) / max;
            G = (G1 * j + G2 * i) / max;
            B = (B1 * j + B2 * i) / max;

            color =  R << 16 | G << 8 | B;

            //System.out.println("R:" + R + " G:" + G + " B:" + B + "CL:" + color);

//-- gambar garis
            g.setColor(color);

            switch (orientation){
                case VERTICAL:   g.drawLine(x, y + i, x2 , y + i); break;
                case HORIZONTAL: g.drawLine(x + i, y, x + i, y2); break;
            }

        }   // for
            
    }
    
    public static int brightColor(int cl, int n){

        float[] rgb = new float[3];
        float c;

        rgb[0] = ((cl >> 16) & 0xff);
        rgb[1] = ((cl >> 8)  & 0xff);
        rgb[2] = ((cl >> 0)  & 0xff);

        for (int i=0; i<3; i++) {
            c = rgb[i]/100f;
            rgb[i] = (int)(rgb[i] + (c*n));        
            
            if (rgb[i]>255) rgb[i]=255;
            if (rgb[i]<0) rgb[i]=0;
        }

        return (int)rgb[0] << 16 | (int)rgb[1] << 8 | (int)rgb[2];
    }

}
