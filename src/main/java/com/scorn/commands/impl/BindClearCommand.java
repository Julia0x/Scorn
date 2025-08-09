package com.scorn.commands.impl;

import com.scorn.Client;
import com.scorn.commands.Command;
import com.scorn.module.Module;

public class BindClearCommand extends Command {
    public BindClearCommand() {
        super("bindclear");
    }

    @Override
    public void execute(String[] commands) {
        for (Module module : Client.INSTANCE.getModuleManager().getModules()) {
            module.setKey(0);
        }
        sendMessage("Reset all binds");
    }
}
