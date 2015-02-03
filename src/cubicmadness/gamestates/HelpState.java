package cubicmadness.gamestates;

import cubicmadness.bin.Config;
import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.bin.ObjectHandler;
import cubicmadness.bin.Utils;
import cubicmadness.coin.Coin;
import cubicmadness.coin.CoinFollower;
import cubicmadness.coin.CoinSlower;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.enemy.EnemyFollowing;
import cubicmadness.enemy.EnemySlowing;
import cubicmadness.input.KeyInput;
import cubicmadness.input.MouseInput;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.menuelements.MenuLabel;
import cubicmadness.menuelements.MenuTextBox;
import cubicmadness.particle.Particle;
import cubicmadness.powerup.BoxLife;
import cubicmadness.powerup.BoxPulse;
import cubicmadness.powerup.BoxShield;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class HelpState extends GameState{
    
    private float offX;
    private int currentPage;
    private final float speed = 50;
    private final int maxPages = 4;
    private List<Rectangle> labels;
    private List<GameObject> displayed;

    public HelpState(GamePanel gp) {
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
        
        List<Particle> particlesToRemove = new ArrayList();
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
        
        for(GameObject o : displayed){
            if (!o.getClass().getSuperclass().equals(EnemyBasic.class) && !o.getClass().equals(EnemyBasic.class)) {
                o.tick();
            }
        }
        
        if(KeyInput.pressed.contains(Config.LEFT) || KeyInput.pressed.contains(KeyEvent.VK_LEFT)){
            KeyInput.pressed.remove(Config.LEFT);
            KeyInput.pressed.remove(KeyEvent.VK_LEFT);
            
            if (this.currentPage > 0) {
                this.currentPage--;
            }
        }
        
        if(KeyInput.pressed.contains(Config.RIGHT) || KeyInput.pressed.contains(KeyEvent.VK_RIGHT)){
            KeyInput.pressed.remove(Config.RIGHT);
            KeyInput.pressed.remove(KeyEvent.VK_RIGHT);
            
            if(this.currentPage < this.maxPages - 1){
                this.currentPage++;
            }
        }
        
        this.menuInteraction();
        
        int curr = currentPage * gp.size.width;
        if(offX < curr){
            offX += speed;
            if(offX > curr)
                offX = curr;
        }else if(offX > curr){
            offX -= speed;
            if(offX < curr)
                offX = curr;
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        
        g.drawImage(gp.bgr.getImage(), 0, 0, gp);
        g.setColor(new Color(1,1,1,0.85f));
        g.fillRect(0, 0, gp.size.width, gp.size.height);
        
        double inter = 0;
        float actualX = currentPage * gp.size.width;

        if(offX < currentPage * gp.size.width){
            if(offX + speed * interpolation < currentPage * gp.size.width){
                inter = speed * interpolation;
                int curr = (int)Math.floor(offX / gp.size.width) * gp.size.width;
                actualX = curr + gp.size.width * Utils.smootherstep((float) (offX + inter), curr, currentPage * gp.size.width);
            }
        }else if(offX > currentPage * gp.size.width){
            if(offX - speed * interpolation > currentPage * gp.size.width){
                inter = -speed * interpolation;
                int curr = (int)Math.ceil(offX / gp.size.width) * gp.size.width;
                actualX = (currentPage * gp.size.width) + gp.size.width * Utils.smootherstep((float) (offX + inter), currentPage * gp.size.width, curr);
            }
        }
        
        for(Rectangle r : labels){
            float oldX = r.x;
            g.setColor(new Color(200,200,200,50));
            r.x = (int) (oldX - actualX);
            g.fill(r);
            r.x = (int) oldX;
        }
        
        for(MenuElement e : objects.elements){   
            float oldX = e.getX();
            e.setX(oldX - actualX);
            e.draw(g, interpolation);
            e.setX(oldX);
        }
        
        for(MenuElement e : objects.buttons){
            e.draw(g, interpolation);
        }
        
        for(Particle p : objects.particles){
            p.draw(g, interpolation);
        }
        
        for(GameObject o : displayed){
            float oldX = o.getX();
            o.setX(oldX - actualX);
            o.draw(g, interpolation);
            o.setX(oldX);
        }
        
        float size = 20;
        float gap = 10;
        float startX = gp.size.width / 2 - (size * this.maxPages + gap * (this.maxPages - 1)) / 2;
        for(int i = 0; i < this.maxPages; i++){
            if(i == currentPage)
                g.setColor(new Color(150,150,150,150));
            else
                g.setColor(new Color(200,200,200,150));
            g.fillRect((int) (startX + (i * size + i * gap)), 130, (int)size, (int)size);
        }
    }

    @Override
    public void restart() {
        init();
    }

    @Override
    public void init() {
        this.offX = 0;
        this.currentPage = 0;
        this.objects = new ObjectHandler();
        this.labels = new ArrayList();
        this.displayed = new ArrayList();
        
        MenuButton e;
        try {
            e = new MenuButton(gp, this, MenuButton.SQUARE, "<", this.getClass().getDeclaredMethod("buttonLeftAction"), 40, 280);
            objects.buttons.add(e);
            
            e = new MenuButton(gp, this, MenuButton.SQUARE, ">", this.getClass().getDeclaredMethod("buttonRightAction"), gp.size.width - MenuButton.SQUARE.width - 40, 280);
            objects.buttons.add(e);            
            
            e = new MenuButton(gp, this, MenuButton.MEDIUM, "Back", this.getClass().getDeclaredMethod("buttonBackAction"));
            e.align(MenuButton.ALIGN_CENTER);
            e.setY(480);
            objects.buttons.add(e);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MainMenuState.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int gap = 20;
        int width = (int) ((gp.size.width * 0.6f - gap*2) / 3f);
        int height = 200;
        GameObject o;
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width / 2, 100, "General", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 0.2f), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 0.2f, 230, width, height, "Goal is to collect the most coins and get the highest score."));
        objects.elements.add(new MenuLabel(gp, this, objects.elements.get(objects.elements.size() - 1), 30, "Goal", MenuLabel.TYPE_H3, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 0.2f + (width + gap)), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 0.2f + (width + gap), 230, width, height, "Pick up power ups that spawn on the ground to help you survive."));
        objects.elements.add(new MenuLabel(gp, this, objects.elements.get(objects.elements.size() - 1), 30, "Surviving", MenuLabel.TYPE_H3, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 0.2f + (width + gap)*2), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 0.2f + (width + gap) *2, 230, width, height, "Pick up coins to get score."));
        objects.elements.add(new MenuLabel(gp, this, objects.elements.get(objects.elements.size() - 1), 30, "Score", MenuLabel.TYPE_H3, MenuLabel.ALIGN_CENTER));
        
        
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width + gp.size.width / 2, 100, "Coins", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 1.2f), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 1.2f, 230, width, height, "+ 10 score, spawns Basic Enemy"));
        o = new Coin(gp, this);
        o.setX(gp.size.width * 1.2f + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        
        this.labels.add(new Rectangle((int) (gp.size.width * 1.2f + (width + gap)), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 1.2f + (width + gap), 230, width, height, "+ 25 score, spawns Enemy Following"));
        o = new CoinFollower(gp, this);
        o.setX(gp.size.width * 1.2f + (width + gap) + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        this.labels.add(new Rectangle((int) (gp.size.width * 1.2f + (width + gap)*2), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 1.2f + (width + gap) * 2, 230, width, height, "+ 35 score, spawns Enemy Slowing"));
        o = new CoinSlower(gp, this);
        o.setX(gp.size.width * 1.2f + (width + gap)*2 + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width * 2 + gp.size.width / 2, 100, "Enemies", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 2.2f), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 2.2f, 230, width, height, "Basic Enemy, don't let him hit you!"));
        o = new EnemyBasic(gp, this);
        o.setX(gp.size.width * 2.2f + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        this.labels.add(new Rectangle((int) (gp.size.width * 2.2f + (width + gap)), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 2.2f + (width + gap), 230, width, height, "Following Enemy, he's going to chase you!"));
        o = new EnemyFollowing(gp, this);
        o.setX(gp.size.width * 2.2f + (width + gap) + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        this.labels.add(new Rectangle((int) (gp.size.width * 2.2f + (width + gap)*2), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 2.2f + (width + gap) * 2, 230, width, height, "Slowing Enemy, he slows you for 1,5 second!"));
        o = new EnemySlowing(gp, this);
        o.setX(gp.size.width * 2.2f + (width + gap)*2 + width / 2 - o.getSize() / 2);
        o.setY(190 - o.getSize()/2);
        displayed.add(o);
        
        
        
        objects.elements.add(new MenuLabel(gp, this, gp.size.width * 3 + gp.size.width / 2, 100, "Power ups", MenuLabel.TYPE_H2, MenuLabel.ALIGN_CENTER));
        
        this.labels.add(new Rectangle((int) (gp.size.width * 3.2f), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 3.2f, 230, width, height, "Power up that gives you one more life (max 5)"));
        o = new BoxLife(gp, this, 0, gp.size.width * 3.2f + width / 2 - o.getSize() / 2, 190 - o.getSize()/2);
        displayed.add(o);
        
        this.labels.add(new Rectangle((int) (gp.size.width * 3.2f + (width + gap)), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 3.2f + (width + gap), 230, width, height, "Power up that gives you shield. Recharges over time."));
        o = new BoxShield(gp, this, 0, gp.size.width * 3.2f + (width + gap) + width / 2 - o.getSize() / 2, 190 - o.getSize()/2);
        displayed.add(o);
        
        this.labels.add(new Rectangle((int) (gp.size.width * 3.2f + (width + gap)*2), 170, width, 40));
        objects.elements.add(new MenuTextBox(gp, this, gp.size.width * 3.2f + (width + gap) * 2, 230, width, height, "Gives you one Pulse that destroyes enemies. Activate by pressing \"" + KeyEvent.getKeyText(Config.ATTACK) + "\" (max 5)"));
        o = new BoxPulse(gp, this, 0, gp.size.width * 3.2f + (width + gap)*2 + width / 2 - o.getSize() / 2, 190 - o.getSize()/2);
        displayed.add(o);
        
        for(GameObject obj : displayed){
            obj.setVelX(0);
            obj.setVelY(0);
        }
    }
    
    public void buttonBackAction(){
        gp.gsm.transition(this, gp.gsm.MAINMENU_STATE, TransitionState.BLACKFADE);
    }
    
    public void buttonLeftAction(){
        if (this.currentPage > 0) {
            this.currentPage--;
        }
    }
    
    public void buttonRightAction(){
        if(this.currentPage < this.maxPages - 1){
            this.currentPage++;
        }
    }

    @Override
    public void init(Object o) {
    }

}
