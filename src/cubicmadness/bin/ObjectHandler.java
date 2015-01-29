package cubicmadness.bin;

import cubicmadness.attack.Pulse;
import cubicmadness.coin.Coin;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.menuelements.MenuButton;
import cubicmadness.menuelements.MenuElement;
import cubicmadness.particle.Particle;
import cubicmadness.player.Player;
import cubicmadness.powerup.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class ObjectHandler {
    public List<EnemyBasic> enemies = new ArrayList();
    public List<Particle> particles = new ArrayList();
    public Player player;
    public Coin coin;
    public Pulse pulse;
    public List<PowerUp> powerups = new ArrayList();
    public List<MenuElement> elements = new ArrayList();
    public List<MenuButton> buttons = new ArrayList();
}
