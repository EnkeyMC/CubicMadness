package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author Martin
 */
public abstract class Particle extends GameObject{
    
    protected int life;
    protected Point center;
    
    public Particle(GamePanel gp, int life, Color c, Point center, float x, float y, Dimension minSize, Dimension maxSize){
        super(gp);
        this.life = life;
        this.color = c;
        this.center = center;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void tick(){
        super.tick();
        this.life--;
        if(life <= 0){
            // remove
        }
    }
}
