/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubicmadness.bin;

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
        return x*x * (3 - 2*x);
    }
    
    public static float smootherstep(float x){
        return x*x*x*(x*(x*6 - 15) + 10);
    }
}
