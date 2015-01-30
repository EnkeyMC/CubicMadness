package cubicmadness.bin;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Martin
 */
public class Frame extends Canvas{
    public Frame (int width, int height, String title, GamePanel game) {
        JFrame frame = new JFrame(title);
        frame.setMinimumSize(new Dimension(width / 2, height / 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        game.setPreferredSize(new Dimension(width, height));
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }
}
