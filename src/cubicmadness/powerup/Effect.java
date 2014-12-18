package cubicmadness.powerup;

import cubicmadness.player.Player;
import java.awt.Graphics2D;

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
        
    }
    
    public void tick(Player player){
        
    }
}