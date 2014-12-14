package cubicmadness.bin;

import cubicmadness.coin.Coin;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class GameObjects {
    public List<EnemyBasic> enemies = new ArrayList();
    public Player player;
    public Coin coin;
    
    public void init(GamePanel panel){
        for(int i = 0; i < 20; i++)
            enemies.add(new EnemyBasic(panel));
        
        coin = new Coin(panel);
        player = new Player(panel);
    }
}
