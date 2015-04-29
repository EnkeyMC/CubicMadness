/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cubicmadness.jsonhandlers.ConfigDeserializer;
import cubicmadness.jsonhandlers.ConfigSerializer;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.Proxy;

/**
 *
 * @author Martin
 */
public class Config {
    
    // VERSION
    public static final String VERSION = "0.9.0";
    
    // GRAPHICS OPTIONS
    public static boolean antialiasing = true;
    public static boolean antialiasingText = true;
    public static boolean transitions = true;
    public static boolean rendering = true;
    public static boolean fullscreen = false;
    
    // CONTROLS
    public static int UP = KeyEvent.VK_W;
    public static int DOWN = KeyEvent.VK_S;
    public static int LEFT = KeyEvent.VK_A;
    public static int RIGHT = KeyEvent.VK_D;
    public static int ATTACK = KeyEvent.VK_SPACE;
    
    // OTHER
    public static final int MAX_NICK_LEN = 20;
    public static final int MIN_NICK_LEN = 4;
    
    public static Proxy proxy = null;

    public static boolean loadConfig(){
        try {
            // read the json file
            FileReader reader = new FileReader("config.json");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Config.class, new ConfigDeserializer())
                    .create();
            gson.fromJson(reader, Config.class);
            System.out.println("file loaded");
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("error loading");
            return false;
        }
    }

    public static boolean saveConfig(){
        try {
            FileWriter writer;
            writer = new FileWriter("config.json");

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(Config.class, new ConfigSerializer()).setPrettyPrinting();
            gson.create().toJson(new Config(), new TypeToken<Config>() {
            }.getType(), writer);
            writer.close();
            System.out.println("file saved");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println("error saving");
            return false;
        }
    }
}
