/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public abstract class GameState {
    
    protected GamePanel gp;
    protected ObjectHandler objects = new ObjectHandler();
    
    public GameState(GamePanel gp){
        this.gp = gp;
    }
    
    public ObjectHandler getObjects(){
        return objects;
    }
    
    public abstract void tick();
    public abstract void draw(Graphics2D g, double interpolation);
    public abstract void pause();
    public abstract void resume();
    public abstract void restart();
    public abstract void init();
}
