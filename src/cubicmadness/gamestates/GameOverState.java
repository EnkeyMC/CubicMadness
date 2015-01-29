package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.bin.Utils;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.menuelements.MenuLabel;
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
    
    private MenuLabel score;

    public GameOverState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {        
        if(!(MouseInput.mouseXY.x == MouseInput.mousePrevXY.x && MouseInput.mouseXY.y == MouseInput.mousePrevXY.y)){            
            MouseInput.mousePrevXY = MouseInput.mouseXY;
            
            for(MenuElement e : objects.buttons){
                if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                    this.unfocusAllElements();
                    e.setFocused(true);
                }
            }
        }
        
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
        
        for (MenuElement e : objects.elements)
            e.tick();
        
        for (MenuElement e : objects.buttons)
            e.tick();
        
        if(delay > 0){
            delay--;
        }else if(alpha < 1){
            alpha += 0.1;
            alpha = Utils.clamp(alpha, 0, 1);
        }
        
        if(alpha != 0){
            this.menuInteraction();
        }
        
        if(KeyInput.pressed.contains(KeyEvent.VK_ENTER) || KeyInput.pressed.contains(Config.ATTACK)){
            KeyInput.pressed.remove(KeyEvent.VK_ENTER);
            for(MenuElement e : objects.buttons){
                if(e.isFocused()){
                    e.actionPerformed();
                    return;
                }
            }
        }
        
        if(MouseInput.LMB){
            MouseInput.LMB = false;
            for(MenuElement e : objects.buttons){
                if(e.isInBounds(MouseInput.mouseXY.x, MouseInput.mouseXY.y)){
                    e.actionPerformed();
                    return;
                }
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
        
        for(MenuElement e : gameObjects.buttons){
            e.draw(g, 0);
        }
        
        // OBJECTS FOR THIS STATE
        for(Particle p : objects.particles){
            p.draw(g, interpolation);
        }
        if(alpha != 0){
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.alpha));
            
            g.setColor(new Color(1,1,1, 0.7f));
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
            for(MenuElement e : objects.buttons){
                e.draw(g, 0);
            }
            
            int s = ((PlayState)gp.gsm.PLAY_STATE).getScore();          
            this.score.setText("Score: " + s);
            this.score.alignCenter();
            this.score.draw(g, interpolation);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
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
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.BIG, "Main menu", this.getClass().getDeclaredMethod("buttonMainMenuAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(300);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        objects.elements.add(new MenuLabel(gp, this, gp.getWidth() / 2, 100, "Game Over!", MenuLabel.TYPE_H1, MenuLabel.ALIGN_CENTER));
        score = new MenuLabel(gp, this, gp.getWidth() / 2, 150, "Score: " + 0, MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER);

    }
    
    public void buttonRestartAction(){
        gp.gsm.transition(this, gp.gsm.PLAY_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonMainMenuAction(){
        gp.gsm.transition(this, gp.gsm.MAINMENU_STATE, TransitionState.BLACKFADE);
    }
}
