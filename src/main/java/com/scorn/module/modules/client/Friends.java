package com.scorn.module.modules.client;

import com.scorn.utils.friends.FriendsManager;
import net.minecraft.entity.player.PlayerEntity;

public class Friends {
    private static FriendsManager friendsManager;
    
    static {
        friendsManager = new FriendsManager();
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