/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.bin.Utils;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuElement;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Martin
 */
public abstract class GameState {
    
    protected GamePanel gp;
    protected ObjectHandler objects = new ObjectHandler();
    protected boolean paused;
    
    public GameState(GamePanel gp){
        this.gp = gp;
        paused = false;
    }
    
    public ObjectHandler getObjects(){
        return objects;
    }
    
    public abstract void tick();
    public abstract void draw(Graphics2D g, double interpolation);
    public abstract void restart();
    public abstract void init();
    public abstract void init(Object o);
    
    
    public void pause(){
        paused = true;
    }
    public void resume(){
        paused = false;
    }
    
    protected void menuInteraction(){
        if(KeyInput.pressed.contains(Config.UP) || KeyInput.pressed.contains(KeyEvent.VK_UP)){
            int i = 0;
            for(MenuElement e : objects.buttons){
                if(e.isFocused()){
                    i = objects.buttons.indexOf(e);
                    e.setFocused(false);
                }
            }
            i = (int) Utils.cycle(i - 1, 0, objects.buttons.size()-1);
            objects.buttons.get(i).setFocused(true);
            KeyInput.pressed.remove(Config.UP);
            KeyInput.pressed.remove(KeyEvent.VK_UP);
        }
        
        if(KeyInput.pressed.contains(Config.DOWN) || KeyInput.pressed.contains(KeyEvent.VK_DOWN)){
            int i = 0;
            for(MenuElement e : objects.buttons){
                if(e.isFocused()){
                    i = objects.buttons.indexOf(e);
                    e.setFocused(false);
                }
            }
            i = (int) Utils.cycle(i + 1, 0, objects.buttons.size()-1);
            objects.buttons.get(i).setFocused(true);
            KeyInput.pressed.remove(Config.DOWN);
            KeyInput.pressed.remove(KeyEvent.VK_DOWN);
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_ENTER) || KeyInput.pressed.contains(Config.ATTACK)){
            KeyInput.pressed.remove(KeyEvent.VK_ENTER);
            KeyInput.pressed.remove(Config.ATTACK);
            for(MenuElement e : objects.buttons){
                if(e.isFocused()){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        if(MouseInput.LMB){
            MouseInput.LMB = false;
            for(MenuElement e : objects.buttons){
                if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                    e.actionPerformed();
                    return;
                }
            }
        }
    }
    
    protected void unfocusAllElements(){
        for(MenuElement e : objects.buttons){
            e.setFocused(false);
        }
    }
}
