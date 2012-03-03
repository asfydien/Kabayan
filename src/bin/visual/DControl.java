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

public abstract class DControl {
    
    public int x, y, width, height;
    private boolean focus;
    private boolean visible; //indica si el control es visible

    
    /** Creates a new instance of DControl */
    public DControl(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isVisible() {
        return visible;
    }
    public boolean hasFocus(){
        return focus;
    }

    public void setFocus(boolean f){
        focus = f;
    }
    
}
