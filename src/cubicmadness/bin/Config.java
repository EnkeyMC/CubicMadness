/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

import java.awt.event.KeyEvent;

/**
 *
 * @author Martin
 */
public class Config {
    
    // VERSION
    public static final String VERSION = "0.8.0";
    
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
}
