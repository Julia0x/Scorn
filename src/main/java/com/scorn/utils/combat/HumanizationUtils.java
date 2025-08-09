package com.scorn.utils.combat;

import com.scorn.utils.math.RandomUtil;
import net.minecraft.client.MinecraftClient;

import java.util.Random;

public class HumanizationUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Random random = new Random();
    
    // Miss chance settings
    public static class HumanizationConfig {
        public boolean enableMissHits = true;
        public double baseMissChance = 0.05; // 5% base miss chance
        public double stressMissChance = 0.15; // 15% miss chance when stressed
        public double lowHealthMissChance = 0.10; // 10% miss chance when low health
        
        public boolean enableAttackDelay = true;
        public int minAttackDelay = 50; // milliseconds
        public int maxAttackDelay = 150; // milliseconds
        
        public boolean enableRotationError = true;
        public double maxRotationError = 2.0; // degrees
        public double minRotationError = 0.5; // degrees
        
        public boolean enableReactionTime = true;
        public int minReactionTime = 100; // milliseconds
        public int maxReactionTime = 300; // milliseconds
        
        public boolean enableFatigue = true;
        public long fatigueStartTime = 60000; // 1 minute of continuous use
        public double fatigueMultiplier = 1.5; // multiply miss chance by this when fatigued
    }
    
    private static long lastActionTime = 0;
    private static long startTime = System.currentTimeMillis();
    private static int consecutiveActions = 0;
    
    public static boolean shouldMissHit(HumanizationConfig config) {
        if (!config.enableMissHits) return false;
        
        double missChance = config.baseMissChance;
        
        // Increase miss chance based on player health (panic/stress)
        if (mc.player != null) {
            float healthPercentage = mc.player.getHealth() / mc.player.getMaxHealth();
            if (healthPercentage < 0.3f) { // Low health
                missChance += config.lowHealthMissChance;
            }
            if (healthPercentage < 0.1f) { // Critical health
                missChance += config.stressMissChance;
            }
        }
        
        // Fatigue system - increase miss chance over time
        if (config.enableFatigue) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > config.fatigueStartTime) {
                missChance *= config.fatigueMultiplier;
            }
        }
        
        // Consecutive action penalty
        if (consecutiveActions > 10) {
            missChance += 0.02 * (consecutiveActions - 10); // 2% per action over 10
        }
        
        return random.nextDouble() < missChance;
    }
    
    public static int getAttackDelay(HumanizationConfig config) {
        if (!config.enableAttackDelay) return 0;
        
        int baseDelay = (int)RandomUtil.smartRandom(config.minAttackDelay, config.maxAttackDelay);
        
        // Add variation based on consecutive actions
        if (consecutiveActions > 5) {
            baseDelay += random.nextInt(50); // Up to 50ms extra delay
        }
        
        return baseDelay;
    }
    
    public static float[] addRotationError(float[] rotations, HumanizationConfig config) {
        if (!config.enableRotationError) return rotations;
        
        double errorRange = RandomUtil.smartRandom(config.minRotationError, config.maxRotationError);
        
        // Reduce error based on distance to target (closer = more accurate)
        if (mc.player != null && mc.targetedEntity != null) {
            double distance = mc.player.distanceTo(mc.targetedEntity);
            if (distance < 2.0) {
                errorRange *= 0.5; // Half error when very close
            } else if (distance > 4.0) {
                errorRange *= 1.5; // More error when far
            }
        }
        
        float yawError = (float) (random.nextGaussian() * errorRange);
        float pitchError = (float) (random.nextGaussian() * errorRange * 0.7); // Less pitch error
        
        return new float[]{
            rotations[0] + yawError,
            rotations[1] + pitchError
        };
    }
    
    public static int getReactionTime(HumanizationConfig config) {
        if (!config.enableReactionTime) return 0;
        
        int reactionTime = (int)RandomUtil.smartRandom(config.minReactionTime, config.maxReactionTime);
        
        // Faster reactions when health is low (adrenaline)
        if (mc.player != null) {
            float healthPercentage = mc.player.getHealth() / mc.player.getMaxHealth();
            if (healthPercentage < 0.3f) {
                reactionTime = (int) (reactionTime * 0.8); // 20% faster when low health
            }
        }
        
        // Slower reactions when fatigued
        long currentTime = System.currentTimeMillis();
        if (config.enableFatigue && currentTime - startTime > config.fatigueStartTime) {
            reactionTime = (int) (reactionTime * 1.3); // 30% slower when fatigued
        }
        
        return reactionTime;
    }
    
    public static void recordAction() {
        long currentTime = System.currentTimeMillis();
        
        // Reset consecutive counter if there's been a break
        if (currentTime - lastActionTime > 5000) { // 5 second break
            consecutiveActions = 0;
            startTime = currentTime; // Reset fatigue timer
        } else {
            consecutiveActions++;
        }
        
        lastActionTime = currentTime;
    }
    
    public static boolean shouldSkipAction(double probability) {
        return random.nextDouble() < probability;
    }
    
    public static void addRandomMovement() {
        if (mc.player == null) return;
        
        // Small random mouse movements to simulate human imperfection
        if (random.nextDouble() < 0.1) { // 10% chance
            float randomYaw = (float) (random.nextGaussian() * 0.5);
            float randomPitch = (float) (random.nextGaussian() * 0.3);
            
            mc.player.setYaw(mc.player.getYaw() + randomYaw);
            mc.player.setPitch(mc.player.getPitch() + randomPitch);
        }
    }
    
    public static void resetStats() {
        consecutiveActions = 0;
        startTime = System.currentTimeMillis();
        lastActionTime = 0;
    }
}