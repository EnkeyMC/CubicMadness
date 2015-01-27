/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.particle;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

/**
 *
 * @author Martin
 */
public class ParticleFading extends Particle{
    
    private final Shape shape;
    private final boolean fill;

    public ParticleFading(GamePanel gp, GameState gs, int life, Color c, Shape shape, boolean fill) {
        super(gp, gs, life, c, null, shape.getBounds().x, shape.getBounds().y, shape.getBounds().width);
        this.shape = shape;
        this.fill = fill;
    }
    
    @Override
    public void tick(List list){
        life--;
        if(life <= 0){
            list.add(this);
        }
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        g.setComposite(this.makeTransparent(life / 10f > 1f ? 1f : life / 10f));
        g.setColor(color);
        if (fill) {
            g.fill(shape);
        }else{
            g.draw(shape);
        }
        g.setComposite(this.makeTransparent(1));
    }
}
