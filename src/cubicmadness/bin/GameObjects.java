package cubicmadness.bin;

import cubicmadness.coin.Coin;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.particle.Particle;
import cubicmadness.player.Player;
import cubicmadness.powerup.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class GameObjects {
    public List<EnemyBasic> enemies = new ArrayList();
    public List<Particle> particles = new ArrayList();
    public Player player;
    public Coin coin;
    public List<PowerUp> powerups = new ArrayList();
}
