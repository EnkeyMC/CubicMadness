package cubicmadness.coin;

import cubicmadness.bin.GamePanel;
import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class CoinFollower extends Coin {

    public CoinFollower(GamePanel gp) {
        super(gp);
        this.points = 25;
    }

    @Override
    public void draw(Graphics2D g, double interpolation){
        super.draw(g, interpolation);
        int size = this.maxSize.width - this.size.width;
        g.fillRect(Math.round(this.x - size), Math.round(this.y - size), size, size);
        g.fillRect(Math.round(this.x + this.size.width), Math.round(this.y + this.size.height), size, size);
        g.fillRect(Math.round(this.x + this.size.width), Math.round(this.y - size), size, size);
        g.fillRect(Math.round(this.x - size), Math.round(this.y + this.size.height), size, size);
    }
}
