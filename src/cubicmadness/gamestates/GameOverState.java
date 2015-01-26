package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.particle.Particle;
import cubicmadness.particle.ParticleZooming;
import cubicmadness.powerup.PowerUp;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class GameOverState extends GameState{
    
    private ObjectHandler gameObjects;
    private int delay;
    private float alpha;

    public GameOverState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
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
        
        if(delay > 0){
            delay--;
        }else if(alpha < 1){
            alpha += 0.1;
            alpha = GamePanel.clamp(alpha, 0, 1);
        }
        
        if(alpha != 0){
            if(KeyInput.pressed.contains(KeyEvent.VK_UP)){
                int i = 0;
                for(MenuElement e : objects.elements){
                    if(e.isFocused()){
                        i = objects.elements.indexOf(e);
                        e.setFocused(false);
                    }
                }
                i = (int) GamePanel.cycle(i - 1, 0, objects.elements.size()-1);
                objects.elements.get(i).setFocused(true);
                KeyInput.pressed.remove(KeyEvent.VK_UP);
            }

            if(KeyInput.pressed.contains(KeyEvent.VK_DOWN)){
                int i = 0;
                for(MenuElement e : objects.elements){
                    if(e.isFocused()){
                        i = objects.elements.indexOf(e);
                        e.setFocused(false);
                    }
                }
                i = (int) GamePanel.cycle(i + 1, 0, objects.elements.size()-1);
                objects.elements.get(i).setFocused(true);
                KeyInput.pressed.remove(KeyEvent.VK_DOWN);
            }
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_ENTER)){
            KeyInput.pressed.remove(KeyEvent.VK_ENTER);
            for(MenuElement e : objects.elements){
                if(e.isFocused()){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        if(MouseInput.LMB){
            MouseInput.LMB = false;
            for(MenuElement e : objects.elements){
                if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        for(MenuElement e : objects.elements){
            if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                this.unfocusAllElements();
                e.setFocused(true);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        // OBJECTS FROM GAME
        gameObjects.coin.draw(g, 0);
        //gameObjects.player.draw(g, 0);
        for(Particle p : gameObjects.particles){
            p.draw(g, 0);
        }
        for(PowerUp p : gameObjects.powerups){
            p.draw(g, 0);
        }
        for(EnemyBasic e : gameObjects.enemies){
            e.draw(g, 0);
        }
        for(MenuElement e : gameObjects.elements){
            e.draw(g, 0);
        }
        
        // OBJECTS FOR THIS STATE
        for(Particle p : objects.particles){
            p.draw(g, interpolation);
        }
        if(alpha != 0){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
            
            g.setColor(new Color(0,0,0, 0.25f));
            g.fillRect(0, 0, gp.getWidth(), gp.getHeight());
            
            for(PowerUp p : objects.powerups){
                p.draw(g, interpolation);
            }
            for(EnemyBasic e : objects.enemies){
                e.draw(g, interpolation);
            }
            for(MenuElement e : objects.elements){
                e.draw(g, interpolation);
            }
            
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(60f));
            g.drawString("Game Over!", (gp.getWidth() / 2) - (g.getFontMetrics().stringWidth("Game Over!") / 2), 100);
            
            g.setFont(g.getFont().deriveFont(45f));
            int score = ((PlayState)gp.gsm.PLAY_STATE).getScore();
            g.drawString("Score: " + score, (gp.getWidth() / 2) - (g.getFontMetrics().stringWidth("Score: " + score) / 2), 150);            
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void unfocusAllElements(){
        for(MenuElement e : objects.elements){
            e.setFocused(false);
        }
    }

    @Override
    public void restart() {
        init(gameObjects);
    }

    @Override
    public void init() {
        System.out.println("[ERROR]Using unsupported method \"init()\" in GameOverState!");
    }
    
    @Override
    public void init(Object gameObjects){
        delay = 25;
        alpha = 0f;
        this.gameObjects = (ObjectHandler)gameObjects;
        this.objects = new ObjectHandler();
        this.objects.particles.add(new ParticleZooming(gp, this,25, this.gameObjects.player.getColor(), this.gameObjects.player.getCenter(), this.gameObjects.player.getX(), this.gameObjects.player.getY(), this.gameObjects.player.getSize(), 0.7f));
        
        MenuButton e;
        try {
            e = new MenuButton(gp, this, MenuButton.BIG, "Restart", this.getClass().getDeclaredMethod("buttonRestartAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(200);
            e.setFocused(true);
            objects.elements.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Main menu", this.getClass().getDeclaredMethod("buttonMainMenuAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(300);
            objects.elements.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void buttonRestartAction(){
        gp.gsm.transition(this, gp.gsm.PLAY_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonMainMenuAction(){
        gp.gsm.transition(this, gp.gsm.MAINMENU_STATE, TransitionState.BLACKFADE);
        //gp.gsm.pushState(gp.gsm.MAINMENU_STATE);
    }
}
