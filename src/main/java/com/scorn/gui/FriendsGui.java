package com.scorn.gui;

import com.scorn.gui.clickgui.imgui.ImGuiImpl;
import com.scorn.module.modules.client.Friends;
import com.scorn.utils.friends.FriendsManager;
import com.scorn.utils.mc.ChatUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FriendsGui extends Screen {
    private final ImString addFriendInput = new ImString(256);
    private final ImString searchInput = new ImString(256);
    private boolean showConfirmClear = false;

    public FriendsGui() {
        super(Text.literal("Friends Manager"));
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ImGuiImpl.render(io -> {
            // Set modern dark theme with accent colors
            ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.12f, 0.12f, 0.15f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 0.15f, 0.15f, 0.18f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.08f, 0.08f, 0.10f, 0.95f);
            ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.10f, 0.10f, 0.12f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.PopupBg, 0.08f, 0.08f, 0.10f, 0.98f);
            ImGui.pushStyleColor(ImGuiCol.Border, 0.25f, 0.25f, 0.30f, 0.8f);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.15f, 0.15f, 0.18f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.18f, 0.18f, 0.22f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.20f, 0.20f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Button, 0.20f, 0.25f, 0.30f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.30f, 0.35f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.30f, 0.35f, 0.40f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Header, 0.18f, 0.22f, 0.28f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0.22f, 0.26f, 0.32f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0.26f, 0.30f, 0.36f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Text, 0.95f, 0.95f, 0.97f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.TextDisabled, 0.60f, 0.60f, 0.65f, 1.0f);
            
            // Enhanced styling
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 12.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 10.0f, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 20.0f, 20.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 8.0f, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, 8.0f, 6.0f);

            renderFriendsWindow();

            // Pop all style modifications
            ImGui.popStyleColor(17);
            ImGui.popStyleVar(10);
        });
    }

    private void renderFriendsWindow() {
        int windowFlags = ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize;
        
        // Center the window
        ImGui.setNextWindowPos(
            ImGui.getMainViewport().getCenterX() - 300,
            ImGui.getMainViewport().getCenterY() - 250,
            imgui.flag.ImGuiCond.Appearing
        );
        ImGui.setNextWindowSize(600, 500, imgui.flag.ImGuiCond.Appearing);

        if (ImGui.begin("✦ Friends Manager ✦", windowFlags)) {
            FriendsManager friendsManager = Friends.getFriendsManager();
            
            // Elegant header with gradient-like effect
            ImGui.pushStyleColor(ImGuiCol.Text, 0.4f, 0.8f, 1.0f, 1.0f);
            ImGui.setWindowFontScale(1.3f);
            ImGui.textColored(0.4f, 0.8f, 1.0f, 1.0f, "Manage Your Friends");
            ImGui.setWindowFontScale(1.0f);
            ImGui.popStyleColor();
            
            ImGui.sameLine();
            ImGui.setCursorPosX(ImGui.getWindowWidth() - 120);
            ImGui.textColored(0.7f, 0.9f, 0.7f, 1.0f, "(" + friendsManager.getFriendsCount() + " friends)");
            
            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            // Add friend section with better styling
            ImGui.pushStyleColor(ImGuiCol.Text, 0.3f, 0.9f, 0.3f, 1.0f);
            ImGui.text("➤ Add New Friend");
            ImGui.popStyleColor();
            ImGui.spacing();
            
            ImGui.setNextItemWidth(400);
            ImGui.inputTextWithHint("##AddFriend", "Enter player name...", addFriendInput, ImGuiInputTextFlags.None);
            
            ImGui.sameLine();
            ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.4f, 0.9f, 0.4f, 1.0f);
            if (ImGui.button("Add Friend", 120, 26) && !addFriendInput.get().trim().isEmpty()) {
                addFriend(addFriendInput.get().trim());
                addFriendInput.set("");
            }
            ImGui.popStyleColor(3);

            ImGui.spacing();
            ImGui.spacing();

            // Friends list section with improved styling
            ImGui.pushStyleColor(ImGuiCol.Text, 0.4f, 0.8f, 1.0f, 1.0f);
            ImGui.text("➤ Friends List");
            ImGui.popStyleColor();
            
            ImGui.sameLine();
            ImGui.setCursorPosX(ImGui.getWindowWidth() - 140);
            if (friendsManager.getFriendsCount() > 0) {
                ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.2f, 0.2f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.3f, 0.3f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.4f, 0.4f, 1.0f);
                if (ImGui.button("Clear All", 120, 26)) {
                    showConfirmClear = true;
                }
                ImGui.popStyleColor(3);
            }
            
            ImGui.spacing();
            
            // Search box with better styling
            ImGui.setNextItemWidth(400);
            ImGui.inputTextWithHint("##Search", "🔍 Search friends...", searchInput, ImGuiInputTextFlags.None);

            ImGui.spacing();

            // Enhanced friends list
            renderFriendsList(friendsManager);

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            // Footer buttons with better styling
            ImGui.setCursorPosX((ImGui.getWindowWidth() - 120) / 2);
            ImGui.pushStyleColor(ImGuiCol.Button, 0.5f, 0.5f, 0.6f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.6f, 0.6f, 0.7f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.7f, 0.8f, 1.0f);
            if (ImGui.button("Close", 120, 32)) {
                close();
            }
            ImGui.popStyleColor(3);

            // Confirmation dialogs
            renderConfirmationDialogs(friendsManager);
        }
        ImGui.end();
    }

    private void renderFriendsList(FriendsManager friendsManager) {
        if (friendsManager.getFriendsCount() == 0) {
            ImGui.textColored(0.7f, 0.7f, 0.7f, 1.0f, "No friends added yet.");
            ImGui.textColored(0.5f, 0.5f, 0.5f, 1.0f, "Add friends using the input field above.");
            return;
        }

        String searchTerm = searchInput.get().toLowerCase();
        List<String> friends = friendsManager.getFriends();
        
        // Begin scrollable region for friends list
        ImGui.beginChild("FriendsList", 0, 200, true);
        
        int index = 1;
        for (String friend : friends) {
            if (searchTerm.isEmpty() || friend.toLowerCase().contains(searchTerm)) {
                ImGui.pushID(friend);
                
                // Friend name with index
                ImGui.textColored(0.2f, 1.0f, 0.2f, 1.0f, index + ". " + friend);
                
                ImGui.sameLine();
                ImGui.setCursorPosX(ImGui.getWindowWidth() - 80);
                
                // Remove button
                if (ImGui.button("Remove", 70, 20)) {
                    friendToRemove = friend;
                    showConfirmRemove = true;
                }
                
                ImGui.popID();
                index++;
            }
        }
        
        ImGui.endChild();
    }

    private void renderConfirmationDialogs(FriendsManager friendsManager) {
        // Clear all confirmation dialog
        if (showConfirmClear) {
            ImGui.openPopup("Clear All Friends?");
        }
        
        if (ImGui.beginPopupModal("Clear All Friends?", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Are you sure you want to remove all " + friendsManager.getFriendsCount() + " friends?");
            ImGui.text("This action cannot be undone!");
            ImGui.spacing();
            
            if (ImGui.button("Yes, Clear All", 120, 30)) {
                friendsManager.clearFriends();
                ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "All friends have been removed!");
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            
            ImGui.sameLine();
            if (ImGui.button("Cancel", 80, 30)) {
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            
            ImGui.endPopup();
        }

        // Remove single friend confirmation dialog
        if (showConfirmRemove && friendToRemove != null) {
            ImGui.openPopup("Remove Friend?");
        }
        
        if (ImGui.beginPopupModal("Remove Friend?", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Are you sure you want to remove:");
            ImGui.textColored(1.0f, 0.2f, 0.2f, 1.0f, friendToRemove);
            ImGui.text("from your friends list?");
            ImGui.spacing();
            
            if (ImGui.button("Yes, Remove", 100, 30)) {
                friendsManager.removeFriend(friendToRemove);
                ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Successfully removed " + 
                    Formatting.RED + friendToRemove + Formatting.WHITE + " from your friends list!");
                friendToRemove = null;
                showConfirmRemove = false;
                ImGui.closeCurrentPopup();
            }
            
            ImGui.sameLine();
            if (ImGui.button("Cancel", 80, 30)) {
                friendToRemove = null;
                showConfirmRemove = false;
                ImGui.closeCurrentPopup();
            }
            
            ImGui.endPopup();
        }
    }

    private void addFriend(String playerName) {
        FriendsManager friendsManager = Friends.getFriendsManager();
        
        if (friendsManager.isFriend(playerName)) {
            ChatUtils.addMessageToChat(Formatting.YELLOW + "[S] " + Formatting.WHITE + playerName + 
                Formatting.YELLOW + " is already in your friends list!");
        } else {
            friendsManager.addFriend(playerName);
            ChatUtils.addMessageToChat(Formatting.GREEN + "[S] " + Formatting.WHITE + "Successfully added " + 
                Formatting.GREEN + playerName + Formatting.WHITE + " to your friends list!");
            ChatUtils.addMessageToChat(Formatting.GRAY + "[S] " + Formatting.WHITE + "Friends count: " + 
                Formatting.AQUA + friendsManager.getFriendsCount());
        }
    }

    @Override
    public boolean shouldPause() {
        return false; // Don't pause the game when friends GUI is open
    }
}