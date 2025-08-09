package com.scorn.utils.combat;

import com.scorn.Client;
import com.scorn.module.modules.client.Friends;
import com.scorn.module.modules.combat.AntiBot;
import com.scorn.utils.math.MathUtils;
import com.scorn.utils.mc.CombatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TargetingUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public enum TargetPriority {
        DISTANCE,
        HEALTH,
        ANGLE,
        FOV,
        ARMOR,
        HYBRID
    }
    
    public static class TargetingConfig {
        public double maxRange = 6.0;
        public double maxAngle = 180.0;
        public boolean ignoreFriends = true;
        public boolean ignoreTeam = false;
        public boolean ignoreBots = true;
        public boolean ignoreInvisible = false;
        public boolean throughWalls = false;
        public TargetPriority priority = TargetPriority.HYBRID;
        public boolean lockTarget = true;
        public int lockTime = 1000; // milliseconds
    }
    
    private static PlayerEntity currentTarget = null;
    private static long targetLockTime = 0;
    
    public static PlayerEntity getBestTarget(TargetingConfig config) {
        if (mc.player == null || mc.world == null) return null;
        
        // Check if we should keep current target (target locking)
        if (config.lockTarget && currentTarget != null && isValidTarget(currentTarget, config)) {
            if (System.currentTimeMillis() - targetLockTime < config.lockTime) {
                return currentTarget;
            }
        }
        
        List<PlayerEntity> potentialTargets = new ArrayList<>();
        
        // Collect all potential targets
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (isValidTarget(player, config)) {
                potentialTargets.add(player);
            }
        }
        
        if (potentialTargets.isEmpty()) {
            currentTarget = null;
            return null;
        }
        
        // Sort targets based on priority
        PlayerEntity bestTarget = switch (config.priority) {
            case DISTANCE -> potentialTargets.stream()
                    .min(Comparator.comparingDouble(TargetingUtils::getDistance))
                    .orElse(null);
            case HEALTH -> potentialTargets.stream()
                    .min(Comparator.comparingDouble(player -> player.getHealth() + player.getAbsorptionAmount()))
                    .orElse(null);
            case ANGLE -> potentialTargets.stream()
                    .min(Comparator.comparingDouble(TargetingUtils::getAngleToTarget))
                    .orElse(null);
            case FOV -> potentialTargets.stream()
                    .min(Comparator.comparingDouble(TargetingUtils::getFovToTarget))
                    .orElse(null);
            case ARMOR -> potentialTargets.stream()
                    .min(Comparator.comparingDouble(TargetingUtils::getArmorValue))
                    .orElse(null);
            case HYBRID -> getHybridTarget(potentialTargets);
        };
        
        // Update target lock
        if (bestTarget != currentTarget) {
            currentTarget = bestTarget;
            targetLockTime = System.currentTimeMillis();
        }
        
        return bestTarget;
    }
    
    private static PlayerEntity getHybridTarget(List<PlayerEntity> targets) {
        return targets.stream()
                .min((p1, p2) -> {
                    double score1 = calculateHybridScore(p1);
                    double score2 = calculateHybridScore(p2);
                    return Double.compare(score1, score2);
                })
                .orElse(null);
    }
    
    private static double calculateHybridScore(PlayerEntity player) {
        double distance = getDistance(player);
        double health = player.getHealth() + player.getAbsorptionAmount();
        double angle = getAngleToTarget(player);
        double armor = getArmorValue(player);
        
        // Normalize and weight the factors
        double distanceScore = (distance / 6.0) * 0.3; // 30% weight
        double healthScore = (health / 40.0) * 0.25;   // 25% weight  
        double angleScore = (angle / 180.0) * 0.25;    // 25% weight
        double armorScore = (armor / 20.0) * 0.2;      // 20% weight
        
        return distanceScore + healthScore + angleScore + armorScore;
    }
    
    private static boolean isValidTarget(PlayerEntity player, TargetingConfig config) {
        if (player == null || player == mc.player) return false;
        if (player.isDead() || player.getHealth() <= 0) return false;
        if (player.isSpectator() || player.isCreative()) return false;
        
        // Distance check
        double distance = getDistance(player);
        if (distance > config.maxRange) return false;
        
        // Angle check
        if (getAngleToTarget(player) > config.maxAngle) return false;
        
        // Friends check
        if (config.ignoreFriends && Friends.isFriend(player)) return false;
        
        // Team check
        if (config.ignoreTeam && CombatUtils.isSameTeam(player)) return false;
        
        // Bot check
        if (config.ignoreBots && Client.INSTANCE.getModuleManager().getModule(AntiBot.class).isBot(player)) return false;
        
        // Invisible check
        if (config.ignoreInvisible && player.isInvisible()) return false;
        
        // Wall check
        if (!config.throughWalls && !canSeeTarget(player)) return false;
        
        return true;
    }
    
    private static boolean canSeeTarget(PlayerEntity target) {
        // Simple line of sight check
        return mc.world.raycast(new net.minecraft.world.RaycastContext(
                mc.player.getCameraPosVec(1.0f),
                target.getCameraPosVec(1.0f),
                net.minecraft.world.RaycastContext.ShapeType.COLLIDER,
                net.minecraft.world.RaycastContext.FluidHandling.NONE,
                mc.player
        )).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
    }
    
    public static double getDistance(PlayerEntity player) {
        return CombatUtils.getDistanceToEntity(player);
    }
    
    public static double getAngleToTarget(PlayerEntity target) {
        float[] rotations = getRotationsTo(target);
        float yawDiff = MathHelper.wrapDegrees(rotations[0] - mc.player.getYaw());
        float pitchDiff = MathHelper.wrapDegrees(rotations[1] - mc.player.getPitch());
        return Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
    }
    
    public static double getFovToTarget(PlayerEntity target) {
        return CombatUtils.fovFromEntity(target);
    }
    
    public static double getArmorValue(PlayerEntity player) {
        return player.getArmor();
    }
    
    public static float[] getRotationsTo(PlayerEntity target) {
        if (target == null) return new float[]{0, 0};
        
        double deltaX = target.getX() - mc.player.getX();
        double deltaY = target.getY() + target.getEyeHeight(target.getPose()) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double deltaZ = target.getZ() - mc.player.getZ();
        
        double distance = MathUtils.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        
        float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) -(Math.atan2(deltaY, distance) * 180.0 / Math.PI);
        
        return new float[]{yaw, pitch};
    }
    
    public static void clearTarget() {
        currentTarget = null;
        targetLockTime = 0;
    }
    
    public static PlayerEntity getCurrentTarget() {
        return currentTarget;
    }
}