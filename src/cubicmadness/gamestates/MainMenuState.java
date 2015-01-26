/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.particle.Particle;
import cubicmadness.particle.ParticleCircular;
import cubicmadness.particle.ParticleTrail;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        if(!(MouseInput.mouseXY.x == MouseInput.mousePrevXY.x && MouseInput.mouseXY.y == MouseInput.mousePrevXY.y)){
            Random r = new Random();
            ParticleTrail p = new ParticleTrail(gp, this, 10, Color.LIGHT_GRAY, new Point2D.Float(MouseInput.mouseXY.x, MouseInput.mouseXY.y), MouseInput.mouseXY.x + r.nextInt(6) - 3, MouseInput.mouseXY.y + r.nextInt(6) - 3, 4,6,null,3);
            objects.particles.add(p);
            
            MouseInput.mousePrevXY = MouseInput.mouseXY;
        }
        
        if(MouseInput.LMB){
            objects.particles.add(new ParticleCircular(gp, this, 10, Color.LIGHT_GRAY, new Point2D.Float(MouseInput.mouseXY.x, MouseInput.mouseXY.y), 4, 6, null, 3, 5, 10, 0.2f));
        }
        
        List<Particle> particlesToRemove = new ArrayList();
        for(Particle p : objects.particles){
            p.tick(particlesToRemove);
        }
        
        for(Particle p : particlesToRemove){
            objects.particles.remove(p);
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_UP)){
            int i = 0;
            for(MenuElement e : objects.elements){
                if(e.isFocused()){
                    i = objects.elements.indexOf(e);
                    e.setFocused(false);
                }
            }
            i = (int) GamePanel.cycle(i - 1, 0, objects.elements.size()-1);
            objects.elements.get(i).setFocused(true);
            KeyInput.pressed.remove(KeyEvent.VK_UP);
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_DOWN)){
            int i = 0;
            for(MenuElement e : objects.elements){
                if(e.isFocused()){
                    i = objects.elements.indexOf(e);
                    e.setFocused(false);
                }
            }
            i = (int) GamePanel.cycle(i + 1, 0, objects.elements.size()-1);
            objects.elements.get(i).setFocused(true);
            KeyInput.pressed.remove(KeyEvent.VK_DOWN);
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_ENTER)){
            KeyInput.pressed.remove(KeyEvent.VK_ENTER);
            for(MenuElement e : objects.elements){
                if(e.isFocused()){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        if(MouseInput.LMB){
            MouseInput.LMB = false;
            for(MenuElement e : objects.elements){
                if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        for(MenuElement e : objects.elements){
            if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                this.unfocusAllElements();
                e.setFocused(true);
            }
        }
    }
    
    private void unfocusAllElements(){
        for(MenuElement e : objects.elements){
            e.setFocused(false);
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        for(MenuElement e : objects.elements){
            e.draw(g, interpolation);
        }
        
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
            e.setY(200);
            e.setFocused(true);
            objects.elements.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Exit", this.getClass().getDeclaredMethod("buttonExitAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(300);
            objects.elements.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonPlayAction(){
        gp.gsm.transition(this, gp.gsm.PLAY_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonExitAction(){
        System.exit(0);
    }

    @Override
    public void init(Object o) {
        init();
    }
}
