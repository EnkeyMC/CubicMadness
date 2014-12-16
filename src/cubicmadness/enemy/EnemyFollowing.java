/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.enemy;

import cubicmadness.bin.GamePanel;
import cubicmadness.particle.ParticleTrail;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class EnemyFollowing extends EnemyBasic{
    
    private final float gravity = 0.3f;

    public EnemyFollowing(GamePanel panel) {
        super(panel);
        this.speed = 10f;
        this.color = new Color(115,88,140);
        this.size = new Dimension(7,7);
    }    
    
    @Override
    public void tick(){
        super.tick();
        switch(this.direction){
            case "UP":
            case "DOWN":
                velX += Math.copySign(gravity, this.panel.objects.player.getRect().getCenterX() - this.getRect().getCenterX());
                //velY = (float) Math.sqrt(Math.pow(speed, 2) - Math.pow(velX, 2));
                break;
            case "LEFT":
            case "RIGHT":
                velY += Math.copySign(gravity, this.panel.objects.player.getRect().getCenterY() - this.getRect().getCenterY());
                //velX = (float) Math.sqrt(Math.pow(speed, 2) - Math.pow(velY, 2));
        }
        if(this.getSpeed() != 0){
            Random r = new Random();
            panel.objects.particles.add(new ParticleTrail(panel, 20, this.color, 
                new Point(Math.round((float) this.predictPosition(5).getCenterX()), (int) Math.round(this.predictPosition(5).getCenterY())), 
                r.nextFloat() * this.size.width + this.x, 
                r.nextFloat() * this.size.height + this.y, 
                new Dimension(1,1), new Dimension(5,5),this, 2));
        }
    }
    
    private Rectangle predictPosition(int ticks){
        return new Rectangle((int)(this.x + (velX * ticks)), (int)(this.y + (velY * ticks)), this.size.width, this.size.height);
    }
}