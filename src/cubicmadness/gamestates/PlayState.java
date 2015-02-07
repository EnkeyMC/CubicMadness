/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GameHistory;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.coin.Coin;
import cubicmadness.coin.CoinFollower;
import cubicmadness.coin.CoinSlower;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
import cubicmadness.enemy.EnemySlowing;
import cubicmadness.input.KeyInput;
import cubicmadness.particle.Particle;
import cubicmadness.particle.ParticleCircular;
import cubicmadness.particle.ParticleText;
import cubicmadness.particle.ParticleZooming;
import cubicmadness.player.Player;
import cubicmadness.powerup.BoxLife;
import cubicmadness.powerup.BoxPulse;
import cubicmadness.powerup.BoxShield;
import cubicmadness.powerup.Effect;
import cubicmadness.powerup.PowerUp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class PlayState extends GameState {
    
    private final int TOP_PANEL_HEIGHT = 50;
    private final int TOP_PANEL_WIDTH = 170;
    
    private int score = 0;
    private int coins = 0;
    private boolean debugMode = false;
    private int nextSpawn = 0;
    private long ticks;
    
    private GameHistory history;

    public PlayState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
        if(!paused){
            List<PowerUp> powerupsToRemove = new ArrayList();
            for(PowerUp p : objects.powerups){
                p.tick(powerupsToRemove);
            }

            for(PowerUp p : powerupsToRemove){
                objects.powerups.remove(p);
                this.objects.particles.add(new ParticleZooming(gp, this,8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), -3));
            }

            for(EnemyBasic e: this.objects.enemies){
                e.tick();
            }

            List<Particle> particlesToRemove = new ArrayList<>();
            for (Particle particle : this.objects.particles) {
                particle.tick(particlesToRemove);
            }

            for (Particle p: particlesToRemove){
                this.objects.particles.remove(p);
            }
            
            if (objects.pulse != null) {
                objects.pulse.tick();
                if(objects.pulse.getLife() < 0){
                    objects.pulse = null;
                }
            }

            objects.player.tick();
            this.objects.coin.tick();

            this.gameCollisions();

            if(KeyInput.pressed.contains(KeyEvent.VK_SEMICOLON)){
                this.debugMode = !this.debugMode;
                KeyInput.pressed.remove(KeyEvent.VK_SEMICOLON);
            }

            if(this.nextSpawn <= 0){
                Random r = new Random();
                int n = r.nextInt(100);
                if(n < 40){
                    objects.powerups.add(new BoxLife(gp, this,r.nextInt(90) + 90, r.nextInt(gp.size.width - 200) + 100, r.nextInt(gp.size.height - 200) + 100));
                }else if(n < 80){
                    objects.powerups.add(new BoxPulse(gp, this,r.nextInt(90) + 90, r.nextInt(gp.size.width - 200) + 100, r.nextInt(gp.size.height - 200) + 100));
                }else{
                    objects.powerups.add(new BoxShield(gp, this,r.nextInt(90) + 90, r.nextInt(gp.size.width - 200) + 100, r.nextInt(gp.size.height - 200) + 100));
                }
                this.nextSpawn = r.nextInt(3) + 2;
            }
        }
        this.ticks++;
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        try{
            for(PowerUp p : objects.powerups){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){}
        
        try{
            for(EnemyBasic e: this.objects.enemies){
                e.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
        }
        
        try{
            for(Particle p : this.objects.particles){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
            
        }
        
        if(objects.pulse != null)
            objects.pulse.draw(g, interpolation);
        
        if(this.objects.coin != null){
            this.objects.coin.draw((Graphics2D)g, interpolation);
        }
        this.objects.player.draw((Graphics2D)g, interpolation);
        
        
        gameHUD(g);        
    }
    
    private void gameHUD(Graphics2D g){
        g.setColor(new Color(200,200,200,150));
        g.fillRect(0, 0, this.TOP_PANEL_WIDTH, this.TOP_PANEL_HEIGHT);
        g.setColor(new Color(130,130,130));
        g.setStroke(new BasicStroke(2));
        g.drawLine(0, this.TOP_PANEL_HEIGHT, this.TOP_PANEL_WIDTH, this.TOP_PANEL_HEIGHT);
        g.drawLine(this.TOP_PANEL_WIDTH, 0, this.TOP_PANEL_WIDTH, this.TOP_PANEL_HEIGHT);
        g.setColor(new Color(50,50,50));
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
        g.drawString("Score: " + score, 10, 33);
        if(debugMode){
            this.debugHUD(g);
        }
    }
    
    private void debugHUD(Graphics g){
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g.drawString("Player:", 10, 70);
        g.drawString("X: " + objects.player.getX(), 20, 90);
        g.drawString("Y: " + objects.player.getY(), 20, 110);
        g.drawString("Coins: " + this.coins, 20, 130);
        g.drawString("Shield: " + objects.player.getShieldLevel(), 20, 150);
        
        g.drawString("Enemies: " + objects.enemies.size(), 10, 180);
        int particles = 0;
        for(Particle p : objects.particles) particles += p.particleAmount();
        g.drawString("Particles: " + particles, 10, 200);
        g.drawString("PowerUps: " + objects.powerups.size(), 10, 220);
        g.drawString("FPS: " + gp.FPS, 10, 250);
    }
    
    private void gameCollisions(){
        List<EnemyBasic> enemiesToRemove = new ArrayList();
        for(EnemyBasic e: this.objects.enemies){
            if(objects.player.getCollisionBox().intersects(e.getCollisionBox()) && !objects.player.isInvincible()){
                if(e.getClass() == EnemySlowing.class){
                    objects.player.applyEffect(new Effect(Effect.SLOWNESS, 1, 90));
                }else{
                    if(objects.player.decreaseSheild()){
                    }else if(objects.player.removeEffect(Effect.LIFE)){
                        objects.player.makeInvincible();
                    }else{
                        gp.gsm.pushState(gp.gsm.GAMEOVER_STATE, this.getObjects());
                    }
                }
            }
            
            if(objects.pulse != null && objects.pulse.getCollisionBox().intersects(e.getCollisionBox())){
                history.enemyDestroyed(e);
                enemiesToRemove.add(e);
                this.objects.particles.add(new ParticleCircular(gp, this,10, e.getColor(), e.getCenter(), 4, 6, e, 1, e.getSize() / 2, 10, 0.1f));
                this.objects.particles.add(new ParticleCircular(gp, this,8, e.getColor(), e.getCenter(), 6, 10, e, 1, e.getSize() / 3, 6, 0.1f));
            }
        }
        
        for(EnemyBasic e : enemiesToRemove){
            objects.enemies.remove(e);
        }
        
        if(objects.player.getCollisionBox().intersects(this.objects.coin.getCollisionBox())){
            score += this.objects.coin.getPoints();
            coins++;
            this.nextSpawn--;
            history.coinCollected(this.objects.coin);
            
            this.objects.particles.add(new ParticleCircular(gp, this,10, this.objects.coin.getColor(), this.objects.coin.getCenter(), 4, 6, this.objects.coin, 1, this.objects.coin.getSize() / 2, 10, 0.1f));
            this.objects.particles.add(new ParticleCircular(gp, this,8, this.objects.coin.getColor(), this.objects.coin.getCenter(), 6, 10, this.objects.coin, 1, this.objects.coin.getSize() / 3, 6, 0.1f));
            this.objects.particles.add(new ParticleText(gp, this, 20, this.objects.coin.getColor(), this.objects.coin.getCenter().x, this.objects.coin.getCenter().y, "+" + this.objects.coin.getPoints()));
            
            if (this.objects.coin.getClass() == Coin.class){
                this.objects.enemies.add(new EnemyBasic(gp, this));
            }else if(this.objects.coin.getClass() == CoinFollower.class){
                this.objects.enemies.add(new EnemyFollowing(gp, this));
            }else if(this.objects.coin.getClass() == CoinSlower.class){
                this.objects.enemies.add(new EnemySlowing(gp, this));
            }
            
            if(coins % 10 == 0){
                this.objects.coin = new CoinSlower(gp, this);
            }else if(coins % 5 == 0){
                this.objects.coin = new CoinFollower(gp, this);
            }else{
                this.objects.coin = new Coin(gp, this);
            }
        }
        List<PowerUp> toRemove = new ArrayList();
        for(PowerUp p : objects.powerups){
            if(objects.player.getCollisionBox().intersects(p.getCollisionBox())){
                if(objects.player.applyEffect(p.getEffect())){
                    history.powerUpCollected(p);
                    toRemove.add(p);
                    this.objects.particles.add(new ParticleZooming(gp, this,8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), 3));
                }
            }
        }
        
        for(PowerUp p : toRemove){
            objects.powerups.remove(p);
        }
    }

    @Override
    public void restart() {
        this.init();
    }

    @Override
    public void init() {
        this.objects = new ObjectHandler();
        this.history = new GameHistory(gp, this);
        objects.player = new Player(gp, this);
        for(int i = 0; i < 2; i++)
            this.objects.enemies.add(new EnemyBasic(gp, this));
        
        this.objects.coin = new Coin(gp, this);
        
        coins = 0;
        score = 0;
        ticks = 0;
    }

    @Override
    public void init(Object o) {
        
    }
    
    public int getScore(){
        return score;
    }

    /**
     * @return the ticks
     */
    public long getTicks() {
        return ticks;
    }

    /**
     * @return the history
     */
    public GameHistory getHistory() {
        return history;
    }
}
