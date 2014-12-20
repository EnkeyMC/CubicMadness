package cubicmadness.player;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.input.KeyInput;
import cubicmadness.particle.ParticleTrail;
import cubicmadness.powerup.Effect;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Martin
 */
public class Player extends GameObject{
    
    private final float speed = 8f;    
    public final List<Effect> effects = new ArrayList();
    private final int TICKS_INV = 30;
    
    private boolean invincible = false;
    private int tickInvincible = 0;
    
    private int animProgress = 0;
    private final int animSpeed = 30;
    
    public Player(GamePanel gp){
        super(gp);
        color = new Color(179,61,67);
        size = 20;
        x = panel.getWidth() / 2 - size / 2;
        y = panel.getHeight() / 2 - size / 2;
    }
    
    @Override
    public void tick(){
        Set<Integer> key = KeyInput.pressed;
        if(key.contains(KeyEvent.VK_UP)) velY = -speed;
        else velY = 0;
        if(key.contains(KeyEvent.VK_DOWN)) velY = speed;
        if(key.contains(KeyEvent.VK_RIGHT)) velX = speed;
        else velX = 0;
        if(key.contains(KeyEvent.VK_LEFT)) velX = -speed;
        
        if(key.contains(KeyEvent.VK_UP) && key.contains(KeyEvent.VK_DOWN)) velY = 0;
        if(key.contains(KeyEvent.VK_RIGHT) && key.contains(KeyEvent.VK_LEFT)) velX = 0;
        
        if(velX != 0 && velY != 0){
            velX = (float)Math.copySign(Math.sqrt(Math.pow(speed, 2) / 2), velX);
            velY = (float)Math.copySign(Math.sqrt(Math.pow(speed, 2) / 2), velY);
        }
        
        this.x += velX;
        this.y += velY;
        
        if(this.x < 0) x = 0;
        if(this.x > panel.getWidth() - this.size) x = panel.getWidth() - this.size;
        if(this.y < 0) y = 0;
        if(this.y > panel.getHeight() - this.size) y = panel.getHeight() - this.size;
        
        if(this.getSpeed() != 0){
            Random r = new Random();
            panel.objects.particles.add(new ParticleTrail(panel, 10, this.color, 
                new Point2D.Float((float) this.predictPosition(5).getCenterX(), (float) this.predictPosition(5).getCenterY()), 
                r.nextFloat() * this.size + this.x, 
                r.nextFloat() * this.size + this.y, 
                3, 7,this, 2));
        }
        
        if(this.invincible){
            this.animProgress += this.animSpeed;
            this.tickInvincible++;
            
            if(this.animProgress > 180){
                this.animProgress -= 180;
            }
            
            if(this.tickInvincible >= this.TICKS_INV){
                this.invincible = false;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        if(this.invincible){
            g.setComposite(this.makeTransparent((float)Math.sin(Math.toRadians(this.animProgress))));
            super.draw(g, interpolation);
            g.setComposite(this.makeTransparent(1f));
        }else{
            super.draw(g, interpolation);
        }
                
        
        Set<Integer> key = KeyInput.pressed;
        if(!key.contains(KeyEvent.VK_UP) && velY < 0){
            this.y += velY * interpolation;
            velY = 0;
        }
        if(!key.contains(KeyEvent.VK_DOWN) && velY > 0){
            this.y += velY * interpolation;
            velY = 0;
        }
        if(!key.contains(KeyEvent.VK_LEFT) && velX < 0){
            this.x += velX * interpolation;
            velX = 0;
        }
        if(!key.contains(KeyEvent.VK_RIGHT) && velX > 0){
            this.x += velX * interpolation;
            velX = 0;
        }
    }
    
    private Rectangle predictPosition(int ticks){
        return new Rectangle((int)(this.x + (velX * ticks)), (int)(this.y + (velY * ticks)), this.getIntSize(), this.getIntSize());
    }
    
    public boolean hasEffect(int e){
        for(Effect ef : effects){
            if(e == ef.getEffect()){
                return true;
            }
        }
        return false;
    }
    
    public boolean removeEffect(int ef){
        Effect tmp = null;
        for(Effect e : this.effects){
            if(e.getEffect() == ef){
                tmp = e;
            }
        }
        if(tmp != null){
            return this.effects.remove(tmp);
        }else{
            return false;
        }
    }
    
    public void makeInvincible(){
        this.invincible = true;
        this.animProgress = 0;
        this.tickInvincible = 0;
    }
    
    public boolean isInvincible(){
        return this.invincible;
    }
}
