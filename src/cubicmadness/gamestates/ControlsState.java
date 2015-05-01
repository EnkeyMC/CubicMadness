/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.menuelements.MenuLabel;
import cubicmadness.particle.Particle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class ControlsState extends GameState{
    
    private MenuButton btnUP;
    private MenuButton btnDOWN;
    private MenuButton btnLEFT;
    private MenuButton btnRIGHT;
    private MenuButton btnATTACK;
    
    private MenuButton activeBtn;
    
    private int key = 0;

    public ControlsState(GamePanel gp) {
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
        
        if(key == -1){
            if(!KeyInput.pressed.isEmpty()){
                key = (int) KeyInput.pressed.toArray()[0];
                KeyInput.pressed.remove(key);
                this.activeBtn.actionPerformed();
            }
        }else{
            this.menuInteraction();
        }
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
        
        g.setColor(new Color(200,200,200,50));
        for(int i = 0; i < objects.buttons.size() - 1; i++){
            g.fillRect((int) (gp.size.width * 0.2), 200 + i*50, 300, 40);
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
            objects.elements.add(new MenuLabel(gp, this, gp.size.width /2, 100, "Controls", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, KeyEvent.getKeyText(Config.UP), this.getClass().getDeclaredMethod("buttonUPAction"));
            e.align(MenuButton.ALIGN_RIGHT);
            e.setY(200);
            e.setFocused(true);
            this.btnUP = e;
            objects.buttons.add(e);
            
            objects.elements.add(new MenuLabel(gp, this, (int) (gp.size.width * 0.2) + 10, 227, "Up", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_LEFT));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, KeyEvent.getKeyText(Config.DOWN), this.getClass().getDeclaredMethod("buttonDOWNAction"));
            e.align(MenuButton.ALIGN_RIGHT);
            e.setY(250);
            this.btnDOWN = e;
            objects.buttons.add(e);
            
            objects.elements.add(new MenuLabel(gp, this, (int) (gp.size.width * 0.2) + 10, 277, "Down", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_LEFT));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, KeyEvent.getKeyText(Config.LEFT), this.getClass().getDeclaredMethod("buttonLEFTAction"));
            e.align(MenuButton.ALIGN_RIGHT);
            e.setY(300);
            this.btnLEFT = e;
            objects.buttons.add(e);
            
            objects.elements.add(new MenuLabel(gp, this, (int) (gp.size.width * 0.2) + 10, 327, "Left", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_LEFT));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, KeyEvent.getKeyText(Config.RIGHT), this.getClass().getDeclaredMethod("buttonRIGHTAction"));
            e.align(MenuButton.ALIGN_RIGHT);
            e.setY(350);
            this.btnRIGHT = e;
            objects.buttons.add(e);
            
            objects.elements.add(new MenuLabel(gp, this, (int) (gp.size.width * 0.2) + 10, 377, "Right", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_LEFT));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, KeyEvent.getKeyText(Config.ATTACK), this.getClass().getDeclaredMethod("buttonATTACKAction"));
            e.align(MenuButton.ALIGN_RIGHT);
            e.setY(400);
            this.btnATTACK = e;
            objects.buttons.add(e);
            
            objects.elements.add(new MenuLabel(gp, this, (int) (gp.size.width * 0.2) + 10, 427, "Pulse", MenuLabel.TYPE_LABEL, MenuLabel.ALIGN_LEFT));
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Back", this.getClass().getDeclaredMethod("buttonBackAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(490);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonBackAction(){
        Config.saveConfig();
        gp.gsm.popTransition(TransitionState.BLACKFADE);
    }
    
    public void buttonUPAction(){
        if(key == 0){
            this.key = -1;
            this.activeBtn = this.btnUP;
            this.btnUP.setText("?");
        }else if(key == -1){
        }else{
            Config.UP = key;
            btnUP.setText(KeyEvent.getKeyText(key));
            key = 0;
        }
    }
    
    public void buttonDOWNAction(){
        if(key == 0){
            this.key = -1;
            this.activeBtn = this.btnDOWN;
            this.btnDOWN.setText("?");
        }else if(key == -1){
        }else{
            Config.DOWN = key;
            btnDOWN.setText(KeyEvent.getKeyText(key));
            key = 0;
        }
    }
    
    public void buttonLEFTAction(){
        if(key == 0){
            this.key = -1;
            this.activeBtn = this.btnLEFT;
            this.btnLEFT.setText("?");
        }else if(key == -1){
        }else{
            Config.LEFT = key;
            btnLEFT.setText(KeyEvent.getKeyText(key));
            key = 0;
        }
    }
    
    public void buttonRIGHTAction(){
        if(key == 0){
            this.key = -1;
            this.activeBtn = this.btnRIGHT;
            this.btnRIGHT.setText("?");
        }else if(key == -1){
        }else{
            Config.RIGHT = key;
            btnRIGHT.setText(KeyEvent.getKeyText(key));
            key = 0;
        }
    }
    
    public void buttonATTACKAction(){
        if(key == 0){
            this.key = -1;
            this.activeBtn = this.btnATTACK;
            this.btnATTACK.setText("?");
        }else if(key == -1){
        }else{
            Config.ATTACK = key;
            btnATTACK.setText(KeyEvent.getKeyText(key));
            key = 0;
        }
    }

    @Override
    public void init(Object o) {
        init();
    }
    
}
