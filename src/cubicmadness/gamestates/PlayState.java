/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.gamestates;

import cubicmadness.bin.GameObjects;
import cubicmadness.bin.GamePanel;
import static cubicmadness.bin.GamePanel.paused;
import cubicmadness.coin.Coin;
import cubicmadness.coin.CoinFollower;
import cubicmadness.coin.CoinSlower;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
import cubicmadness.enemy.EnemySlowing;
import cubicmadness.input.KeyInput;
import cubicmadness.particle.Particle;
import cubicmadness.particle.ParticleCircular;
import cubicmadness.particle.ParticleZooming;
import cubicmadness.player.Player;
import cubicmadness.powerup.BoxLife;
import cubicmadness.powerup.Effect;
import cubicmadness.powerup.PowerUp;
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
    private int score = 0;
    private int coins = 0;
    private boolean debugMode = false;
    private int nextSpawn = 0;
    
    public List<EnemyBasic> enemies = new ArrayList();
    public List<Particle> particles = new ArrayList();
    public Player player;
    public Coin coin;
    public List<PowerUp> powerups = new ArrayList();

    public PlayState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
        List<PowerUp> powerupsToRemove = new ArrayList();
        for(PowerUp p : powerups){
            p.tick(powerupsToRemove);
        }
        
        for(PowerUp p : powerupsToRemove){
            powerups.remove(p);
            particles.add(new ParticleZooming(gp, this,8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), -3));
        }
        
        for(EnemyBasic e: enemies){
            e.tick();
        }
        
        List<Particle> particlesToRemove = new ArrayList<>();
        for (Particle particle : particles) {
            particle.tick(particlesToRemove);
        }
        
        for (Particle p: particlesToRemove){
            particles.remove(p);
        }
        
        player.tick();
        coin.tick();
        
        this.gameCollisions();
        
        if(KeyInput.pressed.contains(KeyEvent.VK_SEMICOLON)){
            this.debugMode = !this.debugMode;
            KeyInput.pressed.remove(KeyEvent.VK_SEMICOLON);
        }
        
        if(this.nextSpawn < 0){
            Random r = new Random();
            switch(r.nextInt(1)){
                case 0:
                    powerups.add(new BoxLife(gp, this,r.nextInt(90) + 90, r.nextInt(gp.getWidth() - 200) + 100, r.nextInt(gp.getHeight() - 200) + 100));
                    break;
                default:
            }
            this.nextSpawn = r.nextInt(90) + 180;
        }else{
            this.nextSpawn--;
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        try{
            for(PowerUp p : powerups){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){}
        
        try{
            for(EnemyBasic e: enemies){
                e.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
        }
        
        try{
            for(Particle p : particles){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
            
        }
        
        if(coin != null){
            coin.draw((Graphics2D)g, interpolation);
        }
        player.draw((Graphics2D)g, interpolation);
        
        
        gameHUD(g);        
    }
    
    private void gameHUD(Graphics2D g){
        g.setColor(player.getColor());
        g.drawString("Score: " + score, 10, 20);
        if(debugMode){
            this.debugHUD(g);
        }
    }
    
    private void debugHUD(Graphics g){
        g.drawString("FPS: " + gp.FPS, 10, 50);
        g.drawString("Player:", 10, 70);
        g.drawString("X: " + player.getX(), 20, 90);
        g.drawString("Y: " + player.getY(), 20, 110);
        g.drawString("Coins: " + this.coins, 20, 130);
        
        int i = 0;
        for(EnemyBasic e : enemies) i++;
        g.drawString("Enemies: " + i, 10, 160);
    }
    
    private void gameCollisions(){
        for(EnemyBasic e: enemies){
            if(player.getCollisionBox().intersects(e.getCollisionBox()) && !player.isInvincible()){
                if(e.getClass() == EnemySlowing.class){
                    player.applyEffect(new Effect(Effect.SLOWNESS, 1, 90));
                }else{
                    if(player.removeEffect(Effect.LIFE)){
                        player.makeInvincible();
                    }else{
                        paused = true;
                    }
                }
            }
        }
        
        if(player.getCollisionBox().intersects(coin.getCollisionBox())){
            score += coin.getPoints();
            coins++;
            
            particles.add(new ParticleCircular(gp, this,10, coin.getColor(), coin.getCenter(), 4, 6, coin, 1, coin.getSize() / 2, 10, 0.1f));
            particles.add(new ParticleCircular(gp, this,8, coin.getColor(), coin.getCenter(), 6, 10, coin, 1, coin.getSize() / 3, 6, 0.1f));
            
            if (coin.getClass() == Coin.class){
                enemies.add(new EnemyBasic(gp, this));
            }else if(coin.getClass() == CoinFollower.class){
                enemies.add(new EnemyFollowing(gp, this));
            }else if(coin.getClass() == CoinSlower.class){
                enemies.add(new EnemySlowing(gp, this));
            }
            
            if(coins % 10 == 0){
                coin = new CoinSlower(gp, this);
            }else if(coins % 5 == 0){
                coin = new CoinFollower(gp, this);
            }else{
                coin = new Coin(gp, this);
            }
        }
        List<PowerUp> toRemove = new ArrayList();
        for(PowerUp p : powerups){
            if(player.getCollisionBox().intersects(p.getCollisionBox())){
                if(player.applyEffect(p.getEffect())){
                    toRemove.add(p);
                    particles.add(new ParticleZooming(gp, this,8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), 3));
                }
            }
        }
        
        for(PowerUp p : toRemove){
            powerups.remove(p);
        }
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void restart() {
        this.init();
    }

    @Override
    public void init() {
        this.enemies = new ArrayList();
        this.particles = new ArrayList();
        this.powerups = new ArrayList();
        player = new Player(gp, this);
        for(int i = 0; i < 2; i++)
            enemies.add(new EnemyBasic(gp, this));
        
        coin = new Coin(gp, this);
        
        coins = 0;
        score = 0;
    }
}
