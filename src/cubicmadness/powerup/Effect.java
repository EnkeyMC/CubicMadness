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
    private final int level;
    private final int duration;
    
    private int time;
    
    public Effect (int effect){
        this.effect = effect;
        this.level = 0;
        this.duration = 0;
        this.time = 0;
    }
    
    public Effect (int effect, int level, int duration){
        this.effect = effect;
        this.level = level;
        this.duration = duration;
        this.time = 0;
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
    
    public boolean tick(Player player){
        switch(this.effect){
            case LIFE:
                break;
            case SHIELD:
                
                break;
            case PULSE:
                
                break;
            case SLOWNESS:
                player.setSpeed(player.getDEFAULT_SPEED() - level*2);
                time++;
                
                if(time >= duration){
                    player.setSpeed(player.getDEFAULT_SPEED());
                    return true;
                }
                break;
            default:
                System.out.println("Unknown effect!");
                return true;
        }
        return false;
    }
    
    public int getEffect(){
        return this.effect;
    }
}