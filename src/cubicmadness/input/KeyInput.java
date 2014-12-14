/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Martin
 */
public class KeyInput extends KeyAdapter {
    
    public static final Set<Integer> pressed = new HashSet<>();
    
    @Override
    public void keyPressed (KeyEvent e){
        pressed.add(e.getKeyCode());
    }
    
    @Override
    public void keyReleased (KeyEvent e){
        pressed.remove(e.getKeyCode());
    }
}
