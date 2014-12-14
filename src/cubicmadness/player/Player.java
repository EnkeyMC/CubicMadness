package cubicmadness.player;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.input.KeyInput;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 *
 * @author Martin
 */
public class Player extends GameObject{
    private final float speed = 7f;
    
    public Player(GamePanel gp){
        super(gp);
        color = new Color(179,61,67);
        size = new Dimension(20,20);
        x = panel.getWidth() / 2 - size.width / 2;
        y = panel.getHeight() / 2 - size.height / 2;
    }
    
    @Override
    public void tick(){
        Set<Integer> key = KeyInput.pressed;
        if(key.contains(KeyEvent.VK_UP)) velY = -speed;
        else velY = 0;
        if(key.contains(KeyEvent.VK_DOWN)) velY = speed;
        if(key.contains(KeyEvent.VK_RIGHT)) velX = speed;
        else velX = 0;
        if(key.contains(KeyEvent.VK_LEFT)) velX = -speed;
        
        if(key.contains(KeyEvent.VK_UP) && key.contains(KeyEvent.VK_DOWN)) velY = 0;
        if(key.contains(KeyEvent.VK_RIGHT) && key.contains(KeyEvent.VK_LEFT)) velX = 0;
        
        this.x += velX;
        this.y += velY;
        
        if(this.x < 0) x = 0;
        if(this.x > panel.getWidth() - this.size.width) x = panel.getWidth() - this.size.width;
        if(this.y < 0) y = 0;
        if(this.y > panel.getHeight() - this.size.height) y = panel.getHeight() - this.size.height;
    }
    
    public float getSpeed(){
        return (float) Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    }
}
