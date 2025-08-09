package com.scorn.module.modules.ghost;

import com.scorn.Client;
import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.player.EventTickPre;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.modules.client.Friends;
import com.scorn.module.modules.combat.AntiBot;
import com.scorn.module.setting.impl.BooleanSetting;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.module.setting.impl.RangeSetting;
import com.scorn.utils.combat.HumanizationUtils;
import com.scorn.utils.math.RandomUtil;
import com.scorn.utils.mc.CombatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class TriggerBot extends Module {
    // Basic Settings
    public final BooleanSetting onlyPlayers = new BooleanSetting("Only Players", true);
    public final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);
    public final BooleanSetting ignoreTeam = new BooleanSetting("Ignore Team", false);
    public final BooleanSetting cooldownCheck = new BooleanSetting("Cooldown Check", true);
    public final NumberSetting minCPS = new NumberSetting("Min CPS", 1.0, 20.0, 8.0, 0.5);
    public final NumberSetting maxCPS = new NumberSetting("Max CPS", 1.0, 20.0, 12.0, 0.5);
    
    // Advanced Settings
    public final BooleanSetting randomDelay = new BooleanSetting("Random Delay", true);
    public final RangeSetting hitDelay = new RangeSetting("Hit Delay", 0, 500, 50, 150, 10);
    public final BooleanSetting smartDelay = new BooleanSetting("Smart Delay", true);
    
    // Humanization Settings
    public final BooleanSetting humanization = new BooleanSetting("Humanization", true);
    public final BooleanSetting missHits = new BooleanSetting("Miss Hits", true);
    public final NumberSetting missChance = new NumberSetting("Miss Chance", 0.01, 0.15, 0.03, 0.01);
    public final BooleanSetting burstClicking = new BooleanSetting("Burst Clicking", true);
    public final NumberSetting burstChance = new NumberSetting("Burst Chance", 0.05, 0.3, 0.15, 0.01);
    public final RangeSetting burstHits = new RangeSetting("Burst Hits", 2, 5, 2, 4, 1);
    
    // Health-based Settings
    public final BooleanSetting panicMode = new BooleanSetting("Panic Mode", true);
    public final NumberSetting panicHealth = new NumberSetting("Panic Health", 1.0, 10.0, 6.0, 0.5);
    public final NumberSetting panicCPSMultiplier = new NumberSetting("Panic CPS Multiplier", 1.1, 2.0, 1.5, 0.1);

    private long lastHitTime = 0;
    private long nextHitTime = 0;
    private boolean inBurstMode = false;
    private int burstHitsLeft = 0;
    private final HumanizationUtils.HumanizationConfig humanizationConfig = new HumanizationUtils.HumanizationConfig();

    public TriggerBot() {
        super("TriggerBot", "Advanced trigger bot with humanization and friends support", 0, ModuleCategory.GHOST);
        this.addSettings(
            onlyPlayers, ignoreFriends, ignoreTeam, cooldownCheck,
            minCPS, maxCPS, randomDelay, hitDelay, smartDelay,
            humanization, missHits, missChance, burstClicking, burstChance, burstHits,
            panicMode, panicHealth, panicCPSMultiplier
        );
        
        // Set up dependencies
        hitDelay.addDependency(randomDelay, true);
        missChance.addDependency(missHits, true);
        burstChance.addDependency(burstClicking, true);
        burstHits.addDependency(burstClicking, true);
        panicCPSMultiplier.addDependency(panicMode, true);
    }

    @Override
    public void onEnable() {
        lastHitTime = 0;
        nextHitTime = 0;
        inBurstMode = false;
        burstHitsLeft = 0;
        updateHumanizationConfig();
        HumanizationUtils.resetStats();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        lastHitTime = 0;
        nextHitTime = 0;
        inBurstMode = false;
        burstHitsLeft = 0;
        super.onDisable();
    }

    @EventLink
    public final Listener<EventTickPre> eventTickPreListener = event -> {
        if (mc.player == null || !mc.player.isAlive() || mc.player.getHealth() <= 0 || mc.player.isSpectator()) return;

        Entity targetedEntity = mc.targetedEntity;
        if (targetedEntity == null) return;

        // Check if we should attack this entity
        if (!shouldAttackEntity(targetedEntity)) return;

        // Check cooldown
        if (cooldownCheck.getValue() && !cooldownCheck()) return;

        // Check hit timing
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextHitTime) return;

        // Check for miss hits
        if (humanization.getValue() && missHits.getValue()) {
            updateHumanizationConfig();
            if (HumanizationUtils.shouldMissHit(humanizationConfig)) {
                scheduleNextHit();
                return;
            }
        }

        // Attack the entity
        hitEntity(targetedEntity);
        scheduleNextHit();
    };

    private boolean shouldAttackEntity(Entity entity) {
        if (entity.equals(mc.player) || entity.equals(mc.cameraEntity)) return false;
        if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isDead() || !entity.isAlive()) return false;
        
        // Only players check
        if (onlyPlayers.getValue() && !(entity instanceof PlayerEntity)) return false;
        
        // Player-specific checks
        if (entity instanceof PlayerEntity player) {
            // Friends check
            if (ignoreFriends.getValue() && Friends.isFriend(player)) return false;
            
            // Team check
            if (ignoreTeam.getValue() && CombatUtils.isSameTeam(player)) return false;
            
            // Bot check
            if (Client.INSTANCE.getModuleManager().getModule(AntiBot.class).isBot(player)) return false;
        }
        
        // Animal checks
        if (entity instanceof Tameable tameable && tameable.getOwnerUuid() != null && 
            tameable.getOwnerUuid().equals(mc.player.getUuid())) return false;
        
        if (entity instanceof AnimalEntity animalEntity && animalEntity.isBaby()) return false;

        return true;
    }

    private boolean cooldownCheck() {
        return mc.player.getAttackCooldownProgress(0.5f) >= 1;
    }

    private void hitEntity(Entity target) {
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        lastHitTime = System.currentTimeMillis();
        
        // Record action for humanization
        if (humanization.getValue()) {
            HumanizationUtils.recordAction();
        }
        
        // Handle burst mode
        if (inBurstMode) {
            burstHitsLeft--;
            if (burstHitsLeft <= 0) {
                inBurstMode = false;
            }
        }
    }

    private void scheduleNextHit() {
        long currentTime = System.currentTimeMillis();
        
        // Calculate base CPS
        double currentMinCPS = minCPS.getValue();
        double currentMaxCPS = maxCPS.getValue();
        
        // Apply panic mode
        if (panicMode.getValue() && mc.player.getHealth() <= panicHealth.getValue()) {
            currentMinCPS *= panicCPSMultiplier.getValue();
            currentMaxCPS *= panicCPSMultiplier.getValue();
        }
        
        // Clamp CPS values
        currentMinCPS = Math.min(currentMinCPS, 20.0);
        currentMaxCPS = Math.min(currentMaxCPS, 20.0);
        
        // Check for burst mode activation
        if (burstClicking.getValue() && !inBurstMode && 
            RandomUtil.smartRandom(0.0, 1.0) < burstChance.getValue()) {
            inBurstMode = true;
            burstHitsLeft = (int)RandomUtil.smartRandom((int)burstHits.getValueMin(), (int)burstHits.getValueMax());
            // Faster CPS during burst
            currentMinCPS *= 1.5;
            currentMaxCPS *= 1.5;
        }
        
        // Calculate delay based on CPS
        double targetCPS = RandomUtil.smartRandom(currentMinCPS, currentMaxCPS);
        long baseDelay = (long) (1000.0 / targetCPS);
        
        // Add random delay
        if (randomDelay.getValue()) {
            long extraDelay = RandomUtil.smartRandom((int)hitDelay.getValueMin(), (int)hitDelay.getValueMax());
            baseDelay += extraDelay;
        }
        
        // Smart delay - adjust based on target type and situation
        if (smartDelay.getValue()) {
            if (mc.targetedEntity instanceof PlayerEntity) {
                // Slightly faster against players
                baseDelay = (long) (baseDelay * 0.9);
            }
            
            // Adjust based on player health
            float healthPercentage = mc.player.getHealth() / mc.player.getMaxHealth();
            if (healthPercentage < 0.3f) {
                baseDelay = (long) (baseDelay * 0.8); // Faster when low health
            }
        }
        
        // Apply humanization delay
        if (humanization.getValue()) {
            updateHumanizationConfig();
            baseDelay += HumanizationUtils.getAttackDelay(humanizationConfig);
        }
        
        nextHitTime = currentTime + Math.max(baseDelay, 50); // Minimum 50ms between hits
    }

    private void updateHumanizationConfig() {
        if (!humanization.getValue()) return;
        
        humanizationConfig.enableMissHits = missHits.getValue();
        humanizationConfig.baseMissChance = missChance.getValue();
        humanizationConfig.enableAttackDelay = randomDelay.getValue();
        humanizationConfig.minAttackDelay = (int) hitDelay.getValueMin();
        humanizationConfig.maxAttackDelay = (int) hitDelay.getValueMax();
        
        // Increase miss chance when panicking
        if (panicMode.getValue() && mc.player.getHealth() <= panicHealth.getValue()) {
            humanizationConfig.stressMissChance = missChance.getValue() * 2;
        }
    }
}
