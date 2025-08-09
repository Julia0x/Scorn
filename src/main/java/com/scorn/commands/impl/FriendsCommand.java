package com.scorn.commands.impl;

import com.scorn.commands.Command;
import com.scorn.module.modules.client.Friends;
import com.scorn.utils.friends.FriendsManager;
import net.minecraft.util.Formatting;

public class FriendsCommand extends Command {
    
    public FriendsCommand() {
        super("friends", new String[]{"friend", "f"});
    }

    @Override
    public void execute(String[] args) {
        FriendsManager friendsManager = Friends.getFriendsManager();
        
        if (args.length < 2) {
            sendUsage();
            return;
        }
        
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "add":
                if (args.length < 3) {
                    sendMessage("Usage: " + Formatting.WHITE + ".friends add <player>");
                    return;
                }
                String playerToAdd = args[2];
                if (friendsManager.isFriend(playerToAdd)) {
                    sendMessage(Formatting.YELLOW + playerToAdd + " is already in your friends list!");
                } else {
                    friendsManager.addFriend(playerToAdd);
                    sendMessage(Formatting.GREEN + "Added " + Formatting.WHITE + playerToAdd + Formatting.GREEN + " to friends list!");
                }
                break;
                
            case "remove":
            case "rem":
            case "del":
            case "delete":
                if (args.length < 3) {
                    sendMessage("Usage: " + Formatting.WHITE + ".friends remove <player>");
                    return;
                }
                String playerToRemove = args[2];
                if (!friendsManager.isFriend(playerToRemove)) {
                    sendMessage(Formatting.YELLOW + playerToRemove + " is not in your friends list!");
                } else {
                    friendsManager.removeFriend(playerToRemove);
                    sendMessage(Formatting.RED + "Removed " + Formatting.WHITE + playerToRemove + Formatting.RED + " from friends list!");
                }
                break;
                
            case "list":
            case "ls":
                if (friendsManager.getFriendsCount() == 0) {
                    sendMessage(Formatting.YELLOW + "Your friends list is empty!");
                } else {
                    sendMessage(Formatting.AQUA + "Friends (" + friendsManager.getFriendsCount() + "):");
                    for (String friend : friendsManager.getFriends()) {
                        sendMessage(Formatting.GRAY + "- " + Formatting.GREEN + friend);
                    }
                }
                break;
                
            case "clear":
                if (friendsManager.getFriendsCount() == 0) {
                    sendMessage(Formatting.YELLOW + "Your friends list is already empty!");
                } else {
                    int count = friendsManager.getFriendsCount();
                    friendsManager.clearFriends();
                    sendMessage(Formatting.RED + "Cleared " + count + " friends from your list!");
                }
                break;
                
            case "check":
                if (args.length < 3) {
                    sendMessage("Usage: " + Formatting.WHITE + ".friends check <player>");
                    return;
                }
                String playerToCheck = args[2];
                if (friendsManager.isFriend(playerToCheck)) {
                    sendMessage(Formatting.GREEN + playerToCheck + " is in your friends list!");
                } else {
                    sendMessage(Formatting.RED + playerToCheck + " is not in your friends list!");
                }
                break;
                
            default:
                sendUsage();
                break;
        }
    }
    
    private void sendUsage() {
        sendMessage(Formatting.AQUA + "Friends Command Usage:");
        sendMessage(Formatting.WHITE + ".friends add <player>" + Formatting.GRAY + " - Add a player to friends");
        sendMessage(Formatting.WHITE + ".friends remove <player>" + Formatting.GRAY + " - Remove a player from friends");
        sendMessage(Formatting.WHITE + ".friends list" + Formatting.GRAY + " - List all friends");
        sendMessage(Formatting.WHITE + ".friends clear" + Formatting.GRAY + " - Clear all friends");
        sendMessage(Formatting.WHITE + ".friends check <player>" + Formatting.GRAY + " - Check if player is a friend");
    }
}