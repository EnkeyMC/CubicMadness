/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.enemy;

import cubicmadness.bin.GamePanel;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author Martin
 */
public class EnemyFollowing extends EnemyBasic{
    
    private final float gravity = 0.3f;

    public EnemyFollowing(GamePanel panel) {
        super(panel);
        this.speed = 10f;
        this.color = new Color(115,88,140);
        this.size = new Dimension(7,7);
    }    
    
    @Override
    public void tick(){
        super.tick();
        switch(this.direction){
            case "UP":
            case "DOWN":
                velX += Math.copySign(gravity, this.panel.objects.player.getRect().getCenterX() - this.getRect().getCenterX());
                break;
            case "LEFT":
            case "RIGHT":
                velY += Math.copySign(gravity, this.panel.objects.player.getRect().getCenterY() - this.getRect().getCenterY());
        }
    }
}
