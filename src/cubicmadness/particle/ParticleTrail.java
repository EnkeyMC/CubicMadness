package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class ParticleTrail extends Particle{
    
    private final GameObject o;

    public ParticleTrail(GamePanel gp, int life, Color c, Point center, float x, float y, Dimension minSize, Dimension maxSize, GameObject o) {
        super(gp, life, c, center, x, y, minSize, maxSize);
        this.o = o;
        
        Random r = new Random();
        int rsize = r.nextInt(maxSize.width - minSize.width) + minSize.width;
        this.size = new Dimension(rsize, rsize);
        this.velX = (float)Math.cos(Math.atan((this.y - center.y) / (this.x - center.x))) * (o.getSpeed() / 2);
        this.velY = (float)Math.sin(Math.atan((this.y - center.y) / (this.x - center.x))) * (o.getSpeed() / 2);
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        g.setComposite(this.makeTransparent(life / 10f > 1f ? 1f : life / 10f));
        g.setColor(color);
        g.fill(getRect(interpolation));
        g.setComposite(this.makeTransparent(1));
    }
    
    private AlphaComposite makeTransparent(float alpha){
        return (AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
}
