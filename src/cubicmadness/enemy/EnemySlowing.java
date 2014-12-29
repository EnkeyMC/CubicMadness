/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.enemy;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import cubicmadness.particle.ParticleTrail;
import cubicmadness.particle.ParticleZooming;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class EnemySlowing extends EnemyBasic{

    public EnemySlowing(GamePanel gp, GameState gs) {
        super(gp, gs);
        DEFAULT_SPEED = 12f;
        this.speed = DEFAULT_SPEED;
        this.color = new Color(40,40,40);
        this.size = 20;
    }
    
    @Override
    public void tick(){
        super.tick();
        Random r = new Random();
        gs.particles.add(new ParticleZooming(gp, gs, 20, this.getColor(), this.getCenter(), this.getX(), this.getY(), this.getSize(), -0.2f));
        gs.particles.add(new ParticleTrail(gp, gs, 20, this.color, 
                new Point2D.Float((float) this.predictPosition(5).getCenterX(), (float) this.predictPosition(5).getCenterY()), 
                r.nextFloat() * this.size + this.x, 
                r.nextFloat() * this.size + this.y, 
                3, 5,this, 2));
    }
    
    private Rectangle predictPosition(int ticks){
        return new Rectangle((int)(this.x + (velX * ticks)), (int)(this.y + (velY * ticks)), this.getIntSize(), this.getIntSize());
    }
}
