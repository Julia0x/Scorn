package com.scorn.module.modules.movement.longjump;

import com.scorn.module.modules.movement.LongJump;
import com.scorn.module.setting.impl.newmodesetting.SubMode;

public class DoubleJumpLongJump extends SubMode<LongJump> {
    public DoubleJumpLongJump(String name, LongJump parentModule) {
        super(name, parentModule);
    }

    @Override
    public void onEnable() {
        mc.player.setJumping(false);
        mc.player.jump();
        mc.player.jump();
        getParentModule().toggle();
        super.onEnable();
    }
}
