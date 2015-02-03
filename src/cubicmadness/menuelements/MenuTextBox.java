/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.menuelements;

import cubicmadness.bin.GamePanel;
import cubicmadness.bin.Utils;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class MenuTextBox extends MenuElement{
    
    private int width, height;
    private String text;

    private final int padding = 10;
            
    public MenuTextBox(GamePanel gp, GameState gs, float x, float y, int width, int height, String text) {
        super(gp, gs, x, y);
        this.width = width;
        this.height = height;
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        g.setColor(new Color(200,200,200,50));
        g.fillRect((int)x, (int)y, width, height);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        g.setColor(new Color(80,80,80));
        Utils.drawString(g, text, (int)x + padding, (int) (y + padding + g.getFontMetrics().getAscent()), width - padding*2);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
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
    
}
