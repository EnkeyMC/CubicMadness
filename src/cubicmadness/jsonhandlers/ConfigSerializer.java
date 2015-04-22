package cubicmadness.jsonhandlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cubicmadness.bin.Config;

import java.lang.reflect.Type;

/**
 * Created by Martin on 22.4.2015.
 */
public class ConfigSerializer implements JsonSerializer<Config> {
    @Override
    public JsonElement serialize(Config config, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject json = new JsonObject();
        json.addProperty("antialiasing", Config.antialiasing);
        json.addProperty("antialiasingText", Config.antialiasingText);
        json.addProperty("transitions", Config.transitions);
        json.addProperty("rendering", Config.rendering);
        json.addProperty("fullscreen", Config.fullscreen);
        json.addProperty("UP", Config.UP);
        json.addProperty("DOWN", Config.DOWN);
        json.addProperty("LEFT", Config.LEFT);
        json.addProperty("ATTAC", Config.ATTACK);
        json.addProperty("proxy", Config.proxy == null ? "null" : Config.proxy.address() + "");

        return json;
    }
}
