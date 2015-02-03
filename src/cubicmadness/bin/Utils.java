/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author Martin
 */
public class Utils {
    public static float clamp(float n, float min, float max){
        if(n < min){
            return min;
        }else if(n > max){
            return max;
        }else{
            return n;
        }
    }
    
    public static float cycle(float n, float min, float max){
        if(n < min){
            return max;
        }else if(n > max){
            return min;
        }else{
            return n;
        }
    }
    
    public static float smoothstep(float x)
    {
        return smoothstep(x, 0, 1);
    }
    
    public static float smoothstep(float x, float edge0, float edge1){
        x = clamp((x - edge0)/(edge1 - edge0), 0.0f, 1.0f); 
        return x*x * (3 - 2*x);
    }
    
    public static float smootherstep(float x){
        return smootherstep(x, 0, 1);
    }
    
    public static float smootherstep(float x, float edge0, float edge1){
        x = clamp((x - edge0)/(edge1 - edge0), 0.0f, 1.0f); 
        return x*x*x*(x*(x*6 - 15) + 10);
    }
    
    public static Color interpolatedColor(double interpolation, Color c1, Color c2){
        int r,g,b, alpha;
        r = (int)Math.round(c1.getRed() * interpolation + c2.getRed() * (1 - interpolation));
        g = (int)Math.round(c1.getGreen() * interpolation + c2.getGreen() * (1 - interpolation));
        b = (int)Math.round(c1.getBlue() * interpolation + c2.getBlue() * (1 - interpolation));
        alpha = (int)Math.round(c1.getAlpha() * interpolation + c2.getAlpha() * (1 - interpolation));
        return new Color(r,g,b, alpha);
    }
    
    public static void drawString(Graphics g, String s, int x, int y, int width)
{
	// FontMetrics gives us information about the width,
	// height, etc. of the current Graphics object's Font.
	FontMetrics fm = g.getFontMetrics();

	int lineHeight = fm.getHeight();

	int curX = x;
	int curY = y;

	String[] words = s.split(" ");

	for (String word : words)
	{
		// Find out thw width of the word.
		int wordWidth = fm.stringWidth(word + " ");

		// If text exceeds the width, then move to next line.
		if (curX + wordWidth >= x + width)
		{
			curY += lineHeight;
			curX = x;
		}

		g.drawString(word, curX, curY);

		// Move over to the right for next word.
		curX += wordWidth;
	}
}
}
