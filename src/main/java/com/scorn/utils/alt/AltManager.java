package com.scorn.utils.alt;

import com.scorn.utils.mc.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import net.minecraft.util.Formatting;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltManager {
    private static final String ALT_FILE = "scorn_alts.txt";
    private final List<Alt> alts = new ArrayList<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public AltManager() {
        loadAlts();
    }

    public void addAlt(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        
        // Check if alt already exists
        for (Alt alt : alts) {
            if (alt.getEmail().equalsIgnoreCase(email.trim())) {
                return; // Alt already exists
            }
        }
        
        Alt alt = new Alt(email.trim(), password != null ? password.trim() : "");
        alts.add(alt);
        saveAlts();
    }

    public void removeAlt(Alt alt) {
        alts.remove(alt);
        saveAlts();
    }

    public void clearAlts() {
        alts.clear();
        saveAlts();
    }

    public List<Alt> getAlts() {
        return new ArrayList<>(alts);
    }

    public int getAltCount() {
        return alts.size();
    }

    public void loginWithAlt(Alt alt) {
        try {
            if (alt.getPassword().isEmpty()) {
                // Cracked account login
                Session session = new Session(alt.getEmail(), UUID.randomUUID().toString(), "", Session.AccountType.MOJANG);
                setSession(session);
                ChatUtils.addMessageToChat(Formatting.GREEN + "[S] " + Formatting.WHITE + "Logged in as " + 
                    Formatting.GREEN + alt.getEmail() + Formatting.WHITE + " (Cracked)");
            } else {
                // For premium accounts, we would need proper authentication
                // This is a simplified implementation for demonstration
                Session session = new Session(alt.getEmail(), UUID.randomUUID().toString(), "", Session.AccountType.MOJANG);
                setSession(session);
                ChatUtils.addMessageToChat(Formatting.GREEN + "[S] " + Formatting.WHITE + "Logged in as " + 
                    Formatting.GREEN + alt.getEmail());
            }
        } catch (Exception e) {
            ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Failed to login: " + e.getMessage());
        }
    }

    private void setSession(Session session) {
        try {
            // Use reflection to set the session
            java.lang.reflect.Field sessionField = MinecraftClient.class.getDeclaredField("session");
            sessionField.setAccessible(true);
            sessionField.set(mc, session);
        } catch (Exception e) {
            ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Failed to set session: " + e.getMessage());
        }
    }

    public String getCurrentUsername() {
        return mc.getSession().getUsername();
    }

    private void saveAlts() {
        try {
            Path altFile = Paths.get(ALT_FILE);
            List<String> lines = new ArrayList<>();
            
            for (Alt alt : alts) {
                lines.add(alt.getEmail() + ":" + alt.getPassword());
            }
            
            Files.write(altFile, lines);
        } catch (IOException e) {
            ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Failed to save alts: " + e.getMessage());
        }
    }

    private void loadAlts() {
        try {
            Path altFile = Paths.get(ALT_FILE);
            if (!Files.exists(altFile)) {
                return;
            }
            
            List<String> lines = Files.readAllLines(altFile);
            alts.clear();
            
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(":", 2);
                if (parts.length >= 1) {
                    String email = parts[0];
                    String password = parts.length > 1 ? parts[1] : "";
                    alts.add(new Alt(email, password));
                }
            }
        } catch (IOException e) {
            ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Failed to load alts: " + e.getMessage());
        }
    }

    public static class Alt {
        private final String email;
        private final String password;

        public Alt(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public boolean isCracked() {
            return password.isEmpty();
        }

        @Override
        public String toString() {
            return email + (isCracked() ? " (Cracked)" : " (Premium)");
        }
    }
}