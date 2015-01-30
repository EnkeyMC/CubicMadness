/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class MenuLabel extends MenuElement {
    
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;
    
    
    // Types
    public static final int TYPE_H1 = 60;
    public static final int TYPE_H2 = 45;
    public static final int TYPE_LABEL = 15;

    private String text;
    private Font font;

    public MenuLabel(GamePanel gp, GameState gs, MenuElement parent, String text, int type, int align) {
        super(gp, gs, 0, 0);
        this.text = text;
        this.font = new Font(Font.SANS_SERIF, Font.PLAIN, type);
        this.y = parent.y - 15;
        
        switch (align) {
            case ALIGN_RIGHT:
                this.x = parent.getX() + parent.getWidth() /2 - gp.getGraphics().getFontMetrics(font).stringWidth(text);
                break;
            case ALIGN_CENTER:
                this.x = parent.getX() + parent.getWidth() /2 - gp.getGraphics().getFontMetrics(font).stringWidth(text) / 2f;
                break;
            default:
                this.x = parent.x;
        }
    }
    
    public MenuLabel(GamePanel gp, GameState gs, int x, int y, String text, int type, int align){
        super(gp, gs, 0, 0);
        this.text = text;
        this.font = new Font(Font.SANS_SERIF, Font.PLAIN, type);
        this.y = y;
        
        switch (align) {
            case ALIGN_RIGHT:
                this.x = x - gp.getGraphics().getFontMetrics(font).stringWidth(text);
                break;
            case ALIGN_CENTER:
                this.x = x - gp.getGraphics().getFontMetrics(font).stringWidth(text) / 2f;
                break;
            default:
                this.x = x;
        }
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        g.setFont(getFont());
        g.setColor(new Color(100,100,100));
        g.drawString(getText(), x, y);
    }

    @Override
    public int getHeight() {
        return gp.getGraphics().getFontMetrics(getFont()).getHeight();
    }

    @Override
    public int getWidth() {
        return gp.getGraphics().getFontMetrics(getFont()).stringWidth(getText());
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
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }
    
    public void alignCenter(){
        this.x = gp.size.width / 2 - this.getWidth() / 2;
    }

}
