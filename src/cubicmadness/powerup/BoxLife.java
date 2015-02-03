package cubicmadness.powerup;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.Utils;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public class BoxLife extends PowerUp {

    public BoxLife(GamePanel gp, GameState gs, int life, float x, float y) {
        super(gp, gs);
        this.life = life;
        this.x = x;
        this.y = y;
        this.ef = new Effect(Effect.LIFE);
        this.size = 20;
        this.c1 = new Color(219,72,75);
        this.c2 = c1.darker();
    }
    
    @Override
    public void tick(List list){
        super.tick(list);
        animProgress += animSpeed;
        if(animProgress > 180)
            animProgress -= 180;
    }
    
    @Override
    public void tick(){
        animProgress += animSpeed;
        if(animProgress > 180)
            animProgress -= 180;
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        g.setColor(Utils.interpolatedColor(Math.sin(Math.toRadians(this.animProgress)), c1, c2));
        g.fill(this.getRect(interpolation)); 
        
        float s = this.size / 4f;
        g.setColor(new Color(75,75,75));
        g.fillRect((int)(this.x - s/2), (int)(this.y - s/2), (int)s, (int)s);
        g.fillRect((int)(this.x + this.size - s/2), (int)(this.y - s/2), (int)s, (int)s);
        g.fillRect((int)(this.x + this.size - s/2), (int)(this.y + this.size - s/2), (int)s, (int)s);
        g.fillRect((int)(this.x - s/2), (int)(this.y + this.size - s/2), (int)s, (int)s);
    }
    
    @Override
    public Color getColor(){
        return this.c1;
    }
}
