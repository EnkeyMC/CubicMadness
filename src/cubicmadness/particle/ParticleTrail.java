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

    public ParticleTrail(GamePanel gp, int life, Color c, Point center, float x, float y, Dimension minSize, Dimension maxSize, GameObject o, int colorVariation) {
        super(gp, life, c, center, x, y, minSize, maxSize);
        this.o = o;
        
        Random r = new Random();
        int rsize = r.nextInt(maxSize.width - minSize.width) + minSize.width;
        this.size = new Dimension(rsize, rsize);
        this.velX = (this.x - center.x) / 20;
        this.velY = (this.y - center.y) / 20;
        
        this.color = r.nextInt(2) == 0 ? this.brighter(c, r.nextInt(colorVariation)) : this.darker(c, r.nextInt(colorVariation));
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
    
    private Color darker (Color c, int times){
        for(int i = 0; i < times; i++){
            c = c.darker();
        }
        return c;
    }
    
    private Color brighter (Color c, int times){
        for(int i = 0; i < times; i++){
            c = c.brighter();
        }
        return c;
    }
}
