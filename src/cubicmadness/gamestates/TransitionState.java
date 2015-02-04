package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GamePanel;
import cubicmadness.particle.Particle;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class TransitionState extends GameState{
    
    // Types
    public static final byte BLACKFADE = 0;
    
    // Global
    private final GameState prev;
    private final GameState next;
    private final byte type;
    private int timer;
    private final int halftime;
    private float alpha;
    
    public TransitionState(GamePanel gp, GameState prev, GameState next, byte type) {
        super(gp);
        this.prev = prev;
        this.next = next;
        this.type = type;
        this.timer = 0;
        this.alpha = 0;
        
        if(type == TransitionState.BLACKFADE){
            this.halftime = 10;
        }else{
            this.halftime = 30;
        }          
    }

    @Override
    public void tick() {
        List<Particle> particlesToRemove = new ArrayList();
        for(Particle p : prev.getObjects().particles){
            p.tick(particlesToRemove);
        }
        
        for(Particle p : particlesToRemove){
            prev.getObjects().particles.remove(p);
        }
        
        if(!Config.transitions){
            gp.gsm.pushState(next);
        }  
        
        
        if(timer < halftime){
            alpha += 1f / halftime;
        }else{
            alpha -= 1f / halftime;
        }
        timer++;
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        if(this.type == TransitionState.BLACKFADE){
            if(alpha < 0){
                gp.gsm.popCurrentState();
                gp.gsm.resume(next);
                return;
            }
            if(timer < halftime){
                this.prev.draw(g, 0);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
                g.setColor(Color.black);
                g.fillRect(0, 0, gp.size.width, gp.size.height);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
            }else if(timer == halftime){
                gp.gsm.prepareState(next);
                g.setColor(Color.black);
                g.fillRect(0, 0, gp.size.width, gp.size.height);
            }else{
                this.next.draw(g, 0);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
                g.setColor(Color.black);
                g.fillRect(0, 0, gp.size.width, gp.size.height);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
            }
        }
    }

    @Override
    public void restart() {
    }

    @Override
    public void init() {
    }

    @Override
    public void init(Object o) {
    }
}
