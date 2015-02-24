/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.HttpRequester;
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
        
        String scores = HttpRequester.getTopTen();
        
        if(scores.equals("ERROR")){
            objects.elements.add(new MenuLabel(gp, this, gp.size.width / 2, 400, "ERROR", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));   
        }else{
        
            String[][] table = new String[10][3];
            
            String[] records = scores.split("\n");
            
            for (int i = 0; i < table.length; i++) {
                String[] data;
                if(i >= records.length){
                    data = new String[]{"", ""};
                }else{
                    data = records[i].split(";");
                }
                
                table[i][0] = String.valueOf(i + 1);
                table[i][1] = data[0];
                table[i][2] = data[1];
            }
            
            objects.elements.add((new MenuTable(gp, this, (gp.size.width / 2) - this.TABLEWIDTH /2, 120, this.TABLEWIDTH, this.TABLEHEIGHT))
                    .setHeader(new MenuTableRow(new String[]{"Rank", "Nickname", "Score"}).setAligns(new Byte[]{MenuTableRow.ALIGN_CENTER, MenuTableRow.ALIGN_CENTER, MenuTableRow.ALIGN_CENTER}))
                    .appendRow(new MenuTableRow(table[0]).setAligns(new Byte[]{MenuTableRow.ALIGN_RIGHT, MenuTableRow.ALIGN_LEFT, MenuTableRow.ALIGN_RIGHT}))
                    .appendRow(new MenuTableRow(table[1]))
                    .appendRow(new MenuTableRow(table[2]))
                    .appendRow(new MenuTableRow(table[3]))
                    .appendRow(new MenuTableRow(table[4]))
                    .appendRow(new MenuTableRow(table[5]))
                    .appendRow(new MenuTableRow(table[6]))
                    .appendRow(new MenuTableRow(table[7]))
                    .appendRow(new MenuTableRow(table[8]))
                    .appendRow(new MenuTableRow(table[9]))
                    .setCellMargine(2)
                    .setPadding(5)
                    .setColumnWidth(new Float[]{0.2f, 0.5f, 0.3f})
                    .setBgrColor(new Color(200,200,200,50))
            );
        }
    }

    @Override
    public void init(Object o) {
        init();
    }
    
    public void buttonBackAction(){
        gp.gsm.popTransition(TransitionState.BLACKFADE);
    }
    
}
