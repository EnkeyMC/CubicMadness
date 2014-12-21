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
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
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
    private GameObjects objects;

    public PlayState(GamePanel gp, GameObjects objects) {
        super(gp);
        this.objects = objects;
    }

    @Override
    public void tick() {
        List<PowerUp> powerupsToRemove = new ArrayList();
        for(PowerUp p : gp.objects.powerups){
            p.tick(powerupsToRemove);
        }
        
        for(PowerUp p : powerupsToRemove){
            gp.objects.powerups.remove(p);
            gp.objects.particles.add(new ParticleZooming(gp, 8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), -3));
        }
        
        for(EnemyBasic e: gp.objects.enemies){
            e.tick();
        }
        
        List<Particle> particlesToRemove = new ArrayList<>();
        for (Particle particle : gp.objects.particles) {
            particle.tick(particlesToRemove);
        }
        
        for (Particle p: particlesToRemove){
            gp.objects.particles.remove(p);
        }
        
        gp.objects.player.tick();
        gp.objects.coin.tick();
        
        this.gameCollisions();
        
        if(KeyInput.pressed.contains(KeyEvent.VK_SEMICOLON)){
            this.debugMode = !this.debugMode;
            KeyInput.pressed.remove(KeyEvent.VK_SEMICOLON);
        }
        
        if(this.nextSpawn < 0){
            Random r = new Random();
            switch(r.nextInt(1)){
                case 0:
                    gp.objects.powerups.add(new BoxLife(gp, r.nextInt(90) + 90, r.nextInt(gp.getWidth() - 200) + 100, r.nextInt(gp.getHeight() - 200) + 100));
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
            for(PowerUp p : gp.objects.powerups){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){}
        
        try{
            for(EnemyBasic e: gp.objects.enemies){
                e.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
        }
        
        try{
            for(Particle p : gp.objects.particles){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
            
        }
        
        if(gp.objects.coin != null){
            gp.objects.coin.draw((Graphics2D)g, interpolation);
        }
        gp.objects.player.draw((Graphics2D)g, interpolation);
        
        
        gameHUD(g);        
    }
    
    private void gameHUD(Graphics2D g){
        g.setColor(gp.objects.player.getColor());
        g.drawString("Score: " + score, 10, 20);
        if(debugMode){
            this.debugHUD(g);
        }
    }
    
    private void debugHUD(Graphics g){
        g.drawString("FPS: " + gp.FPS, 10, 50);
        g.drawString("Player:", 10, 70);
        g.drawString("X: " + gp.objects.player.getX(), 20, 90);
        g.drawString("Y: " + gp.objects.player.getY(), 20, 110);
        g.drawString("Coins: " + this.coins, 20, 130);
        
        int i = 0;
        for(EnemyBasic e : gp.objects.enemies) i++;
        g.drawString("Enemies: " + i, 10, 160);
    }
    
    private void gameCollisions(){
        for(EnemyBasic e: gp.objects.enemies){
            if(gp.objects.player.getCollisionBox().intersects(e.getCollisionBox()) && !gp.objects.player.isInvincible()){
                if(gp.objects.player.removeEffect(Effect.LIFE)){
                    gp.objects.player.makeInvincible();
                }else{
                    paused = true;
                }
            }
        }
        
        if(gp.objects.player.getCollisionBox().intersects(gp.objects.coin.getCollisionBox())){
            score += gp.objects.coin.getPoints();
            coins++;
            
            gp.objects.particles.add(new ParticleCircular(gp, 10, gp.objects.coin.getColor(), gp.objects.coin.getCenter(), 4, 6, gp.objects.coin, 1, gp.objects.coin.getSize() / 2, 10, 0.1f));
            gp.objects.particles.add(new ParticleCircular(gp, 8, gp.objects.coin.getColor(), gp.objects.coin.getCenter(), 6, 10, gp.objects.coin, 1, gp.objects.coin.getSize() / 3, 6, 0.1f));
            
            if (gp.objects.coin.getClass() == Coin.class){
                gp.objects.enemies.add(new EnemyBasic(gp));
            }else if(gp.objects.coin.getClass() == CoinFollower.class){
                gp.objects.enemies.add(new EnemyFollowing(gp));
            }
            
            if(coins % 5 == 0){
                gp.objects.coin = new CoinFollower(gp);
            }else{
                gp.objects.coin = new Coin(gp);
            }
        }
        List<PowerUp> toRemove = new ArrayList();
        for(PowerUp p : gp.objects.powerups){
            if(gp.objects.player.getCollisionBox().intersects(p.getCollisionBox())){
                if(gp.objects.player.applyEffect(p.getEffect())){
                    toRemove.add(p);
                    gp.objects.particles.add(new ParticleZooming(gp, 8, p.getColor(), p.getCenter(), p.getX(), p.getY(), p.getSize(), 3));
                }
            }
        }
        
        for(PowerUp p : toRemove){
            gp.objects.powerups.remove(p);
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
        objects.enemies.clear();
        this.init();
    }

    @Override
    public void init() {
        for(int i = 0; i < 2; i++)
            objects.enemies.add(new EnemyBasic(gp));
        
        objects.coin = new Coin(gp);
        objects.player = new Player(gp);
        
        coins = 0;
        score = 0;
    }
}
