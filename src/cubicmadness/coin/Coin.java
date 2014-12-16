package cubicmadness.coin;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class Coin extends GameObject{
    protected float animProgress = 0;
    protected int points;
    
    protected final int animSpeed = 10;
    protected final Dimension maxSize = new Dimension(15,15);
    
    public Coin (GamePanel gp){
        super(gp);
        this.color = new Color(228,192,48);
        size = new Dimension(10,10);
        points = 10;
        spawn();
    }
    
    private void spawn(){
        Random r = new Random();
        this.x = r.nextInt(panel.getWidth() - 200) + 100;
        this.y = r.nextInt(panel.getHeight() - 200) + 100;
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        Rectangle r = getRect(interpolation);
        int grow = (int) Math.round((maxSize.width - size.width) * Math.sin(Math.toRadians(animProgress)));
        r.grow(grow,grow);
        g.setColor(color);
        g.fill(r);
    }
    
    @Override
    public void tick(){
        animProgress += animSpeed;
        if(animProgress > 180)
            animProgress -= 180;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return this.points;
    }
    
    @Override
    public Rectangle getCollisionBox(){
        return new Rectangle((int)this.x - (this.maxSize.width - this.size.width), (int)this.y - (this.maxSize.height - this.size.height), this.maxSize.width + (this.maxSize.height - this.size.height), this.maxSize.height + (this.maxSize.height - this.size.height));
    }
}
