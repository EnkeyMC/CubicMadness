package cubicmadness.bin;

import cubicmadness.coin.Coin;
import cubicmadness.coin.CoinFollower;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
import cubicmadness.input.KeyInput;
import cubicmadness.particle.Particle;
import cubicmadness.particle.ParticleCircular;
import cubicmadness.particle.ParticleZooming;
import cubicmadness.powerup.BoxLife;
import cubicmadness.powerup.Effect;
import cubicmadness.powerup.PowerUp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author Martin
 */
public class GamePanel extends JPanel implements Runnable{
    
    private final Dimension size = new Dimension(800, 600);
    
    public GameObjects objects = new GameObjects();    
    private Thread thread;
    public static boolean paused = false;
    private double interpolation = 0;
    private int score = 0;
    private int coins = 0;
    private final List<Particle> particlesToRemove = new ArrayList<>();
    private boolean debugMode = false;
    private int nextSpawn = 0;
    
    public GamePanel (){
        init();
        start();
    }
    
    private void init(){
        this.setPreferredSize(size);
        this.setSize(size);
        this.setBackground(Color.white);
        this.setForeground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyInput());
    }
    
    private void start(){
        objects.init(this);
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        try{
            for(PowerUp p : objects.powerups){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){}
        
        try{
            for(EnemyBasic e: objects.enemies){
                e.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
        }
        
        try{
            for(Particle p : objects.particles){
                p.draw((Graphics2D)g, interpolation);
            }
        }catch(Exception e){
            
        }
        
        if(objects.coin != null){
            objects.coin.draw((Graphics2D)g, interpolation);
        }
        objects.player.draw((Graphics2D)g, interpolation);
        
        
        gameHUD(g);        
        
        this.frames++;
    }
    
    private void gameHUD(Graphics g){
        g.setColor(objects.player.color);
        g.drawString("Score: " + score, 10, 20);
        if(debugMode){
            this.debugHUD(g);
        }
    }
    
    private void debugHUD(Graphics g){
        g.drawString("FPS: " + FPS, 10, 50);
        g.drawString("Player:", 10, 70);
        g.drawString("X: " + objects.player.getX(), 20, 90);
        g.drawString("Y: " + objects.player.getY(), 20, 110);
        g.drawString("Coins: " + this.coins, 20, 130);
        
        int i = 0;
        for(EnemyBasic e : objects.enemies) i++;
        g.drawString("Enemies: " + i, 10, 160);
    }
    
    // GAME LOOP

    private static final int UPDATES_PER_SECOND = 30;
    private static final double UPDATE_INTERVAL = 1000 / UPDATES_PER_SECOND * 1000000;
    private static final int MAX_FRAMESKIP = 5;
    
    private long nextUpdate = System.nanoTime();
    private int frames = 0;
    private int FPS = 0;
    
    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        while (true) {
            if(!paused){
                int skippedFrames = 0;
                while (System.nanoTime() > this.nextUpdate && skippedFrames < MAX_FRAMESKIP) {
                    this.gameTick();
                    this.nextUpdate += UPDATE_INTERVAL;
                    skippedFrames++;
                }

                this.interpolation = (System.nanoTime() + UPDATE_INTERVAL - this.nextUpdate) / UPDATE_INTERVAL;
                this.gameRender();
                /*try {
                    Thread.sleep(3);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                }*/

                if(System.currentTimeMillis() - timer > 1000){
                    timer += 1000;
                    FPS = frames;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }else{
                if(KeyInput.pressed.contains(KeyEvent.VK_R)){
                    objects = new GameObjects();
                    objects.init(this);
                    score = 0;
                    coins = 0;
                    this.nextUpdate = System.nanoTime();
                    paused = false;
                }
            }
        }
    }
    
    private void gameTick(){
        
        List<PowerUp> powerupsToRemove = new ArrayList();
        for(PowerUp p : objects.powerups){
            p.tick(powerupsToRemove);
        }
        
        for(PowerUp p : powerupsToRemove){
            objects.powerups.remove(p);
            objects.particles.add(new ParticleZooming(this, 8, p.color, p.getCenter(), p.x, p.y, p.size, -3));
        }
        
        for(EnemyBasic e: objects.enemies){
            e.tick();
        }
        
        for (Particle particle : objects.particles) {
            particle.tick(this.particlesToRemove);
        }
        
        for (Particle p: this.particlesToRemove){
            objects.particles.remove(p);
        }
        this.particlesToRemove.clear();
        
        objects.player.tick();
        objects.coin.tick();
        
        this.gameCollisions();
        
        if(KeyInput.pressed.contains(KeyEvent.VK_SEMICOLON)){
            this.debugMode = !this.debugMode;
            KeyInput.pressed.remove(KeyEvent.VK_SEMICOLON);
        }
        
        if(this.nextSpawn < 0){
            Random r = new Random();
            switch(r.nextInt(1)){
                case 0:
                    objects.powerups.add(new BoxLife(this, r.nextInt(90) + 90, r.nextInt(this.getWidth() - 200) + 100, r.nextInt(this.getHeight() - 200) + 100));
                    break;
                default:
            }
            this.nextSpawn = r.nextInt(90) + 180;
        }else{
            this.nextSpawn--;
        }
    }
    
    private void gameRender(){   
        this.repaint();
    }
    
    private void gameCollisions(){
        for(EnemyBasic e: objects.enemies){
            if(objects.player.getCollisionBox().intersects(e.getCollisionBox()) && !objects.player.isInvincible()){
                if(objects.player.removeEffect(Effect.LIFE)){
                    objects.player.makeInvincible();
                }else{
                    paused = true;
                }
            }
        }
        
        if(objects.player.getCollisionBox().intersects(objects.coin.getCollisionBox())){
            score += objects.coin.getPoints();
            coins++;
            
            objects.particles.add(new ParticleCircular(this, 10, objects.coin.color, objects.coin.getCenter(), 4, 6, objects.coin, 1, objects.coin.getSize() / 2, 10, 0.1f));
            objects.particles.add(new ParticleCircular(this, 8, objects.coin.color, objects.coin.getCenter(), 6, 10, objects.coin, 1, objects.coin.getSize() / 3, 6, 0.1f));
            
            if (objects.coin.getClass() == Coin.class){
                objects.enemies.add(new EnemyBasic(this));
            }else if(objects.coin.getClass() == CoinFollower.class){
                objects.enemies.add(new EnemyFollowing(this));
            }
            
            if(coins % 5 == 0){
                objects.coin = new CoinFollower(this);
            }else{
                objects.coin = new Coin(this);
            }
        }
        List<PowerUp> toRemove = new ArrayList();
        for(PowerUp p : objects.powerups){
            if(objects.player.getCollisionBox().intersects(p.getCollisionBox())){
                if(objects.player.applyEffect(p.getEffect())){
                    toRemove.add(p);
                    objects.particles.add(new ParticleZooming(this, 8, p.color, p.getCenter(), p.x, p.y, p.getSize(), 3));
                }
            }
        }
        
        for(PowerUp p : toRemove){
            objects.powerups.remove(p);
        }
    }
}
