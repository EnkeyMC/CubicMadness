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
    public final GameState GAMEOVER_STATE;
    public final GameState OPTIONSMENU_STATE;
    public final GameState GRAPHICSOPTIONS_STATE;
    public final GameState CONTROLSMENU_STATE;
    public final GameState HELP_STATE;
    public final GameState HIGHSCORES_STATE;
    public final GameState PAUSEMENU_STATE;

    
    private final GamePanel gp;
    
    public GameStateManager(GamePanel gp){
        this.PLAY_STATE = new PlayState(gp);
        this.MAINMENU_STATE = new MainMenuState(gp);
        this.GAMEOVER_STATE = new GameOverState(gp);
        this.OPTIONSMENU_STATE = new OptionsMenuState(gp);
        this.GRAPHICSOPTIONS_STATE = new GraphicsOptionsState(gp);
        this.CONTROLSMENU_STATE = new ControlsState(gp);
        this.HELP_STATE = new HelpState(gp);
        this.HIGHSCORES_STATE = new HighScoresState(gp);
        this.PAUSEMENU_STATE = new PauseMenuState(gp);
        
        this.pushState(this.MAINMENU_STATE);
        this.gp = gp;
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
    
    public void pushState(GameState s, Object o){
        if(!states.empty())
            states.peek().pause();
        states.push(s);
        states.peek().init(o);
    }
    
    public void popCurrentState(){
        states.pop();
        if(!states.empty())
            states.peek().resume();
    }
    
    public GameState getCurrentState(){
        return states.peek();
    }
    
    public void transition(GameState prev, GameState next, byte type){
        states.push(new TransitionState(gp, prev, next, type));
    }
    
    public void prepareState(GameState s){
        s.init();
        s.pause();
    }
    
    public void resume(GameState s){
        states.push(s);
        resume();
    }
    
    public void popTransition(byte type){
        GameState prev = states.peek();
        states.pop();
        GameState next = states.peek();
        states.pop();
        states.push(new TransitionState(gp, prev, next, type));
    }
}
