package cubicmadness.jsonhandlers;

import com.google.gson.*;
import cubicmadness.bin.Config;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created by Martin on 22.4.2015.
 */
public class ConfigDeserializer implements JsonDeserializer<Config> {
    @Override
    public Config deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject json = jsonElement.getAsJsonObject();

        Config.antialiasing = json.get("antialiasing").getAsBoolean();
        Config.antialiasingText = json.get("antialiasingText").getAsBoolean();
        Config.transitions = json.get("transitions").getAsBoolean();
        Config.rendering = json.get("rendering").getAsBoolean();
        Config.fullscreen = json.get("fullscreen").getAsBoolean();
        Config.UP = json.get("UP").getAsInt();
        Config.DOWN = json.get("DOWN").getAsInt();
        Config.LEFT = json.get("LEFT").getAsInt();
        Config.ATTACK = json.get("ATTACK").getAsInt();

        String proxy = json.get("proxy").getAsString();
        if(proxy.equals("null"))
            Config.proxy = null;
        else{
            proxy = proxy.substring(proxy.lastIndexOf("/") + 1);
            String port = proxy.substring(proxy.lastIndexOf(":") + 1);
            proxy = proxy.substring(0, proxy.lastIndexOf(":") - 1);
            Config.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, Integer.parseInt(port)));
        }

        return null;
    }
}
