package cubicmadness.coin;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class CoinFollower extends Coin {

    public CoinFollower(GamePanel gp, GameState gs) {
        super(gp, gs);
        this.points = 25;
    }

    @Override
    public void draw(Graphics2D g, double interpolation){
        super.draw(g, interpolation);
        int size = this.maxSize - this.getIntSize();
        g.fillRect(Math.round(this.x - size), Math.round(this.y - size), size, size);
        g.fillRect(Math.round(this.x + this.size), Math.round(this.y + this.size), size, size);
        g.fillRect(Math.round(this.x + this.size), Math.round(this.y - size), size, size);
        g.fillRect(Math.round(this.x - size), Math.round(this.y + this.size), size, size);
    }
}
