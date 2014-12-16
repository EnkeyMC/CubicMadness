package cubicmadness.bin;

import cubicmadness.coin.Coin;
import cubicmadness.coin.CoinFollower;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
import cubicmadness.input.KeyInput;
import cubicmadness.particle.Particle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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
        //System.out.println(interpolation);
        
        for(EnemyBasic e: objects.enemies){
            e.draw((Graphics2D)g, interpolation);
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
        g.drawString("FPS: " + FPS, 10, 50);
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
    }
    
    private void gameRender(){   
        this.repaint();
    }
    
    private void gameCollisions(){
        for(EnemyBasic e: objects.enemies){
            if(objects.player.getCollisionBox().intersects(e.getCollisionBox())){
                paused = true;
            }
        }
        
        if(objects.player.getCollisionBox().intersects(objects.coin.getCollisionBox())){
            score += objects.coin.getPoints();
            coins++;
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
    }
}
