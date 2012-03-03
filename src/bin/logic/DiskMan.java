/* Manajemen data RMS
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

import java.io.UnsupportedEncodingException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class DiskMan {

    // key mulai ti hiji sarua jeung mulai id rekod
    private String[] arrKey = {"", "dictionary", "language", "keyboard", "tema", "notefont", "listfont", "quickview"};   
    private String[] arrValue = new String[arrKey.length];  // paranti nyimpeun setingan
    
    private String namaRecordStore = "tolombong";
    
    public DiskMan() {
        RecordStore rs = null;
        
        // isi heula arrValue
        for (int i=1; i<arrValue.length; i++) arrValue[i] = "";
        
        try {
            rs = RecordStore.openRecordStore(namaRecordStore, true );
            
            // cek pernah nyieun rekod can, lamun acan alokasikeun rekod baru sajumlah key
            if (rs.getNextRecordID() == 1){ 
                
                byte[] data = "nihil".getBytes();
                
                for (int i=0; i<arrKey.length; i++)
                    rs.addRecord( data, 0, data.length );

            }else{ // lamun rekod pernah di jieun, cokot datana
                
                byte[] data;
                for (int i=1; i<arrKey.length; i++){

                    data = rs.getRecord(i);
                    if (data !=null)
                        try {
                            arrValue[i] = new String(data, "UTF-8");
                        } catch (UnsupportedEncodingException ex) { ex.printStackTrace(); }
                }
               
            }
            
            rs.closeRecordStore();
            
        } catch( RecordStoreException e ){e.printStackTrace();}
    }
    
    private void save(int id, String data){
        RecordStore rs = null;
        byte[] dataByte=null;
        
        try {
            dataByte = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        
        try {
            
            rs = RecordStore.openRecordStore(namaRecordStore, true );
            
            if (id > rs.getNumRecords()){ 
                
                // gedean lamun jumlah alokasi rekod kurang
                int kurang = id-rs.getNumRecords();
                
                byte[] isi = "nihil".getBytes();
                
                for (int i=1; i<=kurang; i++)
                     rs.addRecord(isi, 0, isi.length );
                
                rs.setRecord(id, dataByte, 0, dataByte.length); 
                
            } else {
                rs.setRecord(id, dataByte, 0, dataByte.length); 
            }

            rs.closeRecordStore();
        
        } catch( RecordStoreException e ){}
    }
    
    private int getID(String sKey){
        int id = 0;
        
        for (int i=1; i<arrKey.length; i++)
            if (arrKey[i].equals(sKey)==true){
                id = i;
                break;
            }
        
        return id;
    }
    
    // string
    public void setData(String key, String value){
        int id = getID(key);
        
        arrValue[id] = value;
        save(id, value);
    }
    
    public String getData(String key){
        return arrValue[getID(key)];
    }
    
    // integer
    public void setData(String key, int value){
        int id = getID(key);
        String s = "";
        
        try{
            s = Integer.toString(value);
        } catch (Exception ex) {}
        
        arrValue[id] = s;
        save(id, s);
    }
    
    public int getDataInt(String key){
        int i = 0;
        
        try {
            i = Integer.parseInt(getData(key));
        } catch (Exception ex) { }
        
        return i; 
    }
    
    // boolean
    public void setData(String key, boolean value){
        int id = getID(key);
        String s = "0";
        
        if (value) s = "1";
        
        arrValue[id] = s;
        save(id, s);
    }
    
    public boolean getDataBool(String key){
        return (getDataInt(key)==1) ? true : false;
    }
    
}
