/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.attack;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import cubicmadness.particle.ParticleFading;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Martin
 */
public class Pulse extends GameObject {
    
    private int life;
    
    public Pulse(GamePanel gp, GameState gs, float centerX, float centerY) {
        super(gp, gs);
        this.size = 20;
        this.x = centerX - (size / 2f);
        this.y = centerY - (size / 2f);
        this.color = new Color(150, 150, 150);
        this.speed = 35;
        this.life = 7;
    }
    
    private void grow(float amount) {
        this.x -= amount / 2f;
        this.y -= amount / 2f;
        this.size += amount;
    }
    
    @Override
    public Rectangle getRect(double interpolation) {
        int x = (int) (this.x - interpolation * (speed / 2f));
        int y = (int) (this.y - interpolation * (speed / 2f));
        int size = (int) (this.size + interpolation * speed);
        
        return new Rectangle(x, y, size, size);
    }
    
    @Override
    public void tick() {
        life--;
        this.grow(speed);
        gs.getObjects().particles.add(new ParticleFading(gp, gs, 4, new Color(150, 150, 150), this.getRect(), false));
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation) {
        g.setColor(color);
        g.setStroke(new BasicStroke(3f));
        g.draw(this.getRect(interpolation));
    }
    
    public int getLife() {
        return life;
    }
    
}
