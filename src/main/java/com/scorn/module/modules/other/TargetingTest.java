package com.scorn.module.modules.other;

import com.scorn.event.bus.Listener;
import com.scorn.event.bus.annotations.EventLink;
import com.scorn.event.impl.render.EventRender3D;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.modules.client.Friends;
import com.scorn.module.setting.impl.BooleanSetting;
import com.scorn.module.setting.impl.ModeSetting;
import com.scorn.module.setting.impl.NumberSetting;
import com.scorn.utils.combat.TargetingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TargetingTest extends Module {
    public final ModeSetting priority = new ModeSetting("Priority", "Hybrid", "Distance", "Health", "Angle", "FOV", "Armor", "Hybrid");
    public final NumberSetting maxRange = new NumberSetting("Max Range", 3.0, 10.0, 6.0, 0.5);
    public final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);
    public final BooleanSetting showInfo = new BooleanSetting("Show Info", true);
    
    private final TargetingUtils.TargetingConfig config = new TargetingUtils.TargetingConfig();
    private PlayerEntity lastTarget = null;
    private long lastInfoTime = 0;

    public TargetingTest() {
        super("TargetingTest", "Test the advanced targeting system", 0, ModuleCategory.OTHER);
        this.addSettings(priority, maxRange, ignoreFriends, showInfo);
    }

    @Override
    public void onEnable() {
        TargetingUtils.clearTarget();
        super.onEnable();
    }

    @EventLink
    public Listener<EventRender3D> onRender3D = event -> {
        if (isNull()) return;
        
        updateConfig();
        
        PlayerEntity target = TargetingUtils.getBestTarget(config);
        
        if (target != lastTarget) {
            lastTarget = target;
            if (target != null && showInfo.getValue()) {
                sendTargetInfo(target);
            }
        }
        
        // Show periodic info
        if (target != null && showInfo.getValue() && System.currentTimeMillis() - lastInfoTime > 3000) {
            sendDetailedInfo(target);
            lastInfoTime = System.currentTimeMillis();
        }
    };
    
    private void updateConfig() {
        config.maxRange = maxRange.getValue();
        config.ignoreFriends = ignoreFriends.getValue();
        config.priority = switch (priority.getMode()) {
            case "Distance" -> TargetingUtils.TargetPriority.DISTANCE;
            case "Health" -> TargetingUtils.TargetPriority.HEALTH;
            case "Angle" -> TargetingUtils.TargetPriority.ANGLE;
            case "FOV" -> TargetingUtils.TargetPriority.FOV;
            case "Armor" -> TargetingUtils.TargetPriority.ARMOR;
            default -> TargetingUtils.TargetPriority.HYBRID;
        };
    }
    
    private void sendTargetInfo(PlayerEntity target) {
        if (mc.player == null) return;
        
        String friendStatus = Friends.isFriend(target) ? Formatting.GREEN + "[FRIEND]" : Formatting.RED + "[ENEMY]";
        String message = Formatting.AQUA + "[Targeting] " + Formatting.WHITE + "New target: " + 
                        friendStatus + " " + Formatting.YELLOW + target.getName().getString();
        
        mc.player.sendMessage(Text.literal(message), false);
    }
    
    private void sendDetailedInfo(PlayerEntity target) {
        if (mc.player == null) return;
        
        double distance = TargetingUtils.getDistance(target);
        double health = target.getHealth() + target.getAbsorptionAmount();
        double angle = TargetingUtils.getAngleToTarget(target);
        double fov = TargetingUtils.getFovToTarget(target);
        double armor = TargetingUtils.getArmorValue(target);
        
        String info = String.format(
            Formatting.GRAY + "[Info] " + Formatting.WHITE + "%s: " +
            Formatting.AQUA + "Dist: %.1f " +
            Formatting.RED + "HP: %.1f " +
            Formatting.YELLOW + "Angle: %.1f° " +
            Formatting.GREEN + "FOV: %.1f° " +
            Formatting.BLUE + "Armor: %.0f",
            target.getName().getString(), distance, health, angle, fov, armor
        );
        
        mc.player.sendMessage(Text.literal(info), false);
    }
}