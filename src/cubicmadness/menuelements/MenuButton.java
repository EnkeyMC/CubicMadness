/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class MenuButton extends MenuElement{
    
    public static final byte ALIGN_CENTER = 0;
    
    public static final int BIG = 300;
    public static final int MEDIUM = 150;
    
    private final int HEIGHT = 80;
    
    private final int type;
    private final String text;

    public MenuButton(GamePanel panel, int type, String text) {
        super(panel, 0, 0);
        this.type = type;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        g.setColor(Color.GRAY);
        g.fillRect((int)x, (int)y, type, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(30f));
        float paddingX = (type - g.getFontMetrics().stringWidth(text)) / 2f;
        float paddingY = (HEIGHT - g.getFontMetrics().getHeight()) / 2f;
        g.drawString(text, x + paddingX, y + paddingY + g.getFontMetrics().getHeight() - 10);
        if(this.focused){
            g.setColor(Color.DARK_GRAY);
            g.setStroke(new BasicStroke(5));
            g.drawRect((int)x, (int)y, type, HEIGHT);
        }
    }
    
    public void align(byte align){
        switch(align){
            case ALIGN_CENTER:
                setX((panel.getWidth() / 2f) - (type / 2f));
                break;
            default:
                System.out.println("Unknown button align!");
        }
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return type;
    }
}
