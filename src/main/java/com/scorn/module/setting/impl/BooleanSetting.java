package com.scorn.module.setting.impl;

import com.scorn.module.setting.Setting;
import lombok.Setter;

@Setter
public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public void toggle() {
        setValue(!value);
    }

    public boolean getValue() {
        return value;
    }

}
