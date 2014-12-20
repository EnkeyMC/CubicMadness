package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class ParticleCircular extends ParticleTrail{
    
    private final float radius;
    private final Particle[] particles;
    private final float friction;

    public ParticleCircular(GamePanel gp, int life, Color c, Point2D.Float center, int minSize, int maxSize, GameObject o, int colorVariation, float radius, int amount, float friction) {
        super(gp, life, c, center, 0, 0, minSize, maxSize, o, colorVariation);
        this.radius = radius;
        this.friction = friction;
        particles = new Particle[amount];
        
        spawn(colorVariation);
    }

    private void spawn(int colorVariation) {
        float angle = 360 / particles.length;
        Random r = new Random ();
        for(int i = 0; i < particles.length; i++){
            int s;
            if(maxSize > minSize){
                s = r.nextInt(maxSize - minSize) + minSize;
            }else{
                s = minSize;
            }
            
            Particle p = new Particle(this.panel, this.life, 
                    (r.nextInt(2) == 0 ? this.brighter(this.color, r.nextInt(colorVariation)) : this.darker(this.color, r.nextInt(colorVariation))), 
                    this.center, 0, 0, s);
            
            p.setX((float)(radius * Math.cos(Math.toRadians(angle * i))) + p.center.x);
            p.setY((float)(radius * Math.sin(Math.toRadians(angle * i))) + p.center.y);
            
            p.setVelX(p.getX() - p.center.x);
            p.setVelY(p.getY() - p.center.y);
            
            particles[i] = p;
        }
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        g.setComposite(this.makeTransparent(life / 10f > 1f ? 1f : life / 10f));
        g.setColor(color);
        for(Particle p : particles){
            g.fill(p.getRect(interpolation));
        }
        
        g.setComposite(this.makeTransparent(1));
    }
    
    @Override
    public void tick(List list){
        for(Particle p : particles){
            p.setVelX(p.getVelX() - (p.getVelX() * friction));
            p.setVelY(p.getVelY() - (p.getVelY() * friction));
            p.tick();
        }
        
        this.life--;
        if(life <= 0){
            list.add(this);
        }
    }
}
