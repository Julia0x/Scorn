package com.scorn.module.modules.client;

import com.scorn.Client;
import com.scorn.module.Module;
import com.scorn.module.ModuleCategory;
import com.scorn.module.setting.impl.StringSetting;
import com.scorn.utils.friends.FriendsManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Friends extends Module {
    private final StringSetting addFriend = new StringSetting("Add Friend", "");
    private final StringSetting removeFriend = new StringSetting("Remove Friend", "");
    
    private static FriendsManager friendsManager;
    
    public Friends() {
        super("Friends", "Manage your friends list", 0, ModuleCategory.CLIENT);
        this.addSettings(addFriend, removeFriend);
        
        if (friendsManager == null) {
            friendsManager = new FriendsManager();
        }
    }
    
    @Override
    public void onEnable() {
        String addName = addFriend.getValue().trim();
        String removeName = removeFriend.getValue().trim();
        
        if (!addName.isEmpty()) {
            friendsManager.addFriend(addName);
            if (mc.player != null) {
                mc.player.sendMessage(Text.literal("§a[Scorn] §fAdded §a" + addName + " §fto friends list"), false);
            }
            addFriend.setValue("");
        }
        
        if (!removeName.isEmpty()) {
            friendsManager.removeFriend(removeName);
            if (mc.player != null) {
                mc.player.sendMessage(Text.literal("§c[Scorn] §fRemoved §c" + removeName + " §ffrom friends list"), false);
            }
            removeFriend.setValue("");
        }
        
        // Show current friends
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("§e[Scorn] §fFriends (" + friendsManager.getFriendsCount() + "):"), false);
            for (String friend : friendsManager.getFriends()) {
                mc.player.sendMessage(Text.literal("§7- §a" + friend), false);
            }
        }
        
        this.toggle();
        super.onEnable();
    }
    
    public static FriendsManager getFriendsManager() {
        if (friendsManager == null) {
            friendsManager = new FriendsManager();
        }
        return friendsManager;
    }
    
    public static boolean isFriend(PlayerEntity player) {
        return getFriendsManager().isFriend(player);
    }
    
    public static boolean isFriend(String playerName) {
        return getFriendsManager().isFriend(playerName);
    }
}