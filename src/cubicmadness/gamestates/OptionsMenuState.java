/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

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
public class OptionsMenuState extends GameState{

    public OptionsMenuState(GamePanel gp) {
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
        
        for(MenuElement e : objects.buttons){
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
            objects.elements.add(new MenuLabel(gp, this, gp.size.width /2, 100, "Options", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Controls", this.getClass().getDeclaredMethod("buttonControlsAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(200);
            e.setFocused(true);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Graphics", this.getClass().getDeclaredMethod("buttonGraphicsAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(250);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Back", this.getClass().getDeclaredMethod("buttonBackAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(400);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonBackAction(){
        gp.gsm.transition(this, gp.gsm.MAINMENU_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonControlsAction(){
    }
    
    public void buttonGraphicsAction(){
        gp.gsm.transition(this, gp.gsm.GRAPHICSOPTIONS_STATE, TransitionState.BLACKFADE);
    }

    @Override
    public void init(Object o) {
        init();
    }
    
}
