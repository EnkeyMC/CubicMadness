package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public class Particle extends GameObject{
    
    protected int life;
    protected Point2D.Float center;
    protected int minSize;
    protected int maxSize;
    
    public Particle(GamePanel gp, int life, Color c, Point2D.Float center, float x, float y, int minSize, int maxSize){
        super(gp);
        this.life = life;
        this.color = c;
        this.center = center;
        this.x = x;
        this.y = y;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }
    
    public Particle(GamePanel gp, int life, Color c, Point2D.Float center, float x, float y, float size){
        super(gp);
        this.life = life;
        this.color = c;
        this.center = center;
        this.x = x;
        this.y = y;
        this.size = size;
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
    
    protected Color darker (Color c, int times){
        for(int i = 0; i < times; i++){
            c = c.darker();
        }
        return c;
    }
    
    protected Color brighter (Color c, int times){
        for(int i = 0; i < times; i++){
            c = c.brighter();
        }
        return c;
    }  
}
