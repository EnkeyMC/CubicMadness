/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public abstract class MenuElement {
    
    protected GamePanel gp;
    protected GameState gs;
    protected float x;
    protected float y;
    protected boolean focused;
    
    public MenuElement(GamePanel gp, GameState gs, float x, float y){
        this.gp = gp;
        this.gs = gs;
        this.x = x;
        this.y = y;
    }
    
    public float getX(){
        return x;
    }
    
    public float getY(){
        return y;
    }
    
    public void setFocused(boolean f){
        this.focused = f;
    }
    
    public boolean isFocused(){
        return focused;
    }
    
    public abstract void draw(Graphics2D g, double interpolation);
    public void tick(){}

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }
    
    public boolean isInBounds(int x, int y){
        return ((this.x < x && this.x + getWidth() > x) && (this.y < y && this.y + this.getHeight() > y));
    }
    
    public void actionPerformed(){
        
    }
    
    public abstract int getHeight();
    public abstract int getWidth();
}
