package cubicmadness.enemy;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class EnemyBasic extends GameObject{
    
    protected String direction;
    
    // Finals
    protected final Random gen;

    public EnemyBasic(GamePanel gp, GameState gs){
        super(gp, gs);
        this.gen = new Random();
        DEFAULT_SPEED = 10f;
        this.speed = DEFAULT_SPEED;
        this.color = Color.GRAY;
        size = 10;
        spawn();
    }
    
    protected void spawn(){
        switch(gen.nextInt(4)){
            case 0: // UP
                this.y = -size;
                this.x = gen.nextFloat() * gp.size.width;
                
                this.velY = gen.nextFloat() * (DEFAULT_SPEED - 2f) + 2f;
                this.velX = (float)Math.sqrt(Math.pow(DEFAULT_SPEED, 2) - Math.pow(velY, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "UP";
                break;
            case 1: // DOWN
                this.y = gp.size.height;
                this.x = gen.nextFloat() * gp.size.width;
                
                this.velY = -(gen.nextFloat() * (DEFAULT_SPEED - 2f) + 2f);
                this.velX = (float)Math.sqrt(Math.pow(DEFAULT_SPEED, 2) - Math.pow(velY, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "DOWN";
                break;
            case 2: // LEFT
                this.x = - this.size;
                this.y = gen.nextFloat() * gp.size.height;
                
                this.velX = gen.nextFloat() * (DEFAULT_SPEED - 2f) + 2f;
                this.velY = (float)Math.sqrt(Math.pow(DEFAULT_SPEED, 2) - Math.pow(velX, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "LEFT";
                break;
            case 3: // RIGHT
                this.x = gp.size.width;
                this.y = gen.nextFloat() * gp.size.height;
                
                this.velX = -(gen.nextFloat() * (DEFAULT_SPEED - 2f) + 2f);
                this.velY = (float)Math.sqrt(Math.pow(DEFAULT_SPEED, 2) - Math.pow(velX, 2)) * (gen.nextInt(2) == 0 ? -1 : 1);
                this.direction = "RIGHT";
        }
    }
    
    @Override
    public void tick(){
        if(!this.getRect().intersects(new Rectangle(-50,-50, gp.size.width + 100, gp.size.height + 100))){
            this.spawn();
            return;
        }
        super.tick();
    }
}
