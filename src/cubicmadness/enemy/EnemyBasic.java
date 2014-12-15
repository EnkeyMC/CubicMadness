package cubicmadness.enemy;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class EnemyBasic extends GameObject{
    
    protected String direction;
    protected float speed;
    
    // Finals
    protected final Random gen;

    public EnemyBasic(GamePanel panel){
        super(panel);
        this.gen = new Random();
        this.speed = 10f;
        this.color = Color.GRAY;
        size = new Dimension(10,10);
        spawn();
    }
    
    protected void spawn(){
        switch(gen.nextInt(4)){
            case 0: // UP
                this.y = -size.height;
                this.x = gen.nextFloat() * panel.getWidth();
                
                this.velY = gen.nextFloat() * (speed - 2f) + 2f;
                this.velX = (float)Math.sqrt(Math.pow(speed, 2) - Math.pow(velY, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "UP";
                break;
            case 1: // DOWN
                this.y = panel.getHeight();
                this.x = gen.nextFloat() * panel.getWidth();
                
                this.velY = -(gen.nextFloat() * (speed - 2f) + 2f);
                this.velX = (float)Math.sqrt(Math.pow(speed, 2) - Math.pow(velY, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "DOWN";
                break;
            case 2: // LEFT
                this.x = - this.size.width;
                this.y = gen.nextFloat() * panel.getHeight();
                
                this.velX = gen.nextFloat() * (speed - 2f) + 2f;
                this.velY = (float)Math.sqrt(Math.pow(speed, 2) - Math.pow(velX, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "LEFT";
                break;
            case 3: // RIGHT
                this.x = panel.getWidth();
                this.y = gen.nextFloat() * panel.getHeight();
                
                this.velX = -(gen.nextFloat() * (speed - 2f) + 2f);
                this.velY = (float)Math.sqrt(Math.pow(speed, 2) - Math.pow(velX, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "RIGHT";
        }
    }
    
    @Override
    public void tick(){
        if(!this.getRect().intersects(new Rectangle(-50,-50, panel.getWidth() + 100, panel.getHeight() + 100))){
            this.spawn();
            return;
        }
        this.x += velX;
        this.y += velY;
    }
}
