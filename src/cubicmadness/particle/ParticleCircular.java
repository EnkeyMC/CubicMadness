package cubicmadness.particle;

import cubicmadness.bin.GameObject;
import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author Martin
 */
public class ParticleCircular extends ParticleTrail{
    
    private final Dimension circleSize;

    public ParticleCircular(GamePanel gp, int life, Color c, Point center, Dimension circleSize, Dimension minSize, Dimension maxSize, GameObject o, int colorVariation) {
        super(gp, life, c, center, 0, 0, minSize, maxSize, o, colorVariation);
        this.circleSize = circleSize;
    }
    
}
