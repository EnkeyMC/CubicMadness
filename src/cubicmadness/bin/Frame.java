package cubicmadness.bin;

import javax.swing.JFrame;

/**
 *
 * @author Martin
 */
public class Frame extends JFrame{
    public Frame (){
        this.setTitle("Cubic Madness");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.setResizable(false);
        this.pack();
    }
}
