package cubicmadness.powerup;

import cubicmadness.player.Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Martin
 */
public class Effect {
    
    // EFFECT TYPES
    public static final int LIFE = 1;
    public static final int SHIELD = 2;
    public static final int PULSE = 3;
    public static final int SLOWNESS = 4;
    
    private final int effect;
    
    public Effect (int effect){
        this.effect = effect;
    }
    
    public void draw(Player player, Graphics2D g, double interpolation){
        switch(this.effect){
            case LIFE:
                int num = player.getEffectCount(LIFE);
                Rectangle r = player.getRect(interpolation);
                int gap = 1;
                int size = (int) (player.getSize() - 4) / 5;
                
                for(int i = 0; i < num; i++){
                    g.fillRect(r.x + (size + gap) * i, r.y - 3 - size, size, size);
                }
                
                break;
            case SHIELD:
                
                break;
            case PULSE:
                
                break;
            case SLOWNESS:
                
                break;
            default:
                System.out.println("Unknown effect!");
        }
    }
    
    public void tick(Player player){
        
    }
    
    public int getEffect(){
        return this.effect;
    }
}