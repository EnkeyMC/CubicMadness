package cubicmadness.powerup;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;

/**
 *
 * @author Martin
 */
public abstract class PowerUp extends GameObject{
    
    protected int life;
    protected Effect ef;

    public PowerUp(GamePanel panel) {
        super(panel);
    }

}
