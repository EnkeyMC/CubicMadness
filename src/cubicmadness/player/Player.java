package cubicmadness.player;

import cubicmadness.attack.Pulse;
import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
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
    
    public final List<Effect> effects = new ArrayList();
    private final int TICKS_INV = 30;
    
    private boolean invincible = false;
    private int tickInvincible = 0;
    
    private int animProgress = 0;
    private final int animSpeed = 30;
    
    public Player(GamePanel gp, GameState gs){
        super(gp,gs);
        color = new Color(179,61,67);
        size = 24;
        x = gp.getWidth() / 2 - size / 2;
        y = gp.getHeight() / 2 - size / 2;
        DEFAULT_SPEED = 8f;
        this.speed = DEFAULT_SPEED;
    }
    
    @Override
    public void tick(){
        Set<Integer> key = KeyInput.pressed;
        if(key.contains(KeyEvent.VK_UP)) velY = -getSpeed();
        else velY = 0;
        if(key.contains(KeyEvent.VK_DOWN)) velY = getSpeed();
        if(key.contains(KeyEvent.VK_RIGHT)) velX = getSpeed();
        else velX = 0;
        if(key.contains(KeyEvent.VK_LEFT)) velX = -getSpeed();
        
        if(key.contains(KeyEvent.VK_UP) && key.contains(KeyEvent.VK_DOWN)) velY = 0;
        if(key.contains(KeyEvent.VK_RIGHT) && key.contains(KeyEvent.VK_LEFT)) velX = 0;
        
        if(velX != 0 && velY != 0){
            velX = (float)Math.copySign(Math.sqrt(Math.pow(getSpeed(), 2) / 2), velX);
            velY = (float)Math.copySign(Math.sqrt(Math.pow(getSpeed(), 2) / 2), velY);
        }
        
        this.x += velX;
        this.y += velY;
        
        if(this.x < 0) {
            x = 0;
            velX = 0;
        }
        if(this.x > gp.getWidth() - this.size){
            x = gp.getWidth() - this.size;
            velX = 0;
        }
        if(this.y < 0) {
            y = 0;
            velY = 0;
        }
        if(this.y > gp.getHeight() - this.size) {
            y = gp.getHeight() - this.size;
            velY = 0;
        }
        
        if(this.getVelocity() != 0){
            Random r = new Random();
            gs.getObjects().particles.add(new ParticleTrail(gp, gs, 10, this.color, 
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
        
        for(int i = 0; i < effects.size(); i++){
            if(effects.get(i).tick(this)){
                effects.remove(i);
            }
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_SPACE)){
            KeyInput.pressed.remove(KeyEvent.VK_SPACE);
            if(gs.getObjects().pulse == null && this.hasEffect(Effect.PULSE)){
                gs.getObjects().pulse = new Pulse(gp, gs, this.getCenter().x,this.getCenter().y);
                this.effects.remove(this.getFirstEffect(Effect.PULSE));
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        for(Effect e :effects){
            e.draw(this, g, interpolation);
        }
        
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
    
    public int getEffectCount(int effect){
        int i = 0;
        for(Effect e : this.effects){
            if(e.getEffect() == effect) i++;
        }
        return i;
    }
    
    public boolean applyEffect(Effect e){        
        if(this.getEffectCount(e.getEffect()) < e.getMax()){
            return effects.add(e);
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
    
    public boolean decreaseSheild(){
        if(this.hasEffect(Effect.SHIELD)){
            Effect e = this.getFirstEffect(Effect.SHIELD);
            if(e.getLevel() > 0){
                e.setLevel(e.getLevel() - 1);
                return true;
            }else{
                this.effects.remove(e);
                return false;
            }
        }else{
            return false;
        }
    }
    
    public Effect getFirstEffect(int type){
        for(Effect e : this.effects){
            if(e.getEffect() == type)
                return e;
        }
        return null;
    }
    
    public int getShieldLevel(){
        return this.getFirstEffect(Effect.SHIELD) == null ? 0 : this.getFirstEffect(Effect.SHIELD).getLevel();
    }
}
