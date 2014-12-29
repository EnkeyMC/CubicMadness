/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.enemy;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import cubicmadness.particle.ParticleTrail;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class EnemyFollowing extends EnemyBasic{
    
    private final float gravity = 0.3f;

    public EnemyFollowing(GamePanel gp, GameState gs) {
        super(gp, gs);
        DEFAULT_SPEED = 10;
        this.speed = DEFAULT_SPEED;
        this.color = new Color(115,88,140);
        this.size = 7;
    }    
    
    @Override
    public void tick(){
        super.tick();
        switch(this.direction){
            case "UP":
            case "DOWN":
                velX += Math.copySign(gravity, this.gs.player.getRect().getCenterX() - this.getRect().getCenterX());
                //velY = (float) Math.sqrt(Math.pow(speed, 2) - Math.pow(velX, 2));
                break;
            case "LEFT":
            case "RIGHT":
                velY += Math.copySign(gravity, this.gs.player.getRect().getCenterY() - this.getRect().getCenterY());
                //velX = (float) Math.sqrt(Math.pow(speed, 2) - Math.pow(velY, 2));
        }
        if(this.getVelocity() != 0){
            Random r = new Random();
            gs.particles.add(new ParticleTrail(gp, gs, 20, this.color, 
                new Point2D.Float(Math.round((float) this.predictPosition(5).getCenterX()), (int) Math.round(this.predictPosition(5).getCenterY())), 
                r.nextFloat() * this.size + this.x, 
                r.nextFloat() * this.size + this.y, 
                1, 5,this, 2));
        }
    }
    
    private Rectangle predictPosition(int ticks){
        return new Rectangle((int)(this.x + (velX * ticks)), (int)(this.y + (velY * ticks)), this.getIntSize(), this.getIntSize());
    }
}
