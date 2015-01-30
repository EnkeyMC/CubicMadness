/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.particle;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public class ParticleText extends Particle{
    
    private final String text;
    private final float friction = 0.3f;

    public ParticleText(GamePanel gp, GameState gs, int life, Color c, float centerX, float centerY, String text) {
        super(gp, gs, life, c, null, centerX, centerY, 0);
        this.text = text;
        this.velY = -5;
    }

    @Override
    public void tick(List list) {
        super.tick(list);
        this.velY -= Math.copySign(friction, velY);
    }

    @Override
    public void draw(Graphics2D g, double interpolation) {
        g.setComposite(this.makeTransparent(life / 10f > 1f ? 1f : life / 10f));
        g.setColor(color);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.drawString(text, this.x - (g.getFontMetrics().stringWidth(text) / 2f), this.y);
        g.setComposite(this.makeTransparent(1));
    }   
}
