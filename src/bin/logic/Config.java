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

package bin.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class Config {
    String lang_file; 
    String dictionary;
    
    String selectedKeyboard;
    String[] keys = {"-_@", "abc", "defé", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"}; // <# key anu tiasa digunakeun, pake sunda aksen (é)
    String[] words = {"Cari Kata", "Daftar Kamus", "Keluar", "Bantuan", "Tombol", "Kembali", "Setuju", "Tidak Ditemukan", "Bahasa", 
                      "Ihwal", "Lihat", "OK", "Batal", "Pilih", "Tutup", "Konfigurasi", "Tema", "Ganti Kamus", "Ke Menu Utama", "Kecil", "Sedang", "Besar", "Translate*",
                      "Menu Utama:\\n(2) Naik, (8) Turun, (5) Pilih\\n\nPencarian:\\n(*) Hapus, (0) Naik, (\u0023) Turun"};
    
    String language; // bahasa yang dipilih
    DiskMan disk;
    String encoding;
    
    private String[] tema = {"Default", "Blue Injected", "Lesstrenk"};
            
    /** Creates a new instance of Config */
    public Config() {
        disk = new DiskMan();
        String dicts[] = null;
        selectedKeyboard = "";
        language = "";
        dictionary = disk.getData("dictionary");
        encoding = "UTF8";
       
        dicts = getDictionaries();
        
        //Comprueba que diccionario preferido del usuario sigue estando en el listado de diccionarios
        //Si sigue en el listado selecciono ese diccionario sino eligo el primero que vea
        boolean b = false;
        for(int i = 0; i < dicts.length; i++){
            if(dicts[i].compareTo(dictionary)==0){
                b = true;
                break;
            }
        }
        if (b ==false)
            setDictionary(dicts[0]);
        else
            setDictionary(dictionary);
        
        setLanguage(disk.getData("language"));
        setKeyboard(disk.getData("keyboard"));
        init();
        
    }
    
    public void init(){
        
    }
    
    public int getResMode(int wScreen){
        int ret=1;  // kuduna 0
        
        if (wScreen<=128)
            ret = 1;
        else if (wScreen>=176 & wScreen<240)
            ret = 2;
        else if (wScreen>=240)
            ret = 3;
        
        return ret;
    }
    
    public String[] getDictionaries(){
        
        try {
            String[] item = getKeys("config");
            
            for (int i=0; i<item.length; i++)
                if (item[i].indexOf("|") != -1)
                    item[i] = item[i].substring(0,item[i].indexOf("|"));
                    
            return item;
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return null;
    }
    
    public String[] getDictionariesTag(){
  
        try {
            String[] item = getKeys("config");
            String[] tag = new String[item.length];

            for (int i=0; i<item.length; i++){
                String[] k = getValuesOfKey(item[i], "config");
                
                if (k.length>1)
                    tag[i] = k[1];
                else
                    tag[i] = "N/A";
            }

            return tag;
        } catch (Exception ex){
            //ex.printStackTrace();
        }
        
        return null;
    }
    
    public void setDictionary(String s){
        try {
            String [] v = getValuesOfKey(s, "config");

            if (v !=null)
                lang_file = v[0];
            dictionary = s;
            disk.setData("dictionary", s);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        
    }
    
    public String getDictionary(){
        return dictionary;
    }
    
    private String removeFromString(String s, String c){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i ++) {
          if (c.indexOf(s.charAt(i)) != -1) 
              sb.append(s.charAt(i));
        }
        return sb.toString();
    }
    
    public String getIndexFile(){
        return lang_file;
    }
    
    private String[] getKeys(String file) throws IOException{
        Vector v = new Vector(5, 2);
        StringBuffer lectura = new StringBuffer(13);
        int ch = 0;
        boolean b = true;
        InputStream is = this.getClass().getResourceAsStream("/" + file);
        InputStreamReader ir;
        
        try {
            ir = new InputStreamReader(is, encoding);
        } catch (UnsupportedEncodingException ex) {
            ir = new InputStreamReader(is, "UTF-8");
        }
        
        while ((ch = ir.read()) > -1){
            if (ch=='\n'){
                b = true;
                v.addElement(lectura.toString());
                lectura.setLength(0);
            }else{
                if (ch=='#') b = false;
                if (b == true) lectura.append((char)ch);
            }
        }
        
        if (lectura.length() != 0)
            v.addElement(lectura.toString());
        
        ir.close();
        
        String dicts[] = new String[v.size()];
        for(int i=0; i<v.size(); i++){
            dicts[i] = (String) v.elementAt(i);
        }
        
        return dicts;
    }
    
    public void setKeyboard(String string){
        try {
            String k[] = getValuesOfKey(string, "keyboard");
            if (k.length > 0) keys = k;
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        selectedKeyboard = string;
        disk.setData("keyboard", string);
    }
    
    public String getSelectedKeyboard(){
        return selectedKeyboard;
    }
    
    private String[] getValuesOfKey(String key, String file) throws IOException{
        Vector v = new Vector (10, 3);
        StringBuffer lectura = new StringBuffer(13);
        StringBuffer lectura_valores = new StringBuffer(5);
        int ch = 0;
        boolean b = true;
        
        InputStream is = this.getClass().getResourceAsStream("/" + file);
        InputStreamReader ir;
        
        try {
            ir = new InputStreamReader(is, encoding);
        } catch (UnsupportedEncodingException ex) {
            ir = new InputStreamReader(is, "UTF-8");
        }
        
        while ((ch = ir.read()) > -1){
            if (ch=='\n'){
                if (lectura.toString().compareTo(key)==0)
                    break;
                b = true;
                lectura.setLength(0);
            }else{
                if (ch=='#'){
                    b = false;
                }else{
                    if (b==false){ // Empieza la informacion para el array de teclados
                        if (lectura.toString().compareTo(key)==0){

                            if (ch=='|'){
                                v.addElement(lectura_valores.toString());
                                lectura_valores.setLength(0);
                            }else
                                lectura_valores.append((char) ch);
                            
                        }
                    }
                }
                if (b==true)
                    lectura.append((char)ch);
            }
        }
        
        if (lectura_valores.length() > 0)
                v.addElement(lectura_valores.toString());
        ir.close();
        String salida[] = new String[v.size()];
        for(int i=0; i<v.size(); i++){
            salida[i] = (String) v.elementAt(i);
            
            if (salida[i].indexOf("|") != -1)
                salida[i] = salida[i].substring(0, salida[i].indexOf("|"));
        }
        
        
        
        return salida;
    }
    
    /**
     * retorna el arrray con el mapeo del teclado
     */
    public String[] getKeyboard(){
        return keys;
    }
    
    /**
     *Retorna el array con los teclados disponibles
     */
    public String[] getKyeboards(){
        try {
            return getKeys("keyboard");
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return null;
    }
    
    public void setLanguage(String lang){
        language = lang;
        disk.setData("language", lang);
        String w[];
        try {
            w = getValuesOfKey(lang, "interface");
            if (w.length > 0)
                words = w;
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        
    }
    
    public String[] getLanguages(){
        try {
            return getKeys("interface");
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
        return null;
    }
    
    public String getLanguage(){
        return language;
    }
    
    String[] langKey = {"SEARCH", "DICTIONARY", "EXIT", "HELP", "KEYBOARD", "BACK", "ACCEPT", "NO_RESULT", "LANGUAGE",
                        "ABOUT", "LIHAT", "OK", "BATAL", "PILIH", "TUTUP", "KONFIGURASI", "TEMA", "GANTI_KAMUS", 
                        "KE_MENU_UTAMA", "KECIL", "SEDANG", "BESAR", "TRANSLATE", "TEXT_HELP"}; 
    
    public String getWord(String s){
                
        for (int i=0; i<langKey.length; i++)
            if (langKey[i].equals(s)) return words[i];
        
        return null;
        
    }
    
    public void saveTema(String tema){
        disk.setData("tema", tema);
    }
    
    public String getSavedTema(){
        return disk.getData("tema");
    }
    
    public int getSavedTemaInt(){
        int index = 0;
        
        for (int i=0; i<tema.length; i++){
            if (tema[i].equals(disk.getData("tema"))){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public String[] getTheme(){
        return tema;
    }
    
    public void saveZoom(int zoom){
        disk.setData("notefont", Integer.toString(zoom));
    }
    
    public int getZoom(){
        int fontSize = 0;
        
        try {
            fontSize = Integer.parseInt(disk.getData("notefont"));
        } catch (Exception ex) { }
        
        return fontSize; 
    }
    
    public void setData(String key, String value){
        disk.setData(key, value);
    }
    
    public void setData(String key, int value){
        disk.setData(key, value);
    }
    
    public void setData(String key, boolean value){
        disk.setData(key, value);
    }

    public String getData(String key){
        return disk.getData(key);
    }
    
    public int getDataInt(String key){
        return disk.getDataInt(key);
    }
    
    public boolean getDataBool(String key){
        return disk.getDataBool(key);
    }
 
    int[] arrColor = {};
    
    /**
     * [0]: DTextField border [1]: DTextField text
     * [2]: DList item border1 [3]: DList  item border2 [4]: DList text
     * @param index
     * @return 
     */
    public int getColor(int index){
        return arrColor[index];
    }
}
