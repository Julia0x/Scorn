package com.scorn.utils.render;

import net.minecraft.util.math.MathHelper;

/**
 * Utility class for smooth animations and transitions
 */
public class AnimationUtils {
    
    /**
     * Smooth step interpolation
     */
    public static float smoothStep(float edge0, float edge1, float x) {
        float t = MathHelper.clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        return t * t * (3.0f - 2.0f * t);
    }
    
    /**
     * Ease in-out animation curve
     */
    public static float easeInOut(float t) {
        return t < 0.5f ? 2 * t * t : 1 - 2 * (1 - t) * (1 - t);
    }
    
    /**
     * Bounce animation effect
     */
    public static float bounce(float t) {
        if (t < 1.0f / 2.75f) {
            return 7.5625f * t * t;
        } else if (t < 2.0f / 2.75f) {
            t -= 1.5f / 2.75f;
            return 7.5625f * t * t + 0.75f;
        } else if (t < 2.5f / 2.75f) {
            t -= 2.25f / 2.75f;
            return 7.5625f * t * t + 0.9375f;
        } else {
            t -= 2.625f / 2.75f;
            return 7.5625f * t * t + 0.984375f;
        }
    }
    
    /**
     * Elastic animation effect
     */
    public static float elastic(float t) {
        if (t == 0) return 0;
        if (t == 1) return 1;
        
        float p = 0.3f;
        float s = p / 4.0f;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t - s) * (2 * Math.PI) / p) + 1);
    }
    
    /**
     * Lerp between two values
     */
    public static float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }
    
    /**
     * Pulse animation effect
     */
    public static float pulse(float time, float speed) {
        return (float) (0.5f + 0.5f * Math.sin(time * speed));
    }
    
    /**
     * Wave animation effect
     */
    public static float wave(float time, float amplitude, float frequency) {
        return (float) (amplitude * Math.sin(time * frequency * Math.PI * 2));
    }
}