/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin
 */
public class MenuTableRow {
    public static final Byte ALIGN_LEFT = 0;
    public static final Byte ALIGN_CENTER = 1;
    public static final Byte ALIGN_RIGHT = 2;
    
    private List<String> data = new ArrayList();
    private List<Byte> align = new ArrayList();

    public MenuTableRow(String[] data) {
        this.data.addAll(Arrays.asList(data));
    }
    
    public MenuTableRow(List<String> data){
        this.data.addAll(data);
    }
    
    public List<String> getData(){
        return data;
    }
    
    public int getColumns(){
        return data.size();
    }
    
    public void setData(String[] data){
        this.data.clear();
        this.data.addAll(Arrays.asList(data));
    }
    
    public void setData(List<String> data){
        this.data.clear();
        this.data.addAll(data);
    }
    
    public MenuTableRow setAligns(Byte[] align){
        if(align.length != data.size()){
            throw new IllegalArgumentException();
        }
        this.align.clear();
        this.align.addAll(Arrays.asList(align));
        return this;
    }
    
    public MenuTableRow setAligns(List<Byte> align){
        if(align.size() != data.size()){
            throw new IllegalArgumentException();
        }
        this.align.clear();
        this.align.addAll(align);
        return this;
    }
    
    public String get(int i){
        return data.get(i);
    }
}
