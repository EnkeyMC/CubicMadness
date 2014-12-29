package cubicmadness.powerup;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class PowerUp extends GameObject{
    
    protected int life;
    protected Effect ef;
    protected Color c1;
    protected Color c2;
    protected int animProgress = 0;
    protected int animSpeed = 10;

    public PowerUp(GamePanel gp, GameState gs) {
        super(gp, gs);
    }
    
    public Effect getEffect(){
        return this.ef;
    }
    
    public void tick(List list){
        super.tick();
        life--;
        if(life <= 0){
            list.add(this);
        }
    }
    
    protected Color interpolatedColor(double interpolation){
        int r,g,b;
        r = (int)Math.round(c1.getRed() * interpolation + c2.getRed() * (1 - interpolation));
        g = (int)Math.round(c1.getGreen() * interpolation + c2.getGreen() * (1 - interpolation));
        b = (int)Math.round(c1.getBlue() * interpolation + c2.getBlue() * (1 - interpolation));
        return new Color(r,g,b);
    }
}
