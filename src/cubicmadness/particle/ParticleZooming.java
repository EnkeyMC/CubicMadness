/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.particle;

import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public class ParticleZooming extends Particle{
    
    private final float zoomSpeed;
    private float zoom = 0;

    public ParticleZooming(GamePanel gp, int life, Color c, Point2D.Float center, float x, float y, float size, float zoomSpeed) {
        super(gp, life, c, center, x, y, size);
        this.zoomSpeed = zoomSpeed;
    }
    
    @Override
    public void tick(List list){
        super.tick(list);
        zoom += zoomSpeed;
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        g.setComposite(this.makeTransparent(life / 10f > 1f ? 1f : life / 10f));
        g.setColor(color);
        Rectangle r = this.getRect(interpolation);
        r.grow(Math.round(zoom), Math.round(zoom));
        g.fill(r);
        g.setComposite(this.makeTransparent(1));
    }
}
