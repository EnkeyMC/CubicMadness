/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.Utils;
import cubicmadness.gamestates.GameState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class MenuButton extends MenuElement{
    
    public static final byte ALIGN_CENTER = 0;
    public static final byte ALIGN_RIGHT = 1;
    public static final byte ALIGN_LEFT = 2;
    
    public static final Dimension BIG = new Dimension(300,60);
    public static final Dimension MEDIUM = new Dimension(150,40);
    public static final Dimension SQUARE = new Dimension(60,60);
    
    private final Dimension type;
    private String text;
    private Method action;
    private boolean enabled;
    
    private int animProgress = 0;
    private final int animTime = 10;

    public MenuButton(GamePanel panel, GameState gs, Dimension type, String text, Method m) {
        super(panel, gs, 0, 0);
        this.type = type;
        this.text = text;
        this.action = m;
        this.enabled = true;
    }
    
    public MenuButton(GamePanel panel, GameState gs, Dimension type, String text, Method m, float x, float y){
        super(panel, gs, x,y);
        this.type = type;
        this.text = text;
        this.action = m;
        this.enabled = true;
    }
    
    @Override
    public void tick(){
        super.tick();
        if(this.focused){
            if(this.animProgress < animTime)
                this.animProgress++;
        }else{
            if(this.animProgress > 0)
                this.animProgress--;
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        if(this.isEnabled())
            g.setColor(Utils.interpolatedColor(Utils.smootherstep(this.animProgress / (float)this.animTime), new Color(150,150,150,50), new Color(200,200,200,50)));
        else
            g.setColor(new Color(150,150,150,50));
        g.fillRect((int)this.x, (int)this.y, type.width, type.height);
        g.setColor(new Color(130,130,130));
        g.setStroke(new BasicStroke(2f));
        g.drawLine((int)x, (int)y, (int)x, (int)y + type.height);
        g.drawLine((int)x + type.width, (int)y, (int)x + type.width, (int)y + type.height);
        
        float percent = (this.animProgress / (float)this.animTime);
        percent = Utils.smootherstep(percent);
        int width = (int)(type.width * 0.2f * percent);
        
        g.drawLine((int)x, (int)y, (int)x + width, (int)y);
        g.drawLine((int)x, (int)y + type.height, (int)x + width, (int)y + type.height);
        g.drawLine((int)x + type.width - width, (int)y, (int)x + type.width, (int)y);
        g.drawLine((int)x + type.width - width, (int)y + type.height, (int)x + type.width, (int)y + type.height);
        
        
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
        
        if(type == MEDIUM){
            g.setFont(g.getFont().deriveFont(20f));
        }else if(type == BIG){
            g.setFont(g.getFont().deriveFont(40f));
        }
        
        FontMetrics fm = g.getFontMetrics();
        if(this.type.width * 0.8 < fm.stringWidth(getText())){
            for(float i = 40; i > 10; i--){
                g.setFont(g.getFont().deriveFont(i));
                if(this.type.width * 0.8 >= g.getFontMetrics().stringWidth(getText()))
                    break;
            }
            fm = g.getFontMetrics();
        }
        
        if(this.isEnabled())
            g.setColor(new Color(100,100,100));
        else
            g.setColor(new Color(200,200,200));
        float paddingX = (type.width - fm.stringWidth(getText())) / 2f;
        float paddingY = (float)(type.height - fm.getStringBounds(getText(), g).getHeight()) / 2f;
        g.drawString(getText(), x + paddingX, y + paddingY + fm.getAscent());
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }
    
    public void align(byte align){
        switch(align){
            case ALIGN_CENTER:
                setX((gp.size.width / 2f) - (type.width / 2f));
                break;
            case ALIGN_RIGHT:
                setX(gp.size.width * 0.8f - type.width);
                break;
            case ALIGN_LEFT:
                setX(gp.size.width * 0.2f);
                break;
            default:
                System.out.println("Unknown button align!");
        }
    }
    
    @Override
    public void actionPerformed(){
        if(this.isEnabled()){
            try {
                action.invoke(this.gs);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(MenuButton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public int getHeight() {
        return type.height;
    }

    @Override
    public int getWidth() {
        return type.width;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
