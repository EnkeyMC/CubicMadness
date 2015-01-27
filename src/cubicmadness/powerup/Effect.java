package cubicmadness.powerup;

import cubicmadness.player.Player;
import java.awt.AlphaComposite;
import java.awt.Color;
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
    private int duration;
    private int level;
    
    private int time;
    
    public Effect (int effect){
        this.effect = effect;
        this.level = 0;
        this.duration = 0;
        this.time = 0;
        
        if(effect == SHIELD){
            level = 3;
        }
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
                int ygap = player.hasEffect(Effect.SHIELD) ? 5 : 3;
                int size = (int) (player.getSize() - 4) / 5;
                
                g.setColor(player.getColor());
                for(int i = 0; i < num; i++){
                    g.fillRect(r.x + (size + gap) * i, r.y - ygap - size, size, size);
                }
                
                break;
            case SHIELD:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                Color c = new Color(63,154,240);
                g.setColor(this.interpolatedColor(Math.abs(Math.sin(Math.toRadians(System.currentTimeMillis() / 3))), c, c.darker())); 
                Rectangle r2 = player.getRect(interpolation);
                r2.grow(level / 1, level / 1);
                g.fill(r2);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                break;
            case PULSE:
                int num2 = player.getEffectCount(PULSE);
                Rectangle r3 = player.getRect(interpolation);
                int gap2 = 1;
                int ygap2 = player.hasEffect(Effect.SHIELD) ? 5 : 3;
                int size2 = (int) (player.getSize() - 4) / 5;
                
                g.setColor(new Color(150,150,150));
                for(int i = 0; i < num2; i++){
                    g.fillRect(r3.x + (size2 + gap2) * i, r3.y + ygap2 + (int)player.getSize(), size2, size2);
                }
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
                duration++;
                if(duration > 20){
                    if(level < 3)
                        level++;
                    duration = 0;
                }
                break;
            case PULSE:
                break;
            case SLOWNESS:
                player.setSpeed(player.getDEFAULT_SPEED() - getLevel()*2);
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
    
    public int getMax(){
        switch (effect) {
            case LIFE:
            case PULSE:
                return 5;
            default:
                return 1;
        }
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    private Color interpolatedColor(double interpolation, Color c1, Color c2){
        int r,g,b;
        r = (int)Math.round(c1.getRed() * interpolation + c2.getRed() * (1 - interpolation));
        g = (int)Math.round(c1.getGreen() * interpolation + c2.getGreen() * (1 - interpolation));
        b = (int)Math.round(c1.getBlue() * interpolation + c2.getBlue() * (1 - interpolation));
        return new Color(r,g,b);
    }
}