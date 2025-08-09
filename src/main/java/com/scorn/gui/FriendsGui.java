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
            ImGui.textColored(0.4f, 0.8f, 1.0f, 1.0f, "Manage Your Friends");
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
            ImGui.inputTextWithHint("##Search", "Search friends...", searchInput, ImGuiInputTextFlags.None);

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
            // Enhanced empty state
            ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.12f, 0.12f, 0.15f, 0.8f);
            ImGui.beginChild("EmptyState", 0, 250, true);
            
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 80);
            ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("No friends added yet").x) / 2);
            ImGui.textColored(0.6f, 0.6f, 0.7f, 1.0f, "No friends added yet");
            
            ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("Add friends using the input field above").x) / 2);
            ImGui.textColored(0.5f, 0.5f, 0.6f, 1.0f, "Add friends using the input field above");
            
            ImGui.endChild();
            ImGui.popStyleColor();
            return;
        }

        String searchTerm = searchInput.get().toLowerCase();
        List<String> friends = friendsManager.getFriends();
        
        // Enhanced scrollable region for friends list
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.10f, 0.10f, 0.12f, 1.0f);
        ImGui.beginChild("FriendsList", 0, 250, true);
        
        int index = 1;
        for (String friend : friends) {
            if (searchTerm.isEmpty() || friend.toLowerCase().contains(searchTerm)) {
                ImGui.pushID(friend);
                
                // Alternating row colors for better readability
                if (index % 2 == 0) {
                    ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.12f, 0.12f, 0.15f, 0.5f);
                    ImGui.beginChild("FriendRow", 0, 30, false);
                } else {
                    ImGui.beginChild("FriendRow", 0, 30, false);
                }
                
                // Friend name with better styling
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 4);
                ImGui.pushStyleColor(ImGuiCol.Text, 0.3f, 0.9f, 0.5f, 1.0f);
                ImGui.text(index + ". " + friend);
                ImGui.popStyleColor();
                
                // Centered remove button
                ImGui.sameLine();
                ImGui.setCursorPosY(ImGui.getCursorPosY() - 2);
                ImGui.setCursorPosX(ImGui.getWindowWidth() - 85);
                
                ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.3f, 0.3f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.4f, 0.4f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.5f, 0.5f, 1.0f);
                if (ImGui.button("Remove", 75, 24)) {
                    // Remove immediately without confirmation
                    friendsManager.removeFriend(friend);
                    ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Removed " + 
                        Formatting.RED + friend + Formatting.WHITE + " from your friends list!");
                    ChatUtils.addMessageToChat(Formatting.GRAY + "[S] " + Formatting.WHITE + "Friends count: " + 
                        Formatting.AQUA + friendsManager.getFriendsCount());
                }
                ImGui.popStyleColor(3);
                
                ImGui.endChild();
                if (index % 2 == 0) {
                    ImGui.popStyleColor();
                }
                
                ImGui.popID();
                index++;
            }
        }
        
        ImGui.endChild();
        ImGui.popStyleColor();
    }

    private void renderConfirmationDialogs(FriendsManager friendsManager) {
        // Clear all confirmation dialog only
        if (showConfirmClear) {
            ImGui.openPopup("Clear All Friends?");
        }
        
        if (ImGui.beginPopupModal("Clear All Friends?", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 0.8f, 0.4f, 1.0f);
            ImGui.text("⚠ Are you sure you want to remove all " + friendsManager.getFriendsCount() + " friends?");
            ImGui.popStyleColor();
            ImGui.textColored(0.9f, 0.5f, 0.5f, 1.0f, "This action cannot be undone!");
            ImGui.spacing();
            ImGui.spacing();
            
            // Centered buttons
            float buttonWidth = 120;
            float spacing = 20;
            float totalWidth = (buttonWidth * 2) + spacing;
            ImGui.setCursorPosX((ImGui.getWindowWidth() - totalWidth) / 2);
            
            ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.2f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.3f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.4f, 0.4f, 1.0f);
            if (ImGui.button("Yes, Clear All", buttonWidth, 35)) {
                friendsManager.clearFriends();
                ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "All friends have been removed!");
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor(3);
            
            ImGui.sameLine();
            ImGui.pushStyleColor(ImGuiCol.Button, 0.5f, 0.5f, 0.6f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.6f, 0.6f, 0.7f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.7f, 0.8f, 1.0f);
            if (ImGui.button("Cancel", buttonWidth, 35)) {
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor(3);
            
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