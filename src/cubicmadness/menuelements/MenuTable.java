/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin
 */
public class MenuTable extends MenuElement{
    // TODO text align
    private int width, height;
    private float padding = 0, cellMargine = 0, cellPadding = 10;
    private Color header = new Color(150,150,150,50), data = new Color(200,200,200,50), headerText = new Color(100,100,100), dataText = new Color(100,100,100);
    private List<MenuTableRow> rows;
    private MenuTableRow heading = null;
    private List<Integer> columnWidth = null;
    
    private Font headFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
    private Font dataFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    public MenuTable(GamePanel gp, GameState gs, float x, float y, int width, int height) {
        super(gp, gs, x, y);
        this.width = width;
        this.height = height;
        this.rows = new ArrayList();
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        if(rows.isEmpty() && heading == null)
            return;
        
        int cellHeight, cellWidth, textOffset, startY;
        
        if(heading == null){
            cellHeight = (int) ((this.height - 2*padding - (rows.size() - 1)*cellMargine) / rows.size());
            cellWidth = (int) ((this.width - 2*padding - (rows.get(0).getColumns() - 1)*cellMargine) / rows.get(0).getColumns());         
            
            startY = (int) (this.getY() + padding);
        }else{
            cellHeight = (int) ((this.height - 2*padding - (rows.size())*cellMargine) / (rows.size() + 1));
            cellWidth = (int) ((this.width - 2*padding - (heading.getColumns() - 1)*cellMargine) / heading.getColumns());         
            
            g.setFont(headFont);
            FontMetrics fmh = g.getFontMetrics();
            int currX = (int)this.getX();
            for (int i = 0; i < heading.getColumns(); i++) {
                if(columnWidth != null){
                    cellWidth = columnWidth.get(i);
                }
                
                g.setColor(header);
                g.fillRect((int) (padding + currX + i*cellMargine), (int) (this.getY() + padding), cellWidth, cellHeight);
                
                g.setColor(headerText);
                g.drawString(heading.get(i), padding + currX + cellPadding, this.getY() + padding + (cellHeight - fmh.getHeight()) / 2f + fmh.getAscent());
                currX += cellWidth;
            }
            
            startY = (int) (this.getY() + padding + cellHeight + cellMargine);
        }
        
        FontMetrics fm = g.getFontMetrics(dataFont);
        
        textOffset = (int) ((cellHeight - fm.getHeight()) / 2f) + fm.getAscent();
        
        g.setFont(dataFont);
        int currX;
        for (int r = 0; r < rows.size(); r++) {
            MenuTableRow row = rows.get(r);
            currX = (int) this.getX();
            for (int d = 0; d < row.getColumns(); d++) {
                String text = row.get(d);
                
                if(columnWidth != null){
                    cellWidth = columnWidth.get(d);
                }
                
                g.setColor(getDataColor());
                g.fillRect((int)(currX + padding + d*cellMargine), startY + r*cellHeight + r*(int)cellMargine, cellWidth, cellHeight);
                
                g.setColor(dataText);
                g.drawString(text, currX + padding + cellPadding + d*cellMargine, startY + r*cellHeight + r*cellMargine + textOffset);
                currX += cellWidth;
            }
        }
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    /**
     * @param width the width to set
     */
    public MenuTable setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * @param height the height to set
     */
    public MenuTable setHeight(int height) {
        this.height = height;
        return this;
    }
    
    public MenuTable appendRow(MenuTableRow row){
        rows.add(row);
        return this;
    }
    
    public MenuTable clearRows(){
        rows.clear();
        return this;
    }
    
    public MenuTable setHeader(MenuTableRow head){
        this.heading = head;
        return this;
    }

    /**
     * @return the headFont
     */
    public Font getHeadFont() {
        return headFont;
    }

    /**
     * @param headFont the headFont to set
     */
    public MenuTable setHeadFont(Font headFont) {
        this.headFont = headFont;
        return this;
    }

    /**
     * @return the dataFont
     */
    public Font getDataFont() {
        return dataFont;
    }

    /**
     * @param dataFont the dataFont to set
     */
    public MenuTable setDataFont(Font dataFont) {
        this.dataFont = dataFont;
        return this;
    }
    
    public int getColumnsCount(){
        return heading.getColumns();
    }
    
    public List<MenuTableRow> getRows(){
        return this.rows;
    }
    
    public MenuTable setPadding(float padding){
        this.padding = padding;
        return this;
    }
    
    public float getPadding(){
        return padding;
    }
    
    public MenuTable setCellMargine(float margine){
        this.cellMargine = margine;
        return this;
    }
    
    public float getCellMargine(){
        return cellMargine;
    }

    /**
     * @return the header
     */
    public Color getHeaderColor() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public MenuTable setHeaderColor(Color header) {
        this.header = header;
        return this;
    }

    /**
     * @return the data
     */
    public Color getDataColor() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public MenuTable setDataColor(Color data) {
        this.data = data;
        return this;
    }

    /**
     * @return the headerText
     */
    public Color getHeaderText() {
        return headerText;
    }

    /**
     * @param headerText the headerText to set
     */
    public MenuTable setHeaderTextColor(Color headerText) {
        this.headerText = headerText;
        return this;
    }

    /**
     * @return the dataText
     */
    public Color getDataTextColor() {
        return dataText;
    }

    /**
     * @param dataText the dataText to set
     */
    public MenuTable setDataTextColor(Color dataText) {
        this.dataText = dataText;
        return this;
    }

    /**
     * @return the cellPadding
     */
    public float getCellPadding() {
        return cellPadding;
    }

    /**
     * @param cellPadding the cellPadding to set
     */
    public MenuTable setCellPadding(float cellPadding) {
        this.cellPadding = cellPadding;
        return this; 
    }
    
    public MenuTable setColumnWidth(List<Integer> width){
        if(heading != null){
            if(width.size() != heading.getColumns()){
                throw new IllegalArgumentException();
            }            
        }else if(!rows.isEmpty()){
            if(width.size() != rows.get(0).getColumns()){
                throw new IllegalArgumentException();
            }
        }
        this.columnWidth = new ArrayList();
        this.columnWidth.addAll(width);
        return this;
    }
    
    public MenuTable setColumnWidth(Integer[] width){
        return setColumnWidth(Arrays.asList(width));
    }
    
    public MenuTable setColumnWidth(Float[] width){
        if(heading != null){
            if(width.length != heading.getColumns()){
                throw new IllegalArgumentException();
            }            
        }else if(!rows.isEmpty()){
            if(width.length != rows.get(0).getColumns()){
                throw new IllegalArgumentException();
            }
        }
        
        float sum = 0;
        for(Float f : width){
            sum += sum;
        }
        
        if(sum > 1){
            throw new IllegalArgumentException();
        }
        
        this.columnWidth = new ArrayList();
        
        float w = this.getWidth() - 2*padding - (width.length - 1) * cellMargine;
        
        for(Float f : width){
            columnWidth.add((int)(w * f));
        }
        
        return this;
    }
}
