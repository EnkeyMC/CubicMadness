/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
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
    
    private final List<MenuElement> elements = new ArrayList();
    private final List<Particle> particles = new ArrayList();

    public MainMenuState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
        if(!(MouseInput.mouseXY.x == MouseInput.mousePrevXY.x && MouseInput.mouseXY.y == MouseInput.mousePrevXY.y)){
            Random r = new Random();
            ParticleTrail p = new ParticleTrail(gp, this, 10, Color.LIGHT_GRAY, new Point2D.Float(MouseInput.mouseXY.x, MouseInput.mouseXY.y), MouseInput.mouseXY.x + r.nextInt(6) - 3, MouseInput.mouseXY.y + r.nextInt(6) - 3, 4,6,null,3);
            particles.add(p);
            
            MouseInput.mousePrevXY = MouseInput.mouseXY;
        }
        
        if(MouseInput.LMB){
            particles.add(new ParticleCircular(gp, this, 10, Color.LIGHT_GRAY, new Point2D.Float(MouseInput.mouseXY.x, MouseInput.mouseXY.y), 4, 6, null, 3, 5, 10, 0.2f));
            MouseInput.LMB = false;
        }
        
        List<Particle> particlesToRemove = new ArrayList();
        for(Particle p : particles){
            p.tick(particlesToRemove);
        }
        
        for(Particle p : particlesToRemove){
            particles.remove(p);
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_ENTER)){
            this.elements.get(0).actionPerformed();
            KeyInput.pressed.remove(KeyEvent.VK_ENTER);
        }
        
        for(MenuElement e : elements){
            if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                e.setFocused(true);
            }else{
                e.setFocused(false);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        for(MenuElement e : elements){
            e.draw(g, interpolation);
        }
        
        for(Particle p : particles){
            p.draw(g, interpolation);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void restart() {
    }

    @Override
    public void init() {
        MenuButton e;
        try {
            e = new MenuButton(gp, MenuButton.BIG, "Play", this.getClass().getDeclaredMethod("buttonPlayAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(200);
            elements.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonPlayAction(){
        gp.gsm.pushState(gp.gsm.PLAY_STATE);
    }
}
