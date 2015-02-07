/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

import cubicmadness.attack.Pulse;
import cubicmadness.coin.Coin;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.gamestates.PlayState;
import cubicmadness.powerup.PowerUp;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Martin
 */
public class GameHistory {
    
    private final GamePanel gp;
    private final PlayState ps;
    
    private final HashMap<Long, Coin> collectedCoins;
    private final HashMap<Long, PowerUp> collectedPowerUps;
    private final HashMap<Long, Pulse> firedPulses;
    private final HashMap<Long, EnemyBasic> destroyedEnemies;
            
    public GameHistory(GamePanel gp, PlayState gs){
        this.gp = gp;
        this.ps = gs;
        this.collectedCoins = new HashMap();
        this.collectedPowerUps = new HashMap();
        this.firedPulses = new HashMap();
        this.destroyedEnemies = new HashMap();
    }
    
    public void coinCollected(Coin c){
        this.collectedCoins.put(ps.getTicks(), c);
    }
    
    public void powerUpCollected(PowerUp p){
        this.collectedPowerUps.put(ps.getTicks(), p);
    }
    
    public void pulseFired(Pulse p){
        this.firedPulses.put(ps.getTicks(), p);
    }
    
    public void enemyDestroyed(EnemyBasic e){
        this.destroyedEnemies.put(ps.getTicks(), e);
    }
    
    public String toJSONString(){
        StringBuilder str = new StringBuilder();
        str.append("{\"collectedCoins\":[");
        
        for(Iterator<Long> iter = this.collectedCoins.keySet().iterator(); iter.hasNext();){
            Long l = iter.next();
            str.append("{\"tick\":");
            str.append(l);
            str.append(", \"name\":\"");
            str.append(this.collectedCoins.get(l).getClass().getSimpleName());
            str.append("\"}");
            if(iter.hasNext())
                str.append(",");
        }
        
        str.append("], \"collectedPowerUps\":[");
        
        for(Iterator<Long> iter = this.collectedPowerUps.keySet().iterator(); iter.hasNext();){
            Long l = iter.next();
            str.append("{\"tick\":");
            str.append(l);
            str.append(", \"name\":\"");
            str.append(this.collectedPowerUps.get(l).getClass().getSimpleName());
            str.append("\"}");
            if(iter.hasNext())
                str.append(",");
        }
        
        str.append("], \"firedPulses\":[");
        
        for(Iterator<Long> iter = this.firedPulses.keySet().iterator(); iter.hasNext();){
            Long l = iter.next();
            str.append("{\"tick\":");
            str.append(l);
            str.append(", \"name\":\"");
            str.append(this.firedPulses.get(l).getClass().getSimpleName());
            str.append("\"}");
            if(iter.hasNext())
                str.append(",");
        }
        
        str.append("], \"destroyedEnemies\":[");
        
        for(Iterator<Long> iter = this.destroyedEnemies.keySet().iterator(); iter.hasNext();){
            Long l = iter.next();
            str.append("{\"tick\":");
            str.append(l);
            str.append(", \"name\":\"");
            str.append(this.destroyedEnemies.get(l).getClass().getSimpleName());
            str.append("\"}");
            if(iter.hasNext())
                str.append(",");
        }
        
        str.append("]}");
        
        return str.toString();
    }
}
