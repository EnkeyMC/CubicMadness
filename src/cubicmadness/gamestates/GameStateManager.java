/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import java.awt.Graphics2D;
import java.util.Stack;

/**
 *
 * @author Martin
 */
public class GameStateManager {
    
    private final Stack<GameState> states = new Stack();
    
    public final GameState PLAY_STATE;
    public final GameState MAINMENU_STATE;
    
    public GameStateManager(GamePanel gp){
        this.PLAY_STATE = new PlayState(gp);
        this.MAINMENU_STATE = new MainMenuState(gp);
        this.pushState(this.MAINMENU_STATE);
    }
    
    public void tick(){
        states.peek().tick();
    }   
    
    public void draw(Graphics2D g, double interpolation){
        states.peek().draw(g, interpolation);
    }
    
    public void pause(){
        states.peek().pause();
    }
    
    public void resume(){
        states.peek().resume();
    }
    
    public void restart(){
        states.peek().restart();
    }
    
    public void init(){
        states.peek().init();
    }
    
    public void pushState(GameState s){
        if(!states.empty())
            states.peek().pause();
        states.push(s);
        states.peek().init();
    }
    
    public void popCurrentState(){
        states.pop();
        if(states.empty()){
            System.exit(0);
        }
        states.peek().resume();
    }
    
    public GameState getCurrentState(){
        return states.peek();
    }
}
