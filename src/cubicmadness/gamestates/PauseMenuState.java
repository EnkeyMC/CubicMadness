package cubicmadness.gamestates;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.menuelements.MenuLabel;
import cubicmadness.particle.Particle;
import cubicmadness.powerup.PowerUp;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Martin on 26.4.2015.
 */
public class PauseMenuState extends GameState {

    private ObjectHandler gameObjects;
    private int timer;
    private float offset;
    private float elemTime;

    private final int animTime = 30;
    private final int initOffset = -600;

    public PauseMenuState(GamePanel gp) {
        super(gp);
    }

    @Override
    public void tick() {
        if(!(MouseInput.mouseXYtransform.x == MouseInput.mousePrevXY.x && MouseInput.mouseXYtransform.y == MouseInput.mousePrevXY.y)){
            MouseInput.mousePrevXY = MouseInput.mouseXYtransform;

            for(MenuElement e : objects.buttons){
                if(e.isInBounds(MouseInput.mouseXYtransform.x, MouseInput.mouseXYtransform.y)){
                    this.unfocusAllElements();
                    e.setFocused(true);
                }
            }
        }

        java.util.List<Particle> particlesToRemove = new ArrayList();
        for(Particle p : objects.particles){
            p.tick(particlesToRemove);
        }

        for(Particle p : particlesToRemove){
            objects.particles.remove(p);
        }

        for(MenuElement e : objects.elements)
            e.tick();

        for(MenuElement e : objects.buttons)
            e.tick();

        if(KeyInput.pressed.contains(KeyEvent.VK_ESCAPE)){
            this.buttonContinueAction();
            KeyInput.pressed.remove(KeyEvent.VK_ESCAPE);
        }

        if(timer <= animTime)
            timer++;

        this.menuInteraction();
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        // OBJECTS FROM GAME
        gameObjects.coin.draw(g, 0);
        gameObjects.player.draw(g, 0);
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

        g.setColor(new Color(1, 1, 1, 0.7f));
        g.fillRect(0, 0, gp.size.width, gp.size.height);

        for(PowerUp p : objects.powerups){
            p.draw(g, interpolation);
        }
        for(EnemyBasic e : objects.enemies){
            e.draw(g, interpolation);
        }

        float off;

        if(timer <= animTime){
            int i = 0;
            for(MenuElement e : objects.elements){
                if(timer >= i * this.elemTime){
                    off = offset + ((-offset) * ((timer - i*elemTime) / elemTime));
                    g.getTransform().translate(off, 0);
                    e.draw(g, interpolation);
                    g.getTransform().translate(-off, 0);
                }else if(timer > (i + 1) * this.elemTime){
                    e.draw(g, interpolation);
                }
                i++;
            }
            for(MenuElement e : objects.buttons){
                e.draw(g, 0);
                i++;
            }
        }else{
            for(MenuElement e : objects.elements){
                e.draw(g, interpolation);
            }
            for(MenuElement e : objects.buttons){
                e.draw(g, 0);
            }
        }
    }

    @Override
    public void restart() {

    }

    @Override
    public void init() {

    }

    @Override
    public void init(Object o) {
        this.gameObjects = (ObjectHandler)o;
        this.timer = 0;
        this.offset = this.initOffset;
        this.objects = new ObjectHandler();
        MenuButton e;
        try {
            e = new MenuButton(gp, this, MenuButton.BIG, "Restart", this.getClass().getDeclaredMethod("buttonRestartAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(200);
            e.setFocused(true);
            objects.buttons.add(e);

            e = new MenuButton(gp, this, MenuButton.BIG, "Main menu", this.getClass().getDeclaredMethod("buttonMainMenuAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(270);
            objects.buttons.add(e);

            e = new MenuButton(gp, this, MenuButton.BIG, "Options", this.getClass().getDeclaredMethod("buttonOptionsAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(340);
            objects.buttons.add(e);

            e = new MenuButton(gp, this, MenuButton.BIG, "Continue", this.getClass().getDeclaredMethod("buttonContinueAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(410);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }

        objects.elements.add(new MenuLabel(gp, this, gp.size.width / 2, 150, "Score: " + ((PlayState)gp.gsm.PLAY_STATE).getScore(), MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));

        this.elemTime = this.animTime / (float)(objects.elements.size() + objects.buttons.size());
    }

    public void buttonRestartAction(){
        gp.gsm.transition(this, gp.gsm.PLAY_STATE, TransitionState.BLACKFADE);
    }

    public void buttonMainMenuAction(){
        gp.gsm.transition(this, gp.gsm.MAINMENU_STATE, TransitionState.BLACKFADE);
    }

    public void buttonOptionsAction(){
        gp.gsm.transition(this, gp.gsm.OPTIONSMENU_STATE, TransitionState.BLACKFADE);
    }

    public void buttonContinueAction(){
        gp.gsm.popCurrentState();
    }
}
