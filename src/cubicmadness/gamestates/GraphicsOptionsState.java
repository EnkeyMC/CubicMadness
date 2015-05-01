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
public class GraphicsOptionsState extends GameState {
    
    private MenuButton transition;
    private MenuButton antialiasing;
    private MenuButton rendering;
    private MenuButton fullscreen;

    public GraphicsOptionsState(GamePanel gp) {
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
    }

    @Override
    public void init() {
        this.objects = new ObjectHandler();
        
        MenuButton e;
        try {
            objects.elements.add(new MenuLabel(gp, this, gp.size.width /2, 100, "Graphics options", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, Config.transitions ? "ON" : "OFF", this.getClass().getDeclaredMethod("buttonTransitionAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(150);
            e.setFocused(true);
            this.transition = e;
            objects.elements.add(new MenuLabel(gp, this, e, "Toggle Transitions", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_CENTER));
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, Config.antialiasing ? "ON" : "OFF", this.getClass().getDeclaredMethod("buttonAntialiasingAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(230);
            this.antialiasing = e;
            objects.elements.add(new MenuLabel(gp, this, e, "Toggle Atialiasing", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_CENTER));
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, Config.rendering ? "Quality" : "Speed", this.getClass().getDeclaredMethod("buttonRenderingAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(310);
            this.rendering = e;
            objects.elements.add(new MenuLabel(gp, this, e, "Rendering", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_CENTER));
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, Config.fullscreen ? "ON" : "OFF", this.getClass().getDeclaredMethod("buttonFullscreenAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(390);
            this.fullscreen = e;
            objects.elements.add(new MenuLabel(gp, this, e, "Toggle fullscreen", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_CENTER));
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Back", this.getClass().getDeclaredMethod("buttonBackAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(480);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonTransitionAction(){
        Config.transitions = !Config.transitions;
        this.transition.setText(Config.transitions ? "ON" : "OFF");
    }
    
    public void buttonAntialiasingAction(){
        Config.antialiasing = !Config.antialiasing;
        Config.antialiasingText = Config.antialiasing;
        this.antialiasing.setText(Config.antialiasing ? "ON" : "OFF");
    }
    
    public void buttonRenderingAction(){
        Config.rendering = !Config.rendering;
        this.rendering.setText(Config.rendering ? "Quality" : "Speed");
    }
    
    public void buttonFullscreenAction(){
        Config.fullscreen = !Config.fullscreen;
        this.fullscreen.setText(Config.fullscreen ? "ON" : "OFF");
        gp.makeFullscreen(Config.fullscreen);
    }
    
    public void buttonBackAction(){
        Config.saveConfig();
        gp.gsm.popTransition(TransitionState.BLACKFADE);
    }

    @Override
    public void init(Object o) {
        init();
    }

}
