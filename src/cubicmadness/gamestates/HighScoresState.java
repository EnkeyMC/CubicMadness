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
import cubicmadness.menuelements.MenuTable;
import cubicmadness.menuelements.MenuTableRow;
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
public class HighScoresState extends GameState{
    
    private final int TABLEWIDTH = 300, TABLEHEIGHT = 350;

    public HighScoresState(GamePanel gp) {
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
        /*
        g.setColor(new Color(200,200,200,50));
        g.fillRect((gp.size.width / 2) - this.TABLEWIDTH /2, 120, this.TABLEWIDTH, this.TABLEHEIGHT);
        */
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
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Back", this.getClass().getDeclaredMethod("buttonBackAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(480);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width / 2, 100, "High Scores", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));      
        
        objects.elements.add((new MenuTable(gp, this, (gp.size.width / 2) - this.TABLEWIDTH /2, 120, this.TABLEWIDTH, this.TABLEHEIGHT))
                .setHeader(new MenuTableRow(new String[]{"Rank", "Nickname", "Score"}))
                .appendRow(new MenuTableRow(new String[]{"1", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"2", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"3", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"4", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"5", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"5", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"5", "Meeeeee", "1684"}))
                .appendRow(new MenuTableRow(new String[]{"5", "Meeeeee", "1684"}))
                .setCellMargine(2)
                .setPadding(5)
                .setColumnWidth(new Float[]{0.2f, 0.5f, 0.3f})
        );
    }

    @Override
    public void init(Object o) {
        init();
    }
    
    public void buttonBackAction(){
        gp.gsm.popTransition(TransitionState.BLACKFADE);
    }
    
}
