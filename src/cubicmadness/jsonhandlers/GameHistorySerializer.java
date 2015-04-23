package cubicmadness.jsonhandlers;

import com.google.gson.*;
import cubicmadness.bin.GameHistory;
import cubicmadness.coin.Coin;

import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Created by Martin on 23.4.2015.
 */
public class GameHistorySerializer implements JsonSerializer<GameHistory> {
    @Override
    public JsonElement serialize(GameHistory gameHistory, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject main = new JsonObject();

        JsonArray jsonArray = new JsonArray();
        for (Iterator<Long> iter = gameHistory.getCollectedCoins().keySet().iterator(); iter.hasNext();) {
            Long l = iter.next();
            JsonObject o = new JsonObject();
            o.addProperty("tick", l);
            o.addProperty("name", gameHistory.getCollectedCoins().get(l).getClass().getSimpleName());
            jsonArray.add(o);
        }

        main.add("collectedCoins", jsonArray);

        jsonArray = new JsonArray();
        for (Iterator<Long> iter = gameHistory.getCollectedPowerUps().keySet().iterator(); iter.hasNext();) {
            Long l = iter.next();
            JsonObject o = new JsonObject();
            o.addProperty("tick", l);
            o.addProperty("name", gameHistory.getCollectedPowerUps().get(l).getClass().getSimpleName());
            jsonArray.add(o);
        }

        main.add("collectedPowerUps", jsonArray);

        jsonArray = new JsonArray();
        for (Iterator<Long> iter = gameHistory.getDestroyedEnemies().keySet().iterator(); iter.hasNext();) {
            Long l = iter.next();
            JsonObject o = new JsonObject();
            o.addProperty("tick", l);
            o.addProperty("name", gameHistory.getDestroyedEnemies().get(l).getClass().getSimpleName());
            jsonArray.add(o);
        }

        main.add("destroyedEnemies", jsonArray);

        jsonArray = new JsonArray();
        for (Iterator<Long> iter = gameHistory.getFiredPulses().keySet().iterator(); iter.hasNext();) {
            Long l = iter.next();
            JsonObject o = new JsonObject();
            o.addProperty("tick", l);
            o.addProperty("name", gameHistory.getFiredPulses().get(l).getClass().getSimpleName());
            jsonArray.add(o);
        }

        main.add("firedPulses", jsonArray);

        return main;
    }
}
