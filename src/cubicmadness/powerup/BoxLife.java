package cubicmadness.powerup;

import cubicmadness.bin.GamePanel;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class BoxLife extends PowerUp {

    public BoxLife(GamePanel panel, int life, float x, float y) {
        super(panel);
        this.life = life;
        this.x = x;
        this.y = y;
        this.ef = new Effect(Effect.LIFE);
        this.size = 20;
    }
    
    @Override
    public void tick(){
        super.tick();
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        
    }
}
