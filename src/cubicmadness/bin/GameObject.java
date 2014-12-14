package cubicmadness.bin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Martin
 */
public abstract class GameObject {
    protected float x, y;
    protected float velX, velY;
    protected Color color;
    protected Dimension size;
    
    protected final GamePanel panel;
    
    public GameObject(GamePanel panel){
        this.panel = panel;
    }
    
    public void draw(Graphics2D g, double interpolation){
        g.setColor(color);
        g.fill(getRect(interpolation));
    }
    
    public Rectangle getRect(){
        return getRect(0);
    }
    
    public Rectangle getRect(double i){
        return new Rectangle((int)(x + (velX * i)), (int) (y + (velY * i)), size.width, size.height);
    }
    
    public void tick(){        
        this.x += velX;
        this.y += velY;
    }
    
    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the velX
     */
    public float getVelX() {
        return velX;
    }

    /**
     * @param velX the velX to set
     */
    public void setVelX(float velX) {
        this.velX = velX;
    }

    /**
     * @return the velY
     */
    public float getVelY() {
        return velY;
    }

    /**
     * @param velY the velY to set
     */
    public void setVelY(float velY) {
        this.velY = velY;
    }

    /**
     * @return the size
     */
    public Dimension getSize() {
        return size;
    }
}
