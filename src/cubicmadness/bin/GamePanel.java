package cubicmadness.bin;

import cubicmadness.gamestates.GameStateManager;
import cubicmadness.input.KeyInput;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 *
 * @author Martin
 */
public class GamePanel extends JPanel implements Runnable{
    
    private final Dimension size = new Dimension(800, 600);
    
    public GameObjects objects = new GameObjects();    
    public GameStateManager gsm;
    private Thread thread;
    public static boolean paused = false;
    private double interpolation = 0;
    
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
        this.gsm = new GameStateManager(this);
        thread = new Thread(this);
        thread.start();
    }
    
    // GAME LOOP

    private static final int UPDATES_PER_SECOND = 30;
    private static final double UPDATE_INTERVAL = 1000 / UPDATES_PER_SECOND * 1000000;
    private static final int MAX_FRAMESKIP = 5;
    
    private long nextUpdate = System.nanoTime();
    public int frames = 0;
    public int FPS = 0;
    
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

                if(System.currentTimeMillis() - timer > 1000){
                    timer += 1000;
                    FPS = frames;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }else{
                if(KeyInput.pressed.contains(KeyEvent.VK_R)){
                    gsm.restart();
                    this.nextUpdate = System.nanoTime();
                    paused = false;
                }
            }
            
            if(KeyInput.pressed.contains(KeyEvent.VK_ESCAPE)){
                gsm.popCurrentState();
            }
        }
    }
    
    private void gameTick(){
        gsm.tick();
    }
    
    private void gameRender(){   
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        
        gsm.draw((Graphics2D)g, interpolation);
        
        this.frames++;
    }
}
