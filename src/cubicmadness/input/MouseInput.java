/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Martin
 */
public class MouseInput extends MouseAdapter implements MouseMotionListener{
    
    public static Point mouseXY = new Point(0,0);
    public static Point mousePrevXY = new Point(0,0);
    public static boolean LMB = false;
    public static boolean RMB = false;
    
    @Override
    public void mouseMoved(MouseEvent e){
        mousePrevXY = mouseXY;
        mouseXY = e.getPoint();
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getButton() == 1) LMB = true;
        if(e.getButton() == 3) RMB = true;        
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        if(e.getButton() == 1) LMB = false;
        if(e.getButton() == 3) RMB = false;   
    }
}
