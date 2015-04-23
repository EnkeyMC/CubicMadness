/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cubicmadness.attack.Pulse;
import cubicmadness.coin.Coin;
import cubicmadness.enemy.EnemyBasic;
import cubicmadness.gamestates.PlayState;
import cubicmadness.jsonhandlers.ConfigSerializer;
import cubicmadness.jsonhandlers.GameHistorySerializer;
import cubicmadness.powerup.PowerUp;

import java.io.FileWriter;
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
        this.getCollectedCoins().put(ps.getTicks(), c);
    }
    
    public void powerUpCollected(PowerUp p){
        this.getCollectedPowerUps().put(ps.getTicks(), p);
    }
    
    public void pulseFired(Pulse p){
        this.getFiredPulses().put(ps.getTicks(), p);
    }
    
    public void enemyDestroyed(EnemyBasic e){
        this.getDestroyedEnemies().put(ps.getTicks(), e);
    }
    
    public String toJSONString(){
        try {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(GameHistory.class, new GameHistorySerializer()).setPrettyPrinting();
            String json = gson.create().toJson(this, new TypeToken<GameHistory>() {
            }.getType());
            return json;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return "";
        }
    }

    public HashMap<Long, Coin> getCollectedCoins() {
        return collectedCoins;
    }

    public HashMap<Long, PowerUp> getCollectedPowerUps() {
        return collectedPowerUps;
    }

    public HashMap<Long, Pulse> getFiredPulses() {
        return firedPulses;
    }

    public HashMap<Long, EnemyBasic> getDestroyedEnemies() {
        return destroyedEnemies;
    }
}
