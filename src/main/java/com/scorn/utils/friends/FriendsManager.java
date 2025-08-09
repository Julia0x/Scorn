package com.scorn.utils.friends;

import net.minecraft.entity.player.PlayerEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendsManager {
    private static final String FRIENDS_FILE = "scorn_friends.txt";
    private final List<String> friends = new CopyOnWriteArrayList<>();
    
    public FriendsManager() {
        loadFriends();
    }
    
    public void addFriend(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) return;
        
        String name = playerName.trim();
        if (!friends.contains(name.toLowerCase())) {
            friends.add(name.toLowerCase());
            saveFriends();
        }
    }
    
    public void removeFriend(String playerName) {
        if (playerName == null) return;
        
        friends.remove(playerName.toLowerCase());
        saveFriends();
    }
    
    public boolean isFriend(String playerName) {
        if (playerName == null) return false;
        return friends.contains(playerName.toLowerCase());
    }
    
    public boolean isFriend(PlayerEntity player) {
        if (player == null) return false;
        return isFriend(player.getName().getString());
    }
    
    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }
    
    public void clearFriends() {
        friends.clear();
        saveFriends();
    }
    
    private void loadFriends() {
        try {
            if (Files.exists(Paths.get(FRIENDS_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(FRIENDS_FILE));
                friends.clear();
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        friends.add(line.trim().toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load friends: " + e.getMessage());
        }
    }
    
    private void saveFriends() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FRIENDS_FILE))) {
            for (String friend : friends) {
                writer.println(friend);
            }
        } catch (Exception e) {
            System.err.println("Failed to save friends: " + e.getMessage());
        }
    }
    
    public int getFriendsCount() {
        return friends.size();
    }
}