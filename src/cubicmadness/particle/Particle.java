package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class Particle extends GameObject{
    
    protected int life;
    protected Point center;
    protected Dimension minSize;
    protected Dimension maxSize;
    
    public Particle(GamePanel gp, int life, Color c, Point center, float x, float y, Dimension minSize, Dimension maxSize){
        super(gp);
        this.life = life;
        this.color = c;
        this.center = center;
        this.x = x;
        this.y = y;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }
    
    public void tick(List list){
        super.tick();
        this.life--;
        if(life <= 0){
            list.add(this);
        }
    }
    
    @Override
    public Rectangle getCollisionBox(){
        return new Rectangle(0,0,0,0);
    }
}
