package com.scorn.commands.impl;

import com.scorn.commands.Command;
import com.scorn.module.modules.client.Friends;
import com.scorn.utils.friends.FriendsManager;
import com.scorn.utils.mc.ChatUtils;
import net.minecraft.util.Formatting;

public class FriendsCommand extends Command {
    
    public FriendsCommand() {
        super("friends", new String[]{"friend", "f"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            sendUsage();
            return;
        }
        
        FriendsManager friendsManager = Friends.getFriendsManager();
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "add":
                if (args.length < 3) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Usage: " + Formatting.GRAY + ".friends add <player>");
                    return;
                }
                String playerToAdd = args[2].trim();
                
                if (playerToAdd.isEmpty()) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Player name cannot be empty!");
                    return;
                }
                
                if (friendsManager.isFriend(playerToAdd)) {
                    ChatUtils.addMessageToChat(Formatting.YELLOW + "[Scorn] " + Formatting.WHITE + playerToAdd + Formatting.YELLOW + " is already in your friends list!");
                } else {
                    friendsManager.addFriend(playerToAdd);
                    ChatUtils.addMessageToChat(Formatting.GREEN + "[Scorn] " + Formatting.WHITE + "Successfully added " + 
                        Formatting.GREEN + playerToAdd + Formatting.WHITE + " to your friends list!");
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Friends count: " + 
                        Formatting.AQUA + friendsManager.getFriendsCount());
                }
                break;
                
            case "remove":
            case "rem":
            case "del":
            case "delete":
                if (args.length < 3) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Usage: " + Formatting.GRAY + ".friends remove <player>");
                    return;
                }
                String playerToRemove = args[2].trim();
                
                if (playerToRemove.isEmpty()) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Player name cannot be empty!");
                    return;
                }
                
                if (!friendsManager.isFriend(playerToRemove)) {
                    ChatUtils.addMessageToChat(Formatting.YELLOW + "[Scorn] " + Formatting.WHITE + playerToRemove + 
                        Formatting.YELLOW + " is not in your friends list!");
                } else {
                    friendsManager.removeFriend(playerToRemove);
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Successfully removed " + 
                        Formatting.RED + playerToRemove + Formatting.WHITE + " from your friends list!");
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Friends count: " + 
                        Formatting.AQUA + friendsManager.getFriendsCount());
                }
                break;
                
            case "list":
            case "ls":
            case "show":
                if (friendsManager.getFriendsCount() == 0) {
                    ChatUtils.addMessageToChat(Formatting.YELLOW + "[Scorn] " + Formatting.WHITE + "Your friends list is empty!");
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Use " + 
                        Formatting.AQUA + ".friends add <player>" + Formatting.WHITE + " to add friends.");
                } else {
                    ChatUtils.addMessageToChat(Formatting.AQUA + "[Scorn] " + Formatting.WHITE + "Friends List " + 
                        Formatting.GRAY + "(" + Formatting.GREEN + friendsManager.getFriendsCount() + Formatting.GRAY + "):");
                    int count = 1;
                    for (String friend : friendsManager.getFriends()) {
                        ChatUtils.addMessageToChat(Formatting.GRAY + " " + count + ". " + Formatting.GREEN + friend);
                        count++;
                    }
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Use " + 
                        Formatting.RED + ".friends remove <player>" + Formatting.WHITE + " to remove friends.");
                }
                break;
                
            case "clear":
            case "reset":
                if (friendsManager.getFriendsCount() == 0) {
                    ChatUtils.addMessageToChat(Formatting.YELLOW + "[Scorn] " + Formatting.WHITE + "Your friends list is already empty!");
                } else {
                    int count = friendsManager.getFriendsCount();
                    friendsManager.clearFriends();
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Successfully cleared " + 
                        Formatting.RED + count + Formatting.WHITE + " friends from your list!");
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Your friends list is now empty.");
                }
                break;
                
            case "check":
            case "test":
                if (args.length < 3) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Usage: " + Formatting.GRAY + ".friends check <player>");
                    return;
                }
                String playerToCheck = args[2].trim();
                
                if (playerToCheck.isEmpty()) {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Player name cannot be empty!");
                    return;
                }
                
                if (friendsManager.isFriend(playerToCheck)) {
                    ChatUtils.addMessageToChat(Formatting.GREEN + "[Scorn] " + Formatting.WHITE + playerToCheck + 
                        Formatting.GREEN + " is in your friends list! " + Formatting.WHITE + "✓");
                } else {
                    ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + playerToCheck + 
                        Formatting.RED + " is not in your friends list! " + Formatting.WHITE + "✗");
                }
                break;
                
            case "help":
            case "?":
                sendUsage();
                break;
                
            default:
                ChatUtils.addMessageToChat(Formatting.RED + "[Scorn] " + Formatting.WHITE + "Unknown action: " + 
                    Formatting.YELLOW + action);
                ChatUtils.addMessageToChat(Formatting.GRAY + "[Scorn] " + Formatting.WHITE + "Type " + 
                    Formatting.AQUA + ".friends help" + Formatting.WHITE + " for available commands.");
                break;
        }
    }
    
    private void sendUsage() {
        ChatUtils.addMessageToChat(Formatting.AQUA + "═══════════════════════════════════════");
        ChatUtils.addMessageToChat(Formatting.AQUA + "[Scorn] " + Formatting.WHITE + "Friends Command Help");
        ChatUtils.addMessageToChat(Formatting.AQUA + "═══════════════════════════════════════");
        ChatUtils.addMessageToChat(Formatting.GREEN + ".friends add <player>" + Formatting.GRAY + " - Add a player to friends");
        ChatUtils.addMessageToChat(Formatting.RED + ".friends remove <player>" + Formatting.GRAY + " - Remove a player from friends");
        ChatUtils.addMessageToChat(Formatting.YELLOW + ".friends list" + Formatting.GRAY + " - Show all friends");
        ChatUtils.addMessageToChat(Formatting.LIGHT_PURPLE + ".friends check <player>" + Formatting.GRAY + " - Check if player is a friend");
        ChatUtils.addMessageToChat(Formatting.DARK_RED + ".friends clear" + Formatting.GRAY + " - Clear all friends");
        ChatUtils.addMessageToChat(Formatting.AQUA + ".friends help" + Formatting.GRAY + " - Show this help menu");
        ChatUtils.addMessageToChat(Formatting.AQUA + "═══════════════════════════════════════");
        ChatUtils.addMessageToChat(Formatting.GRAY + "Aliases: " + Formatting.WHITE + ".friend, .f");
        
        FriendsManager friendsManager = Friends.getFriendsManager();
        ChatUtils.addMessageToChat(Formatting.GRAY + "Current friends: " + Formatting.AQUA + friendsManager.getFriendsCount());
    }
}