/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.menuelements.MenuLabel;
import cubicmadness.particle.Particle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class MainMenuState extends GameState {

    public MainMenuState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
        if(!(MouseInput.mouseXYtransform.x == MouseInput.mousePrevXY.x && MouseInput.mouseXYtransform.y == MouseInput.mousePrevXY.y)){
            MouseInput.mousePrevXY = MouseInput.mouseXYtransform;
            
            for(MenuElement e : objects.buttons){
                if(e.isInBounds(MouseInput.mouseXYtransform.x, MouseInput.mouseXYtransform.y)){
                    this.unfocusAllElements();
                    e.setFocused(true);
                }
            }
        }
        
        List<Particle> particlesToRemove = new ArrayList();
        for(Particle p : objects.particles){
            p.tick(particlesToRemove);
        }
        
        for(Particle p : particlesToRemove){
            objects.particles.remove(p);
        }
        
        for(MenuElement e : objects.elements)
            e.tick();
        
        for(MenuElement e : objects.buttons)
            e.tick();
        
        this.menuInteraction();
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        
        g.drawImage(gp.bgr.getImage(), 0, 0, gp);
        g.setColor(new Color(1,1,1,0.85f));
        g.fillRect(0, 0, gp.size.width, gp.size.height);
        
        for(MenuElement e : objects.elements){
            e.draw(g, interpolation);
        }
        
        for(MenuButton b : objects.buttons)
            b.draw(g, interpolation);
        
        for(Particle p : objects.particles){
            p.draw(g, interpolation);
        }
    }

    @Override
    public void restart() {
        init();
    }

    @Override
    public void init() {
        this.objects = new ObjectHandler();
        MenuButton e;
        try {
            e = new MenuButton(gp, this, MenuButton.BIG, "Play", this.getClass().getDeclaredMethod("buttonPlayAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(160);
            e.setFocused(true);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Options", this.getClass().getDeclaredMethod("buttonOptionsAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(230);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "High Score", this.getClass().getDeclaredMethod("buttonHighScoreAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(300);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Help", this.getClass().getDeclaredMethod("buttonHelpAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(370);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Exit", this.getClass().getDeclaredMethod("buttonExitAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(440);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width / 2, 100, "Cubic Madness", MenuLabel.TYPE_H1, MenuLabel.ALIGN_CENTER));
        objects.elements.add(new MenuLabel(gp, this, 20, gp.size.height - 20, "Made by Martin Omacht", 12, 0));
        objects.elements.add(new MenuLabel(gp, this, gp.size.width - 20, gp.size.height - 20, "Version: " + Config.VERSION, 12, MenuLabel.ALIGN_RIGHT));
    }
    
    public void buttonPlayAction(){
        gp.gsm.transition(this, gp.gsm.PLAY_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonExitAction(){
        System.exit(0);
    }
    
    public void buttonOptionsAction(){
        gp.gsm.transition(this, gp.gsm.OPTIONSMENU_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonHighScoreAction(){
        gp.gsm.transition(this, gp.gsm.HIGHSCORES_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonHelpAction(){
        gp.gsm.transition(this, gp.gsm.HELP_STATE, TransitionState.BLACKFADE);
    }

    @Override
    public void init(Object o) {
        init();
    }
}
