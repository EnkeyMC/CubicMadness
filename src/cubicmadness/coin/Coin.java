package cubicmadness.coin;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
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
    protected final int maxSize = 15;
    
    public Coin (GamePanel gp, GameState gs){
        super(gp, gs);
        this.color = new Color(228,192,48);
        size = 10;
        points = 10;
        spawn();
    }
    
    private void spawn(){
        Random r = new Random();
        this.x = r.nextInt(gp.size.width - 200) + 100;
        this.y = r.nextInt(gp.size.height - 200) + 100;
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        Rectangle r = getRect(interpolation);
        int grow = (int) Math.round((maxSize - size) * Math.sin(Math.toRadians(animProgress)));
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
        int dif = (int)this.maxSize - this.getIntSize();
        return new Rectangle((int)this.x - dif, (int)this.y - dif, this.maxSize + dif, this.maxSize + dif);
    }
}
