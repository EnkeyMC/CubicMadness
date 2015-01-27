/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.powerup;

import cubicmadness.bin.GamePanel;
import cubicmadness.gamestates.GameState;
import java.awt.Color;

/**
 *
 * @author Martin
 */
public class BoxPulse extends BoxLife{
    public BoxPulse(GamePanel gp, GameState gs, int life, float x, float y) {
        super(gp, gs, life, x, y);
        this.ef = new Effect(Effect.PULSE);
        this.c1 = new Color(150,150,150);
        this.c2 = c1.darker();
    }
}
