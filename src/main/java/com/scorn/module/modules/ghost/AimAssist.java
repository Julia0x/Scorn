package com.scorn.module.modules.ghost;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.render.EventRender3D;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.client.Friends;
import com.scorn.module.modules.combat.AntiBot;
import com.scorn.module.setting.impl.BooleanSetting;
import com.scorn.module.setting.impl.ModeSetting;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.RangeSetting;
import com.scorn.utils.combat.HumanizationUtils;
import com.scorn.utils.combat.TargetingUtils;
import com.scorn.utils.math.MathUtils;
import com.scorn.utils.math.RandomUtil;
import com.scorn.utils.mc.CombatUtils;
import com.scorn.utils.rotation.RotationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimAssist extends Module {
    // Basic Settings
    public final BooleanSetting clickOnly = new BooleanSetting("Click Only", true);
    public final BooleanSetting fovBased = new BooleanSetting("FOV Based", true);
    public final BooleanSetting pitchAssist = new BooleanSetting("Pitch Assist", true);
    public final BooleanSetting breakBlocks = new BooleanSetting("Break Blocks", false);
    
    // Targeting Settings
    public final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);
    public final BooleanSetting ignoreTeam = new BooleanSetting("Ignore Team", false);
    public final BooleanSetting targetLock = new BooleanSetting("Target Lock", true);
    public final NumberSetting lockTime = new NumberSetting("Lock Time", 500, 2000, 1000, 50);
    public final NumberSetting maxRange = new NumberSetting("Max Range", 3.0, 8.0, 6.0, 0.1);
    public final NumberSetting maxAngle = new NumberSetting("Max Angle", 30.0, 180.0, 90.0, 5.0);
    
    // Priority Settings
    public final ModeSetting priority = new ModeSetting("Priority", "Hybrid", "Distance", "Health", "Angle", "FOV", "Armor", "Hybrid");
    
    // Speed Settings
    public final RangeSetting rotSpeedV = new RangeSetting("Speed", 1, 100, 20, 50, 1);
    public final BooleanSetting smoothing = new BooleanSetting("Smoothing", true);
    public final NumberSetting smoothingFactor = new NumberSetting("Smoothing Factor", 0.1, 1.0, 0.5, 0.1);
    
    // Humanization Settings
    public final BooleanSetting humanization = new BooleanSetting("Humanization", true);
    public final BooleanSetting missHits = new BooleanSetting("Miss Hits", true);
    public final NumberSetting missChance = new NumberSetting("Miss Chance", 0.01, 0.2, 0.05, 0.01);
    public final BooleanSetting rotationError = new BooleanSetting("Rotation Error", true);
    public final NumberSetting maxRotationError = new NumberSetting("Max Rotation Error", 0.5, 5.0, 2.0, 0.1);
    public final BooleanSetting reactionTime = new BooleanSetting("Reaction Time", true);
    public final RangeSetting reactionDelay = new RangeSetting("Reaction Delay", 50, 500, 100, 250, 10);

    private final TargetingUtils.TargetingConfig targetingConfig = new TargetingUtils.TargetingConfig();
    private final HumanizationUtils.HumanizationConfig humanizationConfig = new HumanizationUtils.HumanizationConfig();
    
    private PlayerEntity currentTarget = null;
    private long lastTargetTime = 0;
    private long nextActionTime = 0;
    private boolean shouldMiss = false;

    public AimAssist() {
        super("AimAssist", "Advanced aim assistance with humanization", 0, ModuleCategory.GHOST);
        this.addSettings(
            clickOnly, fovBased, pitchAssist, breakBlocks,
            ignoreFriends, ignoreTeam, targetLock, lockTime, maxRange, maxAngle,
            priority, rotSpeedV, smoothing, smoothingFactor,
            humanization, missHits, missChance, rotationError, maxRotationError,
            reactionTime, reactionDelay
        );
        
        // Set up dependencies
        lockTime.addDependency(targetLock, true);
        smoothingFactor.addDependency(smoothing, true);
        missChance.addDependency(missHits, true);
        maxRotationError.addDependency(rotationError, true);
        reactionDelay.addDependency(reactionTime, true);
    }

    @Override
    public void onEnable() {
        updateConfigs();
        TargetingUtils.clearTarget();
        HumanizationUtils.resetStats();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        currentTarget = null;
        TargetingUtils.clearTarget();
        super.onDisable();
    }

    @EventLink
    public Listener<EventRender3D> eventTickPreListener = event -> {
        if (isNull()) return;
        if (mc.currentScreen != null) return;
        if (clickOnly.getValue() && !mc.options.attackKey.isPressed()) return;
        if (breakBlocks.getValue() && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) return;
        
        // Check reaction time delay
        if (System.currentTimeMillis() < nextActionTime) return;
        
        updateConfigs();
        
        // Get target using advanced targeting system
        PlayerEntity target = TargetingUtils.getBestTarget(targetingConfig);
        if (target == null) return;
        
        // Update current target
        if (target != currentTarget) {
            currentTarget = target;
            lastTargetTime = System.currentTimeMillis();
            
            // Set reaction time for new target
            if (reactionTime.getValue() && humanization.getValue()) {
                nextActionTime = System.currentTimeMillis() + 
                    RandomUtil.smartRandom((int)reactionDelay.getValueMin(), (int)reactionDelay.getValueMax());
                return;
            }
        }
        
        // Check if we should miss this attempt
        if (humanization.getValue() && missHits.getValue()) {
            if (shouldMiss || HumanizationUtils.shouldMissHit(humanizationConfig)) {
                shouldMiss = true;
                // Skip this rotation but reset miss flag for next time
                if (RandomUtil.smartRandom(0, 100) < 30) { // 30% chance to reset miss
                    shouldMiss = false;
                }
                return;
            }
        }
        
        // Calculate rotations to target
        float[] targetRotations = TargetingUtils.getRotationsTo(target);
        if (targetRotations == null) return;
        
        // Apply humanization errors
        if (humanization.getValue() && rotationError.getValue()) {
            targetRotations = HumanizationUtils.addRotationError(targetRotations, humanizationConfig);
        }
        
        // Calculate rotation speed with FOV consideration
        double fov = CombatUtils.fovFromEntity(target);
        double rotSpeed = RandomUtil.smartRandom(rotSpeedV.getValueMin(), rotSpeedV.getValueMax());
        
        if (fovBased.getValue()) {
            rotSpeed *= (Math.abs(fov) * 2 / 180);
        }
        
        // Apply smoothing
        if (smoothing.getValue()) {
            rotSpeed *= smoothingFactor.getValue();
        }
        
        // Get current rotations
        float currentYaw = mc.player.getYaw();
        float currentPitch = mc.player.getPitch();
        
        // Calculate final rotations
        float[] finalRotations = RotationUtils.getPatchedAndCappedRots(
            new float[]{currentYaw, currentPitch},
            new float[]{targetRotations[0], pitchAssist.getValue() ? targetRotations[1] : currentPitch},
            (float) rotSpeed
        );
        
        // Apply rotations
        mc.player.setYaw(finalRotations[0]);
        if (pitchAssist.getValue()) {
            mc.player.setPitch(finalRotations[1]);
        }
        
        // Record action for humanization
        if (humanization.getValue()) {
            HumanizationUtils.recordAction();
        }
        
        // Add random movement for humanization
        if (humanization.getValue()) {
            HumanizationUtils.addRandomMovement();
        }
    };

    private void updateConfigs() {
        // Update targeting config
        targetingConfig.maxRange = maxRange.getValue();
        targetingConfig.maxAngle = maxAngle.getValue();
        targetingConfig.ignoreFriends = ignoreFriends.getValue();
        targetingConfig.ignoreTeam = ignoreTeam.getValue();
        targetingConfig.lockTarget = targetLock.getValue();
        targetingConfig.lockTime = (int) lockTime.getValue();
        
        // Update priority
        targetingConfig.priority = switch (priority.getMode()) {
            case "Distance" -> TargetingUtils.TargetPriority.DISTANCE;
            case "Health" -> TargetingUtils.TargetPriority.HEALTH;
            case "Angle" -> TargetingUtils.TargetPriority.ANGLE;
            case "FOV" -> TargetingUtils.TargetPriority.FOV;
            case "Armor" -> TargetingUtils.TargetPriority.ARMOR;
            default -> TargetingUtils.TargetPriority.HYBRID;
        };
        
        // Update humanization config
        if (humanization.getValue()) {
            humanizationConfig.enableMissHits = missHits.getValue();
            humanizationConfig.baseMissChance = missChance.getValue();
            humanizationConfig.enableRotationError = rotationError.getValue();
            humanizationConfig.maxRotationError = maxRotationError.getValue();
            humanizationConfig.enableReactionTime = reactionTime.getValue();
            humanizationConfig.minReactionTime = (int) reactionDelay.getValueMin();
            humanizationConfig.maxReactionTime = (int) reactionDelay.getValueMax();
        }
    }

    public PlayerEntity getCurrentTarget() {
        return currentTarget;
    }
}
