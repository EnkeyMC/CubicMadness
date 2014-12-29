/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.coin;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Martin
 */
public class CoinSlower extends Coin{

    public CoinSlower(GamePanel gp, GameState gs) {
        super(gp, gs);
        this.points = 35;
    }
    
    @Override
    public void draw(Graphics2D g, double interpolation){
        super.draw(g, interpolation);
        Rectangle r = new Rectangle((int)Math.floor(this.x + (this.size /4f)), (int)Math.floor(this.y + (this.size /4f)), (int)size/2, (int)size/2);
        g.setColor(new Color(40,40,40));
        g.fill(r);
    }
}
